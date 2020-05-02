package com.jamesstapleton.com.bems.normalizer;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.normalizer.string_rules.StringNormalizerRule;
import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;

/**
 * {
 * "type": "StringNormalizer",
 * "rules": [
 * {
 * "normalizedValue": "breakfast_foods",
 * "matches": [
 * {
 * "type": "BasicRule",
 * "field": "field",
 * "value": [
 * "cheese",
 * "eggs"
 * ],
 * "op": "EQ"
 * },
 * {
 * "type": "BasicRule",
 * "field": "field",
 * "value": "breakfast",
 * "op": "STARTS_WITH"
 * }
 * ]
 * },
 * {
 * "normalizedValue": "chips",
 * "matches": [
 * {
 * "type": "BasicRule",
 * "field": "field",
 * "value": "chips"
 * "op": "ENDS_WITH"
 * }
 * ]
 * },
 * {
 * "type": "IdentityRule"
 * }
 * ]
 * }
 */
@Value.Immutable
@Model
public interface StringNormalizer extends ValueNormalizer {
    List<StringNormalizerRule> getRules();

    /**
     * @param field        the field name being normalized
     * @param fieldValue   the value of the field to normalize
     * @param fullDocument the full document context for reference
     * @return the normalized version of fieldValue.
     */
    @Override
    default Object normalize(String field, Object fieldValue, DocumentContext fullDocument) {
        return getRules().stream()
                .map(r -> r.normalize(fieldValue))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
