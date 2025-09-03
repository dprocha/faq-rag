package com.mongodb.faq.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the RAG (Retrieval-Augmented Generation) service.
 * This class bootstraps the Spring Boot application.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
