package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.stream.Stream;

@Value.Immutable
@Model
public interface LCString extends Mapper {
    static ImmutableLCString.Builder builder() {
        return ImmutableLCString.builder();
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            return Stream.of(((String) input).toLowerCase());
        }

        return Stream.of(input);
    }
}
