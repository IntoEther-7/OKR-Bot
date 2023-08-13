package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 11:10
 * @project okrbot
 */
public class DocumentMapper {
    private final String folderToken = "XfdTf6VPklVbRXdKbkVcdcT8nxf";

    /**
     * 返回文档信息Json
     *
     * @param tenant_access_token
     * @param documentName
     * @return
     * @throws UnirestException
     * @throws JsonProcessingException
     */
    public JsonString newDocument(String tenant_access_token, String documentName) throws UnirestException, JsonProcessingException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/docx/v1/documents")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Bearer t-g1048dcCO3NZQTWHCNN6FOIYTYUUXF4ZL7WG2XRF")
                .field("folder_token", "XfdTf6VPklVbRXdKbkVcdcT8nxf")
                .field("title", "test")
                .asString();

        return new JsonString(response.getBody());
    }
}
