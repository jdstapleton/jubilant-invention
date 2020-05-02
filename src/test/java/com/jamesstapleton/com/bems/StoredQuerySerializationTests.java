package com.jamesstapleton.com.bems;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamesstapleton.com.bems.boolexp.Rule;
import com.jamesstapleton.com.bems.boolexp.Term;
import com.jamesstapleton.com.bems.model.Metadata;
import com.jamesstapleton.com.bems.model.StoredQuery;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoredQuerySerializationTests {
    private static final String SAMPLE_CNF_JSON = readResource("/sample-cnf.json");
    private static final StoredQuery SAMPLE_QUERY = StoredQuery.builder()
            .id("id-xyz")
            .name("xyz")
            .rule(Rule.createCNF(List.of(Term.of("b", "pepper")), List.of(
                    Term.of("b", "eggs", "cheese"),
                    Term.of("a", "hello"),
                    Term.after("date", OffsetDateTime.of(2020, 4, 1, 16, 8, 0, 0, ZoneOffset.UTC))
            )))
            .metadata(Metadata.of("C"))
            .build();

    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Test
    public void shouldSerializeToJson() throws JsonProcessingException {
        final var actual = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(SAMPLE_QUERY);

        assertEquals(SAMPLE_CNF_JSON, actual);
    }

    @Test
    public void shouldDeserializeFromJson() throws JsonProcessingException {
        final var actual = mapper.readValue(SAMPLE_CNF_JSON, StoredQuery.class);
        assertEquals(SAMPLE_QUERY, actual);
    }


    private static String readResource(String path) {
        try (var s = StoredQuerySerializationTests.class.getResourceAsStream(path)) {
            return StreamUtils.copyToString(s, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
