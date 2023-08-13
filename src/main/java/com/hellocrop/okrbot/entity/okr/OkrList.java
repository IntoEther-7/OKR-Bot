package com.hellocrop.okrbot.entity.okr;

import lombok.Data;

import java.util.List;

/**
 * @author IntoEther-7
 * @date 2023/8/13 10:30
 * @project okrbot
 */
@Data
public class OkrList {
    private String userId;
    private List<Okr> okr_list;

    public OkrList(String userId, List<Okr> okr_list) {
        this.userId = userId;
        this.okr_list = okr_list;
    }
}
