package com.swifttech.messageservice.repository;

import com.swifttech.messageservice.entity.MessageHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageHistoryRepository extends JpaRepository<MessageHistoryEntity, UUID> {
}
