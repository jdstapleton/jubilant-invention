package com.jamesstapleton.com.bems.normalizer;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Value.Immutable
@Model
public interface DateTimeNormalizer extends ValueNormalizer {
    @Override
    default Object normalize(String field, Object fieldValue, DocumentContext fullDocument) {
        if (fieldValue instanceof String) {
            try {
                return OffsetDateTime.parse((CharSequence) fieldValue);
            } catch (DateTimeException e) {
                return null;
            }
        } else if (fieldValue instanceof OffsetDateTime) {
            return fieldValue;
        } else if (fieldValue instanceof Number) {
            var num = (Number) fieldValue;

            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(num.longValue()), ZoneOffset.UTC);
        } else {
            return null;
        }
    }
}
