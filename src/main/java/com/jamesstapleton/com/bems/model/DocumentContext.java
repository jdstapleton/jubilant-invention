package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.Map;

@JsonDeserialize(builder = ImmutableDocumentContext.Builder.class)
@Value.Immutable
@Model
public interface DocumentContext {
    static DocumentContext of(Map<String, Object> ctx) {
        return ImmutableDocumentContext.of(ctx);
    }

    @Value.Parameter
    Map<String, Object> getCtx();

    static ImmutableDocumentContext.Builder builder() {
        return ImmutableDocumentContext.builder();
    }
}
