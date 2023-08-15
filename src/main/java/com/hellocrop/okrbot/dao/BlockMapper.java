package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:53
 * @project okrbot
 */
public class BlockMapper {
    public JsonString insertBlock(String tenant_access_token, String documentId, String blockId, BlockMessage blockMessage) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/docx/v1/documents/%s/blocks/%s/children".formatted(documentId, blockId))
                .header("Content-Type", "application/json")
                .header("Authorization", tenant_access_token)
                .body(JsonString.objectMapper.writeValueAsString(blockMessage))
                .asString();
        return new JsonString(response.getBody());
    }

    public JsonString deleteBlock(String tenant_access_token, String documentId, String blockId, int start_index, int end_index) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);

        Map<String, Integer> map = new HashMap<>();
        map.put("start_index", start_index);
        map.put("end_index", end_index);


        HttpResponse<String> response = Unirest.delete("https://open.feishu.cn/open-apis/docx/v1/documents/%s/blocks/%s/children/batch_delete".formatted(documentId, blockId))
                .header("Content-Type", "application/json")
                .header("Authorization", tenant_access_token)
                .body(JsonString.objectMapper.writeValueAsString(map))
                .asString();
        return new JsonString(response.getBody());
    }

    public JsonString getBlocks(String tenant_access_token, String documentId, String blockId) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);

        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/docx/v1/documents/%s/blocks/%s/children".formatted(documentId, blockId))
                .header("Content-Type", "application/json")
                .header("Authorization", tenant_access_token)
                .asString();
        return new JsonString(response.getBody());
    }
}
