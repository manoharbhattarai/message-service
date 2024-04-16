//package com.swifttech.messageservice.util;
//
//import com.swifttech.messageservice.enums.StatusEnum;
//import com.swifttech.messageservice.entity.MessageEntity;
//import com.swifttech.messageservice.payload.request.ComposeMessageRequest;
//import com.swifttech.messageservice.repository.MessageRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RequiredArgsConstructor
//public class SmsSender {
//
//    private final MessageRepository messageRepository;
//    private final ApiConnector apiConnector;
//    public void sendInstantMessage(MessageEntity messageEntity) {
//        try {
//            sendBulkMessage(messageEntity);
//            messageEntity.setScheduledStatusEnum(StatusEnum.COMPLETED);
//        } catch (Exception e) {
//            messageEntity.setScheduledStatusEnum(StatusEnum.FAILED);
//            e.printStackTrace();
//        }
//        messageRepository.save(messageEntity);
//    }
//
//    public void sendScheduledMessage(MessageEntity messageEntity) {
//        try {
//            sendBulkMessage(messageEntity);
//            messageEntity.setScheduledStatusEnum(StatusEnum.COMPLETED);
//        } catch (Exception e) {
//            messageEntity.setScheduledStatusEnum(StatusEnum.FAILED);
//            e.printStackTrace();
//        }
//        messageRepository.save(messageEntity);
//    }
//
//    public void sendBulkMessage(MessageEntity messageEntity) {
//        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
//        composeMessageRequest.setMessage(messageEntity.getMessage());
//        composeMessageRequest.setMobileNumber(messageEntity.getCustomerIds().getCustomerIdsList());
//        apiConnector.sendMessage(composeMessageRequest);
//    }
//}
