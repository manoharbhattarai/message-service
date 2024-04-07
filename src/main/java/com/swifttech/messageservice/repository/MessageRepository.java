package com.swifttech.messageservice.repository;

import com.swifttech.messageservice.enums.Status;
import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.MessageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByScheduleIsTrue();


}
