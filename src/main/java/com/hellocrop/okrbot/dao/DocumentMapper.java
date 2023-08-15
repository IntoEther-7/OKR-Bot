package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 11:10
 * @project okrbot
 */
public class DocumentMapper {
    private final String folderToken = "Pg1BfZ7sOl1gRCdHpm2ckxX0nnd";
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
    public JsonString newDocument(String tenant_access_token, String documentName) throws UnirestException, JsonProcessingException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/docx/v1/documents").header("Content-Type", "application/x-www-form-urlencoded").header("Authorization", tenant_access_token).field("folder_token", folderToken).field("title", documentName).asString();

        return new JsonString(response.getBody());
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
    }
