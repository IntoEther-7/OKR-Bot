package com.hellocrop.okrbot.entity.block.type;

import lombok.Data;

import java.util.*;

@Data
public class TextBlock {
    List<Object> elements;
    Object style;

    public TextBlock() {
        elements = new LinkedList<>();
    }

    public TextBlock(String content) {
        TextRunBlock textRunBlock = new TextRunBlock();
        textRunBlock.setContent(content);
        elements = new ArrayList<>(1);
        Map<String, Object> map = new HashMap<>();
        map.put("text_run", textRunBlock);
        elements.add(map);
    }

    public static TextBlock mentionUserBlock(String openId) {
        TextBlock textBlock = new TextBlock();
        MentionUser mentionUser = new MentionUser();
        mentionUser.setUser_id(openId);
        textBlock.elements = new ArrayList<>(1);
        Map<String, Object> map = new HashMap<>();
        map.put("mention_user", mentionUser);
        textBlock.elements.add(map);
        return textBlock;
    }
}
