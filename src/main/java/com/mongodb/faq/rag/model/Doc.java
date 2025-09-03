package com.mongodb.faq.rag.model;

import lombok.Data;
import org.springframework.data.domain.Vector;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Represents a document with its content, metadata, and embedding vector.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@Data
@Document(collection = "docs")
public class Doc {

    @MongoId(FieldType.STRING)
    private String id;
    private String title;
    private String content;
    private Metadata metadata;
    private Vector embedding;

}