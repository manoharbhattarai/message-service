package com.swifttech.messageservice.payload.request;

import com.swifttech.messageservice.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSpecificationRequest {

    private String nationality;
    private String registrationType;
    private String platform;
    @Enumerated(EnumType.STRING)
    private Gender gender;


}
