package com.jamesstapleton.com.bems.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.service.StoredQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rule")
public class RuleController {
    private final StoredQueryService sqs;

    @Autowired
    public RuleController(StoredQueryService sqs) {
        this.sqs = sqs;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<RuleSummary> listStoredQueries(Pageable pageable) {
        return sqs.findAll(pageable).map(i -> new RuleSummary(i.getId(), i.getName()));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StoredQuery getStoredQuery(@PathVariable("id") String id) {
        return sqs.findById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StoredQuery createNewQuery(@RequestBody StoredQuery storedQuery) {
        return sqs.save(storedQuery);
    }

    static class RuleSummary {
        private final String id;
        private final String name;

        RuleSummary(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @JsonProperty
        public String getId() {
            return id;
        }

        @JsonProperty
        public String getName() {
            return name;
        }
    }
}
