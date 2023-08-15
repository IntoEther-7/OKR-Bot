package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.contentblock.ContentTextRun;
import lombok.Data;

@Data
public class TextRun {
    String content;
    TextElementStyle style;

    public TextRun() {
    }

    public TextRun(String content) {
        this.content = content;
    }

    public TextRun(String content, TextElementStyle style) {
        this.content = content;
        this.style = style;
    }

    public static TextRun fromContenTextRunBlock(ContentTextRun contentTextRun) {
        if (contentTextRun == null) return null;
        TextRun textRunBlock = new TextRun();

        textRunBlock.content = contentTextRun.getText();
        textRunBlock.style = TextElementStyle.fromContentTextStyle(contentTextRun.getStyle());

        return textRunBlock;
    }
}
