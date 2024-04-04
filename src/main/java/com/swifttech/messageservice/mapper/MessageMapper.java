package com.swifttech.messageservice.mapper;

import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.response.AllMessagesResponse;
import com.swifttech.messageservice.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    Message toEntity(MessageRequest messageRequest);

    MessageRequest toDto(Message message);

    AllMessagesResponse toResponse(Message message);
}
