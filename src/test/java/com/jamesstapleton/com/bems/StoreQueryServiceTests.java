package com.jamesstapleton.com.bems;

import com.jamesstapleton.com.bems.boolexp.Rule;
import com.jamesstapleton.com.bems.boolexp.Term;
import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.Metadata;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.repositories.StoredQueryRepository;
import com.jamesstapleton.com.bems.service.StoredQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreQueryServiceTests {
    StoredQueryService sqs = new StoredQueryService(new StoredQueryRepository());
    final static StoredQuery Q1 = new StoredQuery(Rule.createCNF(List.of(Term.of("a", "hello"))), new Metadata(Set.of("A")));
    final static StoredQuery Q2 = new StoredQuery(Rule.createCNF(List.of(Term.of("a", "hello")), List.of(
            Term.of("a", "world"),
            Term.of("a", "universe")
    )), new Metadata(Set.of("B")));
    final static StoredQuery Q3 = new StoredQuery(Rule.createCNF(List.of(Term.of("b", "pepper")), List.of(
            Term.of("b", "eggs"),
            Term.of("a", "hello")
    )), new Metadata(Set.of("C")));

    @BeforeEach
    public void beforeEach() {
        sqs.save(Q1);
        sqs.save(Q2);
        sqs.save(Q3);
    }

    @Test
    public void shouldFindMatchesSimple() {
        long startTime = System.currentTimeMillis();
        final var doc = new DocumentContext(Map.of("a", Set.of("hello"), "b", "pepper"));
        final var actual = Set.copyOf(sqs.findMatches(doc));

        assertEquals(Set.of(Q1, Q3), actual);
        System.out.println("Time spent " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
