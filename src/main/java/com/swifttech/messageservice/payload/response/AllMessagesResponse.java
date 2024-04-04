package com.swifttech.messageservice.payload.response;

import com.swifttech.messageservice.payload.request.NotificationDetailsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMessagesResponse {

    boolean isSchedule;
    LocalDateTime scheduledTime;
    NotificationDetailsRequest notification;
    String channel;
}
