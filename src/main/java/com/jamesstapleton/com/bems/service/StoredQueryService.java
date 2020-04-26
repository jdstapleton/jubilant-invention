package com.jamesstapleton.com.bems.service;


import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.model.UserContext;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoredQueryService {
    Map<String, StoredQuery> queries = new HashMap<>();

    public StoredQuery save(StoredQuery query) {
        queries.put(query.getId(), query);

        return query;
    }

    public Optional<StoredQuery> findById(String id) {
        return Optional.ofNullable(queries.get(id));
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext) {
        UserContext userContext = getUserContext();
        return queries.values().stream()
                .filter(i -> i.getRule().matches(documentContext))
                .filter(i ->
                        i.getMetadata().getVisibilities().stream()
                                .anyMatch(v -> userContext.getAuthorizations().contains(v)))
                .collect(Collectors.toList());
    }

    private UserContext getUserContext() {
        return new UserContext(Set.of("A", "B", "C"));
    }
}
