package com.jamesstapleton.com.bems.normalizer.string_rules;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.normalizer.StringNormalizer;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable(singleton = true)
@Model
public abstract class IdentityRule implements StringNormalizer {
    @Override
    public Set<String> normalize(String field, Object fieldValue, DocumentContext fullDocument) {
        return StringNormalizerRule.getNormalizedValue(fieldValue);
    }
}
