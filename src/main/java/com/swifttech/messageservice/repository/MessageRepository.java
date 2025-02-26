package com.swifttech.messageservice.repository;

import com.swifttech.messageservice.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID>, JpaSpecificationExecutor<MessageEntity> {

    List<MessageEntity> findAllByScheduleIsTrue();


}
