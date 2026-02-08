package it.shorty.repository;

import it.shorty.exception.CodeCollisionException;
import it.shorty.repository.model.UrlEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.util.Optional;

@Repository
public class DynamoDbUrlRepository implements UrlRepository {

    private final DynamoDbTable<UrlEntity> table;

    public DynamoDbUrlRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                 @Value("${spring.custom.dynamodb.table-name}") String tableName) {
        this.table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(UrlEntity.class));
    }

    @Override
    public void save(UrlEntity urlEntity) {
        table.putItem(urlEntity);
    }

    @Override
    public void saveUnique(UrlEntity urlEntity) {
        try {
            Expression condition = Expression.builder()
                    .expression("attribute_not_exists(code)")
                    .build();

            table.putItem(PutItemEnhancedRequest.<UrlEntity>builder(UrlEntity.class)
                    .item(urlEntity)
                    .conditionExpression(condition)
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new CodeCollisionException("Code already exists: " + urlEntity.getCode());
        }
    }

    @Override
    public Optional<UrlEntity> findByCode(String code) {
        UrlEntity result = table.getItem(Key.builder().partitionValue(code).build());
        return Optional.ofNullable(result);
    }
}
