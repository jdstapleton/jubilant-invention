package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Set;

@JsonDeserialize(builder = ImmutableDocumentContext.Builder.class)
@Value.Immutable
@Model
public interface DocumentContext {
    static DocumentContext of(Map<String, Object> ctx) {
        return ImmutableDocumentContext.of(ctx);
    }

    @Value.Parameter
    Map<String, Object> getCtx();

    default <T> T getAs(String fieldName, Class<T> valueType) {
        return valueType.cast(getCtx().get(fieldName));
    }

    @SuppressWarnings("unchecked")
    default <T> Set<T> getSetAs(String fieldName, Class<T> valueType) {
        var value = getCtx().get(fieldName);

        if (value instanceof Set &&
                ((Set<Object>) value).stream().findFirst().map(valueType::isInstance).orElse(true)) {
            return (Set<T>) value;
        }

        return null;
    }


    static ImmutableDocumentContext.Builder builder() {
        return ImmutableDocumentContext.builder();
    }
}
