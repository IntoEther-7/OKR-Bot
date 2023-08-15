package com.hellocrop.okrbot.util;

import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.BlockMessage;
import com.hellocrop.okrbot.entity.block.type.BlockType;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.block.type.TextRunBlock;
import com.hellocrop.okrbot.entity.okr.view.ObjectiveView;
import com.hellocrop.okrbot.entity.okr.view.ProgressView;
import com.hellocrop.okrbot.entity.okr.view.UserView;

import java.util.*;

public class BlockGenerator {
    public static List<BlockMessage> generateDocumentBlock(List<UserView> views) {
        BlockMessage blockMessage = new BlockMessage();
        blockMessage.setChildren(new LinkedList<>());

        LinkedList<Object> noOkr = new LinkedList<>();
        LinkedList<Object> noPgs = new LinkedList<>();
        LinkedList<Object> pgs = new LinkedList<>();


        // 先过滤
        for (UserView userView : views) {
            userView.filter();

            // 标题
            Block title = new Block(BlockType.HEADING2);
            title.setHeading2(TextBlock.mentionUserBlock(userView.getUseIdx()));
            // blockMessage.getChildren().add(title);

            if (userView.getObjectiveViews() == null) {
                // 1. 没有okr，objective == null
                Block body = new Block(BlockType.TEXT);
                body.setText(new TextBlock("未设置 OKR"));
                // blockMessage.getChildren().add(body);
                noOkr.add(title);
                noOkr.add(body);
            } else if (userView.getObjectiveViews().isEmpty()) {
                // 2. 有okr，但没进展， objective长度为0
                Block body = new Block(BlockType.TEXT);
                body.setText(new TextBlock("未找到进展"));
                // blockMessage.getChildren().add(body);
                noPgs.add(title);
                noPgs.add(body);
            } else {
                pgs.add(title);
                // 3. 有okr，有进展，objective长度不为0
                int idx = 0;
                for (ObjectiveView objectiveView : userView.getObjectiveViews()) {
                    // 遍历目标，取出目标block，作为三级标题
                    Block title3 = new Block(BlockType.HEADING3);

                    Map<String, Object> map = new HashMap<>();
                    TextRunBlock title_idx = new TextRunBlock("%d. ".formatted(++idx));
                    map.put("text_run", title_idx);

                    objectiveView.getBlocks().getElements().add(0, map);
                    title3.setHeading3(objectiveView.getBlocks());
                    // blockMessage.getChildren().add(title3);

                    TextElementStyle textElementStyle = new TextElementStyle();
                    textElementStyle.setText_color(5);

                    title_idx.setStyle(textElementStyle);

                    pgs.add(title3);

                    for (ProgressView progressView : objectiveView.getProgressViews()) {
                        // 遍历进展，取出进展，加入文本
                        // blockMessage.getChildren().addAll(progressView.getBlocks());
                        pgs.addAll(progressView.getBlocks());
                    }
                }
            }


        }
        blockMessage.getChildren().addAll(pgs);
        // blockMessage.getChildren().addAll(noPgs);
        // blockMessage.getChildren().addAll(noOkr);

        return splitBlockMessage(blockMessage);
    }

    private static List<BlockMessage> splitBlockMessage(BlockMessage blockMessage) {
        List<BlockMessage> blockMessages = new LinkedList<>();
        int size = blockMessage.getChildren().size();
        int idx = 0;
        while (idx < size) {
            BlockMessage slice = new BlockMessage();
            slice.setChildren(new LinkedList<>());
            for (int i = 0; i < 50 && i + idx < size; i++) {
                slice.getChildren().add(blockMessage.getChildren().get(i + idx));
            }
            blockMessages.add(slice);
            idx += 50;
        }

        return blockMessages;
    }

}
