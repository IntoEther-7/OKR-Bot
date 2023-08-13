package com.hellocrop.okrbot.entity.okr;

import lombok.Data;

import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 10:37
 * @project okrbot
 */
@Data
public class KeyResult {
    private String id;
    private String content;
    private int score;
    private int weight;
    private int kr_weight;
    private ProgressRate progress_rate;
    private String progress_rate_percent_last_updated_time; // ���һ�ν��Ȱٷֱȸ���ʱ�� ����
    private String progress_rate_status_last_updated_time; // ���һ��״̬����ʱ�� ����
    private String progress_record_last_updated_time; // ���һ���ڲ�����������߱༭��չ��ʱ�� ����
    private String progress_report_last_updated_time; // ���һ�α༭��ע��ʱ�� ����
    private String score_last_updated_time; // ���һ�δ�ָ���ʱ�� ����
    private String deadline; // ��ֹʱ�� ����
    private List<ProgressRecord> progress_record_list;
}
