package com.jamesstapleton.com.bems.mappers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jamesstapleton.com.bems.Model;
import org.immutables.value.Value;
import org.springframework.util.PatternMatchUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value.Immutable
@Model
public interface SimpleRemapper extends Mapper {
    static ImmutableSimpleRemapper.Builder builder() {
        return ImmutableSimpleRemapper.builder();
    }

    @Value.Default
    default List<Map<String, String>> getMappers() {
        return List.of();
    }

    @JsonIgnore
    @Value.Derived
    default List<Map.Entry<String, String>> getPatterns() {
        return getMappers().stream()
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toList());
    }

    @Override
    default Stream<Object> map(Object input) {
        if (input instanceof String) {
            var strInput = (String) input;
            return getPatterns().stream()
                    .filter(pattern -> PatternMatchUtils.simpleMatch(pattern.getKey(), strInput))
                    .findFirst()
                    .map(pattern -> (Object) pattern.getValue())
                    .stream();
        }

        return Stream.of(input);
    }
}
