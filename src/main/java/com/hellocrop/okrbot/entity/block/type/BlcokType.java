package com.hellocrop.okrbot.entity.block.type;

/**
 * @author IntoEther-7
 * @date 2023/8/13 13:03
 * @project okrbot
 */
public enum BlcokType {
    TEXT("text", 2, "文本 Block"), HEADING1("heading1", 3, "一级标题 Block"), HEADING2("heading2", 4, "二级标题 Block"),
    HEADING3("heading3", 5, "三级标题 Block"), HEADING4("heading4", 6, "四级标题 Block"), HEADING5("heading5", 7, "五级标题 " +
            "Block"), HEADING6("heading6", 8, "六级标题 Block"), HEADING7("heading7", 9, "七级标题 Block"), HEADING8(
            "heading8", 10, "八级标题 Block"), HEADING9("heading9", 11, "九级标题 Block"), BULLET("bullet", 12,
            "无序列表" + " Block"), ORDERED("ordered", 13, "有序列表 Block"), CODE("code", 14, "代码块 Block"), QUOTE("quote",
            15, "引用 " + "Block"), EQUATION("equation", 999, "公式 Block"), TODO("todo", 17, "待办事项 Block"), BITABLE(
            "bitable", 18, "多维表格 Block"), CALLOUT("callout", 19, "高亮块 Block"), CHAT_CARD("chat_card", 20,
            "群聊卡片 Block"), DIVIDER("divider", 22, "分割线 Block"), FILE("file", 23, "文件 Block"), GRID("grid", 24,
            "分栏 " + "Block"), IFRAME("iframe", 26, "内嵌 Block"), IMAGE("image", 27, "图片 Block"), ISV("isv", 999,
            "三方 " + "Block"), ADD_ONS("add_ons", 999, "Add-ons"), SHEET("sheet", 30, "电子表格 Block"), TABLE("table", 31
            , "表格 " + "Block"), QUOTE_CONTAINER("quote_container", 34, "引用容器 Block"), OKR("okr", 36,
            "OKR " + "Block" + "，仅可在使用" + " " + "user_access_token 时创建"), COMMENT_IDS("comment_ids", 999, "评论 id " +
            "列表，示例值：[1660030311959965796]"), WIKI_CATALOG("wiki_catalog", 42, "Wiki " + "子目录" + " Block");
    public String name;
    public int type;
    public String desc;

    BlcokType(String name, int type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }
}

