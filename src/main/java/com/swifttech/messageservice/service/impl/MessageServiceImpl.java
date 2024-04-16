package com.swifttech.messageservice.service.impl;

import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.entity.MessageEntity;
import com.swifttech.messageservice.entity.MessageHistoryEntity;
import com.swifttech.messageservice.enums.BroadCastMode;
import com.swifttech.messageservice.enums.ChannelMode;
import com.swifttech.messageservice.enums.StatusEnum;
import com.swifttech.messageservice.mapper.MessageMapper;
import com.swifttech.messageservice.payload.request.*;
import com.swifttech.messageservice.payload.response.DataPaginationResponse;
import com.swifttech.messageservice.payload.response.MessageList;
import com.swifttech.messageservice.repository.MessageHistoryRepository;
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
    private final MessageHistoryRepository messageHistoryRepository;

    @Override
    public void createMessage(MessageRequest messageRequest) {

        List<String> customerIdsList = messageRequest.getCustomerIds().getCustomerList();
        MessageEntity messageEntity = MessageMapper.INSTANCE.toEntity(messageRequest);

        LocalDateTime scheduledTime = messageEntity.getScheduledTime();
        LocalDateTime currentTime = LocalDateTime.now();

        if (scheduledTime != null && scheduledTime.isBefore(currentTime)) {
            throw new IllegalArgumentException("Scheduled time should be in the future.You have entered past time");
        }

        if(messageRequest.getChannelMode().equals(ChannelMode.BROADCAST)) {
            switch (messageRequest.getBroadCastMode()) {
                case BroadCastMode.ALL_CUSTOMER -> {
                    List<String> customerIds = getAllCustomerMobile();
                    messageEntity.setCustomerIds(CustomerApiRequest.builder()
                            .customerList(customerIds)
                            .build());
                }
                case BroadCastMode.SPECIFIED_CUSTOMER ->  {
                    List<String> customerIds =getSpecificCustomerMobile();
                    messageEntity.setCustomerIds(CustomerApiRequest.builder()
                            .customerList(customerIds)
                            .build());
                }
            }

        }
        if (scheduledTime != null && scheduledTime.isAfter(currentTime)) {
            messageEntity.setScheduledStatusEnum(StatusEnum.SCHEDULED);

            messageRepository.save(messageEntity);
        } else {
            sendInstantMessage(messageEntity);
        }

    }



    @Scheduled(cron = "0 * * * * *")
    @Transactional
    @Override
    public void checkScheduledMessages() {
        List<MessageEntity> scheduledMessageEntities = messageRepository.findAllByScheduleIsTrue();
        LocalDateTime currentTime = LocalDateTime.now();
        for (MessageEntity messageEntity : scheduledMessageEntities) {
            LocalDateTime scheduledTime = messageEntity.getScheduledTime();
            if (scheduledTime != null && scheduledTime.isBefore(currentTime)){
                sendScheduledMessage(messageEntity);
            }
        }
    }


    private void sendInstantMessage(MessageEntity messageEntity) {
        try {
            sendBulkMessage(messageEntity);
            messageEntity.setScheduledStatusEnum(StatusEnum.COMPLETED);
            messageEntity.setSuccessCount(messageEntity.getSuccessCount() + 1);
            logMessageHistory(messageEntity, "Message sent successfully", StatusEnum.COMPLETED);

        } catch (Exception e) {
            messageEntity.setScheduledStatusEnum(StatusEnum.FAILED);
            messageEntity.setFailCount(messageEntity.getFailCount() + 1);
            logMessageHistory(messageEntity, e.getMessage(), StatusEnum.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(messageEntity);
    }

    private void sendScheduledMessage(MessageEntity messageEntity) {
        try {
            sendBulkMessage(messageEntity);
            messageEntity.setScheduledStatusEnum(StatusEnum.COMPLETED);
            messageEntity.setSuccessCount(messageEntity.getSuccessCount() + 1);
            logMessageHistory(messageEntity, "Message sent successfully", StatusEnum.COMPLETED);
        } catch (Exception e) {
            messageEntity.setScheduledStatusEnum(StatusEnum.FAILED);
            messageEntity.setFailCount(messageEntity.getFailCount() + 1);
            logMessageHistory(messageEntity, e.getMessage(), StatusEnum.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(messageEntity);
    }

    private void sendBulkMessage(MessageEntity messageEntity) {

        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
        composeMessageRequest.setMessage(messageEntity.getMessage());
        composeMessageRequest.setMobileNumber(messageEntity.getCustomerIds().getCustomerList());

        apiConnector.sendMessage(composeMessageRequest);
    }





    private void logMessageHistory(MessageEntity messageEntity, String response, StatusEnum status) {
        MessageHistoryEntity historyEntity = new MessageHistoryEntity();
        historyEntity.setMessage(messageEntity);
        historyEntity.setCustomer(UUID.fromString(messageEntity.getCustomerIds().getCustomerList().get(0))); // Assuming you want to log the first customer only
        historyEntity.setDate(LocalDateTime.now());
        historyEntity.setResponse(response);
        historyEntity.setStatus(status);
        messageHistoryRepository.save(historyEntity);
    }


    @Override
    public DataPaginationResponse messageList(MessageSearchFilterPaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(),
                Sort.by(Objects.equals(request.getDirection(), "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        request.getSortBy() == null ? "createdAt" : request.getSortBy()));
        Specification<MessageEntity> messageSpecification = CustomSpecification.filterMessage(request);
        Page<MessageEntity> messagePage = messageRepository.findAll(messageSpecification, pageable);
        List<MessageEntity> messageEntityList = messagePage.getContent();
        List<MessageList> messageLists = new ArrayList<>();
        for (MessageEntity messageEntity : messageEntityList) {
            String category = messageEntity.getCustomer().getPlatform();
            MessageList messages = MessageMapper.INSTANCE.toMessageList(messageEntity);
            messages.setCategory(category);
            messageLists.add(messages);

        }
        return DataPaginationResponse.builder()
                .result(messageLists)
                .totalElementCount(messagePage.getTotalElements())
                .build();
    }


    @Override
    public MessageEntity updateMessage(UUID id, MessageRequest messageRequest) {

        MessageEntity messageEntityId = messageRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Message not found"));
        if (messageEntityId.getScheduledStatusEnum() == StatusEnum.SCHEDULED || messageEntityId.getScheduledStatusEnum() == StatusEnum.FAILED) {
            MessageEntity messageEntityUpdate = MessageMapper.INSTANCE.toDto(messageRequest, messageEntityId);
            if (messageEntityId.getScheduledStatusEnum() == StatusEnum.FAILED) {
                messageEntityUpdate.setScheduledStatusEnum(StatusEnum.SCHEDULED);
            }
            return messageRepository.save(messageEntityUpdate);
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
        customerApiRequest.setCustomerList(customerList);
        ApiResponse apiResponse = apiConnector.getCustomerList(customerApiRequest);
        if (apiResponse.isSuccess()) {
            responseList.addAll((List<String>) apiResponse.getData());
        }
        log.info("Customer list: {}", responseList);
       return responseList;
    }


    private List<String> getAllCustomerMobile() {
        List<String> allCustomerNumber = new ArrayList<>();
        CustomerApiRequest listAllCustomer = new CustomerApiRequest();
        ApiResponse apiResponse = apiConnector.getAllCustomer(listAllCustomer);
        if (apiResponse.isSuccess()) {
            List<String> allCustomer = (List<String>) apiResponse.getData();
            allCustomerNumber.addAll(allCustomer);
        }
        log.info("Customer list: {}", allCustomerNumber);
        return allCustomerNumber;

    }

    private List<String> getSpecificCustomerMobile() {
            List<String> specifiedCustomerNumber = new ArrayList<>();
            SpecifiedCustomerApiRequest listSpecifiedCustomer = new SpecifiedCustomerApiRequest();
            ApiResponse apiResponse = apiConnector.getSpecifiedCustomer(listSpecifiedCustomer);
            if (apiResponse.isSuccess()) {
                List<String> responseData = (List<String>) apiResponse.getData();
                specifiedCustomerNumber.addAll(responseData);
            }
            log.info("Customer mobile numbers: {}", specifiedCustomerNumber);
            return specifiedCustomerNumber;
        }



    }
