package com.jamesstapleton.com.bems.boolexp;

import com.jamesstapleton.com.bems.model.DocumentContext;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTermTest {
    private static final OffsetDateTime DATE_1 = OffsetDateTime.parse("2020-04-01T03:05:00Z");
    private static final OffsetDateTime DATE_2 = OffsetDateTime.parse("2020-04-02T12:30:30Z");
    private static final OffsetDateTime DATE_3 = OffsetDateTime.parse("2020-04-03T20:45:59Z");

    @Test
    public void shouldConstructAndHaveAGoodToString() {
        var actual = DateTimeTerm.after("test", DATE_2);
        assertEquals("test", actual.getField());
        assertEquals(DATE_2, actual.getValue());
        assertEquals(DateTimeTerm.Operator.AFTER, actual.getOp());
        assertEquals("test AFTER 2020-04-02T12:30:30Z", actual.toString());
    }

    @Test
    public void shouldThrowOnEmptyField() {
        assertThrows(RuntimeException.class, () -> DateTimeTerm.after("", DATE_1));
    }

    @Test
    public void shouldAllowMinDateTime() {
        assertDoesNotThrow(() -> DateTimeTerm.after("test", OffsetDateTime.MIN));
    }

    @Test
    public void shouldAllowMaxDateTime() {
        assertDoesNotThrow(() -> DateTimeTerm.after("test", OffsetDateTime.MAX));
    }


    @Test
    public void shouldMatchAfter() {
        var actual = DateTimeTerm.after("test", DATE_2);

        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", DATE_1).build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", DATE_2).build()));
        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", DATE_3).build()));
    }

    @Test
    public void shouldMatchBefore() {
        var actual = DateTimeTerm.before("test", DATE_2);

        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", DATE_1).build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", DATE_2).build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", DATE_3).build()));
    }

    @Test
    public void shouldNotMatchAnotherTerm() {
        var actual = DateTimeTerm.after("test2", DATE_2);

        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", DATE_3).build()));
    }
}