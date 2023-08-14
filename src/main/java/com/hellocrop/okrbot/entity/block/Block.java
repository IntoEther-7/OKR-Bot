package com.hellocrop.okrbot.entity.block;

import com.hellocrop.okrbot.entity.block.type.BlcokType;
import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
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
    private MultiTypeBlock block;
    private BlcokType blcokType;
    private final String name;

    public Block(BlcokType blcokType, MultiTypeBlock block) {
        block_type = blcokType.type;
        name = blcokType.name;
    }

    /**
     * block�ṹ��װ
     *
     * @param type
     * @param block
     * @return
     */
    public static Map<String, MultiTypeBlock> constructBlock(BlcokType type, MultiTypeBlock block) {
        Map<String, MultiTypeBlock> map = new HashMap<>();
        map.put(type.name, block);
        return map;
    }
}
