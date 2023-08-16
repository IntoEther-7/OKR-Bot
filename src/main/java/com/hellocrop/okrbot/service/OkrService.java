package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.*;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.hellocrop.okrbot.entity.block.type.BlockType;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
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

    public void weekReport(String appId, String appSecret) throws Exception {
        // 鉴权
        JsonString auth = new AuthMapper().auth(appId, appSecret);
        tenant_access_token = "Bearer " + auth.get("tenant_access_token").string();

        List<String> userIdxList = getAllEmployee();

        List<Block> allUserBlock = new LinkedList<>();
        for (String userIdx : userIdxList) {
            // 根据人员获取OKR
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);

            // 解析
            // TODO
            UserView userView = parseOne(okrList, true);

            // 构建这个人的blocks
            allUserBlock.addAll(constructBlock(userView));
        }

        // 分割blocks
        List<BlockMessage> blockMessages = splitBlocks(50, allUserBlock);

        // 知道是需要更新哪个文档
        String documentId = getDocumentId();

        // 发送到对应文档
        DocumentMapper documentMapper = new DocumentMapper();
        documentMapper.cleanDocument(tenant_access_token, documentId);
        for (BlockMessage blockMessage : blockMessages) {
            // JsonString.objectMapper.writeValueAsString(blockMessage)
            blockMessage.check();
            documentMapper.insertDocument(tenant_access_token, documentId, blockMessage);
        }

        // 发送给对应的人
        new MessageMapper().send2Person(tenant_access_token, "ou_d37e7a78deaefb355cc3390f224e5900", "https://automq66.feishu.cn/docx/%s".formatted(documentId));

        // log
        log.info("结束");
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
                        objective.getProgressRecords().add(progressRecord);
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
        return travelOkrView(userView);
    }

    private List<Block> travelOkrView(UserView userView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的

        // 如果有信息，加入自身(User)和下层(Okr/显示未找到进展/显示未设置 OKR)信息
        // 加入用户信息


        // 无Okr -> "未设置 OKR" -> 只包含user信息
        // TODO: 如果需要显示，需要修改上层逻辑

        // 有Okr -> 无进展数据 -> 第二个block为null
        // TODO: 如果需要显示，需要修改上层逻辑

        List<Block> other = new LinkedList<>();
        for (OkrView okrView : userView.getOkrViews()) {
            // 对每个OkrView，遍历下层(Objective)
            List<Block> ov = travelObjectiveView(okrView);
            if (!ov.isEmpty()) {
                blocks.add(Block.builder().block_type(BlockType.HEADING1.type).heading1(TextBlock.mentionUserBlock(userView.getUseIdx())).build());
                other.addAll(ov);
            }
        }

        blocks.addAll(other);

        return blocks;
    }

    private List<Block> travelObjectiveView(OkrView okrView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        // 如果有信息，加入自身(Okr)和下层(Objective)信息
        for (ObjectiveView objectiveView : okrView.getObjectiveViews()) {
            // 对每个ObjectiveView，遍历KeyResult和ProgressView
            List<Block> pv = travelKeyResultAndProgressView(objectiveView);
            if (!pv.isEmpty()) {
                blocks.add(Block.builder().block_type(BlockType.HEADING3.type).heading3(okrView.getBlock()).build());
                blocks.addAll(pv);
            }
        }
        return blocks;
    }

    private List<Block> travelKeyResultAndProgressView(ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        // 如果有信息，加入自身(Objective)和下层(Progress)信息
        // 获取所有PR，不为空加入object描述信息和pr
        List<Block> pr = travelProgressView(objectiveView);
        if (!pr.isEmpty()) {
            blocks.add(Block.builder().block_type(BlockType.HEADING4.type).heading4(objectiveView.getBlock()).build());
            blocks.addAll(pr);
        }

        // 如果有信息，加入自身(Objective)和下层(KeyResult)信息
        // KR->PR遍历，不为空加入kr描述信息和progress
        for (KeyResultView keyResultView : objectiveView.getKeyResultViews()) {
            List<Block> kr_pr = travelProgressView(keyResultView);
            if (!kr_pr.isEmpty()) {
                blocks.add(Block.builder().block_type(BlockType.HEADING4.type).heading4(keyResultView.getBlock()).build());
                blocks.addAll(kr_pr);
            }
        }

        return blocks;
    }

    private List<Block> travelProgressView(ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储Progress

        // 底层，只能加入自身信息（Progress）
        for (ProgressView progressView : objectiveView.getProgressViews()) {
            blocks.addAll(progressView.getBlockMessage().getChildren());
        }

        return blocks;
    }

    private List<Block> travelProgressView(KeyResultView keyResultView) {
        List<Block> blocks = new LinkedList<>(); // 存储Progress

        // 底层，只能加入自身信息（Progress）
        for (ProgressView progressView : keyResultView.getProgressViews()) {
            blocks.addAll(progressView.getBlockMessage().getChildren());
        }
        return blocks;
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
}
