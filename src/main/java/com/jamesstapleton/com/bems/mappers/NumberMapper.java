package com.jamesstapleton.com.bems.mappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.exceptions.DocumentContextParseException;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;

import java.util.stream.Stream;

@Value.Immutable
@Model
public interface NumberMapper extends Mapper {
    static ImmutableNumberMapper.Builder builder() {
        return ImmutableNumberMapper.builder();
    }

    @NonNull
    @Value.Default
    default NumberType getNumberType() {
        return NumberType.LONG;
    }

    @JsonIgnore
    @NonNull
    @Value.Derived
    default NumberStringParser getNumberParser() {
        switch (getNumberType()) {
            case LONG:
                return Long::parseLong;
            case DOUBLE:
                return Double::parseDouble;
        }

        throw new IllegalStateException("Unable to determine number parser");
    }

    @JsonIgnore
    @NonNull
    @Value.Derived
    default NumberValueConverter getNumberValueConverter() {
        switch (getNumberType()) {
            case LONG:
                return Number::longValue;
            case DOUBLE:
                return Number::doubleValue;
        }

        throw new IllegalStateException("Unable to determine number parser");
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            return Stream.of(getNumberParser().parse((String) input));
        }

        if (input instanceof Number) {
            return Stream.of(getNumberValueConverter().convert((Number) input));
        }

        throw new DocumentContextParseException("Unable to parse value to number: Unknown value type");
    }

    enum NumberType {
        LONG,
        DOUBLE
    }

    interface NumberValueConverter {
        Number convert(Number num);
    }

    interface NumberStringParser {
        Number doParse(String number) throws NumberFormatException;

        default Number parse(String number) {
            try {
                return doParse(number);
            } catch (NumberFormatException e) {
                throw new DocumentContextParseException("Unable to parse value to number.", e);
            }
        }
    }
}
