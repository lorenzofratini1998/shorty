package it.shorty.services;

import it.shorty.controller.dto.ShortenRequest;
import it.shorty.controller.dto.ShortenResponse;
import it.shorty.exception.CodeCollisionException;
import it.shorty.exception.UrlNotFoundException;
import it.shorty.repository.UrlRepository;
import it.shorty.repository.model.UrlEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlServiceImpl urlService;

    @Test
    void createShortUrl_shouldSaveAndReturnResponse() {
        ShortenRequest request = new ShortenRequest("https://www.google.com", null, 5);
        String baseUrl = "https://shorty.aws";

        doNothing().when(urlRepository).saveUnique(any(UrlEntity.class));

        ShortenResponse response = urlService.createShortUrl(request, baseUrl);

        assertNotNull(response);
        assertTrue(response.shortUrl().startsWith(baseUrl));
        verify(urlRepository, times(1)).saveUnique(any(UrlEntity.class));
    }

    @Test
    void createShortUrl_shouldRetryOnCollision() {
        ShortenRequest request = new ShortenRequest("https://www.google.com", null, 5);
        String baseUrl = "https://shorty.aws";

        doThrow(new CodeCollisionException("Collision"))
                .doNothing()
                .when(urlRepository).saveUnique(any(UrlEntity.class));

        assertDoesNotThrow(() -> urlService.createShortUrl(request, baseUrl));

        verify(urlRepository, times(2)).saveUnique(any(UrlEntity.class));
    }

    @Test
    void getOriginalUrl_shouldThrowException_whenExpired() {
        UrlEntity expiredEntity = UrlEntity.create("xzy", "https://google.com", Instant.now().minusSeconds(3600));

        when(urlRepository.findByCode("xzy")).thenReturn(Optional.of(expiredEntity));

        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl("xzy"));
    }

    @Test
    void createShortUrl_shouldSaveCustomAlias_whenRequested() {
        String customAlias = "alias";
        ShortenRequest request = new ShortenRequest("https://google.com", customAlias, 30);
        String baseUrl = "https://shorty.aws";

        doNothing().when(urlRepository).saveUnique(any(UrlEntity.class));

        ShortenResponse response = urlService.createShortUrl(request, baseUrl);

        assertNotNull(response);
        assertTrue(response.shortUrl().endsWith("/r/" + customAlias));
        verify(urlRepository).saveUnique(argThat(entity ->
                entity.getCode().equals(customAlias) &&
                        entity.getOriginalUrl().equals("https://google.com")
        ));
    }

    @Test
    void createShortUrl_shouldThrowException_whenMaxRetriesReached() {
        ShortenRequest request = new ShortenRequest("https://google.com", null, 30);
        String baseUrl = "https://shorty.aws";

        doThrow(new CodeCollisionException("Collision")).when(urlRepository).saveUnique(any(UrlEntity.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                urlService.createShortUrl(request, baseUrl)
        );

        assertEquals("Impossible to generate a unique code after 3 attempts.", exception.getMessage());
        verify(urlRepository, times(3)).saveUnique(any(UrlEntity.class));
    }
}
