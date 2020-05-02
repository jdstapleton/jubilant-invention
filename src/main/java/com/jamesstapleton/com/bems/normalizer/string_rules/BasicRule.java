package com.jamesstapleton.com.bems.normalizer.string_rules;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.boolexp.StringTerm;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * For the matches, the 'FieldName' of StringTerms are ignored.
 * TODO improve the requirement of specifying the field name while still reusing StringTerm behavior
 */
@Value.Immutable
@Model
public abstract class BasicRule implements StringNormalizerRule {
    public abstract Set<String> getNormalizedValue();

    public abstract Set<StringTerm> getMatches();

    @Value.Check
    protected BasicRule check() {
        if (getNormalizedValue().isEmpty()) {
            throw new RuntimeException("Normalized value must be non empty");
        }

        if (getMatches().isEmpty()) {
            throw new RuntimeException("Normalization rules must be non empty");
        }

        if (getMatches().stream().allMatch(t -> t.getField().equals("field"))) {
            return this;
        }

        return ImmutableBasicRule.builder()
                .normalizedValue(getNormalizedValue())
                .matches(getMatches().stream()
                        .map(r -> r.withField("field"))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public Set<String> normalize(Object fieldValue) {
        var docMatch = DocumentContext.of(Map.of("field", StringNormalizerRule.getNormalizedValue(fieldValue)));
        if (getMatches().stream().anyMatch(t -> t.matches(docMatch))) {
            return getNormalizedValue();
        }

        // no values
        return null;
    }
}
