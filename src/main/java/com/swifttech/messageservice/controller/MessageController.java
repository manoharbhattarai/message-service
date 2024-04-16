package com.swifttech.messageservice.controller;

import com.swifttech.messageservice.annotation.MasterRestController;
import com.swifttech.messageservice.core.builder.ServiceResponseBuilder;
import com.swifttech.messageservice.core.model.Response;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.request.MessageSearchFilterPaginationRequest;
import com.swifttech.messageservice.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@MasterRestController
@RequiredArgsConstructor
@Tag(name = "message")
@Validated
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final MessageSource messageSource;

    @PostMapping("/create")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operation success",
            content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Operation failed when creating new message")})
    public ResponseEntity<Response> sendMessage(@RequestBody MessageRequest messageRequest) throws IOException, InterruptedException {

        messageService.createMessage(messageRequest);

        return ResponseEntity.ok(ServiceResponseBuilder.buildSuccessResponse(
                messageSource.getMessage("message.message.create.success",
                        null, LocaleContextHolder.getLocale())
        ));
    }

    @PostMapping("/list")
    @Operation(summary = "Fetching message list")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operation success",
            content = @Content(schema = @Schema(implementation = Response.class)))})
    public Mono<ResponseEntity<Response>> listAllMessages(@RequestBody MessageSearchFilterPaginationRequest request) {

        return Mono.just(ResponseEntity.ok(ServiceResponseBuilder
                .buildSuccessResponse(messageService.messageList(request))));

    }

    @PostMapping("/upload/file")
    public void uploadCSVFile(@RequestParam("file") MultipartFile multipartFile) {

        log.info("List of customer: {} ", multipartFile.getOriginalFilename());

        List<String> customerList = messageService.readCSVFile(multipartFile);


    }

//    @GetMapping("/send")
//    public void senBulkMessage(){
//        // call scheduler
//        /*List<Message> message = messageRepository.findAllByScheduleIsTrue();
//        ComposeMessageRequest composeMessageRequest = new ComposeMessageRequest();
//        for (Message message1: message){
//             composeMessageRequest = ComposeMessageRequest.builder()
//                    .message(message1.getMessage())
//                    .mobileNumber(message1.getCustomerIds().getCustomerIdsList())
//                    .build();
//        }
//        apiConnector.sendMessage(composeMessageRequest);*/
//       messageService.checkScheduledMessages();
//    }


    @PutMapping("/edit/{id}")
    @Operation(summary = "Update message")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operation success",
            content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Operation failed when user not found.")})
    public ResponseEntity<Response> editMessage(@PathVariable("id") UUID id, @Valid @RequestBody MessageRequest messageRequest) {
        messageService.updateMessage(id, messageRequest);
        return ResponseEntity.ok(ServiceResponseBuilder.buildSuccessResponse(null,
                messageSource.getMessage("message.message.update.success",
                        null, LocaleContextHolder.getLocale())));
    }

}
