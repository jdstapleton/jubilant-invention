package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtractReverseDottedTest {
    private static final ExtractReverseDotted defaultMapper = ExtractReverseDotted.builder().build();

    @Test
    public void shouldExtractReverseDotted() {
        var actual = first(defaultMapper.map("com.jamesstapleton.com.bems.mappers"));
        assertEquals("mappers.bems.com.jamesstapleton.com", actual);
    }
}