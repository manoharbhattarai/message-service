package com.swifttech.messageservice.util;

import com.swifttech.messageservice.core.model.ApiResponse;
import com.swifttech.messageservice.payload.request.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApiConnector {

    private final WebClient.Builder webClient;

    public ApiResponse getCustomerList(CustomerApiRequest customerApiRequest) {
        log.info("Calling customer service");

        var apiResponse = webClient.build()
                .post()
                .uri("https://uat-gateway.swifttech.com.np/api/v2/customer/mobile-by-ids")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerApiRequest)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();

        log.info("Customer Response: {}", apiResponse);
        return apiResponse;

    }

    public ApiResponse getCountryList(CountryRequest countryRequest) {
        var apiResponse = webClient.build()
                .post()
                .uri("http://")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(countryRequest)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();
        log.info("Response: {}", apiResponse);
        return apiResponse;


    }

    public ApiResponse sendSMS(ComposeMessageRequest messageRequest) {

        List<SmsDetailsRequest> requests;
          requests=  messageRequest.getMobileNumber().stream().parallel().map(m -> {
              return SmsDetailsRequest.builder()
                    .receiverNo(BigInteger.valueOf(Long.parseLong(m)))
                    .message(messageRequest.getMessage())
                    .build();
            }).collect(Collectors.toList());


//        for (String mobile : messageRequest.getMobileNumber()) {
//            SmsDetailsRequest smsDetailsRequest = SmsDetailsRequest.builder()
//                    .receiverNo(BigInteger.valueOf(Long.parseLong(mobile)))
//                    .message(messageRequest.getMessage())
//                    .build();
//            requests.add(smsDetailsRequest);
//        }
        try {
            CredentialRequest credentialRequest =
                    new CredentialRequest("N",
                            "RemitTest",
                            "Remit@Test123",
                            "RemitTest", Date.from(Instant.now()), "B1", requests);


            HttpHeaders headers = new HttpHeaders();
            headers.add("OrganisationCode", "RemitTest");
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth("RemitTest", "Remit@Test123");

            ApiResponse response = webClient.build()
                    .post()
                    .uri("https://fastapi.swifttech.com.np:8080/api/Sms/ExecuteSendSms")
                    .headers(h -> h.addAll(headers))
                    .bodyValue(credentialRequest)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();


            log.info("RESPONSE {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage(), e);

            return null;
        }
    }


}
