package com.jamesstapleton.com.bems.boolexp;

import com.jamesstapleton.com.bems.model.DocumentContext;

import java.util.Collection;
import java.util.Set;

public class StringTerm implements Term {
    public enum Operator {
        EQ
    }
    private final String field;
    private final Set<String> value;
    private final Operator op;

    public StringTerm(String field, Set<String> value, Operator op) {
        this.field = field;
        this.value = value;
        this.op = op;
        if (value.isEmpty()) {
            throw new IllegalStateException("Value may not be empty");
        }
    }

    @Override
    public boolean matches(DocumentContext context) {
        var ctxValue = getValue(context);
        if (ctxValue == null) {
            return false;
        }

        return ctxValue.stream().anyMatch(value::contains);
    }

    private Collection<String> getValue(DocumentContext context) {
        Object ctxValue = context.getCtx().get(field);
        if (ctxValue instanceof Collection) {
            //noinspection unchecked
            return (Collection<String>)ctxValue;
        } else if (ctxValue instanceof String) {
            return Set.of((String) ctxValue);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", field, op,  valueAsString());
    }

    private String valueAsString() {
        if (value.isEmpty()) {
            return "\"\"";
        } else if (value.size() == 1) {
            return "\"" + value.stream().findFirst().get() + "\"";
        } else {
            return "(\"" + String.join("\",\"", value) + "\")";
        }
    }
}
