package com.jamesstapleton.com.bems.mappers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.jamesstapleton.com.bems.utils.ImmutablesIdResolver;

import java.util.stream.Stream;

@JsonTypeIdResolver(ImmutablesIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
public interface Mapper {
    Stream<Object> map(Object input);
}
