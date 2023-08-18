import com.hellocrop.okrbot.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTest {
    @Test
    public void test1() {
        DateUtil dateUtil1 = new DateUtil(new SimpleDateFormat("yyyyMMdd HH:mm"));
        System.out.println(dateUtil1.string());

        DateUtil dateUtil2 = new DateUtil(new SimpleDateFormat("yyyyMMdd HH:mm", Locale.CHINA));
        System.out.println(dateUtil2.string());
    }

    @Test
    public void test2() {
        Date date = new Date();

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd HH:mm");
        simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd HH:mm");
        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));

        System.out.println(simpleDateFormat1.format(date));
        System.out.println(simpleDateFormat2.format(date));
    }
}
