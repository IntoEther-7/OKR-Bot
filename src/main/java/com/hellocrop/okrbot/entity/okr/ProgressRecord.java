package com.hellocrop.okrbot.entity.okr;

import com.hellocrop.okrbot.entity.contentblock.ContentBlock;
import lombok.Data;

/**
 * @author IntoEther-7
 * @date 2023/8/13 21:37
 * @project okrbot
 */
@Data
public class ProgressRecord {
    String progress_id;
    String modify_time;
    ContentBlock content;
}
