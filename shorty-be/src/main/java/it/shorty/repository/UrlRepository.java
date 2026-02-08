package it.shorty.repository;

import it.shorty.repository.model.UrlEntity;

import java.util.Optional;

public interface UrlRepository {
    void save(UrlEntity urlEntity);
    void saveUnique(UrlEntity urlEntity);
    Optional<UrlEntity> findByCode(String code);
}
