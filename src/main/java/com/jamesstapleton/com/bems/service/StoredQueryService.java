package com.jamesstapleton.com.bems.service;


import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.model.UserContext;
import com.jamesstapleton.com.bems.repositories.StoredQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class StoredQueryService {
    private final StoredQueryRepository storedQueryRepository;

    @Autowired
    public StoredQueryService(StoredQueryRepository storedQueryRepository) {
        this.storedQueryRepository = storedQueryRepository;
    }

    public Page<StoredQuery> findAll(Pageable pageable) {
        return storedQueryRepository.findAllForUser(pageable, getUserContext());
    }

    public StoredQuery save(StoredQuery query) {
        if (!query.getId().isEmpty()) {
            assertDoesNotExist(query.getId());
        }

        return storedQueryRepository.save(query);
    }

    public StoredQuery findById(String id) {
        return storedQueryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Stored Query by id " + id + " was not found."));
    }

    public List<StoredQuery> findMatches(DocumentContext documentContext) {
        return storedQueryRepository.findMatches(documentContext, getUserContext());
    }

    private void assertDoesNotExist(String id) {
        storedQueryRepository.findById(id).ifPresent((x) -> {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Store query by id " + id + " already exists.");
        });
    }

    private UserContext getUserContext() {
        return UserContext.of(Set.of("A", "B", "C"));
    }
}
