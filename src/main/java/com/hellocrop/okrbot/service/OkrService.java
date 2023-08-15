package com.hellocrop.okrbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hellocrop.okrbot.dao.*;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.Block;
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

        for (String userIdx : userIdxList) {
            // 根据人员获取OKR
            JsonString okrsByEmployer = getOkrsByEmployer(userIdx);
            OkrList okrList = deserializationOKR(userIdx, okrsByEmployer);

            // 解析
            // TODO
            UserView userView = parseOne(okrList, true);

            // 构建这个人的block
            constructBlock(userView);
        }
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

//    // 获取个人进展转变为block
//    private List<Object> pgs2block(List<ProgressRecord> progressRecords) throws UnirestException, JsonProcessingException {
//        List<Object> blocks = new LinkedList<>();
//
//        ProgressMapper progressMapper = new ProgressMapper();
//        Iterator<ProgressRecord> iterator = progressRecords.iterator();
//        while (iterator.hasNext()) {
//            ProgressRecord next = iterator.next();
//            JsonString progress = progressMapper.getProgress(tenant_access_token, next.getId());
//
//            // 判断时间对不对, 是不是这周的
//            Date before = new Date(Long.parseLong(progress.get("data").get("modify_time").string()));
//            if (dateUtil.inThisWeek(before)) {
//                List<Object> pgsBlock = JsonString.objectMapper.readValue(JsonString.objectMapper.writeValueAsString(progress.get("data").get("content").get("block").getNode()), new TypeReference<>() {
//                });
//                blocks.add(pgsBlock);
//            }
//        }
//        return blocks;
//
//    }
//
//    // 获取每个人的进展具体内容，返回map
//    private Map<String, List<JsonString>> getAllPgs(Map<String, OkrList> okrListMap) throws UnirestException, JsonProcessingException {
//        Map<String, List<JsonString>> map_pgs_js = new LinkedHashMap<>();
//
//        Map<String, List<ProgressRecord>> map_pgs = new OkrUtil(okrListMap).getMap_pgs();
//        ProgressMapper progressMapper = new ProgressMapper();
//        Iterator<Map.Entry<String, List<ProgressRecord>>> map_pgsEntryIterator = map_pgs.entrySet().iterator();
//        while (map_pgsEntryIterator.hasNext()) {
//            Map.Entry<String, List<ProgressRecord>> next = map_pgsEntryIterator.next();
//            Iterator<ProgressRecord> progressRecordIterator = next.getValue().iterator();
//            while (progressRecordIterator.hasNext()) {
//                // 单个进展
//                JsonString progress = progressMapper.getProgress(tenant_access_token, progressRecordIterator.next().getId());
//                if (!map_pgs_js.containsKey(next.getKey())) {
//                    map_pgs_js.put(next.getKey(), new LinkedList<>());
//                }
//                map_pgs_js.get(next.getKey()).add(progress);
//            }
//            if (!map_pgs_js.containsKey(next.getKey())) {
//                map_pgs_js.put(next.getKey(), null);
//            }
//        }
//
//        return map_pgs_js;
//    }
//
//
//    private Map<String, JsonString> id2pgs(Map<String, OkrList> okrListMap) throws UnirestException, JsonProcessingException {
//        Map<String, JsonString> map_pgs_js = new LinkedHashMap<>();
//
//        Map<String, List<ProgressRecord>> map_pgs = new OkrUtil(okrListMap).getMap_pgs();
//        ProgressMapper progressMapper = new ProgressMapper();
//        Iterator<Map.Entry<String, List<ProgressRecord>>> map_pgsEntryIterator = map_pgs.entrySet().iterator();
//        while (map_pgsEntryIterator.hasNext()) {
//            Map.Entry<String, List<ProgressRecord>> next = map_pgsEntryIterator.next();
//            Iterator<ProgressRecord> progressRecordIterator = next.getValue().iterator();
//            while (progressRecordIterator.hasNext()) {
//                String id = progressRecordIterator.next().getId();
//                // 单个进展
//                JsonString progress = progressMapper.getProgress(tenant_access_token, id);
//                if (dateUtil.inThisWeek(new Date(Long.parseLong(progress.get("data").get("modify_time").string()))))
//                    map_pgs_js.put(id, progress);
//            }
//        }
//
//        return map_pgs_js;
//    }

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

    private void constructBlock(UserView userView) {
        List<Block> blocks = new LinkedList<>();

        blocks.add(Block.builder().block_type(BlockType.HEADING1.type).heading1(userView.getBlock()).build());

        // 遍历整棵树，找到有Progress的路径，封装一条Block
        for (OkrView okrView : userView.getOkrViews()) {
            if (okrView.getObjectiveViews().isEmpty()) {
                // 如果没有目标
                continue;
            } else {
                // 如果有目标，遍历目标
                for (ObjectiveView objectiveView : okrView.getObjectiveViews()) {
                    // kr
                    if (objectiveView.getKeyResultViews().isEmpty()) {
                        // 如果没有kr
                        continue;
                    } else {
                        // 如果有kr，遍历kr
                        for (KeyResultView keyResultView : objectiveView.getKeyResultViews()) {

                        }
                    }
                    // progress
                    if (objectiveView.getProgressViews().isEmpty()) {
                        // 如果没有progress
                        continue;
                    } else {
                        // 如果有progress，遍历progress，想办法取出blocks并写入

                    }
                }
            }
        }
        return;
    }

    private List<Block> travelOkrView(UserView userView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        // 尝试遍历OkrView
        for (OkrView okrView : userView.getOkrViews()) {

        }
        return blocks;
    }

    private List<Block> travelObjectiveView(OkrView okrView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        // 尝试遍历ObjectiveView

        return blocks;
    }

    private List<Block> travelKeyResultView(ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的

        return blocks;
    }

    private List<Block> travelProgressView(ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的

        return blocks;
    }

    private List<Block> travelProgressView(KeyResultView keyResultView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        for (ProgressView progressView : keyResultView.getProgressViews()) {

        }
        return blocks;
    }
}
