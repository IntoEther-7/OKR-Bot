import com.hellocrop.okrbot.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author IntoEther-7
 * @date 2023/8/13 19:07
 * @project okrbot
 */
public class DateTest {
    @Test
    public void test1() {
        //格式化时间  具体要啥格式你们自己写 EE是星期的意思 一般时候用不到的 年月日足以
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd EE");
        //拿到今天的时间  这个时间如果你不要当前时间 要自己传时间进来 在这里进行修改即可
        Calendar nowTime = Calendar.getInstance();
        //！！！设置每周的第一天，我们中国人第一天是周一，切记修改这个，否则会出现获取周日的时候，时间错误的问题
        //例如我今天是 2022-8-23周二 对于我们来说 本周的周日就应该是 8-28日 但是如果你没有设置初始时间
        //你在去获取周日的时间 就会是8-21 因为默认周日是开始的日期
        nowTime.setFirstDayOfWeek(Calendar.MONDAY);
        //把现在的时间翻到我们指定的周几上，MONDAY，TUESDAY，WEDNESDAY，THURSDAY，FRIDAY，SATURDAY，SUNDAY
        //这样我们就可以拿到我们所选的那个时间所在周的任意周X的日期了
        //那基于这个 算出来的时间-7就是上周周X  +7就是下周周X  哇塞的简单
        nowTime.set(Calendar.DAY_OF_WEEK,  Calendar.SATURDAY);
        Calendar oldTime = new GregorianCalendar();
        oldTime.setTime(nowTime.getTime());
        //参数1： 1则代表的是对年份操作，2是对月份操作，3是对星期操作，5是对日期操作，11是对小时操作，12是对分钟操作，13是对秒操作，14是对毫秒操作
        //参数2:把日期往后增加或者减去,整数  往后推,负数往前移动
        oldTime.add(Calendar.DATE, -7);
        System.out.println(format.format(oldTime.getTime()));
    }

    @Test
    public void test2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd EE");
        Calendar thisSaturday = Calendar.getInstance();
        thisSaturday.setFirstDayOfWeek(Calendar.SUNDAY);
        thisSaturday.set(Calendar.DAY_OF_WEEK,  Calendar.SATURDAY);
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
}
