package com.jamesstapleton.com.bems.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
@ConfigurationProperties("bem")
public class BemsProperties {
    private Path documentContextSchemaFile;

    public Path getDocumentContextSchemaFile() {
        return documentContextSchemaFile;
    }

    public void setDocumentContextSchemaFile(Path documentContextSchemaFile) {
        this.documentContextSchemaFile = documentContextSchemaFile;
    }
}
