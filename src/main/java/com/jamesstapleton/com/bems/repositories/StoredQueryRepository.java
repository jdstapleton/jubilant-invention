package com.jamesstapleton.com.bems.repositories;

import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class StoredQueryRepository {
    final Map<String, StoredQuery> queries = new HashMap<>();

    @Autowired
    public StoredQueryRepository() {

    }

    public static Predicate<StoredQuery> filterForUser(UserContext userContext) {
        return i -> i.getMetadata().getVisibilities().stream()
                .anyMatch(v -> userContext.getAuthorizations().contains(v));
    }

    public Page<StoredQuery> findAllForUser(Pageable pageable, UserContext userContext) {
        return new PageImpl<>(queries.values().stream()
                .filter(filterForUser(userContext))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList()),
                pageable,
                queries.values().stream().filter(filterForUser(userContext)).count());
    }

    public Optional<StoredQuery> findById(String id) {
        return Optional.ofNullable(queries.get(id));
    }

    public StoredQuery save(StoredQuery query) {
        var toStore = query.getId().isEmpty() ? query.withId(UUID.randomUUID().toString()) : query;
        queries.put(toStore.getId(), toStore);

        return toStore;
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext, UserContext userContext) {
        return queries.values().stream()
                .filter(filterForUser(userContext))
                .filter(i -> i.getRule().matches(documentContext))
                .collect(Collectors.toList());
    }
}
