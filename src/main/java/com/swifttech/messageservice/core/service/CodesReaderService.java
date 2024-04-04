package com.swifttech.messageservice.core.service;

import com.swifttech.messageservice.core.base.Codes;
import com.swifttech.messageservice.core.exception.RemitException;
import com.swifttech.messageservice.core.records.CodeRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
@RequiredArgsConstructor
public class CodesReaderService {

    private final Codes codes;

    /**
     * Read codes properties file list.
     *
     * @return the list
     */
    public List<CodeRecord> readCodesPropertiesFile() {
        List<CodeRecord> codeRecords = new ArrayList<>();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("code.properties")) {
            if (input == null) {
                log.error("unable to find code.properties");
                throw new RemitException(codes.pick("MCF000"));
            }

            // Load the properties file
            Properties properties = new Properties();
            properties.load(input);

            // Convert properties to Map
            for (String key : properties.stringPropertyNames()) {
                codeRecords.add(new CodeRecord(key, properties.getProperty(key)));
            }

        } catch (IOException ex) {
            throw new RemitException(codes.pick("MCF000"), ex.getMessage());
        }

        return codeRecords;
    }

}
