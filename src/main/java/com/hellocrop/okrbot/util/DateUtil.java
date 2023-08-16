package com.hellocrop.okrbot.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author IntoEther-7
 * @date 2023/8/13 19:13
 * @project okrbot
 */
@Data
public class DateUtil {

    private final Calendar thisSat;
    private final Calendar lastSat;
    private final Calendar lastLastSat;
    private DateFormat format;


    public DateUtil() {

        Calendar thisSat = Calendar.getInstance();
        thisSat.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSat.setTime(new Date());
        thisSat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        // 上周六
        Calendar lastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());
        lastSat.add(Calendar.DATE, -7);

        // 上上周六
        Calendar lastLastSat = Calendar.getInstance();
        lastLastSat.setTime(thisSat.getTime());
        lastLastSat.add(Calendar.DATE, -14);

        this.thisSat = thisSat;
        this.lastSat = lastSat;
        this.lastLastSat = lastLastSat;

        format = new SimpleDateFormat("yyyyMMdd");
    }

    public DateUtil(DateFormat format) {

        Calendar thisSat = Calendar.getInstance();
        thisSat.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        // 上周六
        Calendar lastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());
        lastSat.add(Calendar.DATE, -7);

        // 上上周六
        Calendar lastLastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());
        lastSat.add(Calendar.DATE, -14);

        this.thisSat = thisSat;
        this.lastSat = lastSat;
        this.lastLastSat = lastLastSat;

        this.format = format;
    }

    public DateUtil(int day) {
        Calendar thisSat = Calendar.getInstance();
        thisSat.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSat.setTime(new Date());
        thisSat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        thisSat.add(Calendar.DATE, day);

        // 上周六
        Calendar lastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());
        lastSat.add(Calendar.DATE, -7);

        // 上上周六
        Calendar lastLastSat = Calendar.getInstance();
        lastLastSat.setTime(thisSat.getTime());
        lastLastSat.add(Calendar.DATE, -14);

        this.thisSat = thisSat;
        this.lastSat = lastSat;
        this.lastLastSat = lastLastSat;

        format = new SimpleDateFormat("yyyyMMdd");
    }

    public String string() {
        return format.format(lastLastSat.getTime()) + "-" + format.format(lastSat.getTime());
    }

    public boolean inThisWeek(Date date) {
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastLastSat.before(record) && lastSat.after(record);
    }
    public boolean inThisWeek(String time) {
        Date date = new Date(Long.parseLong(time));
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastLastSat.before(record) && lastSat.after(record);
    }

    public boolean inThisWeek(Long time) {
        Date date = new Date(time);
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        return lastLastSat.before(record) && lastSat.after(record);
    }
}
