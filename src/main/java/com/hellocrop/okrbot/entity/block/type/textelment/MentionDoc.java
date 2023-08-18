package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.contentblock.ContentDocsLink;
import lombok.Data;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class MentionDoc {
    String token;
    /**
     * 1：Doc
     * <p>
     * 3：Sheet
     * <p>
     * 8：Bitable
     * <p>
     * 11：MindNote
     * <p>
     * 12：File
     * <p>
     * 15：Slide
     * <p>
     * 16：Wiki
     * <p>
     * 22：Docx
     */
    int obj_type;
    String url;
    TextElementStyle text_element_style;

    public static MentionDoc fromContentDocsLink(ContentDocsLink contentDocsLink) {
        MentionDoc mentionDoc = new MentionDoc();

        String contentDocsLinkUrl = contentDocsLink.getUrl();
        String decode = URLDecoder.decode(contentDocsLinkUrl, StandardCharsets.UTF_8);

        Pattern pattern = Pattern.compile("https://automq66.feishu.cn/([a-zA-z]+)/([a-zA-Z0-9]+)");
        Matcher matcher = pattern.matcher(decode);

        if (matcher.find()) {
            mentionDoc.token = matcher.group(2);

            switch (matcher.group(1)) {
                case "doc":
                    mentionDoc.obj_type = 1;
                    break;
                case "sheet":
                    mentionDoc.obj_type = 3;
                    break;
                case "bitable":
                    mentionDoc.obj_type = 8;
                    break;
                case "mindNote":
                    mentionDoc.obj_type = 11;
                    break;
                case "file":
                    mentionDoc.obj_type = 12;
                    break;
                case "slide":
                    mentionDoc.obj_type = 15;
                    break;
                case "wiki":
                    mentionDoc.obj_type = 16;
                    break;
                case "docx":
                    mentionDoc.obj_type = 22;
                    break;
                default:
                    return null;
            }
            mentionDoc.url = contentDocsLinkUrl;
        } else {
            return null;
        }

        return mentionDoc;
    }
}
