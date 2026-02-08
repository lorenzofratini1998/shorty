package it.shorty.services;

import it.shorty.controller.dto.ShortenRequest;
import it.shorty.controller.dto.ShortenResponse;

public interface UrlService {
    ShortenResponse createShortUrl(ShortenRequest request, String baseUrl);
    String getOriginalUrl(String code);
}
