package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.*;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.okr.Okr;
import com.hellocrop.okrbot.entity.okr.OkrList;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import com.hellocrop.okrbot.util.DateUtil;
import com.hellocrop.okrbot.util.OkrUtil;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:22
 * @project okrbot
 */
@Slf4j
public class OkrService {

    private String tenant_access_token;
    private OkrMapper okrMapper = new OkrMapper();
    private DateUtil dateUtil = new DateUtil();

    public void weekReport(String appId, String appSecret) throws Exception {
        // ��Ȩ
        JsonString auth = new AuthMapper().auth(appId, appSecret);
        tenant_access_token = "Bearer " + auth.get("tenant_access_token").string();

        List<String> userIdxList = getAllEmployee();

        // ������Ա��ȡOKR
        Map<String, OkrList> okrListMap = new HashMap<>();
        for (String userIdx : userIdxList) {
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);
            okrListMap.put(userIdx, okrList);
        }

        // �½��ĵ�
        JsonString documentJsonString = new DocumentMapper().newDocument(tenant_access_token,
                "ȫԱ�ܱ���%s��".formatted(dateUtil.string()));

        // ����OKR����, ȡ����չ
        Map<String, List<ProgressRecord>> map_pgs = new OkrUtil(okrListMap).getMap_pgs();


        // ������չ��
        Map<String, List<Object>> pgsBlocks = new LinkedHashMap<>();
        Iterator<Map.Entry<String, List<ProgressRecord>>> entryIterator = map_pgs.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, List<ProgressRecord>> next = entryIterator.next();
            pgsBlocks.put(next.getKey(), pgs2block(next.getValue()));
        }

        // Ϊÿ���˹�����
        Iterator<Map.Entry<String, List<Object>>> iterator = pgsBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Object>> next = iterator.next();
            String userIdx = next.getKey();
            List<Object> userPgs = next.getValue();

            // TODO: ����һ�������

            // TODO: ����������
        }


        log.info(null);
    }

    private List<String> getAllEmployee() throws UnirestException, IOException {
        // ��ȡ������Ϣ
        JsonString allDepartment = new DepartmentMapper().allDepartment(tenant_access_token);
        JsonString departments = allDepartment.get("data").get("items");
        List<String> departmentIdList = new LinkedList<>();
        for (int i = 0; i < departments.getNode().size(); i++) {
            departmentIdList.add(departments.get(i).get("department_id").string());
        }
        departmentIdList.add("0");

        // ���ݲ��Ų�ѯ������Ա��Ϣ
        List<String> employeeIdList = new LinkedList<>();
        EmployerMapper employerMapper = new EmployerMapper();
        for (String departmentId : departmentIdList) {
            JsonString personsByDepartment = employerMapper.getPersonsByDepartment(tenant_access_token, departmentId);

            // ��ѯ��Ա
            JsonString employee = personsByDepartment.get("data").get("items");
            for (int i = 0; i < employee.getNode().size(); i++) {
                employeeIdList.add(employee.get(i).get("user_id").string());
            }
        }
        return employeeIdList;
    }


    // �õ�okr
    private JsonString getOkrsByEmployer(String userId) throws UnirestException, JsonProcessingException {
        return okrMapper.getOkrsByEmployer(tenant_access_token, userId);
    }

    // ����OKR����, �õ�Ŀ������
    private OkrList deserializationOKR(String userIdx, JsonString okrJsonString) throws JsonProcessingException {
        List<Okr> okrs = JsonString.objectMapper.readValue(okrJsonString.get("data").get("okr_list").getString(),
                new TypeReference<List<Okr>>() {
                });
        return new OkrList(userIdx, okrs);
    }

    // ��ȡ���˽�չת��Ϊblock
    private List<Object> pgs2block(List<ProgressRecord> progressRecords) throws UnirestException,
            JsonProcessingException {
        List<Object> blocks = new LinkedList<>();

        ProgressMapper progressMapper = new ProgressMapper();
        Iterator<ProgressRecord> iterator = progressRecords.iterator();
        while (iterator.hasNext()) {
            ProgressRecord next = iterator.next();
            JsonString progress = progressMapper.getProgress(tenant_access_token, next.getId());

            // �ж�ʱ��Բ���, �ǲ������ܵ�
            Date before = new Date(Long.parseLong(progress.get("data").get("modify_time").string()));
            if (dateUtil.inThisWeek(before)) {
                List<Object> pgsBlock =
                        JsonString.objectMapper.readValue(JsonString.objectMapper.writeValueAsString(progress.get(
                                "data").get("content").get("blocks").getNode()), new TypeReference<>() {
                        });
                blocks.addAll(pgsBlock);
            }
        }
        return blocks;

    }
}
