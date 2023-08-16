import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.hellocrop.okrbot.entity.JsonString;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JacksonTest {
    @Test
    public void test1() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("int", 29);
        map.put("text", "fahifjhkasjfkjskfjskaj");
        String s = JsonString.objectMapper.writeValueAsString(map);
        JsonNode jsonNode = JsonString.objectMapper.readTree(s);
        System.out.println(jsonNode.toString());
    }
}
