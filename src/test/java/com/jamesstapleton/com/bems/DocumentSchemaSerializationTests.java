package com.jamesstapleton.com.bems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamesstapleton.com.bems.model.DocumentSchema;
import org.junit.jupiter.api.Test;

import static com.jamesstapleton.com.bems.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentSchemaSerializationTests {
    private static final String DOC_CTX = readYamlResource("/sample-document-context-schema.yaml");

    @Test
    public void shouldMarshallToFromYaml() throws JsonProcessingException {
        final var actual = YAML_MAPPER.readValue(DOC_CTX, DocumentSchema.class);
        assertDoesNotThrow(() -> throwingSupplier(() -> YAML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual)));
        // just a simple spot check as if it didn't throw its probably correct.
        assertEquals(4, actual.getIndexedFieldDefinitions().size(), () -> pp(actual.getIndexedFieldDefinitions()));
    }
}
