package com.swifttech.messageservice.mapper;

import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.response.MessageList;
import com.swifttech.messageservice.payload.response.MessagesResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    Message toEntity(MessageRequest messageRequest);

    @Mapping(target = "id", ignore = true)
    Message toDto(MessageRequest messageRequest,@MappingTarget Message message);

    MessagesResponse toResponse(Message message);
    MessageList toMessageList(Message message);


}
