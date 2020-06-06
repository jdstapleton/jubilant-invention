package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateTimeMapperTest {
    private static final DateTimeMapper defaultUnderTest = DateTimeMapper.builder().build();
    private static final OffsetDateTime DATE_1 = OffsetDateTime.parse("2020-04-01T03:05:00Z");

    private static OffsetDateTime startOfDay(OffsetDateTime d) {
        /*
         *  According to the Java docs, truncatedTo() will try to preserve the time zone in the case of overlap, but
         *  atStartOfDay() will find the first occurrence of midnight.
         */
        return d.toLocalDate().atStartOfDay(d.getOffset()).toOffsetDateTime();
    }

    @Test
    public void shouldConvertFromISO8601Time() {
        assertEquals(defaultUnderTest.map("2020-04-01T03:05:00Z").findFirst().orElseThrow(IllegalStateException::new), DATE_1);
    }

    @Test
    public void shouldConvertFromDateOnly() {
        var underTest = DateTimeMapper.builder()
                .formatPattern("yyyy-MM-dd")
                .parsedDateType(DateTimeMapper.ParsedDateType.DateOnlyStartOfDayUTC)
                .build();
        assertEquals(underTest.map("2020-04-01").findFirst().orElseThrow(IllegalStateException::new), startOfDay(DATE_1));
    }

    @Test
    public void shouldConvertFromNonTZDateTime() {
        var underTest = DateTimeMapper.builder()
                .formatPattern("yyyy-MM-dd HH:mm")
                .parsedDateType(DateTimeMapper.ParsedDateType.DateTimeImplicitUTC)
                .build();
        assertEquals(underTest.map("2020-04-01 03:05").findFirst().orElseThrow(IllegalStateException::new), DATE_1);
    }

    @Test
    public void shouldConvertFromEpocMilli() {
        var epochMilli = DATE_1.toInstant().toEpochMilli();
        assertEquals(defaultUnderTest.map(epochMilli).findFirst().orElseThrow(IllegalStateException::new), DATE_1);
    }

    @Test
    public void shouldThrowExceptionOnBadDateFormat() {
        assertThrows(DateTimeParseException.class, () -> defaultUnderTest.map("Not a real date"));
        assertThrows(DateTimeParseException.class, () -> defaultUnderTest.map("2020-13-01T00:00:00Z"));
    }

}