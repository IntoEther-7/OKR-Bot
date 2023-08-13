package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:53
 * @project okrbot
 */
public class EmployerMapper {
    public JsonString getPersonsByDepartment(String tenant_access_token, String departmentId) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/contact/v3/users?user_id_type" +
                        "=open_id&department_id_type=department_id&department_id=%s&page_size=100".formatted(departmentId))
                .header("Authorization", tenant_access_token)
                .asString();

        return new JsonString(response.getBody());
    }
}
