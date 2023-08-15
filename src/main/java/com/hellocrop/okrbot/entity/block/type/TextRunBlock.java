package com.hellocrop.okrbot.entity.block.type;

import lombok.Data;

@Data
public class TextRunBlock {
    String content;
    Object style;

    public TextRunBlock() {
    }

    public TextRunBlock(String content) {
        this.content = content;
    }
}
