package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.contentblock.ContentPerson;
import lombok.Data;

@Data
public class MentionUser {
    String user_id;
    TextElementStyle text_element_style;

    public MentionUser() {

    }

    public MentionUser(String user_id) {
        this.user_id = user_id;
    }

    public MentionUser(String user_id, TextElementStyle text_element_style) {
        this.user_id = user_id;
        this.text_element_style = text_element_style;
    }

    public static MentionUser fromContentPerson(ContentPerson contentPerson) {
        if (contentPerson == null) return null;
        MentionUser mentionUser = new MentionUser();
        mentionUser.user_id = contentPerson.getOpenId();
        return mentionUser;
    }
}
