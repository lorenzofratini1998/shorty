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
    void shouldReturn201_whenRequestIsValid() throws Exception {
        ShortenRequest req = new ShortenRequest("https://google.com", "validAlias", 30);
        ShortenResponse res = new ShortenResponse("https://short.y/r/validAlias", "https://google.com", "validAlias", Instant.now());

        when(urlService.createShortUrl(any(ShortenRequest.class), anyString())).thenReturn(res);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Host", "localhost")
                        .header("X-Forwarded-Proto", "https"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("https://short.y/r/validAlias"));
    }

    @Test
    void shouldReturn400_whenOriginalUrlIsInvalid() throws Exception {
        ShortenRequest req = new ShortenRequest("invalid-url", "validAlias", 30);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Host", "localhost"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenAliasIsTooShort() throws Exception {
        ShortenRequest req = new ShortenRequest("https://google.com", "abcd", 30);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Host", "localhost"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenAliasIsTooLong() throws Exception {
        String longAlias = "a".repeat(21);
        ShortenRequest req = new ShortenRequest("https://google.com", longAlias, 30);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Host", "localhost"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenAliasContainsInvalidCharacters() throws Exception {
        ShortenRequest req = new ShortenRequest("https://google.com", "invalid@alias", 30);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .header("Host", "localhost"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400_whenHostHeaderIsMissing() throws Exception {
        ShortenRequest req = new ShortenRequest("https://google.com", "validAlias", 30);

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
