package com.swifttech.messageservice.core.base;

import com.swifttech.messageservice.core.records.CodeRecord;
import org.springframework.stereotype.Component;

@FunctionalInterface
@Component
public interface Codes {

    CodeRecord pick(String code);
}
