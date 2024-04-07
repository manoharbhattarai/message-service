package com.swifttech.messageservice.util;

import com.swifttech.messageservice.enums.Status;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.ComposeMessageRequest;
import com.swifttech.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SmsSender {

    private final MessageRepository messageRepository;
    private final ApiConnector apiConnector;
    public void sendInstantMessage(Message message) {
        try {
            sendBulkMessage(message);
            message.setScheduledStatus(Status.COMPLETED);
        } catch (Exception e) {
            message.setScheduledStatus(Status.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(message);
    }

    public void sendScheduledMessage(Message message) {
        try {
            sendBulkMessage(message);
            message.setScheduledStatus(Status.COMPLETED);
        } catch (Exception e) {
            message.setScheduledStatus(Status.FAILED);
            e.printStackTrace();
        }
        messageRepository.save(message);
    }

    public void sendBulkMessage(Message message) {
        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
        composeMessageRequest.setMessage(message.getMessage());
        composeMessageRequest.setMobileNumber(message.getCustomerIds().getCustomerIdsList());
        apiConnector.sendMessage(composeMessageRequest);
    }
}
