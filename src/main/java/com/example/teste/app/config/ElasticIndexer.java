package com.example.teste.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElasticIndexer {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ObjectMapper objectMapper;

    public ElasticIndexer(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.objectMapper = new ObjectMapper();
    }

    public <T> void index(T entity, String indexName) {
        try {
            // Convert entity to a map with a consistent structure
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("timestamp", System.currentTimeMillis());
            logMap.put("data_type", entity.getClass().getSimpleName());

            // Convert the entity to a JSON string and then to a Map
            String entityJson = objectMapper.writeValueAsString(entity);
            Map<String, Object> entityMap = objectMapper.readValue(entityJson, Map.class);

            // Add the entity data to the log map
            logMap.put("data", entityMap);

            // Create the index query with the log map
            String json = objectMapper.writeValueAsString(logMap);
            IndexQuery query = new IndexQueryBuilder()
                    .withSource(json)
                    .build();

            // Index the log map in Elasticsearch
            elasticsearchOperations.index(query, IndexCoordinates.of(indexName));
        } catch (Exception e) {
            // Log the error but don't throw it to avoid breaking the application
            System.err.println("Error indexing entity: " + e.getMessage());
        }
    }
}
