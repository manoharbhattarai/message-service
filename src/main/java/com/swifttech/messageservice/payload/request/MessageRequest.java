package com.swifttech.messageservice.payload.request;

import com.swifttech.messageservice.enums.BroadCastMode;
import com.swifttech.messageservice.enums.ChannelMode;
import com.swifttech.messageservice.enums.CommunicationMode;
import com.swifttech.messageservice.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

    private String message;
    private CustomerApiRequest customerIds;
    private Boolean schedule;
    private LocalDateTime scheduledTime;
    private ChannelMode channelMode;
    private BroadCastMode broadCastMode;
    private Status scheduledStatus;
    private CommunicationMode communicationMode;
    private NotificationDetailsRequest notification;
    private CustomerSpecificationRequest customer;


}
