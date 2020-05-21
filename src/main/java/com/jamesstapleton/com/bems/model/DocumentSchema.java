package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value.Immutable
@Model
public abstract class DocumentSchema {
    public static final Object INVALID_VALUE = new Object();

    public abstract List<FieldMapper> getContext();

    @JsonIgnore
    @Value.Derived
    public MultiValueMap<String, FieldMapper> getIndexedFieldDefinitions() {
        // Reduce the List<FieldNormalizers> into a multimap of FieldName -> [FieldNormalizer...]
        return getContext().stream()
                .collect(() -> CollectionUtils.toMultiValueMap(new HashMap<>()),
                        (x, y) -> x.add(y.getField(), y),
                        MultiValueMap::addAll);
    }

    @Value.Check
    public DocumentSchema check() {
        // fail fast if these are invalid rather than on first web request
        for (Map.Entry<String, List<FieldMapper>> e : getIndexedFieldDefinitions().entrySet()) {
            getFieldWrapperType(e.getValue(), e.getKey());
        }

        return this;
    }

    /**
     * This function prepares the DocumentContext to be used by the boolean expression service
     *
     * @param documentContext the documentContext to convert its fields to concrete types
     * @return a converted DocumentContext
     */
    public DocumentContext normalizeToSchema(DocumentContext documentContext) {
        return DocumentContext.of(documentContext.getCtx().entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        this::normalizeValue)));
    }

    private FieldWrapperType getFieldWrapperType(List<FieldMapper> fieldDefs, String fieldName) {
        return fieldDefs.stream()
                .map(FieldMapper::getType)
                .sorted()
                .distinct()
                .reduce((x, y) -> {
                    throw new RuntimeException("Multiple output types found for " + fieldName);
                })
                .orElseThrow();
    }

    @NonNull
    private Object normalizeValue(Map.Entry<String, Object> entry) {
        var fieldDefs = getIndexedFieldDefinitions().get(entry.getKey());
        var fieldMappers = fieldDefs.stream()
                .flatMap(fieldMapper -> fieldMapper.getMappers().stream())
                .collect(Collectors.toList());

        @SuppressWarnings("unchecked")
        var valueAsStream = entry.getValue() instanceof Collection ?
                ((Collection<Object>) entry.getValue()).stream() :
                Stream.of(entry.getValue());

        // Basically this is a:
        // normalizedEntry = Pipeline.of(func1, func2, func3, func4).apply(Stream.of(entry));
        var values = fieldMappers.stream()
                .sequential()
                .reduce(valueAsStream,
                        (acc, mapper) -> acc.flatMap(mapper::map),
                        // since we are sequential our combiner is simply return the newest accumulator
                        (x, y) -> y);

        var fieldOutputType = getFieldWrapperType(fieldDefs, entry.getKey());

        switch (fieldOutputType) {
            case List:
                return values.collect(Collectors.toList());
            case Set:
                return values.collect(Collectors.toSet());
            case SingleValue:
                // return INVALID_VALUE when the value.size() != 1 otherwise the single value.
                // this way we are non-null
                return values.reduce((x, y) -> INVALID_VALUE).orElse(INVALID_VALUE);
        }

        return INVALID_VALUE;
    }
}
