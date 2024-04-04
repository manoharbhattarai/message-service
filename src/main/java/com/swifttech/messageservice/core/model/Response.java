package com.swifttech.messageservice.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract sealed class Response permits ApiError, ApiResponse {

    private boolean success;
    @JsonIgnore
    private HttpStatus httpStatus;
    private String code;
    private String message;
}
