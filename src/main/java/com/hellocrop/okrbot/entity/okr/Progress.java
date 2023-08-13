package com.hellocrop.okrbot.entity.okr;

import lombok.Data;

import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 21:50
 * @project okrbot
 */
@Data
public class Progress {
    private String source_title;
    private String source_url;
    private String target_id;
    private Integer target_type;
    private ContentBlock content;
    private String modify_time;
    private String progress_id;
}

@Data
class ContentBlock {
    private List<Object> blocks;
}