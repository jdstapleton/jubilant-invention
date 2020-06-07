package com.jamesstapleton.com.bems.mappers;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleRemapperTest {
    @Test
    public void shouldRemapMatches() {
        var mapper = SimpleRemapper.builder()
                .addMappers(
                        Map.of("FAT*", "FAT"),
                        Map.of("*SUGAR*", "SUGAR"),
                        Map.of("COOKIE", "COOKIE"),
                        Map.of("*", "OTHER"))
                .build();
        assertEquals("FAT", first(mapper.map("FAT")));
        assertEquals("FAT", first(mapper.map("FAT ACIDS")));
        assertEquals("FAT", first(mapper.map("FAT SUGAR")));
        assertEquals("SUGAR", first(mapper.map("SUGAR COOKIE")));
        assertEquals("SUGAR", first(mapper.map("SUGAR FREE")));
        assertEquals("SUGAR", first(mapper.map("BROWN SUGAR")));
        assertEquals("SUGAR", first(mapper.map("BROWN SUGAR COOKIES")));
        assertEquals("COOKIE", first(mapper.map("COOKIE")));
        assertEquals("OTHER", first(mapper.map("COOKIE DOUGH")));
        assertEquals("OTHER", first(mapper.map("ICE CREAM")));
    }
}