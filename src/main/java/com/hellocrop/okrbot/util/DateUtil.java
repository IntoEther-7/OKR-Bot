package com.hellocrop.okrbot.util;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author IntoEther-7
 * @date 2023/8/13 19:13
 * @project okrbot
 */
@Data
public class DateUtil {

    private final Calendar thisSun;
    private final Calendar lastSun;
    private DateFormat format;


    public DateUtil() {

        // 这周日
        Calendar thisMon = Calendar.getInstance();
        thisMon.setFirstDayOfWeek(Calendar.MONDAY);
        thisMon.setTime(new Date());
        thisMon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisMon.set(Calendar.SECOND, 0);
        thisMon.set(Calendar.MINUTE, 0);
        thisMon.set(Calendar.HOUR_OF_DAY, 0);
        thisMon.set(Calendar.MILLISECOND, 0);
        thisMon.add(Calendar.DATE, 7);


        // 上周日
        Calendar lastMon = Calendar.getInstance();
        lastMon.setTime(thisMon.getTime());
        lastMon.add(Calendar.DATE, -7);

        this.thisSun = thisMon;
        this.lastSun = lastMon;

        format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    }

    public DateUtil(DateFormat format) {

        Calendar thisMon = Calendar.getInstance();
        thisMon.setFirstDayOfWeek(Calendar.MONDAY);
        thisMon.setTime(new Date());
        thisMon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisMon.set(Calendar.SECOND, 0);
        thisMon.set(Calendar.MINUTE, 0);
        thisMon.set(Calendar.HOUR_OF_DAY, 0);
        thisMon.set(Calendar.MILLISECOND, 0);
        thisMon.add(Calendar.DATE, 7);

        // 上周日
        Calendar lastMon = Calendar.getInstance();
        lastMon.setTime(thisMon.getTime());
        lastMon.add(Calendar.DATE, -7);

        this.thisSun = thisMon;
        this.lastSun = lastMon;


        this.format = format;
    }

    public DateUtil(int day) {
        Calendar thisMon = Calendar.getInstance();
        thisMon.setFirstDayOfWeek(Calendar.MONDAY);
        thisMon.setTime(new Date());
        thisMon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        thisMon.set(Calendar.SECOND, 0);
        thisMon.set(Calendar.MINUTE, 0);
        thisMon.set(Calendar.HOUR_OF_DAY, 0);
        thisMon.set(Calendar.MILLISECOND, 0);
        thisMon.add(Calendar.DATE, 7);

        // 上周日
        Calendar lastMon = Calendar.getInstance();
        lastMon.setTime(thisMon.getTime());
        lastMon.add(Calendar.DATE, -7);


        this.thisSun = thisMon;
        this.lastSun = lastMon;

        format = new SimpleDateFormat("yyyyMMdd");
    }

    public String string() {
        return format.format(lastSun.getTime()) + "-" + format.format(thisSun.getTime());
    }

    public boolean inThisWeek(Date date) {
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastSun.before(record) && thisSun.after(record);
    }

    public boolean inThisWeek(String time) {
        Date date = new Date(Long.parseLong(time));
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastSun.before(record) && thisSun.after(record);
    }

    public boolean inThisWeek(Long time) {
        Date date = new Date(time);
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastSun.before(record) && thisSun.after(record);
    }
}
