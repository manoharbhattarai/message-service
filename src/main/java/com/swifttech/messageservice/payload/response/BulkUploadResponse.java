package com.swifttech.messageservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadResponse {

    private String messageName;
    private String channel;
    private String fileName;
    private String createdBy;
    private String startDateTime;
    private String category;
    private String status;
}
