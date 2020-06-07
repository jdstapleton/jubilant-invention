package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.exceptions.DocumentContextParseException;
import com.jamesstapleton.com.bems.model.Food;
import com.jamesstapleton.com.bems.model.sub.Drink;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.config.BootstrapMode;

import static com.jamesstapleton.com.bems.utils.StreamUtils.first;
import static org.junit.jupiter.api.Assertions.*;

class EnumifierTest {
    private static final Enumifier defaultUnderTest = Enumifier.builder()
            .name("Food")
            .build();

    @Test
    public void shouldParseAnExistingEnum() {
        assertEquals(Food.EGGS, first(defaultUnderTest.map("EGGS")));
        assertEquals(Food.CHEESE, first(defaultUnderTest.map("CHEESE")));
    }

    @Test
    public void shouldBeEmptyWhenParsingANonEnumValue() {
        assertTrue(defaultUnderTest.map("ROCKS").findFirst().isEmpty());
    }

    @Test
    public void shouldAllowRelativePackage() {
        var fqEnum = Enumifier.builder()
                .name(".sub.Drink")
                .build();

        assertEquals(Drink.COFFEE, first(fqEnum.map("COFFEE")));
    }

    @Test
    public void shouldAllowFullQualifiedClassPath() {
        var fqEnum = Enumifier.builder()
                .name(BootstrapMode.class.getCanonicalName())
                .build();

        assertEquals(BootstrapMode.LAZY, first(fqEnum.map("LAZY")));
    }

    @Test
    public void shouldFailToBuildOnNonExistentType() {
        assertThrows(DocumentContextParseException.class, () -> Enumifier.builder().name("Dne").build());
    }

    @Test
    public void shouldFailToBuildOnNonEnumType() {
        assertThrows(DocumentContextParseException.class, () -> Enumifier.builder().name("DocumentContext").build());
    }

}