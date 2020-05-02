package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.jamesstapleton.com.bems.model.DocumentContext;

import java.time.OffsetDateTime;

@JsonTypeIdResolver(TermIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
public interface Term {
    boolean matches(DocumentContext context);

    static Term of(String field, String... values) {
        return StringTerm.eq(field, values);
    }

    static Term after(String field, OffsetDateTime value) {
        return DateTimeTerm.after(field, value);
    }
}
