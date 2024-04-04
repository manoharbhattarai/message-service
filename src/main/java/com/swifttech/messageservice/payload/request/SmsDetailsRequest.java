package com.swifttech.messageservice.payload.request;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsDetailsRequest {

    private BigInteger receiverNo;
    private String message;
}
