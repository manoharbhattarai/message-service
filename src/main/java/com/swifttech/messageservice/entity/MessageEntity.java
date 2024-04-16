
package com.swifttech.messageservice.entity;

import com.swifttech.messageservice.core.base.entity.BaseAuditEntity;
import com.swifttech.messageservice.enums.BroadCastMode;
import com.swifttech.messageservice.enums.StatusEnum;
import com.swifttech.messageservice.payload.request.CustomerApiRequest;
import com.swifttech.messageservice.payload.request.CustomerSpecification;
import com.swifttech.messageservice.payload.request.NotificationDetails;
import com.swifttech.messageservice.enums.ChannelMode;
import com.swifttech.messageservice.enums.CommunicationMode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity extends BaseAuditEntity {

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private CustomerApiRequest customerIds;

    @Column(name = "is_schedule")
    private boolean schedule;

    private LocalDateTime scheduledTime;

    @Column(columnDefinition = "text")
    private String message;

    @Enumerated(EnumType.STRING)
    private ChannelMode channelMode;

    @Enumerated(EnumType.STRING)
    private BroadCastMode broadCastMode;

    @Enumerated(EnumType.STRING)
    private CommunicationMode communicationMode;

    @Enumerated(EnumType.STRING)
    private StatusEnum scheduledStatusEnum;

    @Type(value = JsonBinaryType.class)
    @Column(name = "notification_details", columnDefinition = "jsonb")
    private NotificationDetails notification;

    @Type(value = JsonBinaryType.class)
    @Column(name = "customer_specification", columnDefinition = "jsonb")
    private CustomerSpecification customer;

    private Long successCount;
    private Long failCount;


}