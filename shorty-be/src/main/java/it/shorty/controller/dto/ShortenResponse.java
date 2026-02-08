package it.shorty.controller.dto;

import it.shorty.repository.model.UrlEntity;

import java.time.Instant;

public record ShortenResponse(
        String shortUrl,
        String originalUrl,
        String alias,
        Instant expiresAt
) {

    public static ShortenResponse from(UrlEntity entity, String baseUrl) {
        return new ShortenResponse(
                baseUrl + "/r/" + entity.getCode(),
                entity.getOriginalUrl(),
                entity.getAlias(),
                Instant.ofEpochSecond(entity.getExpirationAt())
        );
    }
}
