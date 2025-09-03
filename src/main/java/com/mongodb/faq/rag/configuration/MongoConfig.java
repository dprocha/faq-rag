package com.mongodb.faq.rag.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * MongoDB configuration class to customize the MongoTemplate bean.
 * This configuration removes the _class field from being stored in MongoDB documents.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@Configuration
public class MongoConfig {

    @Bean
    @Primary
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
    }

}