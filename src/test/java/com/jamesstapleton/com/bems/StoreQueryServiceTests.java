package com.jamesstapleton.com.bems;

import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.service.StoredQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StoreQueryServiceTests {
    StoredQueryService sqs = new StoredQueryService();

    @Test
    public void shouldStoreExpression() {
        StoredQuery query = sqs.save("A AND B");
        assertEquals(query.getExpressionString(), "A AND B");
    }

    @Test
    public void shouldFindMatchesSimple() {
        sqs.save("A AND B");
        sqs.save("A OR B");
        sqs.save("!A AND B");
        sqs.save("A AND !B");
        {
            final var doc = new DocumentContext(Map.of("A", true, "B", false));
            final var actual = sqs.findMatches(doc).stream().map(StoredQuery::getExpressionString).collect(Collectors.toSet());
            assertEquals(Set.of("A OR B", "A AND !B"), actual);
        }
        {
            final var doc = new DocumentContext(Map.of("A", true, "B", true));
            final var actual = sqs.findMatches(doc).stream().map(StoredQuery::getExpressionString).collect(Collectors.toSet());
            assertEquals(Set.of("A OR B", "A AND B"), actual);
        }
    }

    @Test
    public void shouldFindMatchesWithSets() {
        sqs.save("A.contains('APPLE') AND B");
        sqs.save("A.contains('APPLE') OR B");
        sqs.save("!A.contains('APPLE') AND B");
        sqs.save("A.contains('APPLE') AND !B");
        {
            final var doc = new DocumentContext(Map.of("A", Set.of("APPLE", "BANANA"), "B", false));
            final var actual = sqs.findMatches(doc).stream().map(StoredQuery::getExpressionString).collect(Collectors.toSet());
            assertEquals(Set.of("A.contains('APPLE') OR B", "A.contains('APPLE') AND !B"), actual);
        }
        {
            final var doc = new DocumentContext(Map.of("A", Set.of("PINEAPPLE", "BANANA"), "B", true));
            final var actual = sqs.findMatches(doc).stream().map(StoredQuery::getExpressionString).collect(Collectors.toSet());
            assertEquals(Set.of("!A.contains('APPLE') AND B", "A.contains('APPLE') OR B"), actual);
        }
    }
}
