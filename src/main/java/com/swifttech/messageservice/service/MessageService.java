package com.swifttech.messageservice.service;

import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.PaginationRequest;
import com.swifttech.messageservice.payload.response.AllMessagesResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {

    void sendScheduleMessage(MessageRequest messageRequest) throws InterruptedException;

   // List<AllMessagesResponse> messageList();

    List<AllMessagesResponse> messageList(PaginationRequest paginationRequest);

     List<String> readCSVFile(MultipartFile file);


}
