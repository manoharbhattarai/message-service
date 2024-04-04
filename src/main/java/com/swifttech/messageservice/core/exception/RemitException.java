package com.swifttech.messageservice.core.exception;

import com.swifttech.messageservice.core.records.CodeRecord;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class RemitException extends RuntimeException implements BaseException {

    private final String code;
    private final HttpStatus httpStatus;
    @Setter
    private String debugMessage;

    public RemitException(CodeRecord codeRecord) {
        super(codeRecord.message_en());
        this.code = codeRecord.code();
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
        this.debugMessage = "";
    }

    public RemitException(CodeRecord codeRecord, String debugMessage) {
        super(codeRecord.message_en());
        this.code = codeRecord.code();
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
        this.debugMessage = debugMessage;
    }

    public RemitException(CodeRecord codeRecord, HttpStatus httpStatus) {
        super(codeRecord.message_en());
        this.code = codeRecord.code();
        this.httpStatus = httpStatus;
        this.debugMessage = "";
    }

    public RemitException(CodeRecord codeRecord, HttpStatus httpStatus, String debugMessage) {
        super(codeRecord.message_en());
        this.code = codeRecord.code();
        this.httpStatus = httpStatus;
        this.debugMessage = debugMessage;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
