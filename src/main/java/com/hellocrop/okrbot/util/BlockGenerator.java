package com.hellocrop.okrbot.util;

import com.hellocrop.okrbot.entity.block.Block;
import com.hellocrop.okrbot.entity.block.type.BlockType;
import com.hellocrop.okrbot.entity.block.type.Link;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
import com.hellocrop.okrbot.entity.block.type.TextElementStyle;
import com.hellocrop.okrbot.entity.block.type.textelment.TextElement;
import com.hellocrop.okrbot.entity.okr.view.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class BlockGenerator {
    private static Integer idx;

    public static void insertIntro(List<Block> content) {
        content.add(0, Block.builder().block_type(BlockType.TEXT.type).text(TextBlock.simpleTextBlock("本文档由办公自动化系统根据 OKR 的进展自动生成，用于向全员同步短期进展和计划，推荐大家对具体进展直接在此份文档中通过评论来讨论。")).build());
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        content.add(1, Block.builder().block_type(BlockType.TEXT.type).text(TextBlock.simpleTextBlock("最后更新时间：%s".formatted(dateFormat.format(date)))).build());
        content.add(2, Block.builder().block_type(BlockType.TEXT.type).text(TextBlock.simpleTextBlock("")).build());
    }

    public static List<Block> travelOkrView(UserView userView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的

        idx = 1;

        // 如果有信息，加入自身(User)和下层(Okr/显示未找到进展/显示未设置 OKR)信息
        // 加入用户信息


        // 无Okr -> "未设置 OKR" -> 只包含user信息
        // TODO: 如果需要显示，需要修改上层逻辑

        // 有Okr -> 无进展数据 -> 第二个block为null
        // TODO: 如果需要显示，需要修改上层逻辑

        for (OkrView okrView : userView.getOkrViews()) {
            // 对每个OkrView，遍历下层(Objective)
            List<Block> ov = travelObjectiveView(okrView);
            // 如果有信息，加入自身(OKR)和下层(O)信息
            if (!ov.isEmpty()) {
                blocks.add(Block.builder().block_type(BlockType.HEADING4.type).heading4(okrView.getBlock()).build());
                blocks.addAll(ov);
            }
        }

        // 如果有信息，加入自身(U)和下层(OKR)信息
        if (!blocks.isEmpty())
            blocks.add(0, Block.builder().block_type(BlockType.HEADING3.type).heading3(TextBlock.mentionUserBlock(userView.getUserIdx())).build());

        idx = null;

        return blocks;
    }

    private static List<Block> travelObjectiveView(OkrView okrView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的


        for (ObjectiveView objectiveView : okrView.getObjectiveViews()) {
            // 对每个ObjectiveView，遍历KeyResult和ProgressView
            List<Block> pv = travelKeyResultAndProgressView(okrView, objectiveView);
            // 如果有信息，加入信息
            if (!pv.isEmpty()) {
                blocks.addAll(pv);
            }
        }

        return blocks;
    }

    private static List<Block> travelKeyResultAndProgressView(OkrView okrView, ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储目标和下层的
        // 如果有信息，加入自身(Objective)和下层(Progress)信息
        // 获取所有PR，不为空加入object描述信息和pr
        // 给一个格式
        TextElementStyle textElementStyle = TextElementStyle.builder().text_color(5).build();

        List<Block> pr = travelProgressView(objectiveView);
        if (!pr.isEmpty()) {
            objectiveView.getBlock().getElements().add(0, TextElement.simpleTextRun("%d. （%s）".formatted(idx++, okrView.getName())));
            Block build = Block.builder().block_type(BlockType.HEADING4.type).heading4(objectiveView.getBlock()).build();

            // 设置格式
            for (TextElement element : build.getHeading4().getElements()) {
                element.getText_run().setText_element_style(textElementStyle);
            }
            blocks.add(build);
            blocks.addAll(pr);
        }

        // KR->PR遍历，不为空加入kr描述信息和progress
        for (KeyResultView keyResultView : objectiveView.getKeyResultViews()) {
            List<Block> kr_pr = travelProgressView(keyResultView);
            // 如果有信息，加入自身(KR)和下层(P)信息
            if (!kr_pr.isEmpty()) {
                keyResultView.getBlock().getElements().add(0, TextElement.simpleTextRun("%d. （%s）".formatted(idx++, okrView.getName())));
                Block build = Block.builder().block_type(BlockType.HEADING4.type).heading4(keyResultView.getBlock()).build();

                // 设置格式
                for (TextElement element : build.getHeading4().getElements()) {
                    // TODO 链接
                    Link link = new Link("https://www.baidu.com");
                    element.getText_run().setText_element_style(textElementStyle);
                }
                blocks.add(build);
                blocks.addAll(kr_pr);
            }
        }

        return blocks;
    }

    private static List<Block> travelProgressView(ObjectiveView objectiveView) {
        List<Block> blocks = new LinkedList<>(); // 存储Progress

        // 底层，加入O和自身信息（Progress）
        for (ProgressView progressView : objectiveView.getProgressViews()) {
            if (!progressView.getBlockMessage().getChildren().isEmpty()) {
                // 加入正文
                blocks.addAll(progressView.getBlockMessage().getChildren());
            }
        }

        return blocks;
    }

    private static List<Block> travelProgressView(KeyResultView keyResultView) {
        List<Block> blocks = new LinkedList<>(); // 存储Progress

        // 底层，加入PR和自身信息（Progress）
        for (ProgressView progressView : keyResultView.getProgressViews()) {
            if (!progressView.getBlockMessage().getChildren().isEmpty()) {
                // 加入正文
                blocks.addAll(progressView.getBlockMessage().getChildren());
            }
        }
        return blocks;
    }
}
