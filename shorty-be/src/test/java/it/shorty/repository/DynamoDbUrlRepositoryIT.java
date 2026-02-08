package it.shorty.repository;

import it.shorty.AbstractIntegrationTest;
import it.shorty.exception.CodeCollisionException;
import it.shorty.repository.model.UrlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DynamoDbUrlRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private DynamoDbUrlRepository repository;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @BeforeEach
    void setupTable() {
        DynamoDbTable<UrlEntity> table = enhancedClient.table(DYNAMODB_TABLE_NAME, TableSchema.fromBean(UrlEntity.class));
        try {
            table.createTable();
        } catch (Exception e) {
            // Ignore if the table already exists
        }
    }

    @Test
    void saveUnique_shouldThrowException_whenCodeExists() {
        UrlEntity entity1 = UrlEntity.create("duplicate", "https://google.com", Instant.now());
        UrlEntity entity2 = UrlEntity.create("duplicate", "https://amazon.com", Instant.now());

        repository.saveUnique(entity1);

        assertThrows(CodeCollisionException.class, () -> repository.saveUnique(entity2));
    }

    @Test
    void findByCode_shouldReturnEmpty_whenNotExists() {
        Optional<UrlEntity> result = repository.findByCode("non-existent");
        assertTrue(result.isEmpty());
    }

    @Test
    void saveAndRetrieve() {
        UrlEntity entity = UrlEntity.create("test-code", "https://example.com", Instant.now());
        repository.save(entity);

        Optional<UrlEntity> retrieved = repository.findByCode("test-code");
        assertTrue(retrieved.isPresent());
        assertEquals("https://example.com", retrieved.get().getOriginalUrl());
    }

    @Test
    void save_shouldOverwriteItem_whenCalled() {
        UrlEntity entity = UrlEntity.create("overwriteMe", "https://v1.com", Instant.now());
        repository.save(entity);

        UrlEntity entityV2 = UrlEntity.create("overwriteMe", "https://v2.com", Instant.now());
        repository.save(entityV2);

        Optional<UrlEntity> result = repository.findByCode("overwriteMe");
        assertTrue(result.isPresent());
        assertEquals("https://v2.com", result.get().getOriginalUrl());
    }

    @Test
    void findByCode_shouldReturnItem_whenExists() {
        UrlEntity entity = UrlEntity.create("findMe", "https://found.com", Instant.now());
        repository.save(entity);

        Optional<UrlEntity> result = repository.findByCode("findMe");

        assertTrue(result.isPresent());
        assertEquals("https://found.com", result.get().getOriginalUrl());
    }
}
