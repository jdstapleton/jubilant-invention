package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.model.DocumentContext;
import org.immutables.value.Value;

import java.util.stream.Stream;

@Value.Immutable
@Model
public interface Enumifier extends Mapper {
    String getName();

    @SuppressWarnings("unchecked")
    @Value.Derived
    default <T extends Enum<?>> Class<T> getEnumType() {
        try {
            // if no '.' we will assume its in our Model package.
            // the Enum type is checked in the Immutable's Check function since this is a generic
            if (getName().contains(".")) {
                return (Class<T>) Class.forName(getName());
            } else {
                return (Class<T>) Class.forName(DocumentContext.class.getPackageName() + "." + getName());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Value.Check
    default Enumifier check() {
        if (!getEnumType().isEnum()) {
            throw new RuntimeException(
                    "Name must be in reference to an enum type by instead found: " + getEnumType().getCanonicalName());
        }

        return this;
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            try {
                return Stream.of(Enum.valueOf(getEnumType(), (String) input));
            } catch (IllegalArgumentException badArgumentEx) {
                return Stream.of();
            }
        }
        // if its not a string, then it can't be converted to an ENUM, so it gets filtered out
        return Stream.of();
    }
}
