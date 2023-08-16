package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.hellocrop.okrbot.entity.JsonString;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

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

    public List<String> belongUserGroupId(String tenant_access_token, String openId)  throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/contact/v3/group/member_belong?member_id=%s&member_id_type=open_id&page_size=1000".formatted(openId))
                .header("Authorization", tenant_access_token)
                .asString();

        JsonNode jsonNode = JsonString.objectMapper.readTree(response.getBody()).get("data").get("group_list");
        return JsonString.objectMapper.readValue(jsonNode.toString(), new TypeReference<List<String>>() {
        });

    }
}
