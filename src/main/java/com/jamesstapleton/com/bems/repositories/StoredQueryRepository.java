package com.jamesstapleton.com.bems.repositories;

import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StoredQueryRepository {
    Map<String, StoredQuery> queries = new HashMap<>();

    public StoredQuery save(StoredQuery query) {
        queries.put(query.getId(), query);

        return query;
    }

    public Optional<StoredQuery> findById(String id) {
        return Optional.ofNullable(queries.get(id));
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext) {
        return queries.values().stream()
                .filter(i -> i.getRule().matches(documentContext))
                .collect(Collectors.toList());
    }
}
