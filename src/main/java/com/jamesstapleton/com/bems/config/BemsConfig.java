package com.jamesstapleton.com.bems.config;

import com.jamesstapleton.com.bems.model.DocumentSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BemsConfig {
    @Bean
    public DocumentSchema getDocumentSchema() {
        // this will be filled in by spring meta-magic
        return DocumentSchema.builder().build();
    }
}
