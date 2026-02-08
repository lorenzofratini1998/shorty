package it.shorty.controller;

import it.shorty.exception.CodeCollisionException;
import it.shorty.exception.UrlNotFoundException;
import it.shorty.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UrlController.class, RedirectController.class})
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    void handleCodeCollision_shouldReturn409() throws Exception {
        when(urlService.createShortUrl(any(), anyString()))
                .thenThrow(new CodeCollisionException("Alias in use"));

        mockMvc.perform(post("/api/v1/url/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"originalUrl\":\"https://google.com\", \"customAlias\":\"dup\"}")
                        .header("Host", "localhost"))
                .andExpect(status().isConflict()) // 409
                .andExpect(jsonPath("$.title").value("Alias already in use"))
                .andExpect(jsonPath("$.detail").value("Alias in use"));
    }

    @Test
    void handleUrlNotFound_shouldReturn404() throws Exception {
        when(urlService.getOriginalUrl("unknown")).thenThrow(new UrlNotFoundException("Not found"));

        mockMvc.perform(get("/r/unknown"))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.title").value("Url not found"));
    }
}
