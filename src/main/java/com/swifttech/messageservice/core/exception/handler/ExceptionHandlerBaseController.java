package com.swifttech.messageservice.core.exception.handler;

import com.swifttech.messageservice.core.builder.ServiceResponseBuilder;
import com.swifttech.messageservice.core.exception.RemitException;
import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.core.model.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExceptionHandlerBaseController {

    @ExceptionHandler(RemitException.class)
    public ResponseEntity<Response> handleRemitsException(RemitException e) {

        return ResponseEntity.status(e.getHttpStatus())
                .body(ServiceResponseBuilder.buildFailResponse(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ServiceResponseBuilder.buildUnknownFailResponse(e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setCode(String.valueOf(HttpStatus.EXPECTATION_FAILED.value()));
        apiResponse.setData(errors);
        return ResponseEntity.badRequest()
                .body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Response> handleResourceNotFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ServiceResponseBuilder.buildUnknownFailResponse(e));
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<Response> s(final AuthenticationException e, final HttpServletResponse response) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(ServiceResponseBuilder.buildUnknownFailResponse(e));
//    }

    @ExceptionHandler(WebClientResponseException.Forbidden.class)
    public ResponseEntity<Response> handleNotAuthorizedException(final WebClientResponseException.Forbidden e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ServiceResponseBuilder.buildUnknownFailResponse(e));
    }

}