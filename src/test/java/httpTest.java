import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hellocrop.okrbot.dao.AuthMapper;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.okr.Progress;
import com.hellocrop.okrbot.service.OkrService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author IntoEther-7
 * @date 2023/8/13 9:05
 * @project okrbot
 */
@Slf4j
public class httpTest {
    @Test
    public void testAuth() throws Exception {
        new OkrService().weekReport("cli_a45f78cb4a79500e", "9ZIvC5EoYrqHqMSBE6agV62t7IWBrvzT");
    }

    @Test
    public void toUpperCase() {
        System.out.println("""
                text
                                
                heading1
                                
                heading2
                                
                heading3
                                
                heading4
                                
                heading5
                                
                heading6
                                
                heading7
                                
                heading8
                                
                heading9
                                
                bullet
                                
                ordered
                                
                code
                                
                quote
                                
                equation
                                
                todo
                                
                bitable
                                
                callout
                                
                chat_card
                                
                divider
                                
                file
                                
                grid
                                
                iframe
                                
                image
                                
                isv
                                
                add_ons
                                
                sheet
                                
                table
                                
                quote_container
                                
                okr
                                
                comment_ids
                                
                wiki_catalog
                """.toUpperCase());
    }

    @Test
    public void test3() throws JsonProcessingException {
        String s = """
                {"code":0,"data":{"content":{"blocks":[{"paragraph":{"elements":[{"textRun":{"style":{},"text":"本周进展：由"},"type":"textRun"},{"person":{"openId":"ou_dc0664d3d275ff2cbcbbd60450fde475"},"type":"person"},{"textRun":{"style":{},"text":"负责全年预算初稿"},"type":"textRun"}]},"type":"paragraph"},{"paragraph":{"elements":[{"textRun":{"style":{},"text":"下周计划：敲定全年预算"},"type":"textRun"},{"person":{"openId":"ou_80f012ab640bde78bcc18daa378a4f31"},"type":"person"},{"person":{"openId":"ou_05ab03d6f1fba287a2aa4e1decde4f12"},"type":"person"},{"person":{"openId":"ou_dc0664d3d275ff2cbcbbd60450fde475"},"type":"person"}]},"type":"paragraph"}]},"modify_time":"1691639873828","progress_id":"7265534423021961218"},"msg":"success"}
                """;
        ObjectMapper mapper = new ObjectMapper();
        Progress data = mapper.readValue(mapper.writeValueAsString(mapper.readTree(s).get("data")), Progress.class);
        log.info(s);
    }
}
