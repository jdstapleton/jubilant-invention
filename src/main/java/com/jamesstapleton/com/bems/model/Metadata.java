package com.jamesstapleton.com.bems.model;

import java.util.Set;

public class Metadata {
    public final Set<String> visibilities;

    public Metadata(Set<String> visibilities) {
        this.visibilities = visibilities;
    }

    public Set<String> getVisibilities() {
        return visibilities;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "visibilities=" + visibilities +
                '}';
    }
}
