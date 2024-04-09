package com.swifttech.messageservice.payload.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataPaginationResponse {

    long totalElementCount;
    List<?> result;
}
