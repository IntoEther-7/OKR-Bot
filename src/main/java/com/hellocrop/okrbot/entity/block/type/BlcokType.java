package com.hellocrop.okrbot.entity.block.type;

/**
 * @author IntoEther-7
 * @date 2023/8/13 13:03
 * @project okrbot
 */
public enum BlcokType {
    TEXT("text", 2, "�ı� Block"), HEADING1("heading1", 3, "һ������ Block"), HEADING2("heading2", 4, "�������� Block"),
    HEADING3("heading3", 5, "�������� Block"), HEADING4("heading4", 6, "�ļ����� Block"), HEADING5("heading5", 7, "�弶���� " +
            "Block"), HEADING6("heading6", 8, "�������� Block"), HEADING7("heading7", 9, "�߼����� Block"), HEADING8(
            "heading8", 10, "�˼����� Block"), HEADING9("heading9", 11, "�ż����� Block"), BULLET("bullet", 12,
            "�����б�" + " Block"), ORDERED("ordered", 13, "�����б� Block"), CODE("code", 14, "����� Block"), QUOTE("quote",
            15, "���� " + "Block"), EQUATION("equation", 999, "��ʽ Block"), TODO("todo", 17, "�������� Block"), BITABLE(
            "bitable", 18, "��ά��� Block"), CALLOUT("callout", 19, "������ Block"), CHAT_CARD("chat_card", 20,
            "Ⱥ�Ŀ�Ƭ Block"), DIVIDER("divider", 22, "�ָ��� Block"), FILE("file", 23, "�ļ� Block"), GRID("grid", 24,
            "���� " + "Block"), IFRAME("iframe", 26, "��Ƕ Block"), IMAGE("image", 27, "ͼƬ Block"), ISV("isv", 999,
            "���� " + "Block"), ADD_ONS("add_ons", 999, "Add-ons"), SHEET("sheet", 30, "���ӱ�� Block"), TABLE("table", 31
            , "��� " + "Block"), QUOTE_CONTAINER("quote_container", 34, "�������� Block"), OKR("okr", 36,
            "OKR " + "Block" + "��������ʹ��" + " " + "user_access_token ʱ����"), COMMENT_IDS("comment_ids", 999, "���� id " +
            "�б�ʾ��ֵ��[1660030311959965796]"), WIKI_CATALOG("wiki_catalog", 42, "Wiki " + "��Ŀ¼" + " Block");
    public String name;
    public int type;
    public String desc;

    BlcokType(String name, int type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }
}

