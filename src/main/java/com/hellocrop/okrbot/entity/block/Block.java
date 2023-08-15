package com.hellocrop.okrbot.entity.block;

import com.hellocrop.okrbot.entity.block.type.BlockType;
import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.block.type.textelment.TextElement;
import com.hellocrop.okrbot.entity.contentblock.ContentBlock;
import com.hellocrop.okrbot.entity.contentblock.ContentBlockElement;
import com.hellocrop.okrbot.entity.contentblock.ContentParagraph;
import com.hellocrop.okrbot.entity.contentblock.ContentParagraphElement;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:54
 * @project okrbot
 */
@Data
@Builder
/**
 * 一个能够插入的块
 */ public class Block {

    private Integer block_type;

    private TextBlock text;
    private TextBlock heading1;
    private TextBlock heading2;
    private TextBlock heading3;
    private TextBlock heading4;
    private TextBlock heading5;
    private TextBlock heading6;
    private TextBlock heading7;
    private TextBlock heading8;
    private TextBlock heading9;
    private TextBlock bullet;
    private TextBlock ordered;
    private TextBlock code;
    private TextBlock quote;
    private TextBlock equation;
    private TextBlock todo;

    // TODO Image
    // TODO 支持更多块

    public Block() {

    }

    public Block(BlockType blockType) {
        block_type = blockType.type;
    }

    public static Block fromContentBlockElement(ContentBlockElement contentBlockElement) {
        Block block = new Block();

        switch (contentBlockElement.getType()) {
            case "paragraph":
                // 段落块转换成块
                Map<String, TextBlock> map = TextBlock.fromContentParagraph(contentBlockElement.getParagraph());
                for (Map.Entry<String, TextBlock> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "text" -> block.text = entry.getValue();
                        case "number" -> block.ordered = entry.getValue();
                        case "bullet" -> block.bullet = entry.getValue();
                        case "checkBox" -> block = null;
                        case "checkedBox" -> block = null;
                        case "indent" -> block = null;
                    }
                }
                break;
            case "gallery":
                block = null;
                break;
        }


        return block;
    }
}
