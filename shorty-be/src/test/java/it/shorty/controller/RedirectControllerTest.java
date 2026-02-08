package it.shorty.controller;

import it.shorty.exception.UrlNotFoundException;
import it.shorty.services.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedirectController.class)
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    void redirect_shouldReturn302AndLocationHeader_whenCodeExists() throws Exception {
        String code = "AbCd123";
        String originalUrl = "https://www.google.com";

        when(urlService.getOriginalUrl(code)).thenReturn(originalUrl);

        mockMvc.perform(get("/r/" + code))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, originalUrl));
    }

    @Test
    void redirect_shouldReturn404_whenCodeDoesNotExist() throws Exception {
        String code = "not-found";

        when(urlService.getOriginalUrl(code)).thenThrow(new UrlNotFoundException("Not found"));

        mockMvc.perform(get("/r/" + code))
                .andExpect(status().isNotFound());
    }
}
