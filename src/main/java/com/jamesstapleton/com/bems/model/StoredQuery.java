package com.jamesstapleton.com.bems.model;

import com.jamesstapleton.com.bems.boolexp.Rule;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.UUID;

public class StoredQuery {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private final String id;
    private final Rule rule;
    private final Metadata metadata;

    public StoredQuery(Rule rule, Metadata metadata) {
        this(UUID.randomUUID().toString(), rule, metadata);
    }

    public StoredQuery(String id, Rule rule, Metadata metadata) {
        this.id = id;
        this.rule = rule;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Rule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "StoredQuery{" +
                "id='" + id + '\'' +
                ", rule=" + rule +
                ", metadata=" + metadata +
                '}';
    }
}
