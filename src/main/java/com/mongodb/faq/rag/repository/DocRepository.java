package com.mongodb.faq.rag.repository;

import com.mongodb.faq.rag.model.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Doc entities in MongoDB.
 * Extends MongoRepository to provide CRUD operations and custom queries.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@Repository
public interface DocRepository extends MongoRepository<Doc, String> {

    List<Doc> findDocumentationByEmbeddingIsNull();

}
