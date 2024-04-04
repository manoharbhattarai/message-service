package com.swifttech.messageservice.payload.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ComposeMessageRequest {

    private String message;
    private List<String> mobileNumber;
}
