package it.shorty;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
class SpringBeApplicationTests {

    @MockitoBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Test
    void main_shouldStartApplication() {
        ShortyBeApplication.main(new String[]{});
    }

}
