package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.contentblock.ContentTextRun;
import lombok.Data;

@Data
public class TextRun {
    String content;
    TextElementStyle text_element_style;

    public void setContent(String content) {
        if (content.endsWith("\n")) content = content.replaceAll("\n", "");
        this.content = content;
    }

    public TextRun() {
    }

    public TextRun(String content) {
        setContent(content);
    }

    public TextRun(String content, TextElementStyle text_element_style) {
        setContent(content);
        this.text_element_style = text_element_style;
    }

    public static TextRun fromContenTextRunBlock(ContentTextRun contentTextRun) {
        if (contentTextRun == null) return null;
        TextRun textRunBlock = new TextRun();

        textRunBlock.setContent(contentTextRun.getText());
        textRunBlock.text_element_style = TextElementStyle.fromContentTextStyle(contentTextRun.getStyle());

        return textRunBlock;
    }
}
