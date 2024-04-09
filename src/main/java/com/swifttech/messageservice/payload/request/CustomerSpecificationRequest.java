package com.swifttech.messageservice.payload.request;

import com.swifttech.messageservice.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSpecificationRequest {

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
