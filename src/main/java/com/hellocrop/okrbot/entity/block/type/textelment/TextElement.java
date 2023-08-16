package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.contentblock.ContentParagraphElement;
import lombok.Data;

@Data
// 能够放在TextBlock中的所有Block
public class TextElement {
    TextRun text_run;
    MentionUser mention_user;

    public TextElement() {

    }

    public static TextElement simpleTextRun(String content) {
        TextElement textElement = new TextElement();
        textElement.text_run = new TextRun(content);
        return textElement;
    }

    public static TextElement simpleMentionUser(String userId) {
        TextElement textElement = new TextElement();
        textElement.mention_user = new MentionUser(userId);
        return textElement;
    }

    public static TextElement fromContentParagraphElement(ContentParagraphElement contentParagraphElement) {
        if (contentParagraphElement == null) return null;
        TextElement textElement = new TextElement();

        switch (contentParagraphElement.getType()) {
            case "textRun":
                textElement.text_run = TextRun.fromContenTextRunBlock(contentParagraphElement.getTextRun());
                break;
            case "person":
                textElement.mention_user = MentionUser.fromContentPerson(contentParagraphElement.getPerson());
                break;
            case "docsLink":
                textElement = null;
                break;
        }

        return textElement;
    }
}
