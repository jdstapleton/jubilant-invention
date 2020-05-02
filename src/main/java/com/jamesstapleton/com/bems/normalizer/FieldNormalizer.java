package com.jamesstapleton.com.bems.normalizer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.List;

/**
 * Sample JSONs:
 * {
 * "fieldName": "field-1"
 * }
 * <p>
 * {
 * "fieldName": "field-2",
 * "normalizers": [
 * {
 * "type": "StringNormalizer",
 * "rules": [
 * { "type": "BasicRule",
 * "normalizedValue": "breakfast_foods",
 * "matches": [ {"type": "BasicRule", "field": "field","value": "breakfast","op": "STARTS_WITH" } ]
 * },
 * { "type": "IdentityRule" }
 * ]
 * }
 * ]
 * }
 */
@Value.Immutable
@Model
public interface FieldNormalizer {
    String getFieldName();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Value.Default
    default List<ValueNormalizer> getNormalizers() {
        return List.of();
    }
}
