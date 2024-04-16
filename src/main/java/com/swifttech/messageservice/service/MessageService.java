package com.swifttech.messageservice.service;

import com.swifttech.messageservice.entity.MessageEntity;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.MessageSearchFilterPaginationRequest;
import com.swifttech.messageservice.payload.response.DataPaginationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void createMessage(MessageRequest messageRequest);

    DataPaginationResponse messageList(MessageSearchFilterPaginationRequest request);

    List<String> readCSVFile(MultipartFile file);

    void checkScheduledMessages();

    MessageEntity updateMessage(UUID id, MessageRequest messageRequest);

//    void sendBroadCastMessage(MessageRequest messageRequest);




}
