package com.jamesstapleton.com.bems.boolexp;

import com.jamesstapleton.com.bems.model.DocumentContext;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StringTermTest {
    @Test
    public void shouldConstructAndHaveAGoodToString() {
        var actual = StringTerm.eq("test", "value-1");
        assertEquals("test", actual.getField());
        assertEquals(Set.of("value-1"), actual.getValue());
        assertEquals(StringTerm.Operator.EQ, actual.getOp());
        assertEquals("test EQ \"value-1\"", actual.toString());
    }

    @Test
    public void shouldThrowOnEmptyField() {
        assertThrows(RuntimeException.class, () -> StringTerm.eq("", "value-1"));
    }

    @Test
    public void shouldAllowNoValue() {
        assertDoesNotThrow(() -> StringTerm.eq("test"));
    }

    @Test
    public void shouldMatchSingleValue() {
        var actual = StringTerm.eq("test", "value-1");
        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", "value-1").build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", "value-2").build()));
    }

    @Test
    public void shouldMatchAnyValueInTermValue() {
        var actual = StringTerm.eq("test", "value-1", "value-2");
        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", "value-1").build()));
        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", "value-2").build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", "value-3").build()));
    }

    @Test
    public void shouldMatchEmptyTerm() {
        // note this require the term to exist in the DocumentContext
        var actual = StringTerm.eq("test");
        assertTrue(actual.matches(DocumentContext.builder().putCtx("test", "").build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test", "value").build()));
        assertFalse(actual.matches(DocumentContext.builder().putCtx("test-2", "").build()));
    }

    @Test
    public void shouldNotMatchAnotherTerm() {
        var actual = StringTerm.eq("test", "value-1", "value-2");
        assertFalse(actual.matches(DocumentContext.builder()
                .putCtx("test2", "value-1")
                .putCtx("test", "not-correct").build()));
    }
}