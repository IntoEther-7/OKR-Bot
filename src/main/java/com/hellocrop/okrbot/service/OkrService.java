package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.*;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.hellocrop.okrbot.entity.okr.*;
import com.hellocrop.okrbot.entity.okr.view.*;
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

    public OkrService() {
    }


    public void weekReport(String appId, String appSecret) throws Exception {
        // 鉴权
        JsonString auth = new AuthMapper().auth(appId, appSecret);
        tenant_access_token = "Bearer " + auth.get("tenant_access_token").string();

        List<String> userIdxList = getAllEmployee();

        // TODO: 根据用户组过滤
        filterUser(userIdxList);

        List<Block> content = new LinkedList<>();
        for (String userIdx : userIdxList) {
            // 根据人员获取OKR
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);

            // 解析
            UserView userView = parseOne(okrList, true);

            // 构建这个人的blocks
            content.addAll(constructBlock(userView));
        }

        // 插入序言
        // 本文档由办公自动化系统根据 OKR 的进展自动生成，用于向全员同步短期进展和计划，如需评论，可点击具体链接跳转到 OKR 系统进行评论和转发。
        // 最后更新时间：2023/01/01 22:00
        BlockGenerator.insertIntro(content);

        // 分割blocks
        List<BlockMessage> blockMessages = splitBlocks(50, content);

        // 知道是需要更新哪个文档
        // String documentId = getDocumentId();
        String documentId;
        DocumentMapper documentMapper = new DocumentMapper();
        Map<String, String> documentMap = documentMapper.documentMap(tenant_access_token);
        if (documentMap.containsKey(documentName)) {
            documentId = documentMap.get(documentName);
        } else {
            documentId = documentMapper.newDocument(tenant_access_token, documentName);
        }

        // 发送到对应文档
        documentMapper.cleanDocument(tenant_access_token, documentId);
        for (BlockMessage blockMessage : blockMessages) {
            // JsonString.objectMapper.writeValueAsString(blockMessage)
            blockMessage.check();
            log.info(documentMapper.insertDocument(tenant_access_token, documentId, blockMessage).getString());
        }

        // 发送给对应的人
        // "誓嘉": "ou_05ab03d6f1fba287a2aa4e1decde4f12"
        // "我": "ou_d37e7a78deaefb355cc3390f224e5900"
        new MessageMapper().send2Person(tenant_access_token, "ou_d37e7a78deaefb355cc3390f224e5900", "https://automq66.feishu.cn/docx/%s".formatted(documentId));
    }

    private List<String> getAllEmployee() throws UnirestException, IOException {
        // 获取部门信息
        JsonString allDepartment = new DepartmentMapper().allDepartment(tenant_access_token);
        JsonString departments = allDepartment.get("data").get("items");
        List<String> departmentIdList = new LinkedList<>();
        departmentIdList.add("0");
        for (int i = 0; i < departments.getNode().size(); i++) {
            departmentIdList.add(departments.get(i).get("department_id").string());
        }

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
        List<Okr> okrs = null;

        okrs = JsonString.objectMapper.readValue(okrJsonString.get("data").get("okr_list").getString(), new TypeReference<List<Okr>>() {
        });

        return new OkrList(userIdx, okrs);
    }


    /**
     * 解析一个人的OkrList
     *
     * @param okrList
     * @param filter
     * @throws UnirestException
     * @throws JsonProcessingException
     */
    private UserView parseOne(OkrList okrList, boolean filter) throws UnirestException, JsonProcessingException {

        ProgressMapper progressMapper = new ProgressMapper();

        // 获取进展，填入Objective和KeyResult
        for (Okr okr : okrList.getOkr_list()) {
            // okr -> objective
            List<Objective> objectives = okr.getObjective_list();
            for (Objective objective : objectives) {
                // objective -> ProgressRecordSimplify
                objective.setProgressRecords(new LinkedList<>());
                for (ProgressRecordSimplify progressRecordSimplify : objective.getProgress_record_list()) {
                    // ProgressRecordSimplify -> idx
                    ProgressRecord progressRecord = progressMapper.getProgress(tenant_access_token, progressRecordSimplify.getId());
                    if (filter && !dateUtil.inThisWeek(progressRecord.getModify_time())) {
                        continue;
                    }
                    objective.getProgressRecords().add(progressRecord);
                }
                // objective -> KeyResult
                for (KeyResult keyResult : objective.getKr_list()) {
                    // KeyResult -> ProgressRecordSimplify
                    keyResult.setProgressRecords(new LinkedList<>());
                    for (ProgressRecordSimplify progressRecordSimplify : keyResult.getProgress_record_list()) {
                        // ProgressRecordSimplify -> idx
                        ProgressRecord progressRecord = progressMapper.getProgress(tenant_access_token, progressRecordSimplify.getId());
                        if (filter && !dateUtil.inThisWeek(progressRecord.getModify_time())) {
                            continue;
                        }
                        keyResult.getProgressRecords().add(progressRecord);
                    }
                }
            }
        }

        // 将OkrList转变为UserView
        UserView userView = UserView.fromOkrList(okrList);
        return userView;
    }

    private List<Block> constructBlock(UserView userView) {
        // 遍历整棵树，找到有Progress的路径，封装一条Block
        return BlockGenerator.travelOkrView(userView);
    }


    // 分片
    private List<BlockMessage> splitBlocks(int max, List<Block> blocks) {
        List<BlockMessage> blockMessages = new LinkedList<>();

        assert max > 0 && max <= 50;
        int curr = 0;
        while (curr < blocks.size()) {
            List<Block> slice = new ArrayList<>(max);
            slice.addAll(blocks.subList(curr, Math.min(curr + max, blocks.size())));

            BlockMessage blockMessage = new BlockMessage();
            blockMessage.setChildren(slice);
            blockMessages.add(blockMessage);

            curr += max;
        }
        return blockMessages;
    }

    @Deprecated
    private String getDocumentId() throws UnirestException, JsonProcessingException {

        String documentId;
        DocumentCheckUtil documentCheckUtil = new DocumentCheckUtil();
        if (documentCheckUtil.getDocumentIdThisWeek(documentName) == null) {
            documentId = new DocumentMapper().newDocument(tenant_access_token, documentName);
            documentCheckUtil.insertDocumentIdThisWeek(documentName, documentId);
        } else {
            documentId = documentCheckUtil.getDocumentIdThisWeek(documentName);
        }
        return documentId;
    }

    private void filterUser(List<String> userList) throws UnirestException, JsonProcessingException {
        // TODO: 实现过滤
        // 用户组ID
        // g7b3ecgeac8f34a8

        EmployerMapper employerMapper = new EmployerMapper();

        Iterator<String> iterator = userList.iterator();
        while (iterator.hasNext()) {
            String openId = iterator.next();
            List<String> strings = employerMapper.belongUserGroupId(tenant_access_token, openId);

            boolean remove = false;
            for (String string : strings) {
                if (string.equals("g7b3ecgeac8f34a8")) {
                    remove = true;
                    break;
                }
            }

            if (remove) {
                iterator.remove();
            }
        }
    }
}
