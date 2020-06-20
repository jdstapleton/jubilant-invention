package com.jamesstapleton.com.bems.controllers;

import com.jamesstapleton.com.bems.model.DocumentSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RestController
@RequestMapping("documentContextSchema")
public class SchemaController {
    private final DocumentSchema documentSchema;

    @Autowired
    public SchemaController(DocumentSchema documentSchema) {
        this.documentSchema = documentSchema;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public DocumentSchema listStoredQueries() {
        return documentSchema;
    }
}
