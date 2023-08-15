package com.hellocrop.okrbot.entity.okr;

import com.hellocrop.okrbot.entity.contentblock.ContentBlock;
import lombok.Data;

/**
 * @author IntoEther-7
 * @date 2023/8/13 21:50
 * @project okrbot
 */
@Data
public class Progress {
    private ContentBlock content;
    private String modify_time;
    private String progress_id;
}

