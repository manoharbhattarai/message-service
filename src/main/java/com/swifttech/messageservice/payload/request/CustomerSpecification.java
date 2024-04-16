package com.swifttech.messageservice.payload.request;

import com.swifttech.messageservice.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSpecification {

    @NotNull(message ="Nationality is required")
    @NotEmpty(message ="Nationality is required.")
    private String nationality;
    @NotNull(message = "Registration Type is required")
    @NotEmpty(message = "Registration Type is required.")
    private String registrationType;
    @NotNull(message = "Platform is required.")
    @NotEmpty(message = "Platform is required.")
    private String platform;
    @NotNull(message = "Gender is required.")
    @NotEmpty(message = "Gender is required.")
    @Enumerated(EnumType.STRING)
    private Gender gender;


}
