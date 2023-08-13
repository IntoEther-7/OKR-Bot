package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import okhttp3.*;

import java.io.IOException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 8:57
 * @project okrbot
 */
public class AuthMapper {

    public JsonString auth(String appId, String appSecret) throws UnirestException, JsonProcessingException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("app_id", "cli_a45f78cb4a79500e")
                .field("app_secret", "9ZIvC5EoYrqHqMSBE6agV62t7IWBrvzT")
                .asString();


        return new JsonString(response.getBody());
    }
}
