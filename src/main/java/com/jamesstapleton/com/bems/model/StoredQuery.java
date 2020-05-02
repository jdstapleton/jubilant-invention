package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.boolexp.Rule;
import org.immutables.value.Value;

@JsonDeserialize(builder = ImmutableStoredQuery.Builder.class)
@Value.Immutable
@Model
public interface StoredQuery {
    static ImmutableStoredQuery.Builder builder() {
        return ImmutableStoredQuery.builder();
    }

    @Value.Default
    default String getId() {
        return "";
    }

    @Value.Default
    default String getName() {
        return "";
    }

    Rule getRule();

    Metadata getMetadata();

    StoredQuery withId(String id);
}
