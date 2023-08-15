package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

@Data
public class ContentTextStyle {
    Boolean bold;
    Boolean strikeThrough;
    ContentColor backColor;
    ContentColor textColor;
    ContentLink link;
}
