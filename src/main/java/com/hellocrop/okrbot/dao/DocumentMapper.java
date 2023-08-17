package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 11:10
 * @project okrbot
 */
public class DocumentMapper {
    private final String folderToken = "XfdTf6VPklVbRXdKbkVcdcT8nxf";
    private final BlockMapper blockMapper = new BlockMapper();

    /**
     * 新建文档，返回文档信息Json
     *
     * @param tenant_access_token
     * @param documentName
     * @return
     * @throws UnirestException
     * @throws JsonProcessingException
     */
    public String newDocument(String tenant_access_token, String documentName) throws UnirestException, JsonProcessingException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/docx/v1/documents").header("Content-Type", "application/x-www-form-urlencoded").header("Authorization", tenant_access_token).field("folder_token", folderToken).field("title", documentName).asString();

        JsonNode jsonNode = JsonString.objectMapper.readTree(response.getBody());
        String text = jsonNode.get("data").get("document").get("document_id").asText();

        return text;
    }

    public JsonString cleanDocument(String tenant_access_token, String documentId) throws UnirestException, JsonProcessingException {
        JsonString blocks = blockMapper.getBlocks(tenant_access_token, documentId, documentId);
        int size = blocks.get("data").get("items").getNode().size();
        JsonString jsonString = blockMapper.deleteBlock(tenant_access_token, documentId, documentId, 0, size);
        return jsonString;
    }

    public JsonString insertDocument(String tenant_access_token, String documentId, BlockMessage blockMessage) throws UnirestException, JsonProcessingException {
        return blockMapper.insertBlock(tenant_access_token, documentId, documentId, blockMessage);
    }

    public Map<String, String> documentMap(String tenant_access_token) throws UnirestException, JsonProcessingException {
        Map<String, String> docMap = new LinkedHashMap<>();

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/drive/v1/files?direction=DESC&folder_token=%s&order_by=EditedTime&page_size=200".formatted(folderToken)).header("Authorization", tenant_access_token).asString();


        JsonNode jsonNode = JsonString.objectMapper.readTree(response.getBody()).get("data").get("files");
        for (int i = 0; i < jsonNode.size(); i++) {
            String name = jsonNode.get("name").toString();
            String token = jsonNode.get("token").toString();
            docMap.put(name, token);
        }

        return docMap;
    }
}
