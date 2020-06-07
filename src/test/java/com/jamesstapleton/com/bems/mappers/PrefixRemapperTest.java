package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PrefixRemapperTest {
    @Test
    public void shouldLookAtPrefix() {
        var mapper = PrefixRemapper.builder()
                .addMappers(
                        Map.of("com.kellogg", "kellogg"),
                        Map.of("com.kraft", "kraft"),
                        Map.of("com.cambells", "cambell"),
                        Map.of("com.go.disney", "disney"),
                        Map.of("com.go.abc", "abc"),
                        Map.of("com.go", "go"))
                .build();
        assertEquals("disney", first(mapper.map("com.go.disney")));
        assertEquals("go", first(mapper.map("com.go.nbc")));
        assertEquals("cambell", first(mapper.map("com.cambells")));
        assertEquals("kraft", first(mapper.map("com.kraft")));
        assertEquals("kellogg", first(mapper.map("com.kellogg")));
        assertNull(first(mapper.map("dne")));
    }
}