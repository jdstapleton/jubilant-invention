package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberMapperTest {
    private static final NumberMapper defaultMapper = NumberMapper.builder().build();

    @Test
    public void shouldConvertToNumber() {
        assertEquals(1234L, first(defaultMapper.map("1234")));
        assertEquals(1234L, first(defaultMapper.map(1234)));
    }
}