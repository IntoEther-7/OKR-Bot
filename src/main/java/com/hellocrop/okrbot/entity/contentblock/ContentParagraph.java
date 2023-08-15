package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ContentParagraph {
    ContentParagraphStyle style;
    List<ContentParagraphElement> elements = new LinkedList<>();
}
