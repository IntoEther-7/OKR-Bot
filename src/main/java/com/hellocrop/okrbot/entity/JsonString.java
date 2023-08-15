package com.hellocrop.okrbot.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:17
 * @project okrbot
 */
@Data
public class JsonString {
    public static ObjectMapper objectMapper = new ObjectMapper();
    private String string;
    private JsonNode node;

    static {
        // objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public JsonString(String json) throws JsonProcessingException {
        this.string = json;
        node = objectMapper.readTree(json);
    }

    public JsonString get(String name) throws JsonProcessingException {
        JsonNode jsonNode = node.get(name);
        return new JsonString(objectMapper.writeValueAsString(jsonNode));
    }

    public JsonString get(int i) throws JsonProcessingException {
        JsonNode jsonNode = node.get(i);
        return new JsonString(objectMapper.writeValueAsString(jsonNode));
    }

    public String string() throws JsonProcessingException {
        return node.asText();
    }
}
