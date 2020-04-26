package com.jamesstapleton.com.bems.boolexp;

import com.jamesstapleton.com.bems.model.DocumentContext;

import java.util.Set;

public interface Term {
    boolean matches(DocumentContext context);

    static Term of(String field, String... values) {
        return new StringTerm(field, Set.of(values), StringTerm.Operator.EQ);
    }
}
