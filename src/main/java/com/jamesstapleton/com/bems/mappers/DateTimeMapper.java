package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Value.Immutable
@Model
public interface DateTimeMapper extends Mapper {
    @NonNull
    default String getFormatPattern() {
        return "";
    }

    @Value.Derived
    default DateTimeFormatter getFormatter() {
        if (getFormatPattern().equals("")) {
            return DateTimeFormatter.ISO_DATE_TIME;
        } else {
            return DateTimeFormatter.ofPattern(getFormatPattern());
        }
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            return Stream.of(OffsetDateTime.parse((CharSequence) input, getFormatter()));
        } else if (input instanceof Number) {
            return Stream.of(OffsetDateTime.ofInstant(Instant.ofEpochMilli((Long) input), ZoneOffset.UTC));
        }

        return Stream.of(input);
    }
}
