package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.Set;

@JsonDeserialize(builder = ImmutableMetadata.Builder.class)
@Value.Immutable
@Model
public interface Metadata {
    static Metadata of(String... visibilities) {
        return ImmutableMetadata.builder().addVisibilities(visibilities).build();
    }

    @JsonProperty
    Set<String> getVisibilities();
}
