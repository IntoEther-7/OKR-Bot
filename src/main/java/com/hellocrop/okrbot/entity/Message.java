package com.hellocrop.okrbot.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Message {
    String receive_id;
    String msg_type;
    String content;

    public Message(String receive_id, String content) throws JsonProcessingException {
        this.receive_id = receive_id;
        this.msg_type = "text";
        Map<String, String> map = new HashMap<>();
        map.put("text", content);
        this.content = JsonString.objectMapper.writeValueAsString(map);
    }
}
