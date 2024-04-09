package com.swifttech.messageservice.payload.response;

import com.swifttech.messageservice.enums.ChannelMode;
import com.swifttech.messageservice.enums.Status;
import com.swifttech.messageservice.payload.request.CustomerSpecificationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageList {

    UUID id;
    ChannelMode channel;
    UUID createdBy;
    String category;
    LocalDateTime scheduledTime;
    Status status;
}
