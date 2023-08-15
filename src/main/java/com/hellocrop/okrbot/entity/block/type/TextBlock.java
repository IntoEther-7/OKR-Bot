package com.hellocrop.okrbot.entity.block.type;

import com.hellocrop.okrbot.entity.block.type.textelment.MentionUser;
import com.hellocrop.okrbot.entity.block.type.textelment.TextElement;
import com.hellocrop.okrbot.entity.block.type.textelment.TextRun;
import com.hellocrop.okrbot.entity.contentblock.ContentParagraph;
import com.hellocrop.okrbot.entity.contentblock.ContentParagraphElement;
import lombok.Data;

import java.util.*;

@Data
public class TextBlock {
    List<TextElement> elements = new LinkedList<>();
    TextStyle style;

    public static TextBlock simpleTextBlock(String content) {
        TextBlock textBlock = new TextBlock();
        textBlock.elements.add(TextElement.simpleTextRun(content));
        return textBlock;
    }


    public static TextBlock mentionUserBlock(String openId) {
        TextBlock textBlock = new TextBlock();
        textBlock.elements.add(TextElement.simpleMentionUser(openId));
        return textBlock;
    }

    public static Map<String, TextBlock> fromContentParagraph(ContentParagraph contentParagraph) {
        if (contentParagraph == null) return null;
        Map<String, TextBlock> map = new LinkedHashMap<>();
        TextBlock textBlock = new TextBlock();

        for (ContentParagraphElement contentParagraphElement : contentParagraph.getElements()) {
            textBlock.elements.add(TextElement.fromContentParagraphElement(contentParagraphElement));
        }

        if (contentParagraph.getStyle() != null && contentParagraph.getStyle().getList() != null && contentParagraph.getStyle().getList().getType() != null) {
            String type = contentParagraph.getStyle().getList().getType();
            map.put(type, textBlock);
        } else {
            // 普通文本块
            String type = "text";
            map.put(type, textBlock);
        }

        return map;
    }
}
