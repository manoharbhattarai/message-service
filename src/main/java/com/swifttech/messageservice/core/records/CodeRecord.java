package com.swifttech.messageservice.core.records;

public record CodeRecord(String code, String message_en) {

    @Override
    public String toString() {
        return "CodeRecord{" +
                "code='" + code + '\'' +
                ", message_en='" + message_en + '\'' +
                '}';
    }
}
