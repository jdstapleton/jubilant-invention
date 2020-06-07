package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Value.Immutable
@Model
public interface ExtractReverseDotted extends Mapper {
    static ImmutableExtractReverseDotted.Builder builder() {
        return ImmutableExtractReverseDotted.builder();
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            var domainParts = ((String) input).split("\\.");
            List<Object> list = new ArrayList<>(List.of(domainParts));
            Collections.reverse(list);
            return list.stream();
        }

        return Stream.of(input);
    }
}
