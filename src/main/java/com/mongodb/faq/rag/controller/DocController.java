package com.mongodb.faq.rag.controller;

import com.mongodb.faq.rag.service.DocsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle document-related endpoints.
 * It provides endpoints to load documents and generate embeddings.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@RestController
@RequestMapping("/api/docs")
public class DocController {

    private final DocsService docsService;

    public DocController(DocsService docsService) {
        this.docsService = docsService;
    }

    @GetMapping("/load")
    public String loadDocuments() {
        return docsService.loadDocs();
    }

    @GetMapping("/embeddings")
    public void generateEmbeddings() {
        docsService.generateEmbeddings();
    }

}
