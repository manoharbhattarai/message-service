package com.swifttech.messageservice.payload.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerApiRequest {

    List<String> customerList;

}
