package com.hellocrop.okrbot.entity.block;

import com.hellocrop.okrbot.entity.block.type.BlockType;
import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:54
 * @project okrbot
 */
@Data
public class Block {

    private Integer block_type;
    private TextBlock text;
    private TextBlock heading1;
    private TextBlock heading2;
    private TextBlock heading3;
    private TextBlock heading4;
    private TextBlock heading5;
    private TextBlock bullet;

    public Block(BlockType blockType) {
        block_type = blockType.type;
    }

    /**
     * block�ṹ��װ
     *
     * @param type
     * @param block
     * @return
     */
    public static Map<String, MultiTypeBlock> constructBlock(BlockType type, MultiTypeBlock block) {
        Map<String, MultiTypeBlock> map = new HashMap<>();
        map.put(type.name, block);
        return map;
    }
}
