package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 10:06
 * @project okrbot
 */
public class OkrMapper {
    public JsonString getOkrsByEmployer(String tenant_access_token, String userId) throws UnirestException, JsonProcessingException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/okr/v1/users/%s/okrs?user_id_type=open_id&offset=0&limit=10&lang=zh_cn".formatted(userId))
                .header("Authorization", tenant_access_token)
                .asString();

        return new JsonString(response.getBody());
    }
}
