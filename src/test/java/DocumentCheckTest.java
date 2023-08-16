import com.hellocrop.okrbot.util.DocumentCheckUtil;
import org.junit.jupiter.api.Test;

import java.nio.file.ClosedDirectoryStreamException;

public class DocumentCheckTest {
    @Test
    public void test1() {
        DocumentCheckUtil documentCheckUtil = new DocumentCheckUtil();
        documentCheckUtil.insertDocumentIdThisWeek("test1", "我是开心超人");
        documentCheckUtil.insertDocumentIdThisWeek("test2", "开不开心都超人");
        System.out.println(documentCheckUtil.getDocumentIdThisWeek("test1"));
        System.out.println(documentCheckUtil.getDocumentIdThisWeek("test2"));
        System.out.println(documentCheckUtil.getDocumentIdThisWeek("test3"));
    }

    @Test
    public void test2() {
        String s = "\n\n\n\nfskfjsal\n\n\n";
        s = s.replaceAll("\n","");
        System.out.println(s);
    }
}
