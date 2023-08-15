package com.hellocrop.okrbot.entity.block.type.textelment;

import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.contentblock.ContentPerson;
import lombok.Data;

@Data
public class MentionUser {
    String user_id;
    TextElementStyle style;

    public MentionUser() {

    }

    public MentionUser(String user_id) {
        this.user_id = user_id;
    }

    public MentionUser(String user_id, TextElementStyle style) {
        this.user_id = user_id;
        this.style = style;
    }

    public static MentionUser fromContentPerson(ContentPerson contentPerson) {
        if (contentPerson == null) return null;
        MentionUser mentionUser = new MentionUser();
        mentionUser.user_id = contentPerson.getOpenId();
        return mentionUser;
    }
}
