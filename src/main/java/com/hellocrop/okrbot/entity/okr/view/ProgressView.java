package com.hellocrop.okrbot.entity.okr.view;

import com.hellocrop.okrbot.entity.JsonString;
import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
public class ProgressView {
    String pgsIdx;
    List<Object> blocks; // 一个Block对象，已经封装好
}
