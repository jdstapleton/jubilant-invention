package com.jamesstapleton.com.bems.normalizer;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.stream.Collectors;

@Value.Immutable
@Model
public interface LcStringNormalizer extends ValueNormalizer {
    @SuppressWarnings("unchecked")
    @Override
    default Object normalize(String field, Object fieldValue, DocumentContext fullDocument) {
        if (fieldValue instanceof Collection) {
            return ((Collection<String>) fieldValue).stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        } else if (fieldValue instanceof String) {
            return ((String) fieldValue).toLowerCase();
        } else {
            return fieldValue;
        }
    }
}
