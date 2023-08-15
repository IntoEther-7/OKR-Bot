package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.okr.KeyResult;
import com.hellocrop.okrbot.entity.okr.Objective;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ObjectiveView {
    String objIdx;
    TextBlock block;
    List<KeyResultView> keyResultViews;
    List<ProgressView> progressViews; // 一系列进展

    public static ObjectiveView fromObjective(Objective objective, int objIdx) {
        ObjectiveView objectiveView = new ObjectiveView();

        objectiveView.objIdx = objective.getId();

        objectiveView.block = TextBlock.simpleTextBlock("O%d: %s".formatted(objIdx, objective.getContent()));

        objectiveView.keyResultViews = new LinkedList<>();
        List<KeyResult> krList = objective.getKr_list();
        for (int i = 0; i < krList.size(); i++) {
            KeyResult keyResult = krList.get(i);
            objectiveView.keyResultViews.add(KeyResultView.fromKeyResult(keyResult, objIdx, i + 1));
        }

        objectiveView.progressViews = new LinkedList<>();
        for (ProgressRecord progressRecord : objective.getProgressRecords()) {
            objectiveView.progressViews.add(ProgressView.fromProgressRecord(progressRecord));
        }

        return objectiveView;
    }
}
