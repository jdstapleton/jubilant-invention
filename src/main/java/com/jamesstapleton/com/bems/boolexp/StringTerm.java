package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.Set;

@Value.Immutable
@Model
public abstract class StringTerm implements Term {
    @Override
    public boolean matches(DocumentContext context) {
        var ctxValue = context.getSetAs(getField(), String.class);

        if (ctxValue == null) {
            return false;
        }

        switch (getOp()) {
            case EQ:
                return valueEq(ctxValue);
            case STARTS_WITH:
                return valueStartsWith(ctxValue);
            case ENDS_WITH:
                return valueEndsWith(ctxValue);
        }

        return false;
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

    public abstract StringTerm withField(String newFieldName);

    private boolean valueEq(Set<String> ctxValue) {
        if (getValue().isEmpty()) {
            return ctxValue.isEmpty()
                    || (ctxValue.size() == 1 && ctxValue.stream().findFirst().orElse("").isEmpty());
        }

        return ctxValue.stream().anyMatch(getValue()::contains);
    }

    private boolean valueEndsWith(Set<String> ctxValue) {
        // naive implementation (should build a tree)
        return ctxValue.stream().anyMatch(x -> getValue().stream().anyMatch(x::endsWith));
    }

    private boolean valueStartsWith(Set<String> ctxValue) {
        // naive implementation (should build a tree)
        return ctxValue.stream().anyMatch(x -> getValue().stream().anyMatch(x::startsWith));
    }

    public enum Operator {
        /**
         * EQ to empty or no value requires the term to exist as well as being empty
         */
        EQ,
        STARTS_WITH,
        ENDS_WITH
    }

    @Value.Check
    protected void check() {
        if (getField().isEmpty()) {
            throw new RuntimeException("StringTerm requires a field to be specified.");
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
