package com.jamesstapleton.com.bems.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.mappers.Mapper;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(builder = ImmutableFieldMapper.Builder.class)
@Value.Immutable
@Model
public interface FieldMapper {
    String getField();

    @Value.Default
    default FieldWrapperType getType() {
        return FieldWrapperType.SingleValue;
    }

    List<Mapper> getMappers();
}
