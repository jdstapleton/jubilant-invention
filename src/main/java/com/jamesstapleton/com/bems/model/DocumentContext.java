package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
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

    @SuppressWarnings("unchecked")
    default <T> T getAs(String fieldName) {
        return (T) getCtx().get(fieldName);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    default <T> Collection<T> getCollectionAs(String fieldName) {
        var val = getCtx().get(fieldName);
        if (val == null) {
            return null;
        }

        return val instanceof Collection ? (Collection<T>) val : Collections.singleton((T) val);
    }


    static ImmutableDocumentContext.Builder builder() {
        return ImmutableDocumentContext.builder();
    }
}
