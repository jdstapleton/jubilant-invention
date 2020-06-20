package com.jamesstapleton.com.bems.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.jamesstapleton.com.bems.model.DocumentSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BemsConfig {
    private final BemsProperties bemsProperties;
    private final ObjectMapper yamlMapper = new ObjectMapper(
            new YAMLFactory()
                    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                    .enable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID))
            .findAndRegisterModules();

    public BemsConfig(BemsProperties bemsProperties) {
        this.bemsProperties = bemsProperties;
    }

    @Bean
    public DocumentSchema getDocumentSchema() {
        try {
            return yamlMapper.readValue(
                    bemsProperties.getDocumentContextSchemaFile().toAbsolutePath().toFile(),
                    DocumentSchema.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
