package com.mongodb.faq.rag.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents metadata information associated with a document.
 *
 * @author Diego Rocha
 * @since JDK 24
 */
@Data
public class Metadata {

    private String format;
    private String pageDescription;
    private String action;
    private String sourceName;
    private String contentType;
    private LocalDateTime updated;
    private String url;
    private List<String> tags;

}