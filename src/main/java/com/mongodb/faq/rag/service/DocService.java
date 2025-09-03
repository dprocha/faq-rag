package com.mongodb.faq.rag.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.faq.rag.model.Doc;
import com.mongodb.faq.rag.model.Metadata;
import com.mongodb.faq.rag.repository.DocRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Vector;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing documents, including loading from JSON,
 * generating embeddings, and storing in the database.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@Slf4j
@Service
@AllArgsConstructor
public class DocService {

    private static final int MAX_TOKENS_PER_CHUNK = 2000;

    private final DocRepository docRepository;
    private final EmbeddingModel embeddingModel;
    private final ObjectMapper objectMapper;

    /**
     * Loads documents from a JSON file, splits them into chunks if necessary,
     *
     * @return
     */
    public String loadDocs() {
        try (InputStream inputStream = new ClassPathResource("docs/devcenter-content-snapshot.2024-05-20.json").getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            List<Doc> documentations = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                Map<String, Object> jsonDoc = objectMapper.readValue(line, Map.class);
                String content = (String) jsonDoc.get("body");

                // Split the content into smaller chunks if it exceeds the token limit
                List<String> chunks = splitIntoChunks(content);

                // Create a Document for each chunk and add it to the list
                for (String chunk : chunks) {
                    Doc documentation = createDocument(jsonDoc, chunk);
                    documentations.add(documentation);
                }

                // Add documents in batches to avoid memory overload
//                if (documentations.size() >= 100) {
//                    vectorStore.add(documentations);
//                    documents.clear();
//                }
            }

            docRepository.saveAll(documentations);
            return "All documents added successfully!";
        } catch (Exception e) {
            return "An error occurred while adding documents: " + e.getMessage();
        }
    }

    /**
     * Creates a Doc object from the provided JSON map and content chunk.
     *
     * @param jsonMap
     * @param content
     * @return
     */
    private Doc createDocument(Map<String, Object> jsonMap, String content) {
        Map<String, Object> map = (Map<String, Object>) jsonMap.get("metadata");

        Metadata metadata = new Metadata();
        metadata.setSourceName((String) jsonMap.get("sourceName"));
        metadata.setUrl((String) jsonMap.get("url"));
        metadata.setAction((String) jsonMap.get("action"));
        metadata.setFormat((String) jsonMap.get("format"));

        OffsetDateTime offsetDateTime = OffsetDateTime.parse((String) jsonMap.get("updated"));
        metadata.setUpdated(offsetDateTime.toLocalDateTime());

        metadata.setTags((List<String>) map.get("tags"));
        metadata.setPageDescription((String) map.get("pageDescription"));
        metadata.setContentType((String) map.get("contentType"));

        Doc doc = new Doc();
        doc.setTitle((String) jsonMap.get("title"));
        doc.setContent(content);
        doc.setMetadata(metadata);
        return doc;
    }

    /**
     * Generates a vector embedding for the given content using the embedding model.
     *
     * @param content
     * @return
     */
    private Vector createVectorEmbedding(String content) {
        float[] embeddings = embeddingModel.embed(content);
        return Vector.of(embeddings);
    }

    /**
     * Splits the content into smaller chunks based on the maximum token limit.
     *
     * @param content
     * @return
     */
    private List<String> splitIntoChunks(String content) {
        List<String> chunks = new ArrayList<>();
        String[] words = content.split("\\s+");
        StringBuilder chunk = new StringBuilder();
        int tokenCount = 0;
        for (String word : words) {
            int wordTokens = word.length() / 4;  // Rough estimate: 1 token = ~4 characters

            if (tokenCount + wordTokens > DocService.MAX_TOKENS_PER_CHUNK) {
                chunks.add(chunk.toString());
                chunk.setLength(0); // Clear the buffer
                tokenCount = 0;
            }

            chunk.append(word).append(" ");
            tokenCount += wordTokens;
        }
        if (!chunk.isEmpty()) {
            chunks.add(chunk.toString());
        }
        return chunks;
    }

    /**
     * Generates embeddings for all documents in the database that do not have an embedding yet.
     */
    public void generateEmbeddings() {
        List<Doc> docs = docRepository.findDocumentationByEmbeddingIsNull();
        for (Doc doc : docs) {
            Vector embedding = createVectorEmbedding(doc.getContent());
            doc.setEmbedding(embedding);
            log.info("Embedding Generated for Document ID {} vector {}", doc.getId(), doc.getEmbedding());
            docRepository.save(doc);
        }
    }
}
