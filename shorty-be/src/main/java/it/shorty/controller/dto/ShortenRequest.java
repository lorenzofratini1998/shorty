package it.shorty.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record ShortenRequest(
        @NotBlank @URL String originalUrl,
        @Size(min = 5, max = 20, message = "Alias must be between 5 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9-_]*$", message = "Alias can only contain letters, numbers, and dashes")
        String customAlias,
        Integer ttlInDays
) {
}
