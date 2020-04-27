package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jamesstapleton.com.bems.model.DocumentContext;

import java.util.Set;

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, property="type")
public interface Term {
    boolean matches(DocumentContext context);

    static Term of(String field, String... values) {
        return new StringTerm(field, Set.of(values), StringTerm.Operator.EQ);
    }
}
