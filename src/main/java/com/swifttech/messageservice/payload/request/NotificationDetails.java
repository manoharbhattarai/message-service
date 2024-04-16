package com.swifttech.messageservice.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {

    private String redirectTo;
    private String redirectType;
    @NotNull(message = "Title is required")
    private String title;
    private String image;


}
