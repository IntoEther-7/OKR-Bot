package com.hellocrop.okrbot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.okr.Progress;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;

import java.util.*;

/**
 * @author IntoEther-7
 * @date 2023/8/13 22:00
 * @project okrbot
 */
public class ProgressBlockUtil {

    public List<String> extract(List<JsonString> progresses) throws JsonProcessingException {
        return extract(progresses, null);
    }

    public List<String> extract(List<JsonString> progresses, DateUtil dateUtil) throws JsonProcessingException {
        if (progresses == null) return null;
        // 提取每个人的信息
        Iterator<JsonString> iterator = progresses.iterator();
        while (iterator.hasNext()) {
            JsonString next = iterator.next();
            JsonString blocks = next.get("data").get("content").get("blocks"); // 这是blocks的列表
            if (dateUtil != null) {
                long modify_time = Long.parseLong(next.get("data").get("modify_time").string());// 这是修改时间
                if (dateUtil.inThisWeek(new Date(modify_time))) {
                    iterator.remove(); // 删了这次结果
                    continue;
                }
            }
            // 解析这次的blocks
            List<JsonString> list = extractString(blocks);
            System.out.println(list); // TODO 删了
        }
        return null;
    }

    public List<JsonString> extractString(JsonString blocks) throws JsonProcessingException {
        List<JsonString> list = new LinkedList<>();
        // 提取每个块的信息
        for (int i = 0; i < blocks.getNode().size(); i++) {
            // 提取信息构建新的块
            JsonString block = blocks.get(i);
            // 先知道类型
            JsonString jsonString = constructBlock(block);
            list.add(jsonString);
        }
        return list;
    }

    private JsonString constructBlock(JsonString block) throws JsonProcessingException {
        // TODO
        String type = block.get("type").string();
        // paragraph -> 段落，一般多段文本组成 -> 提取text
        // textRun -> 文本，可能由多文本组成 -> 不变
        JsonString jsonString = null;
        switch (type) {
            case "paragraph":
                // 重构成普通的text块
                jsonString = constructParagraph(block);
                break;
            case "textRun":
                // 不用重构
                jsonString = constructTextRun(block);
                break;
            case "person":
                jsonString = constructPerson(block);
                break;

        }
        return jsonString;
    }

    private JsonString constructParagraph(JsonString block) throws JsonProcessingException {
        // TODO
        // 拿到element的所有文本
        List<JsonString> texts = new LinkedList<>();
        JsonString elements = block.get("paragraph").get("elements");
        for (int i = 0; i < elements.getNode().size(); i++) {
            JsonString element = elements.get(i);
            texts.add(element);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("element", texts);

        return new JsonString(JsonString.objectMapper.writeValueAsString(map));
    }

    private JsonString constructTextRun(JsonString block) throws JsonProcessingException {
        // JsonString string = block.get("textRun").get("text");
        // // 转换成TEXT块
        return block;
    }

    private JsonString constructPerson(JsonString block) throws JsonProcessingException {
        String openId = block.get("person").get("openId").string();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", openId);

        return new JsonString(JsonString.objectMapper.writeValueAsString(map));
    }
}
