package com.hellocrop.okrbot.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DocumentCheckUtil {

    private final Properties properties;


    public DocumentCheckUtil() {
        properties = new Properties();
        try {
            File file = new File("src/main/resources/DocInfo/document.properties");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            properties.load(new FileReader(file, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDocumentIdThisWeek(String key) {
        return properties.containsKey(key) ? properties.getProperty(key) : null;
    }

    public void insertDocumentIdThisWeek(String key, String documentId) {
        properties.setProperty(key, documentId);
        try {
            properties.store(new FileWriter("src/main/resources/DocInfo/document.properties", StandardCharsets.UTF_8), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
