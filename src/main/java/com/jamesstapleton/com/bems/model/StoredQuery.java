package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesstapleton.com.bems.boolexp.Rule;

import java.util.Objects;
import java.util.UUID;


public class StoredQuery {
    @JsonProperty
    private final String id;
    @JsonProperty
    private final Rule rule;
    @JsonProperty
    private final Metadata metadata;

    public StoredQuery(Rule rule, Metadata metadata) {
        this(UUID.randomUUID().toString(), rule, metadata);
    }

    @JsonCreator
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredQuery that = (StoredQuery) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(rule, that.rule) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rule, metadata);
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
