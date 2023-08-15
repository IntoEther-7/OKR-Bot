package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class ObjectiveView {
    String objIdx;
    TextBlock blocks;
    List<ProgressView> progressViews; // 一系列进展

    public boolean isEmpty() {
        return progressViews.isEmpty();
    }
}
