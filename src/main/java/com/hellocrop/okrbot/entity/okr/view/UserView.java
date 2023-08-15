package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.type.TextBlock;
import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class UserView {
    String useIdx;
    TextBlock mention;
    List<ObjectiveView> objectiveViews; // 一系列目标

    /**
     * 没有进展
     *
     * @return
     */
    public boolean isEmpty() {
        if (objectiveViews.isEmpty()) return true;

        boolean isEmpty = true;
        for (ObjectiveView ov : objectiveViews) {
            isEmpty &= ov.isEmpty();
        }
        return isEmpty;
    }

    public void filter() {
        if (objectiveViews == null) return;
        Iterator<ObjectiveView> iterator = objectiveViews.iterator();
        while (iterator.hasNext()) {
            ObjectiveView objectiveView = iterator.next();
            // 如果没进展，删掉这个目标
            if (objectiveView.getProgressViews().isEmpty()) iterator.remove();
        }
    }
}
