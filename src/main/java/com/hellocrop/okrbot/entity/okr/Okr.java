package com.hellocrop.okrbot.entity.okr;

import lombok.Data;

import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 10:30
 * @project okrbot
 */
@Data
public class Okr {
    private int confirm_status;
    private String id;
    private String name;
    private List<Objective> objective_list;
    private String period_id;
    private String permission;
}
