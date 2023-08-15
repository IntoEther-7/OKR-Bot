package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.okr.Okr;
import com.hellocrop.okrbot.entity.okr.OkrList;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class UserView {
    String useIdx;
    TextBlock block;
    List<OkrView> okrViews; // 一系列目标

    public static UserView fromOkrList(OkrList okrList) {
        UserView userView = new UserView();

        userView.useIdx = okrList.getUserId();

        // 处理block
        userView.block = TextBlock.mentionUserBlock(userView.useIdx);

        // 处理OkrList为List<OkrView>
        userView.okrViews = new LinkedList<>();
        for (Okr okr : okrList.getOkr_list()) {
            userView.okrViews.add(OkrView.fromOkr(okr));
        }

        return userView;
    }
}
