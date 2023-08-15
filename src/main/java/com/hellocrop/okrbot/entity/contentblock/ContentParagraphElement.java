package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

@Data
public class ContentParagraphElement {
    /**
     * 元素类型
     * <p>
     * 可选值有：
     * <p>
     * textRun：文本型元素
     * <p>
     * docsLink：文档链接型元素
     * <p>
     * person：艾特用户型元素
     */
    String type;
    ContentTextRun textRun;
    ContentDocsLink docsLink;
    ContentPerson person;
}
