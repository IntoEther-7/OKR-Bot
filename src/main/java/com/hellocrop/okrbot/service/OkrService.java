package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.*;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.hellocrop.okrbot.entity.okr.Okr;
import com.hellocrop.okrbot.entity.okr.OkrList;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import com.hellocrop.okrbot.entity.okr.view.UserView;
import com.hellocrop.okrbot.util.*;
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
    private String documentName = "全员周报（%s）".formatted(dateUtil.string());

    public void weekReport(String appId, String appSecret) throws Exception {
        // 鉴权
        JsonString auth = new AuthMapper().auth(appId, appSecret);
        tenant_access_token = "Bearer " + auth.get("tenant_access_token").string();

        List<String> userIdxList = getAllEmployee();

        // 根据人员获取OKR
        Map<String, OkrList> okrListMap = new HashMap<>();
        for (String userIdx : userIdxList) {
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);
            okrListMap.put(userIdx, okrList);
        }


        // 处理OKR数据, 拿到每个人的进展
        // Map<String, List<JsonString>> allPgs = getAllPgs(okrListMap);
        Map<String, JsonString> id2pgs = id2pgs(okrListMap);

        Map<String, UserView> map = new ProgressBlockUtil().constructMap(okrListMap, id2pgs);

        List<BlockMessage> blockMessages = BlockGenerator.generateDocumentBlock(map.values().stream().toList());
        // new JsonString(JsonString.objectMapper.writeValueAsString(blockMessages.get(0)))

        DocumentMapper documentMapper = new DocumentMapper();
        String documentName = "全员周报（%s）".formatted(dateUtil.string());
        DocumentCheckUtil documentCheckUtil = new DocumentCheckUtil();

        String documentId;
        if (documentCheckUtil.getDocumentIdThisWeek(documentName) == null) {
            // 如果不存在就新建
            JsonString newDocument = documentMapper.newDocument(tenant_access_token, documentName);
            documentId = newDocument.get("data").get("document").get("document_id").string();
            documentCheckUtil.insertDocumentIdThisWeek(documentName, documentId);
        } else {
            // 如果存在就刷新
            documentId = documentCheckUtil.getDocumentIdThisWeek(documentName);
            documentMapper.cleanDocument(documentId, documentId);
        }

        // 写入文档
        for (BlockMessage blockMessage : blockMessages) {
            // debug
            // JsonString.objectMapper.writeValueAsString(blockMessage)
            log.info(documentMapper.insertDocument(tenant_access_token, documentId, blockMessage).getString());
            Thread.sleep(334);
        }

        new MessageMapper().send2Person(tenant_access_token, "ou_d37e7a78deaefb355cc3390f224e5900", "https://automq66.feishu.cn/docx/%s".formatted(documentId));
        // 新建文档
        // JsonString documentJsonString = new DocumentMapper().newDocument(tenant_access_token,
        //         "全员周报（%s）".formatted(dateUtil.string()));
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
                employeeIdList.add(employee.get(i).get("open_id").string());
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
        List<Okr> okrs = JsonString.objectMapper.readValue(okrJsonString.get("data").get("okr_list").getString(), new TypeReference<List<Okr>>() {
        });
        return new OkrList(userIdx, okrs);
    }

    // 获取个人进展转变为block
    private List<Object> pgs2block(List<ProgressRecord> progressRecords) throws UnirestException, JsonProcessingException {
        List<Object> blocks = new LinkedList<>();

        ProgressMapper progressMapper = new ProgressMapper();
        Iterator<ProgressRecord> iterator = progressRecords.iterator();
        while (iterator.hasNext()) {
            ProgressRecord next = iterator.next();
            JsonString progress = progressMapper.getProgress(tenant_access_token, next.getId());

            // 判断时间对不对, 是不是这周的
            Date before = new Date(Long.parseLong(progress.get("data").get("modify_time").string()));
            if (dateUtil.inThisWeek(before)) {
                List<Object> pgsBlock = JsonString.objectMapper.readValue(JsonString.objectMapper.writeValueAsString(progress.get("data").get("content").get("blocks").getNode()), new TypeReference<>() {
                });
                blocks.add(pgsBlock);
            }
        }
        return blocks;

    }

    // 获取每个人的进展具体内容，返回map
    private Map<String, List<JsonString>> getAllPgs(Map<String, OkrList> okrListMap) throws UnirestException, JsonProcessingException {
        Map<String, List<JsonString>> map_pgs_js = new LinkedHashMap<>();

        Map<String, List<ProgressRecord>> map_pgs = new OkrUtil(okrListMap).getMap_pgs();
        ProgressMapper progressMapper = new ProgressMapper();
        Iterator<Map.Entry<String, List<ProgressRecord>>> map_pgsEntryIterator = map_pgs.entrySet().iterator();
        while (map_pgsEntryIterator.hasNext()) {
            Map.Entry<String, List<ProgressRecord>> next = map_pgsEntryIterator.next();
            Iterator<ProgressRecord> progressRecordIterator = next.getValue().iterator();
            while (progressRecordIterator.hasNext()) {
                // 单个进展
                JsonString progress = progressMapper.getProgress(tenant_access_token, progressRecordIterator.next().getId());
                if (!map_pgs_js.containsKey(next.getKey())) {
                    map_pgs_js.put(next.getKey(), new LinkedList<>());
                }
                map_pgs_js.get(next.getKey()).add(progress);
            }
            if (!map_pgs_js.containsKey(next.getKey())) {
                map_pgs_js.put(next.getKey(), null);
            }
        }

        return map_pgs_js;
    }


    private Map<String, JsonString> id2pgs(Map<String, OkrList> okrListMap) throws UnirestException, JsonProcessingException {
        Map<String, JsonString> map_pgs_js = new LinkedHashMap<>();

        Map<String, List<ProgressRecord>> map_pgs = new OkrUtil(okrListMap).getMap_pgs();
        ProgressMapper progressMapper = new ProgressMapper();
        Iterator<Map.Entry<String, List<ProgressRecord>>> map_pgsEntryIterator = map_pgs.entrySet().iterator();
        while (map_pgsEntryIterator.hasNext()) {
            Map.Entry<String, List<ProgressRecord>> next = map_pgsEntryIterator.next();
            Iterator<ProgressRecord> progressRecordIterator = next.getValue().iterator();
            while (progressRecordIterator.hasNext()) {
                String id = progressRecordIterator.next().getId();
                // 单个进展
                JsonString progress = progressMapper.getProgress(tenant_access_token, id);
                if (dateUtil.inThisWeek(new Date(Long.parseLong(progress.get("data").get("modify_time").string()))))
                    map_pgs_js.put(id, progress);
            }
        }

        return map_pgs_js;
    }

    private void filterUser(Map<String, OkrList> map) {
        // TODO: 删除用户组内的成员
        // TODO: 完善用户组查询
    }
}
