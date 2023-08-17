package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.okr.Okr;
import com.hellocrop.okrbot.entity.okr.OkrList;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class UserView {
    String userIdx;
    String numIdx; // 不知从何获取
    TextBlock block;
    List<OkrView> okrViews; // 一系列目标

    public static UserView fromOkrList(OkrList okrList) {
        UserView userView = new UserView();

        userView.userIdx = okrList.getUserId();

        // 处理block
        userView.block = TextBlock.mentionUserBlock(userView.userIdx);

        // 处理OkrList为List<OkrView>
        // 无Okr -> okrViews = null
        // 有Okr -> okrViews 为空
        userView.okrViews = new LinkedList<>();
        for (Okr okr : okrList.getOkr_list()) {
            OkrView okrView = OkrView.fromOkr(okr);
            if (okrView != null) userView.okrViews.add(okrView);
        }
        if (userView.okrViews.isEmpty()) userView.okrViews = null;

        return userView;
    }
}
