package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.time.DateTimeException;
import java.time.OffsetDateTime;

@Value.Immutable
@Model
public abstract class DateTimeTerm implements Term {
    public enum Operator {
        AFTER,
        BEFORE
    }

    public static DateTimeTerm after(String field, OffsetDateTime value) {
        return ImmutableDateTimeTerm.builder()
                .op(Operator.AFTER)
                .field(field)
                .value(value)
                .build();
    }

    public static DateTimeTerm before(String field, OffsetDateTime value) {
        return ImmutableDateTimeTerm.builder()
                .op(Operator.BEFORE)
                .field(field)
                .value(value)
                .build();
    }

    public abstract String getField();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public abstract OffsetDateTime getValue();

    public abstract Operator getOp();

    @Override
    public final boolean matches(DocumentContext context) {
        var ctxValue = getValue(context);
        if (ctxValue == null) {
            return false;
        }

        switch (getOp()) {
            case AFTER:
                return this.getValue().isAfter(ctxValue);
            case BEFORE:
                return this.getValue().isBefore(ctxValue);
        }

        return false;
    }

    private OffsetDateTime getValue(DocumentContext context) {
        Object ctxValue = context.getCtx().get(getField());
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
    public String toString() {
        return String.format("%s %s %s", getField(), getOp(), getValue());
    }
}
