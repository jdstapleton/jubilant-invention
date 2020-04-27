package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jamesstapleton.com.bems.model.DocumentContext;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class StringTerm implements Term {
    public enum Operator {
        EQ
    }
    @JsonProperty
    private final String field;

    @JsonSerialize(using = SpecializedSetJsonMarshaller.Serializer.class)
    @JsonProperty
    private final Set<String> value;
    @JsonProperty
    private final Operator op;

    @JsonCreator
    public StringTerm(String field, @JsonDeserialize(using = SpecializedSetJsonMarshaller.Deserializer.class)  Set<String> value, Operator op) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTerm that = (StringTerm) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(value, that.value) &&
                op == that.op;
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value, op);
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
