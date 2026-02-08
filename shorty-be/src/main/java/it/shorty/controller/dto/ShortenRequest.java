package it.shorty.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ShortenRequest(
        @NotBlank @URL String originalUrl,
        String customAlias,
        Integer ttlInDays
) {
}
