package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;
import org.springframework.lang.NonNull;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Stream;

@Value.Immutable
@Model
public interface DateTimeMapper extends Mapper {
    OffsetTime ZERO_UTC = OffsetTime.of(LocalTime.of(0, 0), ZoneOffset.UTC);

    static ImmutableDateTimeMapper.Builder builder() {
        return ImmutableDateTimeMapper.builder();
    }

    /**
     * Date pattern as used from {@link DateTimeFormatter#ofPattern(String)}
     *
     * @return the FormatString specified
     */
    @NonNull
    @Value.Default
    default String getFormatPattern() {
        return "";
    }

    @NonNull
    @Value.Default
    default ParsedDateType getParsedDateType() {
        return ParsedDateType.OffsetDateTime;
    }

    @Value.Derived
    default OffsetDatetimeParser getFormatter() {
        if (getFormatPattern().equals("")) {
            return OffsetDateTime::parse;
        } else {
            switch (getParsedDateType()) {
                case DateOnlyStartOfDayUTC:
                    return str -> LocalDate.from(DateTimeFormatter.ofPattern(getFormatPattern()).parse(str)).atTime(ZERO_UTC);
                case OffsetDateTime:
                    return str -> OffsetDateTime.from(DateTimeFormatter.ofPattern(getFormatPattern()).parse(str));
                case DateTimeImplicitUTC:
                    return str -> LocalDateTime.from(DateTimeFormatter.ofPattern(getFormatPattern()).parse(str)).atOffset(ZoneOffset.UTC);
            }
        }

        throw new IllegalStateException("Invalid format specification: " + getFormatPattern() + " @ " + getParsedDateType());
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof CharSequence) {
            return Stream.of(getFormatter().parse((CharSequence) input));
        } else if (input instanceof Number) {
            return Stream.of(OffsetDateTime.ofInstant(Instant.ofEpochMilli((Long) input), ZoneOffset.UTC));
        }

        throw new DateTimeParseException("Unable to convert into a DateTime object.", Objects.toString(input), 0);
    }

    enum ParsedDateType {
        /**
         * Only Dates portion is specified in the format.  Assume start of day UTC for the time portion.
         */
        DateOnlyStartOfDayUTC,
        /**
         * DateTime formats that have an explicit offset/tz attached to the format
         * ... for example ISO 8601 formatted DateTimes
         */
        OffsetDateTime,
        /**
         * DateTime only format without any timezone information.
         * Assume the timezone specified is in UTC.
         */
        DateTimeImplicitUTC
    }

    interface OffsetDatetimeParser {
        OffsetDateTime parse(CharSequence text);
    }
}
