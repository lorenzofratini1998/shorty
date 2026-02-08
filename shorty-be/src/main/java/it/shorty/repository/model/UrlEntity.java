package it.shorty.repository.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
public class UrlEntity {

    private String code;
    private String originalUrl;
    private String alias;
    private Long createdAt;
    private Long expirationAt;

    public UrlEntity() {}

    @DynamoDbPartitionKey
    @DynamoDbAttribute("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @DynamoDbAttribute("original_url")
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @DynamoDbAttribute("alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbAttribute("created_at")
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @DynamoDbAttribute("expiration_at")
    public Long getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(Long expirationAt) {
        this.expirationAt = expirationAt;
    }

    public static UrlEntity create(String code, String originalUrl, Instant expiration) {
        UrlEntity entity = new UrlEntity();
        entity.setCode(code);
        entity.setAlias(code);
        entity.setOriginalUrl(originalUrl);
        entity.setCreatedAt(Instant.now().getEpochSecond());
        entity.setExpirationAt(expiration.getEpochSecond());
        return entity;
    }
}
