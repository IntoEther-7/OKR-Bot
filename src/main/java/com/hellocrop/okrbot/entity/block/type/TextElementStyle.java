package com.hellocrop.okrbot.entity.block.type;

import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
public class TextElementStyle implements MultiTypeBlock {
    Boolean bold;
    Boolean italic;
    Boolean strikethrough;
    Boolean underline;
    Boolean inline_code;
    Integer background_color;
    Integer text_color;  // 5 蓝色
    Link link;
    String[] comment_ids;

    public TextElementStyle() {
    }

    public TextElementStyle(String url) {
        this.link = new Link(url);
        text_color = 5;
    }
}
