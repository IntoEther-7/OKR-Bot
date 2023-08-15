package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

import java.util.List;

@Data
public class ContentBlock {
    private List<ContentBlockElement> blocks;
}
