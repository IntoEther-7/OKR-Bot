package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import okhttp3.*;

import java.io.IOException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:28
 * @project okrbot
 */
public class DepartmentMapper {
    public JsonString allDepartment(String tenant_access_token) throws IOException, UnirestException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/contact/v3/departments?parent_department_id=0&fetch_child=true")
                .header("Authorization", tenant_access_token)
                .asString();

        return new JsonString(response.getBody());
    }
}
