package com.swifttech.messageservice.service;

import com.swifttech.messageservice.enums.Status;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.MessageSearchFilterPaginationRequest;
import com.swifttech.messageservice.payload.request.PaginationRequest;
import com.swifttech.messageservice.payload.response.DataPaginationResponse;
import com.swifttech.messageservice.payload.response.MessagesResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void sendScheduleMessage(MessageRequest messageRequest);

    DataPaginationResponse messageList(MessageSearchFilterPaginationRequest request);

    List<String> readCSVFile(MultipartFile file);

    void checkScheduledMessages();

    Message updateMessage(UUID id, MessageRequest messageRequest);




}
