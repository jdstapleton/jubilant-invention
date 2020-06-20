package com.jamesstapleton.com.bems;

import com.jamesstapleton.com.bems.boolexp.Rule;
import com.jamesstapleton.com.bems.boolexp.Term;
import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.Metadata;
import com.jamesstapleton.com.bems.model.QuerySubject;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.repositories.StoredQueryRepository;
import com.jamesstapleton.com.bems.service.StoredQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreQueryServiceTests {
    final static QuerySubject SUBJECT = QuerySubject.builder()
            .title("Some subject")
            .description("A description")
            .targetUri(URI.create("https://www.google.com"))
            .build();

    final static StoredQuery Q1 = StoredQuery.builder()
            .id("Q1")
            .rule(Rule.createCNF(List.of(Term.of("a", "hello"))))
            .metadata(Metadata.of("A"))
            .subject(SUBJECT.withId("Q1"))
            .build();
    final static StoredQuery Q2 = StoredQuery.builder()
            .id("Q2")
            .rule(
                    Rule.createCNF(List.of(Term.of("a", "hello")), List.of(
                            Term.of("a", "world"),
                            Term.of("a", "universe"))))
            .metadata(Metadata.of("B"))
            .subject(SUBJECT.withId("Q2"))
            .build();
    final static StoredQuery Q3 = StoredQuery.builder()
            .id("Q3")
            .rule(Rule.createCNF(List.of(Term.of("b", "pepper")), List.of(
                    Term.of("b", "eggs"),
                    Term.of("a", "hello"))))
            .metadata(Metadata.of("C"))
            .subject(SUBJECT.withId("Q3"))
            .build();
    final static StoredQuery Q4 = StoredQuery.builder()
            .id("Q4")
            .rule(Rule.createDNF(List.of(Term.of("b", "pepper")), List.of(
                    Term.of("b", "eggs"),
                    Term.of("a", "ham"))))
            .metadata(Metadata.of("C"))
            .subject(SUBJECT.withId("Q4"))
            .build();
    final StoredQueryService sqs = new StoredQueryService(new StoredQueryRepository());

    @BeforeEach
    public void beforeEach() {
        sqs.save(Q1);
        sqs.save(Q2);
        sqs.save(Q3);
        sqs.save(Q4);
    }

    @Test
    public void shouldFindMatchesSimple() {
        long startTime = System.currentTimeMillis();
        final var doc = DocumentContext.of(Map.of("a", Set.of("hello"), "b", "pepper"));
        final var actual = new HashSet<>(sqs.findMatches(doc));

        assertEquals(Set.of(Q1, Q3, Q4), actual);
        System.out.println("Time spent " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Test
    public void dnfShouldMatchOrsOfAnds() {
        long startTime = System.currentTimeMillis();
        final var doc = DocumentContext.of(Map.of("a", Set.of("ham"), "b", "eggs"));
        final var actual = new HashSet<>(sqs.findMatches(doc));

        assertEquals(Set.of(Q4), actual);
        System.out.println("Time spent " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
