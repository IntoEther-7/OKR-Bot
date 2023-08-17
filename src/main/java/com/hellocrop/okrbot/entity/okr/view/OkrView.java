package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.okr.Objective;
import com.hellocrop.okrbot.entity.okr.Okr;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class OkrView {
    String okrIdx;
    TextBlock block;
    String name;
    List<ObjectiveView> objectiveViews;

    public static OkrView fromOkr(Okr okr) {
        OkrView okrView = new OkrView();

        okrView.okrIdx = okr.getId();

        okrView.name = okr.getName();

        okrView.block = TextBlock.simpleTextBlock(okr.getName());

        okrView.objectiveViews = new LinkedList<>();
        List<Objective> objectiveList = okr.getObjective_list();
        if (objectiveList.isEmpty()) {
            // 没设置目标
            // 没有用这个OKR
            // 可以删了
            return null;
        } else {
            for (int i = 0; i < objectiveList.size(); i++) {
                Objective objective = objectiveList.get(i);
                okrView.objectiveViews.add(ObjectiveView.fromObjective(objective, i + 1));
            }
        }

        return okrView;
    }
}
