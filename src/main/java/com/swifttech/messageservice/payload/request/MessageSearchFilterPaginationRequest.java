package com.swifttech.messageservice.payload.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchFilterPaginationRequest extends  PaginateRequest{

    private String searchText;
    private String channelMode;
    private String status;
    private String category;
    private String createdDateFrom;
    private String createdDateTo;
}
