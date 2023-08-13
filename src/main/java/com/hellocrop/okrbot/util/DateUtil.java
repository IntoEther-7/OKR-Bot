package com.hellocrop.okrbot.util;

import lombok.Data;

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
    private DateFormat format;


    public DateUtil() {

        Calendar thisSat = Calendar.getInstance();
        thisSat.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        // 上周六
        Calendar lastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());
        lastSat.add(Calendar.DATE, -7);

        this.thisSat = thisSat;
        this.lastSat = lastSat;

        format = new SimpleDateFormat("yyyyMMdd");
    }

    public DateUtil(DateFormat format) {

        Calendar thisSat = Calendar.getInstance();
        thisSat.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSat.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

        // 上周六
        Calendar lastSat = Calendar.getInstance();
        lastSat.setTime(thisSat.getTime());

        this.thisSat = thisSat;
        this.lastSat = lastSat;
        this.format = format;
    }

    public String string() {
        return format.format(lastSat.getTime()) + "-" + format.format(thisSat.getTime());
    }

    public boolean inThisWeek(Date date) {
        Calendar record = Calendar.getInstance();
        record.setTime(date);
        if (lastSat.before(record) && thisSat.after(record)) return true;
        return false;
    }
}
