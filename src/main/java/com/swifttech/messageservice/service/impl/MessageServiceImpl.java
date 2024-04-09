package com.swifttech.messageservice.service.impl;

import com.swifttech.messageservice.core.exception.RemitException;
import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.enums.Status;
import com.swifttech.messageservice.mapper.MessageMapper;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.*;
import com.swifttech.messageservice.payload.response.DataPaginationResponse;
import com.swifttech.messageservice.payload.response.MessageList;
import com.swifttech.messageservice.payload.response.MessagesResponse;
import com.swifttech.messageservice.repository.MessageRepository;
import com.swifttech.messageservice.service.MessageService;
import com.swifttech.messageservice.specification.CustomSpecification;
import com.swifttech.messageservice.util.ApiConnector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;
    private final ApiConnector apiConnector;

    @Override
    public void sendScheduleMessage(MessageRequest messageRequest) {

        List<String> customerIdsList = messageRequest.getCustomerIds().getCustomerIdsList();
        Message message = MessageMapper.INSTANCE.toEntity(messageRequest);

        LocalDateTime scheduledTime = message.getScheduledTime();
        LocalDateTime currentTime = LocalDateTime.now();

        if (scheduledTime != null && scheduledTime.isBefore(currentTime)) {
            throw new IllegalArgumentException("Scheduled time should be in the future.You have entered past time");
        } else if (scheduledTime != null && scheduledTime.isAfter(currentTime)) {
            message.setScheduledStatus(Status.SCHEDULED);
            messageRepository.save(message);
        } else {
            sendInstantMessage(message);
        }

    }


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    @Override
    public void checkScheduledMessages() {
        List<Message> scheduledMessages = messageRepository.findAllByScheduleIsTrue();
        LocalDateTime currentTime = LocalDateTime.now();
        for (Message message : scheduledMessages) {
            LocalDateTime scheduledTime = message.getScheduledTime();
            if (scheduledTime != null && scheduledTime.isBefore(currentTime)) {
                sendScheduledMessage(message);
            }
        }
    }


    private void sendInstantMessage(Message message) {
        try {
            sendBulkMessage(message);
            message.setScheduledStatus(Status.COMPLETED);
        } catch (Exception e) {
            message.setScheduledStatus(Status.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(message);
    }

    private void sendScheduledMessage(Message message) {
        try {
            sendBulkMessage(message);
            message.setScheduledStatus(Status.COMPLETED);
        } catch (Exception e) {
            message.setScheduledStatus(Status.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(message);
    }

    private void sendBulkMessage(Message message) {

        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
        composeMessageRequest.setMessage(message.getMessage());
        composeMessageRequest.setMobileNumber(message.getCustomerIds().getCustomerIdsList());

        apiConnector.sendMessage(composeMessageRequest);
    }


    @Override
    public DataPaginationResponse messageList(MessageSearchFilterPaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(),
                Sort.by(Objects.equals(request.getDirection(), "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        request.getSortBy() == null ? "createdAt" : request.getSortBy()));
        Specification<Message> messageSpecification = CustomSpecification.filterMessage(request);
        Page<Message> messagePage = messageRepository.findAll(messageSpecification,pageable);
        List<Message> messageList = messagePage.getContent();
        List<MessageList> messageLists = new ArrayList<>();
        for (Message message: messageList){
            String category = message.getCustomer().getPlatform();
            MessageList messages =  MessageMapper.INSTANCE.toMessageList(message);
            messages.setCategory(category);
            messageLists.add(messages);

        }
        return DataPaginationResponse.builder()
                .result(messageLists)
                .totalElementCount(messagePage.getTotalElements())
                .build();
    }


    @Override
    public Message updateMessage(UUID id, MessageRequest messageRequest) {

        Message messageId = messageRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Message not found"));
        if (messageId.getScheduledStatus() == Status.SCHEDULED || messageId.getScheduledStatus() == Status.FAILED){
            Message messageUpdate = MessageMapper.INSTANCE.toDto(messageRequest, messageId);
            if (messageId.getScheduledStatus() == Status.FAILED) {
                messageUpdate.setScheduledStatus(Status.SCHEDULED);
            }
            return messageRepository.save(messageUpdate);
        } else {
            throw new RuntimeException("Message can only be updated when status is Scheduled or Failed");
        }
    }





    @Override
    public List<String> readCSVFile(MultipartFile file) {

        List<String> customerList = new ArrayList<>();
        List<String> responseList = new ArrayList<>();

        String contentType = file.getContentType();
        assert contentType != null;
        if (!contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel")) {

            log.error("Invalid file type. Expected CSV file.");

        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] customerIds = line.trim().split("\\s*,\\s*");
                customerList.addAll(Arrays.asList(customerIds));
            }
        } catch (IOException e) {
            log.error("Error reading the uploaded CSV file: {}", e.getMessage());
        }

        CustomerApiRequest customerApiRequest = new CustomerApiRequest();
        customerApiRequest.setCustomerIdsList(customerList);
        ApiResponse apiResponse = apiConnector.getCustomerList(customerApiRequest);
        if (apiResponse.isSuccess()) {
            responseList.addAll((List<String>) apiResponse.getData());
        }
        log.info("Customer list: {}", responseList);
        return responseList;
    }


}
