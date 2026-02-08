package it.shorty.controller;

import it.shorty.controller.dto.ShortenRequest;
import it.shorty.controller.dto.ShortenResponse;
import it.shorty.services.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortenResponse shorten(@RequestBody @Valid ShortenRequest request,
                                   @RequestHeader(value = "X-Forwarded-Proto", defaultValue = "https") String protocol,
                                   @RequestHeader(value = "Host") String host) {
        String baseUrl = host != null ? protocol + "://" + host : "http://localhost:8080";
        return urlService.createShortUrl(request, baseUrl);
    }
}
