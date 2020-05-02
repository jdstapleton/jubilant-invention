package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.normalizer.FieldNormalizer;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value.Immutable
@Model
public abstract class DocumentSchema {
    private static final Object INVALID_VALUE = new Object();

    public abstract List<FieldNormalizer> getFieldDefinitions();

    @JsonIgnore
    @Value.Derived
    public MultiValueMap<String, FieldNormalizer> getIndexedFieldDefinitions() {
        // Reduce the List<FieldNormalizers> into a multimap of FieldName -> [FieldNormalizer]
        return getFieldDefinitions().stream()
                .collect(() -> CollectionUtils.toMultiValueMap(new HashMap<>()),
                        (x, y) -> {
                            x.add(y.getFieldName(), y);
                        },
                        MultiValueMap::addAll);
    }

    public DocumentContext normalizeToSchema(DocumentContext documentContext) {
        return DocumentContext.of(documentContext.getCtx().entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> normalizeValue(documentContext, entry))));
    }

    @NonNull
    private Object normalizeValue(DocumentContext documentContext, Map.Entry<String, Object> entry) {
        var fieldDefs = getIndexedFieldDefinitions().get(entry.getKey());
        if (fieldDefs == null || fieldDefs.isEmpty()) {
            return INVALID_VALUE;
        }

        // for each normalizer call them in order, bringing in the new normalized value to each of the following normalizer
        var normalizedValue = fieldDefs.stream()
                .flatMap(x -> x.getNormalizers().stream())
                .sequential()
                .reduce(
                        entry.getValue(),
                        (cur, normalizer) -> normalizer.normalize(entry.getKey(), cur, documentContext),
                        (a, b) -> b);

        if (normalizedValue == null) {
            return INVALID_VALUE;
        }

        return normalizedValue;
    }
}
