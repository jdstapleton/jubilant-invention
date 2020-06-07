package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UCStringTest {
    private static final UCString defaultMapper = UCString.builder().build();

    @Test
    public void shouldLowerCaseInput() {
        assertEquals("MAKE ME UPPER CASE", first(defaultMapper.map("Make Me Upper case")));
    }
}