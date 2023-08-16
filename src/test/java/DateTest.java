import com.hellocrop.okrbot.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author IntoEther-7
 * @date 2023/8/13 19:07
 * @project okrbot
 */
public class DateTest {
    @Test
    public void test1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd EE");
        Calendar nowTime = Calendar.getInstance();
        nowTime.setFirstDayOfWeek(Calendar.MONDAY);
        nowTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Calendar oldTime = new GregorianCalendar();
        oldTime.setTime(nowTime.getTime());
        oldTime.add(Calendar.DATE, -7);
        System.out.println(format.format(oldTime.getTime()));
    }

    @Test
    public void test2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd EE");
        Calendar thisSaturday = Calendar.getInstance();
        thisSaturday.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSaturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Calendar lastSaturday = new GregorianCalendar();
        lastSaturday.setTime(thisSaturday.getTime());
        lastSaturday.add(Calendar.DATE, -7);
        System.out.println(format.format(lastSaturday.getTime()));
        System.out.println(format.format(thisSaturday.getTime()));
    }

    @Test
    public void test3() {
        DateUtil dateUtil = new DateUtil();
        System.out.println(dateUtil.string());
    }

    @Test
    public void  test4() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
        System.out.println(dateFormat.format(date));
    }
}
