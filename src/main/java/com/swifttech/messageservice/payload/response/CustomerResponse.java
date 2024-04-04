package com.swifttech.messageservice.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class CustomerResponse {

    private boolean success;
    private List<String> data;
}
