package com.hellocrop.okrbot.entity.contentblock;

import lombok.Data;

@Data
public class ContentList {
    /**
     * 列表类型
     * <p>
     * 可选值有：
     * <p>
     * number：有序列表
     * <p>
     * bullet：无序列表
     * <p>
     * checkBox：任务列表
     * <p>
     * checkedBox：已完成的任务列表
     * <p>
     * indent：tab缩进
     */
    String type;
    Integer indentLevel;
    Integer number;
}
