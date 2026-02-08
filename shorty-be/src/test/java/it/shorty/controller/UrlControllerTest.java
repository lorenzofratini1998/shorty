package it.shorty.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.shorty.controller.dto.ShortenRequest;
import it.shorty.controller.dto.ShortenResponse;
import it.shorty.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UrlController.class)
public class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shorten_shouldReturn201() throws Exception {
        ShortenRequest req = new ShortenRequest("https://google.com", null, 5);
        ShortenResponse res = new ShortenResponse("https://short.y/123", "https://google.com", "123", Instant.now());

        when(urlService.createShortUrl(any(), anyString())).thenReturn(res);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("X-Forwarded-Proto", "https")
                        .header("Host", "my-lambda.aws"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("https://short.y/123"));
    }

    @Test
    void shorten_shouldReturn400_whenUrlIsInvalid() throws Exception {
        ShortenRequest req = new ShortenRequest("", null, 5);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
