package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LCStringTest {
    private static final LCString defaultMapper = LCString.builder().build();

    @Test
    public void shouldLowerCaseInput() {
        assertEquals("make me lower case", first(defaultMapper.map("Make Me Lower case")));
    }
}