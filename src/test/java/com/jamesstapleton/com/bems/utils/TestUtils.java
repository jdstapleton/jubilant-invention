package com.jamesstapleton.com.bems.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.jamesstapleton.com.bems.StoredQuerySerializationTests;

import java.io.IOException;
import java.util.function.Supplier;

public class TestUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    public static final ObjectMapper YAML_MAPPER = new ObjectMapper(
            new YAMLFactory()
                    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
                    .enable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID))
            .findAndRegisterModules();

    public static String readResource(String path) {
        try (var s = StoredQuerySerializationTests.class.getResourceAsStream(path)) {
            // parse and reformat so the test passes even if formatting is different
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readYamlResource(String path) {
        try (var s = StoredQuerySerializationTests.class.getResourceAsStream(path)) {
            // parse and reformat so the test passes even if formatting is different
            return YAML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(YAML_MAPPER.readTree(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pp(Object obj) {
        try {
            return YAML_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public static <T> Supplier<T> throwingSupplier(ThrowableSupplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
