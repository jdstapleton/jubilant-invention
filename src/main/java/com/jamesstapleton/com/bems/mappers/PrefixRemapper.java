package com.jamesstapleton.com.bems.mappers;

import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value.Immutable
@Model
public interface PrefixRemapper extends Mapper {
    @Value.Default
    default List<Map<String, String>> getMappers() {
        return List.of();
    }

    @Value.Derived
    default List<Map.Entry<String, String>> getPrefixes() {
        return getMappers().stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toList());
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            var strInput = (String) input;
            return getPrefixes().stream()
                    .filter(match -> strInput.startsWith(match.getKey()))
                    .findFirst()
                    .map(match -> (Object) match.getValue())
                    .stream();
        }

        return Stream.of(input);
    }
}
