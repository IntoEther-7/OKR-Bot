import com.fasterxml.jackson.core.JsonProcessingException;
import com.hellocrop.okrbot.entity.JsonString;
import com.hellocrop.okrbot.entity.block.type.TextBlock;
import org.junit.jupiter.api.Test;

public class BlockTest {
    @Test
    public void test1() throws JsonProcessingException {
        TextBlock textBlock = TextBlock.simpleTextBlock("jsafhsfkahkfjkasjf");
        String url = JsonString.objectMapper.writeValueAsString(textBlock);
        System.out.println(url);

        TextBlock mu = TextBlock.mentionUserBlock("ou_1a02d3a9cc5dd33c0378a84a694749bb");
        System.out.println(JsonString.objectMapper.writeValueAsString(mu));
    }
}
