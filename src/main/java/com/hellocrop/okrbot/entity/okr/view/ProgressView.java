package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.hellocrop.okrbot.entity.okr.Progress;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import lombok.Data;

import java.util.List;

@Data
public class ProgressView {
    String pgsIdx;
    BlockMessage blockMessage; // 一个Block对象，已经封装好
    String modify_time;

    public static ProgressView fromProgressRecord(ProgressRecord progressRecord) {
        ProgressView progressView = new ProgressView();

        progressView.pgsIdx = progressRecord.getProgress_id();

        progressView.modify_time = progressRecord.getModify_time();

        progressView.blockMessage = BlockMessage.fromProgressRecord(progressRecord);

        return progressView;
    }
}
