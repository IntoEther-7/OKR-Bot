package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

@Data
public class ContentBlockElement {
    /**
     * 文档元素类型
     * 可选值有：
     * paragraph：文本段落
     * gallery：图片
     */
    String type;
    ContentParagraph paragraph;
    ContentGallery gallery;
}
