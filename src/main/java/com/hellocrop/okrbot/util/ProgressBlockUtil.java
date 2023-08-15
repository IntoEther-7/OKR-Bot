package com.hellocrop.okrbot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.type.*;
import com.hellocrop.okrbot.entity.okr.*;
import com.hellocrop.okrbot.entity.okr.view.ObjectiveView;
import com.hellocrop.okrbot.entity.okr.view.ProgressView;
import com.hellocrop.okrbot.entity.okr.view.UserView;

import java.util.*;

/**
 * @author IntoEther-7
 * @date 2023/8/13 22:00
 * @project okrbot
 */
public class ProgressBlockUtil {
    /**
     * 所有人的okr和进展
     *
     * @param okrListMap
     * @param id2pgs
     * @return
     */
    public Map<String, UserView> constructMap(Map<String, OkrList> okrListMap, Map<String, JsonString> id2pgs) {
        Map<String, UserView> map = new HashMap<>();

        Iterator<Map.Entry<String, OkrList>> userIter = okrListMap.entrySet().iterator();
        while (userIter.hasNext()) {
            Map.Entry<String, OkrList> next = userIter.next();
            UserView userView = new UserView();
            userView.setUseIdx(next.getKey());
            userView.setObjectiveViews(getObjectiveView(next.getValue(), id2pgs));
            userView.setMention(TextBlock.mentionUserBlock(next.getKey()));
            map.put(next.getKey(), userView);
        }

        return map;
    }

    public List<ObjectiveView> getObjectiveView(OkrList okrList, Map<String, JsonString> id2pgs) {
        if (okrList == null) return null;
        List<ObjectiveView> objectiveViews = new LinkedList<>();

        // 遍历获得目标
        Iterator<Okr> iterator = okrList.getOkr_list().iterator();
        while (iterator.hasNext()) {
            Okr next = iterator.next();
            Iterator<Objective> objectiveIterator = next.getObjective_list().iterator();
            int obj_idx = 0;// 序列号，例如O5 DONE
            while (objectiveIterator.hasNext()) {
                Objective objective = objectiveIterator.next();
                ObjectiveView objectiveView = new ObjectiveView();
                objectiveView.setObjIdx(objective.getId());

                objective.setContent("O%d: %s".formatted(++obj_idx, objective.getContent()));

                TextBlock textBlock = new TextBlock();
                TextRunBlock textRunBlock = new TextRunBlock(objective.getContent());
                Map<String, TextRunBlock> map = new HashMap<>();


                TextElementStyle textElementStyle = new TextElementStyle();
                textElementStyle.setText_color(5);
                textRunBlock.setStyle(textElementStyle);

                map.put("text_run", textRunBlock);
                textBlock.getElements().add(map);

                objectiveView.setBlocks(textBlock);

                List<ProgressView> list = new LinkedList<>();
                list.addAll(getProgressView(objective, id2pgs));

                int kr_idx = 0; // 序列号，例如O5KR3 DONE

                Iterator<KeyResult> resultIterator = objective.getKr_list().iterator();
                while (resultIterator.hasNext()) {
                    KeyResult keyResult = resultIterator.next();
                    keyResult.setContent("O%dKR%d: %s".formatted(obj_idx, ++kr_idx, keyResult.getContent()));
                    list.addAll(getProgressView(keyResult, id2pgs));
                }
                objectiveView.setProgressViews(list);
                objectiveViews.add(objectiveView);
            }
        }

        return objectiveViews;
    }

    public List<ProgressView> getProgressView(Objective objective, Map<String, JsonString> id2pgs) {
        List<ProgressView> list = new LinkedList<>();
        Iterator<ProgressRecord> progressRecordIterator = objective.getProgress_record_list().iterator();
        while (progressRecordIterator.hasNext()) {
            ProgressRecord progressRecord = progressRecordIterator.next();

            if (id2pgs.containsKey(progressRecord.getId())) {
                ProgressView progressView = new ProgressView();

                progressView.setPgsIdx(progressRecord.getId());
                JsonString progress = id2pgs.get(progressRecord.getId());
                try {
                    progressView.setBlocks(progress2Blocks(progress));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                list.add(progressView);

            }
        }
        return list;
    }

    public List<ProgressView> getProgressView(KeyResult keyResult, Map<String, JsonString> id2pgs) {
        List<ProgressView> list = new LinkedList<>();
        Iterator<ProgressRecord> progressRecordIterator = keyResult.getProgress_record_list().iterator();
        while (progressRecordIterator.hasNext()) {
            ProgressRecord progressRecord = progressRecordIterator.next();

            if (id2pgs.containsKey(progressRecord.getId())) {
                ProgressView progressView = new ProgressView();

                progressView.setPgsIdx(progressRecord.getId());

                JsonString progress = id2pgs.get(progressRecord.getId());
                try {
                    progressView.setBlocks(progress2Blocks(progress));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                list.add(progressView);

            }
        }
        return list;
    }

    private List<Object> progress2Blocks(JsonString progress) throws JsonProcessingException {

        progress = progress.get("data");


        JsonString blocks = progress.get("content").get("blocks");

        // 解析块，取出所有paragraph，封装成text块
        // 每个paragraph内的块转换成textRun或者mention_user
        List<Object> parsed = parseBlocks(blocks);

        // 解析块，如果是textRun就保留
        // 如果是Person就转换成mention_user
        // 如果是

        return parsed;
    }

    private List<Object> parseBlocks(JsonString blocks) throws JsonProcessingException {
        // 应该是一个paragraph的List，每个paragraph转换成text文本块
        List<Object> list = new LinkedList<>();

        for (int i = 0; i < blocks.getNode().size(); i++) {
            JsonString paragraph = blocks.get(i);
            if (Objects.equals(paragraph.get("type").string(), "paragraph")) {
                Block block;

                if (paragraph.get("paragraph").get("style").getString().equals("null")) {
                    //  默认输入文本块
                    block = new Block(BlockType.TEXT);
                    block.setText(parseParagraph(paragraph));
                } else {
                    // 根据类型输入其他块
                    block = new Block(BlockType.TEXT);
                    switch (paragraph.get("paragraph").get("style").get("list").get("type").string()) {
                        case "bullet":
                            block = new Block(BlockType.BULLET);
                            block.setBullet(parseParagraph(paragraph));
                            break;
                        // case "number":
                        //     break;
                        default:
                            // TODO 逐步更新其他块的支持
                            block = new Block(BlockType.TEXT);
                            block.setText(parseParagraph(paragraph));
                            break;
                    }
                }


                list.add(block);
            }
        }

        return list;
    }

    /**
     * parse一个paragraph，返回一个文本块
     *
     * @return
     * @throws JsonProcessingException
     */
    private TextBlock parseParagraph(JsonString paragraph) throws JsonProcessingException {
        List<Object> list = new LinkedList<>();

        JsonString blockList = paragraph.get("paragraph").get("elements");
        // 对每个块，重新构造
        // textRun块保留
        // textRun的block保留
        for (int i = 0; i < blockList.getNode().size(); i++) {
            JsonString block = blockList.get(i);
            list.add(parseBlock(block));
        }

        TextBlock textBlock = new TextBlock();
        textBlock.setElements(list);
        // TODO: 这里有点问题，格式错误
        // textBlock.setStyle(JsonString.objectMapper.readValue(paragraph.get("paragraph").get("style").getString(), Object.class));

        return textBlock;
    }

    private Object parseBlock(JsonString block) throws JsonProcessingException {
        String type = block.get("type").string();
        Map<String, Object> map = new HashMap<>();
        switch (type) {
            case "textRun":
                String content = block.get("textRun").get("text").string();
                TextRunBlock textRunBlock = new TextRunBlock();
                textRunBlock.setContent(content);
                if (!block.get("textRun").get("style").getNode().isEmpty()) {
                    textRunBlock.setStyle(JsonString.objectMapper.readValue(block.get("textRun").get("style").getString(), Object.class));
                }
                map.put("text_run", textRunBlock);
                break;
            case "person":
                String open_id = block.get("person").get("openId").string();
                MentionUser mentionUser = new MentionUser();
                mentionUser.setUser_id(open_id);
                map.put("mention_user", mentionUser);
                break;
        }
        return map;
    }
}
