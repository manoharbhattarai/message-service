
package com.swifttech.messageservice.entity;

import com.swifttech.messageservice.core.base.entity.BaseAuditEntity;
import com.swifttech.messageservice.enums.StatusEnum;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageHistoryEntity extends BaseAuditEntity {

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private UUID customer;

    private LocalDateTime date;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    private String response;

    private StatusEnum status;

}