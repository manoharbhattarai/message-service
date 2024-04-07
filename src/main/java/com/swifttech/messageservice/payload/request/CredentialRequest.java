package com.swifttech.messageservice.payload.request;

import java.util.Date;
import java.util.List;

public record CredentialRequest(String isClientLogin, String username,
                                String password, String organisationCode, Date date,  String batchId, List<SmsDetailsRequest> smsDetails) {

}
