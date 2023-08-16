package com.hellocrop;

import com.hellocrop.okrbot.service.OkrService;

/**
 * @author IntoEther-7
 * @date ${DATE} ${TIME}
 * @project ${PROJECT_NAME}
 */
public class Main {
    public static void main(String[] args) {
        try {
            new OkrService().weekReport(System.getenv("APP_ID"),System.getenv("APP_SECRET"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}