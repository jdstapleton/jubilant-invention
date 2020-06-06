package com.jamesstapleton.com.bems.controllers;

import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.model.DocumentSchema;
import com.jamesstapleton.com.bems.model.StoredQuery;
import com.jamesstapleton.com.bems.service.StoredQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("match")
public class MatchController {
    private final DocumentSchema documentSchema;
    private final StoredQueryService storedQueryService;

    @Autowired
    public MatchController(DocumentSchema documentSchema, StoredQueryService storedQueryService) {
        this.documentSchema = documentSchema;
        this.storedQueryService = storedQueryService;
    }

    @PostMapping("/")
    public Page<StoredQuery> findMatches(@RequestBody DocumentContext context) {
        var normalized = documentSchema.normalizeToSchema(context);

        return new PageImpl<>(storedQueryService.findMatches(normalized));
    }
}
