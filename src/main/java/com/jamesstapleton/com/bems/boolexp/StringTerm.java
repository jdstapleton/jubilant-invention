package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.Set;

@Value.Immutable
@Model
public abstract class StringTerm implements Term {
    public enum Operator {
        EQ
    }

    public static StringTerm eq(String field, String... value) {
        return ImmutableStringTerm.builder()
                .op(Operator.EQ)
                .field(field)
                .addValue(value)
                .build();
    }

    public abstract String getField();

    @JsonDeserialize(using = SpecializedSetJsonMarshaller.Deserializer.class)
    @JsonSerialize(using = SpecializedSetJsonMarshaller.Serializer.class)
    public abstract Set<String> getValue();

    public abstract Operator getOp();

    @Override
    public boolean matches(DocumentContext context) {
        var ctxValue = getValue(context);
        if (ctxValue == null) {
            return false;
        }

        return ctxValue.stream().anyMatch(getValue()::contains);
    }

    private Collection<String> getValue(DocumentContext context) {
        Object ctxValue = context.getCtx().get(getField());
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
        return String.format("%s %s %s", getField(), getOp(), valueAsString());
    }

    private String valueAsString() {
        final var value = getValue();
        if (value.isEmpty()) {
            return "\"\"";
        } else if (value.size() == 1) {
            return "\"" + value.stream().findFirst().get() + "\"";
        } else {
            return "(\"" + String.join("\",\"", value) + "\")";
        }
    }
}
