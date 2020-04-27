package com.jamesstapleton.com.bems.service;


import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.model.UserContext;
import com.jamesstapleton.com.bems.repositories.StoredQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoredQueryService {
    private final StoredQueryRepository storedQueryRepository;

    @Autowired
    public StoredQueryService(StoredQueryRepository storedQueryRepository) {
        this.storedQueryRepository = storedQueryRepository;
    }

    public StoredQuery save(StoredQuery query) {
        return storedQueryRepository.save(query);
    }

    public Optional<StoredQuery> findById(String id) {
        return storedQueryRepository.findById(id);
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext) {
        UserContext userContext = getUserContext();
        return storedQueryRepository.findMatches(documentContext).stream()
                .filter(i ->
                        i.getMetadata().getVisibilities().stream()
                                .anyMatch(v -> userContext.getAuthorizations().contains(v)))
                .collect(Collectors.toList());
    }

    private UserContext getUserContext() {
        return new UserContext(Set.of("A", "B", "C"));
    }
}
