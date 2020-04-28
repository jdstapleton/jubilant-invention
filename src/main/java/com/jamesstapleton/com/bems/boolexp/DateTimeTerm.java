package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jamesstapleton.com.bems.model.DocumentContext;

import java.time.DateTimeException;
import java.time.OffsetDateTime;
import java.util.Objects;

public class DateTimeTerm implements Term {
    public enum Operator {
        AFTER,
        BEFORE
    }

    @JsonProperty
    private final String field;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty
    private final OffsetDateTime value;

    @JsonProperty
    private final Operator op;

    @JsonCreator
    public DateTimeTerm(
            String field,
            OffsetDateTime value,
            Operator op) {
        this.field = field;
        this.value = value;
        this.op = op;
    }

    @Override
    public boolean matches(DocumentContext context) {
        var ctxValue = getValue(context);
        if (ctxValue == null) {
            return false;
        }

        switch (op) {
            case AFTER:
                return this.value.isAfter(ctxValue);
            case BEFORE:
                return this.value.isBefore(ctxValue);
        }

        return false;
    }

    private OffsetDateTime getValue(DocumentContext context) {
        Object ctxValue = context.getCtx().get(field);
        if (ctxValue instanceof String) {
            try {
                return OffsetDateTime.parse((CharSequence) ctxValue);
            } catch (DateTimeException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeTerm that = (DateTimeTerm) o;
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
        return String.format("%s %s %s", field, op, value);
    }
}
