package com.swifttech.messageservice.util;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class ContentTypeFormBase64 {

    @Value("classpath:content.properties")
    private Resource contentPropertiesFile;

    private static final Map<byte[], String> BYTES_TO_CONTENT_TYPE = new HashMap<>();

    @PostConstruct
    public void init() {
        Properties properties = loadPropertiesFile(contentPropertiesFile);
        populateMagicBytesToContentType(properties);
    }

    private Properties loadPropertiesFile(Resource resource) {
        try {
            return PropertiesLoaderUtils.loadAllProperties(String.valueOf(resource));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    private void populateMagicBytesToContentType(Properties properties) {

        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            BYTES_TO_CONTENT_TYPE.put(key.getBytes(), value);
        }
    }


    public static String identifyContentType(byte[] data) {

        for (Map.Entry<byte[], String> entry : BYTES_TO_CONTENT_TYPE.entrySet()) {
            if (startsWithBytes(data, entry.getKey())) {
                return entry.getValue();
            }
        }
        throw new RuntimeException("Invalid File format !");
    }

    private static boolean startsWithBytes(byte[] data, byte[] pattern) {
        if (data.length < pattern.length) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (data[i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }
}

