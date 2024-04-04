package com.swifttech.messageservice.core.builder;

import com.swifttech.messageservice.core.exception.RemitException;
import com.swifttech.messageservice.core.model.ApiError;
import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.core.util.ExceptionUtil;

public class ServiceResponseBuilder {

    public static ApiResponse buildSuccessResponse(Object o) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(Boolean.TRUE);
        response.setData(o);
        return response;
    }

    public static ApiResponse buildSuccessResponse(Object o, String message) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(Boolean.TRUE);
        response.setData(o);
        response.setMessage(message);
        return response;
    }

    public static ApiError buildFailResponse(RemitException exception) {
        ApiError response = new ApiError();
        response.setSuccess(Boolean.FALSE);
        response.setCode(exception.getCode());
        response.setMessage(exception.getMessage());
        return response;

    }

    public static ApiError buildUnknownFailResponse(Exception exception) {
        ApiError response = new ApiError();
        response.setSuccess(Boolean.FALSE);
        response.setCode("000000");
        response.setMessage(exception.getMessage());
        response.setDebugMessage(ExceptionUtil.getStackTraceString(exception));
        return response;

    }

}
