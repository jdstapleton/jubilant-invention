package com.jamesstapleton.com.bems.model;

import com.jamesstapleton.com.bems.Model;
import com.jamesstapleton.com.bems.mappers.Mapper;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Model
public interface FieldMapper {
    String getField();

    default FieldWrapperType getType() {
        return FieldWrapperType.SingleValue;
    }

    List<Mapper> getMappers();
}
