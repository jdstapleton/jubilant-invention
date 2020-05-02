package com.jamesstapleton.com.bems.normalizer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.jamesstapleton.com.bems.model.DocumentContext;
import com.jamesstapleton.com.bems.utils.ImmutablesIdResolver;

import javax.validation.constraints.Null;

@JsonTypeIdResolver(ImmutablesIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
public interface ValueNormalizer {
    @Null
    Object normalize(String field, Object fieldValue, DocumentContext fullDocument);
}
