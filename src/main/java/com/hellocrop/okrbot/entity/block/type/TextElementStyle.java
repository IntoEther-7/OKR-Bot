package com.hellocrop.okrbot.entity.block.type;

import lombok.Data;

import java.util.List;

@Data
public class TextElementStyle implements MultiTypeBlock{
    TextStyle style;
    List<TextElement> elements;
}
