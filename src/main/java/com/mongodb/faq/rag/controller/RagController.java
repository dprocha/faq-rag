package com.mongodb.faq.rag.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle RAG (Retrieval-Augmented Generation) related endpoints.
 * It provides an endpoint to answer frequently asked questions using a chat client
 * integrated with a vector store for document retrieval.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@RestController
public class RagController {

    private final ChatClient chatClient;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .build();
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(value = "message", defaultValue = "How to analyze time-series data with Python and MongoDB? Explain all the steps.") String message) {
        return chatClient.prompt()
                .user(message)          // User message (the query)
                .call()                 // Call the model
                .content();
    }
}
