package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.boolexp.Rule;
import com.jamesstapleton.com.bems.utils.ModelValidator;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;

import static com.jamesstapleton.com.bems.utils.AssertThat.assertThat;

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

    @NonNull
    Rule getRule();

    @NonNull
    Metadata getMetadata();

    StoredQuery withId(String id);

    @Value.Check
    default void check() {
        try (ModelValidator validator = new ModelValidator("StoredQuery")) {
            validator.validate("name", assertThat(getName()).maxLength(32));
        }
    }
}
