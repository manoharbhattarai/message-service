package com.swifttech.messageservice.service.impl;

import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.payload.request.ComposeMessageRequest;
import com.swifttech.messageservice.payload.request.CustomerApiRequest;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.PaginationRequest;
import com.swifttech.messageservice.payload.response.AllMessagesResponse;
import com.swifttech.messageservice.mapper.MessageMapper;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.repository.MessageRepository;
import com.swifttech.messageservice.service.MessageService;
import com.swifttech.messageservice.util.ApiConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;
    private final ApiConnector apiConnector;

    @Override
    public void sendScheduleMessage(MessageRequest messageRequest) throws InterruptedException {


        List<String> customerIdsList = messageRequest.getCustomerIds().getCustomerIdsList();
        Message message = MessageMapper.INSTANCE.toEntity(messageRequest);
        messageRepository.save(message);
        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
        composeMessageRequest.setMessage(messageRequest.getMessage());
        composeMessageRequest.setMobileNumber(customerIdsList);
        apiConnector.sendSMS(composeMessageRequest);
    }


    @Override
    public List<AllMessagesResponse> messageList(PaginationRequest pagination) {
        Pageable pageable = PageRequest.of(pagination.pages(), pagination.size(),
                Sort.by(Objects.equals(pagination.sortDirection(), "asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        pagination.sortBy() == null ? "createdAt" : pagination.sortBy()));

        Page<Message> messagePage = messageRepository.findAll(pageable);
        List<Message> messageList = messagePage.getContent();

        return messageList.stream().map(MessageMapper.INSTANCE::toResponse)
                .toList();
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
