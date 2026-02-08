package it.shorty;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2HttpContext;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequestContext;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.crac.Core;
import org.crac.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler, Resource {
    private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> handler;
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamLambdaHandler.class);

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(ShortyBeApplication.class);
            // If you are using HTTP APIs with the version 2.0 of the proxy model, use the getHttpApiV2ProxyHandler
            // method: handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(Application.class);
            handler.onStartup(c -> {});
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            LOGGER.error("Could not initialize Spring Boot application", e);
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    public StreamLambdaHandler() {
        Core.getGlobalContext().register(this);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }

    @Override
    public void beforeCheckpoint(org.crac.Context<? extends Resource> context) throws Exception {
        LOGGER.info("Executing Priming (beforeCheckpoint)...");

        HttpApiV2ProxyRequest request = new HttpApiV2ProxyRequest();
        request.setRawPath("/api/v1/url/warmup");
        HttpApiV2ProxyRequestContext requestContext = new HttpApiV2ProxyRequestContext();

        HttpApiV2HttpContext httpContext = new HttpApiV2HttpContext();
        httpContext.setMethod("GET");
        httpContext.setPath("/api/v1/url/warmup");
        httpContext.setProtocol("HTTP/1.1");
        httpContext.setUserAgent("AWS-Lambda-Priming");

        requestContext.setHttp(httpContext);
        request.setRequestContext(requestContext);

        handler.proxy(request, null);

        handler.proxy(request, null);

        LOGGER.info("Priming completed.");

    }

    @Override
    public void afterRestore(org.crac.Context<? extends Resource> context) throws Exception {
        LOGGER.info("Restored from checkpoint.");
    }
}