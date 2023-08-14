package com.hellocrop.okrbot.util;

import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
import com.hellocrop.okrbot.entity.okr.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author IntoEther-7
 * @date 2023/8/13 19:45
 * @project okrbot
 */
@Slf4j
@Getter
public class OkrUtil {

    // private Map<String, List<KeyResult>> map_kr;
    // private Map<String, List<Objective>> map_obj;
    private Map<String, List<ProgressRecord>> map_pgs;

    public OkrUtil(Map<String, OkrList> okrListMap) {
        // map_kr = new LinkedHashMap<>();
        // map_obj = new LinkedHashMap<>();
        map_pgs = new LinkedHashMap<>();
        filterOkrLists(okrListMap);
    }

    private void filterOkrLists(Map<String, OkrList> okrListMap) {

        // 依次处理每一个OkrList
        Iterator<Map.Entry<String, OkrList>> iterator = okrListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OkrList> next = iterator.next();

            // map_kr.put(next.getKey(), null);
            // map_obj.put(next.getKey(), null);
            map_pgs.put(next.getKey(), new LinkedList<>());

            filterOkrList(next.getValue().getOkr_list(), next.getKey());
            if (next.getValue().getOkr_list().isEmpty()) next.setValue(null);
        }
        log.info("遍历结束里");
    }

    private void filterOkrList(List<Okr> okrs, String userIdx) {
        // 依次处理每个Okr
        Iterator<Okr> iterator = okrs.iterator();
        while (iterator.hasNext()) {
            Okr next = iterator.next();
            filterObjectiveList(next.getObjective_list(), userIdx);
            if (next.getObjective_list().isEmpty()) {
                iterator.remove();
            }
            // else {
            //     if (map_obj.get(userIdx) == null) {
            //         map_obj.put(userIdx, new LinkedList<>());
            //     }
            //     map_obj.get(userIdx).addAll(next.getObjective_list());
            // }
        }
    }

    private void filterObjectiveList(List<Objective> objectiveList, String userIdx) {
        // 依次处理每个Objective, 如果Objective没有KR, 删掉它
        Iterator<Objective> iterator = objectiveList.iterator();
        while (iterator.hasNext()) {
            Objective next = iterator.next();
            if (next.getKr_list().isEmpty()) {
                iterator.remove();
            } else {
                // 如果有progress_id, 放进map
                map_pgs.get(userIdx).addAll(next.getProgress_record_list());
                Iterator<KeyResult> keyResultIterator = next.getKr_list().iterator();
                while (keyResultIterator.hasNext()) {
                    KeyResult keyResult = keyResultIterator.next();
                    map_pgs.get(userIdx).addAll(keyResult.getProgress_record_list());
                }
            }

            // else {
            //     if (map_kr.get(userIdx) == null) {
            //         map_kr.put(userIdx, new LinkedList<>());
            //     }
            //     map_kr.get(userIdx).addAll(next.getKr_list());
            // }
        }
    }

    public List<MultiTypeBlock> getBlocks() {
        return null;
    }

}
