package com.hellocrop.okrbot.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 21:58
 * @project okrbot
 */
public class ProgressMapper {
    public ProgressRecord getProgress(String token, String pgsIdx) throws UnirestException, JsonProcessingException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("https://open.feishu.cn/open-apis/okr/v1/progress_records/%s".formatted(pgsIdx)).header("Authorization", token).asString();
        return JsonString.objectMapper.readValue((new JsonString(response.getBody()).get("data").getString()), ProgressRecord.class);
    }
}
