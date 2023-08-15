package com.hellocrop.okrbot.entity.block.type;

import com.hellocrop.okrbot.entity.contentblock.ContentParagraphStyle;
import lombok.Data;

@Data
public class TextStyle {
    Integer align;
    Boolean done;
    Boolean folded;
    Integer language;
    Boolean wrap;
}
