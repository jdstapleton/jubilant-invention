package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.stream.Stream;

@Value.Immutable
@Model
public interface UCString extends Mapper {
    static ImmutableUCString.Builder builder() {
        return ImmutableUCString.builder();
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            return Stream.of(((String) input).toUpperCase());
        }

        return Stream.of(input);
    }
}
