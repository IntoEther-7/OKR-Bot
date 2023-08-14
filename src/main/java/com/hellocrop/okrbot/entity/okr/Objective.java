package com.hellocrop.okrbot.entity.okr;

import lombok.Data;

import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 10:31
 * @project okrbot
 */
@Data
public class Objective {
    private String id;
    private String content; // Objective 内容
    private String progress_report; // Objective 备注内容
    private int score; // Objective 分数（0 - 100）
    private float weight; // Objective的权重（0 - 100）
    private List<KeyResult> kr_list; // Objective KeyResult 列表
    private String progress_rate_percent_last_updated_time; // 最后一次进度百分比更新时间 毫秒
    private String progress_rate_status_last_updated_time; // 最后一次状态更新时间 毫秒
    private String progress_record_last_updated_time; // 最后一次在侧边栏新增或者编辑进展的时间 毫秒
    private String progress_report_last_updated_time; // 最后一次编辑备注的时间 毫秒
    private String score_last_updated_time; // 最后一次打分更新时间 毫秒
    private String deadline; // 截止时间 毫秒
    private List<ProgressRecord> progress_record_list;
}
