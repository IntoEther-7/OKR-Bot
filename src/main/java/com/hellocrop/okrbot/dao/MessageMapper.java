package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.Message;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class MessageMapper {
    public JsonString send2Person(String tenant_access_token, String userId, String content) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/im/v1/messages?receive_id_type=open_id")
                .header("Content-Type", "application/json")
                .header("Authorization", tenant_access_token)
                .body(JsonString.objectMapper.writeValueAsString(new Message(userId, content)))
                .asString();

        return new JsonString(response.getBody());
    }
}
