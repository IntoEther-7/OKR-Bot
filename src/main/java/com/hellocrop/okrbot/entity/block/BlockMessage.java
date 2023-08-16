package com.hellocrop.okrbot.entity.block;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.contentblock.ContentBlockElement;
import com.hellocrop.okrbot.entity.okr.ProgressRecord;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:55
 * @project okrbot
 */
@Data
@Slf4j
// 整个请求体
public class BlockMessage {
    List<Block> children = new LinkedList<>();
    int index = -1;

    public static BlockMessage fromProgressRecord(ProgressRecord progressRecord) {
        BlockMessage blockMessage = new BlockMessage();

        List<ContentBlockElement> blocks = progressRecord.getContent().getBlocks();
        for (ContentBlockElement contentBlockElement : blocks) {
            Block block = Block.fromContentBlockElement(contentBlockElement);
            if (block != null) blockMessage.children.add(block);
        }

        return blockMessage;
    }

    public void check() {
        for (Block child : children) {
            try {
                child.check();
            } catch (IllegalAccessException e) {
                try {
                    BlockMessage blockMessage = new BlockMessage();
                    blockMessage.getChildren().add(child);
                    log.error(JsonString.objectMapper.writeValueAsString(blockMessage));
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        }
    }
}
