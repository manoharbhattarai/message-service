package com.swifttech.messageservice.controller;

import com.swifttech.messageservice.annotation.MasterRestController;
import com.swifttech.messageservice.core.builder.ServiceResponseBuilder;
import com.swifttech.messageservice.core.model.Response;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.ComposeMessageRequest;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.PaginationRequest;
import com.swifttech.messageservice.repository.MessageRepository;
import com.swifttech.messageservice.service.MessageService;
import com.swifttech.messageservice.util.ApiConnector;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@MasterRestController
@RequiredArgsConstructor
@Tag(name = "message")
@Validated
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final MessageSource messageSource;
    private final MessageRepository messageRepository;

    private final ApiConnector apiConnector;

    @PostMapping("/message/create")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operation success",
            content = @Content(schema = @Schema(implementation = MessageRequest.class)))})
    public ResponseEntity<Response> sendMessage(@RequestBody MessageRequest messageRequest) throws IOException, InterruptedException {

        messageService.sendScheduleMessage(messageRequest);

        return ResponseEntity.ok(ServiceResponseBuilder.buildSuccessResponse(
                messageSource.getMessage("message created successfully",
                        null, LocaleContextHolder.getLocale())
        ));
    }

    @GetMapping("/message/list")
    public ResponseEntity<Response> listAllMessage(@RequestBody PaginationRequest pagination) {

      return ResponseEntity.ok(ServiceResponseBuilder
              .buildSuccessResponse(messageService.messageList(pagination)));

    }

    @PostMapping("/upload/file")
    public void uploadCSVFile(@RequestParam("file") MultipartFile multipartFile) {

        log.info("List of customer: {} ",multipartFile.getOriginalFilename());

        List<String> customerList = messageService.readCSVFile(multipartFile);


    }

    @GetMapping("/send")
    public void senBulkMessage(){
        // call scheduler
        List<Message> message = messageRepository.findAllByScheduleIsTrue();
        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
        for (Message message1: message){
             composeMessageRequest = ComposeMessageRequest.builder()
                    .message(message1.getMessage())
                    .mobileNumber(message1.getCustomerIds().getCustomerIdsList())
                    .build();
        }
        apiConnector.sendSMS(composeMessageRequest);
    }






}
