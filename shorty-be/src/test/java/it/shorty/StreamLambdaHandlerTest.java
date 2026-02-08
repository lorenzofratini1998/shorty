package it.shorty;

import com.amazonaws.services.lambda.runtime.Context;
import org.crac.Resource;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class StreamLambdaHandlerTest {

    @Test
    void handler_shouldInitializeAndHandleRequest() {
        StreamLambdaHandler handler = new StreamLambdaHandler();

        InputStream inputStream = new ByteArrayInputStream(
                "{\"rawPath\": \"/api/v1/url/health\", \"requestContext\": {\"http\": {\"method\": \"GET\"}}}".getBytes()
        );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Context context = mock(Context.class);

        assertDoesNotThrow(() -> handler.handleRequest(inputStream, outputStream, context));
    }

    @Test
    void cracMethods_shouldExecuteWithoutError() {
        StreamLambdaHandler handler = new StreamLambdaHandler();
        org.crac.Context<? extends Resource> cracContext = mock(org.crac.Context.class);

        assertDoesNotThrow(() -> handler.beforeCheckpoint(cracContext));

        assertDoesNotThrow(() -> handler.afterRestore(cracContext));
    }
}
