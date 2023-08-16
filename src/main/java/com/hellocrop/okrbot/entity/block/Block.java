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
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:54
 * @project okrbot
 */
@Builder
@Data
public class Block {
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


    public static Block fromContentBlockElement(ContentBlockElement contentBlockElement) {
        Block block = null;

        switch (contentBlockElement.getType()) {
            case "paragraph":
                // 段落块转换成块
                Map<String, TextBlock> map = TextBlock.fromContentParagraph(contentBlockElement.getParagraph());
                for (Map.Entry<String, TextBlock> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "text" -> block = Block.builder().block_type(BlockType.TEXT.type).text(entry.getValue()).build(); //block.text = entry.getValue()
                        case "number" -> block = Block.builder().block_type(BlockType.ORDERED.type).ordered(entry.getValue()).build(); // block.ordered = entry.getValue()
                        case "bullet" -> block = Block.builder().block_type(BlockType.BULLET.type).bullet(entry.getValue()).build(); // block.bullet = entry.getValue()
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

    public void check() throws IllegalAccessException {
        Field[] fields = this.getClass().getFields();

        assert block_type != null;
        BlockType blockType = BlockType.id2BlockType(block_type);

        for (Field field : fields) {
            String fieldName = field.getName(); // 属性名
            if (fieldName.equals(blockType) || fieldName.equals("block_type")) {
                // 应该不为空
                assert field.get(this) != null;
            } else {
                // 应该为空
                assert field.get(this) == null;
            }
        }
    }
}




