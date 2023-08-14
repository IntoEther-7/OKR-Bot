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
        //��ʽ��ʱ��  ����Ҫɶ��ʽ�����Լ�д EE�����ڵ���˼ һ��ʱ���ò����� ����������
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd EE");
        //�õ������ʱ��  ���ʱ������㲻Ҫ��ǰʱ�� Ҫ�Լ���ʱ����� ����������޸ļ���
        Calendar nowTime = Calendar.getInstance();
        //����������ÿ�ܵĵ�һ�죬�����й��˵�һ������һ���м��޸�������������ֻ�ȡ���յ�ʱ��ʱ����������
        //�����ҽ����� 2022-8-23�ܶ� ����������˵ ���ܵ����վ�Ӧ���� 8-28�� ���������û�����ó�ʼʱ��
        //����ȥ��ȡ���յ�ʱ�� �ͻ���8-21 ��ΪĬ�������ǿ�ʼ������
        nowTime.setFirstDayOfWeek(Calendar.MONDAY);
        //�����ڵ�ʱ�䷭������ָ�����ܼ��ϣ�MONDAY��TUESDAY��WEDNESDAY��THURSDAY��FRIDAY��SATURDAY��SUNDAY
        //�������ǾͿ����õ�������ѡ���Ǹ�ʱ�������ܵ�������X��������
        //�ǻ������ �������ʱ��-7����������X  +7����������X  �����ļ�
        nowTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Calendar oldTime = new GregorianCalendar();
        oldTime.setTime(nowTime.getTime());
        //����1�� 1�������Ƕ���ݲ�����2�Ƕ��·ݲ�����3�Ƕ����ڲ�����5�Ƕ����ڲ�����11�Ƕ�Сʱ������12�ǶԷ��Ӳ�����13�Ƕ��������14�ǶԺ������
        //����2:�������������ӻ��߼�ȥ,����  ������,������ǰ�ƶ�
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
}
