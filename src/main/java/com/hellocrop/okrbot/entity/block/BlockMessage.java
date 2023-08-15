package com.hellocrop.okrbot.entity.block;

import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
import com.hellocrop.okrbot.entity.contentblock.ContentBlockElement;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:55
 * @project okrbot
 */
@Data

// 整个请求体
public class BlockMessage {
    List<Block> children = new LinkedList<>();
    int index = -1;

    public static BlockMessage fromProgressRecord(ProgressRecord progressRecord) {
        BlockMessage blockMessage = new BlockMessage();

        List<ContentBlockElement> blocks = progressRecord.getContent().getBlocks();
        for (ContentBlockElement contentBlockElement : blocks) {
            blockMessage.children.add(Block.fromContentBlockElement(contentBlockElement));
        }

        return blockMessage;
    }
}
