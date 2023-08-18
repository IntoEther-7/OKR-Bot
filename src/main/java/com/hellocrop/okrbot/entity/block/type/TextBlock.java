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
            // 简单设置缩进
            int indentLevel = contentParagraph.getStyle().getList().getIndentLevel();
            StringBuilder indent = new StringBuilder("");

            // bullet
            switch (type) {
                case "bullet":
                    if (indentLevel > 1) {
                        for (int i = 1; i < indentLevel; i++) {
                            indent.append("\t");
                        }
                        switch (indentLevel % 3) {
                            case 0:
                                indent.append("● ");
                                break;
                            case 1:
                                indent.append("○ ");
                                break;
                            case 2:
                                indent.append("□ ");
                                break;
                        }
                    } else {
                        indent.append("● ");
                    }
                    break;
                case "number":
                    if (indentLevel > 1) {
                        for (int i = 1; i < indentLevel; i++) {
                            indent.append("\t");
                        }
                        switch (indentLevel % 3) {
                            case 0:
                                indent.append("● ");
                                break;
                            case 1:
                                indent.append("○ ");
                                break;
                            case 2:
                                indent.append("□ ");
                                break;
                        }
                    } else {
                        indent.append("● ");
                    }
                    break;
            }

            // indent作为一个block，蓝色
            TextElement textElement = TextElement.simpleTextRun(indent.toString());
            textElement.getText_run().setText_element_style(TextElementStyle.builder().text_color(5).build());
            textBlock.elements.add(0, textElement);

//            if (textBlock.elements != null && textBlock.elements.get(0) != null) {
//                TextRun textRun = textBlock.elements.get(0).getText_run();
//                textRun.setContent(indent.append(textRun.getContent()).toString());
//            }

            // for (TextElement element : textBlock.elements) {
            //     if (element != null && element.getText_run() != null && element.getText_run().getContent() != null)
            //         element.getText_run().setContent(indent.append(element.getText_run().getContent()).toString());
            // }
            map.put("text", textBlock);
        } else {
            // 普通文本块
            String type = "text";
            map.put(type, textBlock);
        }

        return map;
    }
}
