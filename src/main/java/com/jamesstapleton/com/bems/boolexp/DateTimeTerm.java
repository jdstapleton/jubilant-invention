package com.jamesstapleton.com.bems.boolexp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.time.OffsetDateTime;

/**
 * DateTimeTerm can match on a DocumentContext field in one of the following formats:
 *   OffsetDateTime,
 *   ISO8601 DateTime String,
 *   UnixEpochMillis long
 */
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

    @Value.Check
    protected void check() {
        if (getField().isEmpty()) {
            throw new RuntimeException("DateTimeTerm requires a field to be specified.");
        }
    }

    @Override
    public final boolean matches(DocumentContext context) {
        var ctxValue = context.<OffsetDateTime>getAs(getField());

        if (ctxValue == null) {
            return false;
        }

        switch (getOp()) {
            case AFTER:
                return ctxValue.isAfter(this.getValue());
            case BEFORE:
                return ctxValue.isBefore(this.getValue());
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", getField(), getOp(), getValue());
    }
}
