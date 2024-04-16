package com.swifttech.messageservice.mapper;

import com.swifttech.messageservice.entity.MessageEntity;
import com.swifttech.messageservice.payload.request.MessageRequest;
import com.swifttech.messageservice.payload.response.MessageList;
import com.swifttech.messageservice.payload.response.MessagesResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageEntity toEntity(MessageRequest messageRequest);

    @Mapping(target = "id", ignore = true)
    MessageEntity toDto(MessageRequest messageRequest, @MappingTarget MessageEntity messageEntity);

    MessagesResponse toResponse(MessageEntity messageEntity);
    MessageList toMessageList(MessageEntity messageEntity);


}
