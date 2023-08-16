package com.hellocrop.okrbot.entity.block.type;

import com.hellocrop.okrbot.entity.contentblock.ContentTextStyle;
import lombok.Builder;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@Builder
public class TextElementStyle {
    Boolean bold;
    Boolean italic;
    Boolean strikethrough;
    Boolean underline;
    Boolean inline_code;
    Integer background_color;
    Integer text_color;  // 5 蓝色
    Link link;
    String[] comment_ids;


    public static TextElementStyle fromContentTextStyle(ContentTextStyle contentTextStyle) {
        if (contentTextStyle == null) return null;
        TextElementStyle textElementStyle = TextElementStyle.builder().build();

        textElementStyle.bold = contentTextStyle.getBold();
        textElementStyle.strikethrough = contentTextStyle.getStrikeThrough();
        textElementStyle.link = Link.fromContentLink(contentTextStyle.getLink());
        // TODO 思考颜色转变
        return textElementStyle;
    }
}
