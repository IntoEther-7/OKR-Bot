package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.contentblock.ContentDocsLink;
import com.hellocrop.okrbot.entity.contentblock.ContentParagraphElement;
import lombok.Data;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Data
// 能够放在TextBlock中的所有Block
public class TextElement {
    TextRun text_run;
    MentionUser mention_user;
    MentionDoc mention_doc;

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
                // TODO 增加支持, 但需要文档权限，否则会插入失败
                // textElement.mention_doc = MentionDoc.fromContentDocsLink(contentParagraphElement.getDocsLink());
                ContentDocsLink docsLink = contentParagraphElement.getDocsLink();
                String title = docsLink.getTitle();
                String url = docsLink.getUrl();
                textElement.text_run = TextRun.textAndLink("《%s》".formatted(title), url);
                break;
        }

        return textElement;
    }
}
