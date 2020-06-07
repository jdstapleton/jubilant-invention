package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.exceptions.DocumentContextParseException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
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
        assertEquals(DATE_1, first(defaultUnderTest.map("2020-04-01T03:05:00Z")));
    }

    @Test
    public void shouldConvertFromDateOnly() {
        var underTest = DateTimeMapper.builder()
                .formatPattern("yyyy-MM-dd")
                .parsedDateType(DateTimeMapper.ParsedDateType.DateOnlyStartOfDayUTC)
                .build();

        assertEquals(startOfDay(DATE_1), first(underTest.map("2020-04-01")));
    }

    @Test
    public void shouldConvertFromNonTZDateTime() {
        var underTest = DateTimeMapper.builder()
                .formatPattern("yyyy-MM-dd HH:mm")
                .parsedDateType(DateTimeMapper.ParsedDateType.DateTimeImplicitUTC)
                .build();

        assertEquals(DATE_1, first(underTest.map("2020-04-01 03:05")));
    }

    @Test
    public void shouldConvertFromEpocMilli() {
        var epochMilli = DATE_1.toInstant().toEpochMilli();
        assertEquals(DATE_1, first(defaultUnderTest.map(epochMilli)));
    }

    @Test
    public void shouldThrowExceptionOnBadDateFormat() {
        assertThrows(DocumentContextParseException.class, () -> defaultUnderTest.map("Not a real date"));
        assertThrows(DocumentContextParseException.class, () -> defaultUnderTest.map("2020-13-01T00:00:00Z"));
    }

}