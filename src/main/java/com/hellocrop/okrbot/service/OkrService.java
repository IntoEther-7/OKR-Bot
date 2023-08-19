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
import java.text.Collator;
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

        Map<String, String> allEmployee = getAllEmployee();

        // TODO: 根据用户组过滤
        filterUser(allEmployee);

        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);

        TreeMap<String, List<Block>> content = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : allEmployee.entrySet()) {
            String userIdx = entry.getKey();
            String userName = entry.getValue();
            // 根据人员获取OKR
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);

            // 解析
            UserView userView = parseOne(okrList);

            // 构建这个人的blocks
            content.put(userName, constructBlock(userView));
        }

        List<Block> blocks = sortMap(content);

        // 插入序言
        // 本文档由办公自动化系统根据 OKR 的进展自动生成，用于向全员同步短期进展和计划，如需评论，可点击具体链接跳转到 OKR 系统进行评论和转发。
        // 最后更新时间：2023/01/01 22:00
        BlockGenerator.insertIntro(blocks);

        // 分割blocks
        List<BlockMessage> blockMessages = splitBlocks(50, blocks);

        // 知道是需要更新哪个文档
        // String documentId = getDocumentId();
        String documentId;
        boolean init;
        DocumentMapper documentMapper = new DocumentMapper();
        Map<String, String> documentMap = documentMapper.documentMap(tenant_access_token);
        if (documentMap.containsKey(documentName)) {
            documentId = documentMap.get(documentName);
            init = false;
        } else {
            documentId = documentMapper.newDocument(tenant_access_token, documentName);
            init = true;
        }

        // 发送到对应文档
        documentMapper.cleanDocument(tenant_access_token, documentId);
        for (BlockMessage blockMessage : blockMessages) {
            // JsonString.objectMapper.writeValueAsString(blockMessage)
            blockMessage.check();
            log.info(JsonString.objectMapper.writeValueAsString(blockMessage));
            log.info(documentMapper.insertDocument(tenant_access_token, documentId, blockMessage).getString());
        }

        if (init) {
            // 发送给对应的人
            // "誓嘉": "ou_05ab03d6f1fba287a2aa4e1decde4f12"
            new MessageMapper().send2Person(tenant_access_token, "ou_05ab03d6f1fba287a2aa4e1decde4f12", "https://automq66.feishu.cn/docx/%s".formatted(documentId));
            // "尘央": "ou_80f012ab640bde78bcc18daa378a4f31"
            new MessageMapper().send2Person(tenant_access_token, "ou_80f012ab640bde78bcc18daa378a4f31", "https://automq66.feishu.cn/docx/%s".formatted(documentId));
            // "哈克": "ou_d37e7a78deaefb355cc3390f224e5900"
            new MessageMapper().send2Person(tenant_access_token, "ou_d37e7a78deaefb355cc3390f224e5900", "https://automq66.feishu.cn/docx/%s".formatted(documentId));
        }
    }

    private List<Block> sortMap(TreeMap<String, List<Block>> map) {
        List<Block> blocks = new LinkedList<>();
        map.forEach((key, value) -> blocks.addAll(value));
        return blocks;
    }

    private Map<String, String> getAllEmployee() throws UnirestException, IOException {
        // 获取部门信息
        JsonString allDepartment = new DepartmentMapper().allDepartment(tenant_access_token);
        JsonString departments = allDepartment.get("data").get("items");
        List<String> departmentIdList = new LinkedList<>();
        departmentIdList.add("0");
        for (int i = 0; i < departments.getNode().size(); i++) {
            departmentIdList.add(departments.get(i).get("department_id").string());
        }

        // 根据部门查询所有人员信息
        HashMap<String, String> map = new HashMap<>();
        EmployerMapper employerMapper = new EmployerMapper();
        for (String departmentId : departmentIdList) {
            JsonString personsByDepartment = employerMapper.getPersonsByDepartment(tenant_access_token, departmentId);

            // 查询人员
            JsonString employee = personsByDepartment.get("data").get("items");
            for (int i = 0; i < employee.getNode().size(); i++) {
                map.put(employee.get(i).get("open_id").string(), employee.get(i).get("name").string());
            }
        }
        return map;
    }


    // 拿到okr
    private JsonString getOkrsByEmployer(String userId) throws UnirestException, JsonProcessingException {
        return okrMapper.getOkrsByEmployer(tenant_access_token, userId);
    }

    // 处理OKR数据, 拿到目标数据
    private OkrList deserializationOKR(String userIdx, JsonString okrJsonString) throws JsonProcessingException {
        List<Okr> okrs;

        okrs = JsonString.objectMapper.readValue(okrJsonString.get("data").get("okr_list").getString(), new TypeReference<List<Okr>>() {
        });

        return new OkrList(userIdx, okrs);
    }


    /**
     * 解析一个人的OkrList
     *
     * @param okrList
     * @throws UnirestException
     * @throws JsonProcessingException
     */
    private UserView parseOne(OkrList okrList) throws UnirestException, JsonProcessingException {

        ProgressMapper progressMapper = new ProgressMapper();

        // 获取进展，填入Objective和KeyResult
        for (Iterator<Okr> iterator = okrList.getOkr_list().iterator(); iterator.hasNext(); ) {
            Okr okr = iterator.next();
            // okr -> objective
            List<Objective> objectives = okr.getObjective_list();
            if (objectives.isEmpty()) iterator.remove();
            else {
                for (Objective objective : objectives) {
                    // objective -> ProgressRecordSimplify
                    objective.setProgressRecords(new LinkedList<>());
                    for (ProgressRecordSimplify progressRecordSimplify : objective.getProgress_record_list()) {
                        // ProgressRecordSimplify -> idx
                        ProgressRecord progressRecord = progressMapper.getProgress(tenant_access_token, progressRecordSimplify.getId());
                        objective.getProgressRecords().add(progressRecord);
                    }
                    // objective -> KeyResult
                    for (KeyResult keyResult : objective.getKr_list()) {
                        // KeyResult -> ProgressRecordSimplify
                        keyResult.setProgressRecords(new LinkedList<>());
                        for (ProgressRecordSimplify progressRecordSimplify : keyResult.getProgress_record_list()) {
                            // ProgressRecordSimplify -> idx
                            ProgressRecord progressRecord = progressMapper.getProgress(tenant_access_token, progressRecordSimplify.getId());
                            keyResult.getProgressRecords().add(progressRecord);
                        }
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

    private void filterUser(Map<String, String> userMap) throws UnirestException, JsonProcessingException {
        // 实现过滤
        // 用户组ID
        // 暂无 OKR 用户组: "g7b3ecgeac8f34a8"
        // 实习生用户组: "3e3e6e16c4275fd1"

        EmployerMapper employerMapper = new EmployerMapper();

        Iterator<Map.Entry<String, String>> iterator = userMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            List<String> strings = employerMapper.belongUserGroupId(tenant_access_token, next.getKey());

            boolean remove = false;
            for (String string : strings) {
                if (string.equals("g7b3ecgeac8f34a8") || string.equals("3e3e6e16c4275fd1")) {
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
