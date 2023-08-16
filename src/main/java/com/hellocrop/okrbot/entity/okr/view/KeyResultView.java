package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.okr.KeyResult;
import com.hellocrop.okrbot.entity.okr.Objective;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class KeyResultView {
    String krIdx;
    TextBlock block;
    List<ProgressView> progressViews;

    public static KeyResultView fromKeyResult(KeyResult keyResult, Integer objIdx, Integer krIdx) {
        KeyResultView keyResultView = new KeyResultView();

        keyResultView.krIdx = keyResult.getId();

        keyResultView.block = TextBlock.simpleTextBlock("O%dKR%d:  %s".formatted(objIdx, krIdx, keyResult.getContent()));

        keyResultView.progressViews = new LinkedList<>();
        for (ProgressRecord progressRecord : keyResult.getProgressRecords()) {
            keyResultView.progressViews.add(ProgressView.fromProgressRecord(progressRecord));
        }

        return keyResultView;
    }
}
