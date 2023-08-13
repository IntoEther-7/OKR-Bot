import com.hellocrop.okrbot.dao.AuthMapper;
import com.hellocrop.okrbot.entity.JsonString;
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
}
