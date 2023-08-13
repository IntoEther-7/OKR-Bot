package com.hellocrop.okrbot.entity.block;

import com.hellocrop.okrbot.entity.block.type.MultiTypeBlock;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author IntoEther-7
 * @date 2023/8/13 12:55
 * @project okrbot
 */
@Data
public class BlockMessage {
    List<Map<String, MultiTypeBlock>> children;
    int index = -1;
}
