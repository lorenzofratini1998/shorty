package it.shorty.services;

import it.shorty.controller.dto.ShortenRequest;
import it.shorty.controller.dto.ShortenResponse;
import it.shorty.exception.CodeCollisionException;
import it.shorty.exception.UrlNotFoundException;
import it.shorty.repository.UrlRepository;
import it.shorty.repository.model.UrlEntity;
import it.shorty.utils.HashUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    private static final int MAX_RETRIES = 3;
    private static final int DEFAULT_TTL_DAYS = 30;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public ShortenResponse createShortUrl(ShortenRequest request, String baseUrl) {
        int days = (request.ttlInDays() != null) ? request.ttlInDays() : DEFAULT_TTL_DAYS;
        Instant expiration = Instant.now().plus(days, ChronoUnit.DAYS);

        if (request.customAlias() != null && !request.customAlias().isBlank()) {
            return saveCustomAlias(request.originalUrl(), request.customAlias(), expiration, baseUrl);
        }

        return saveRandomCode(request.originalUrl(), expiration, baseUrl);
    }

    private ShortenResponse saveCustomAlias(String originalUrl, String alias, Instant expiration, String baseUrl) {
        UrlEntity entity = UrlEntity.create(alias, originalUrl, expiration);
        urlRepository.saveUnique(entity);
        return ShortenResponse.from(entity, baseUrl);
    }

    private ShortenResponse saveRandomCode(String originalUrl, Instant expiration, String baseUrl) {
        int attempts = 0;

        while (attempts < MAX_RETRIES) {
            String code = HashUtils.generateRandomCode();
            UrlEntity entity = UrlEntity.create(code, originalUrl, expiration);

            try {
                urlRepository.saveUnique(entity);
                return ShortenResponse.from(entity, baseUrl);
            } catch (CodeCollisionException e) {
                attempts++;
            }
        }

        throw new RuntimeException("Impossible to generate a unique code after " + MAX_RETRIES + " attempts.");
    }

    @Override
    public String getOriginalUrl(String code) {
        UrlEntity entity = urlRepository.findByCode(code)
                .orElseThrow(() -> new UrlNotFoundException("Code not found: " + code));

        if (entity.getExpirationAt() < Instant.now().getEpochSecond()) {
            throw new UrlNotFoundException("Link expired");
        }

        return entity.getOriginalUrl();
    }
}