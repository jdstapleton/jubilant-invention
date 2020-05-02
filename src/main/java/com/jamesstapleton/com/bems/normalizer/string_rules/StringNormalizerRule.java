package com.jamesstapleton.com.bems.normalizer.string_rules;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.jamesstapleton.com.bems.utils.ImmutablesIdResolver;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Set;

@JsonTypeIdResolver(ImmutablesIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
public interface StringNormalizerRule {
    @SuppressWarnings("unchecked")
    static Set<String> getNormalizedValue(Object fieldValue) {
        if (fieldValue instanceof Set) {
            return (Set<String>) fieldValue;
        } else if (fieldValue instanceof Collection) {
            return Set.copyOf((Collection<String>) fieldValue);
        } else if (fieldValue instanceof String) {
            return Set.of((String) fieldValue);
        } else {
            return null;
        }
    }

    @Nullable
    Set<String> normalize(@Nullable Object fieldValue);
}
