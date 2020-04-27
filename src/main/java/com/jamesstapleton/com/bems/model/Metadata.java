package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class Metadata {
    @JsonProperty
    public final Set<String> visibilities;

    @JsonCreator
    public Metadata(Set<String> visibilities) {
        this.visibilities = visibilities;
    }

    public Set<String> getVisibilities() {
        return visibilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(visibilities, metadata.visibilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visibilities);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "visibilities=" + visibilities +
                '}';
    }
}
