package com.swifttech.messageservice.core.config;

import com.swifttech.messageservice.core.base.Codes;
import com.swifttech.messageservice.core.records.CodeRecord;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.swifttech.messageservice.core.constant.AppConstant.CODE_NOT_REGISTERED_MESSAGE;

@Configuration
public class CodeConfig {

    private static final Map<String, String> lookup = new HashMap<>();
    @Value("classpath:code.properties") // Load the file from the classpath
    private Resource codePropertiesFile;

    @Bean
    public Codes getCodeLookups() {
        return code -> new CodeRecord(code, lookup.getOrDefault(code, CODE_NOT_REGISTERED_MESSAGE));
    }

    @PostConstruct
    public void initialize() {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(codePropertiesFile);

            for (String key : properties.stringPropertyNames()) {
                String value = properties.getProperty(key);
                lookup.put(key, value);
            }
        } catch (IOException e) {
            // Handle exception appropriately

        }
    }
}