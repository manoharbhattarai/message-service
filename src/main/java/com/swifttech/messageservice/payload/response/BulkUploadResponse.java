package com.swifttech.messageservice.payload.response;

import com.swifttech.messageservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadResponse {

    private String messageName;
    private String channel;
    private String fileName;
    private UUID createdBy;
    private LocalDateTime startDateTime;
    private String category;
    private Status status;
}
