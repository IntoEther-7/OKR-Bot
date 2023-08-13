package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.AuthMapper;
import com.hellocrop.okrbot.dao.DepartmentMapper;
import com.hellocrop.okrbot.dao.EmployerMapper;
import com.hellocrop.okrbot.dao.OkrMapper;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.okr.Okr;
import com.hellocrop.okrbot.entity.okr.OkrList;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:22
 * @project okrbot
 */
@Slf4j
public class OkrService {

    private String tenant_access_token;
    private OkrMapper okrMapper = new OkrMapper();

    public void weekReport(String appId, String appSecret) throws Exception {
        // 鉴权
        JsonString auth = new AuthMapper().auth(appId, appSecret);
        tenant_access_token = "Bearer " + auth.get("tenant_access_token").string();

        List<String> userIdxList = getAllEmployee();

        // 根据人员获取OKR
        List<OkrList> okrLists = new LinkedList<>();
        for (String userIdx : userIdxList) {
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);
            okrLists.add(okrList);
        }
        log.info(null);
    }

    private List<String> getAllEmployee() throws UnirestException, IOException {
        // 获取部门信息
        JsonString allDepartment = new DepartmentMapper().allDepartment(tenant_access_token);
        JsonString departments = allDepartment.get("data").get("items");
        List<String> departmentIdList = new LinkedList<>();
        for (int i = 0; i < departments.getNode().size(); i++) {
            departmentIdList.add(departments.get(i).get("department_id").string());
        }
        departmentIdList.add("0");

        // 根据部门查询所有人员信息
        List<String> employeeIdList = new LinkedList<>();
        EmployerMapper employerMapper = new EmployerMapper();
        for (String departmentId : departmentIdList) {
            JsonString personsByDepartment = employerMapper.getPersonsByDepartment(tenant_access_token, departmentId);

            // 查询人员
            JsonString employee = personsByDepartment.get("data").get("items");
            for (int i = 0; i < employee.getNode().size(); i++) {
                employeeIdList.add(employee.get(i).get("user_id").string());
            }
        }
        return employeeIdList;
    }


    // 拿到okr
    private JsonString getOkrsByEmployer(String userId) throws UnirestException, JsonProcessingException {
        return okrMapper.getOkrsByEmployer(tenant_access_token, userId);
    }

    // 处理OKR数据, 拿到目标数据
    private OkrList deserializationOKR(String userIdx, JsonString okrJsonString) throws JsonProcessingException {
        List<Okr> okrs = JsonString.objectMapper.readValue(okrJsonString.get("data").get("okr_list").getString(),
                new TypeReference<List<Okr>>() {
                });
        return new OkrList(userIdx, okrs);
    }

    // 处理OKR数据, 取出O和KR
}
