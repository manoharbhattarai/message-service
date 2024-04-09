package com.swifttech.messageservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginateRequest {

    Integer pageNo;
    Integer pageSize;
    String sortBy;
    String direction;
}
