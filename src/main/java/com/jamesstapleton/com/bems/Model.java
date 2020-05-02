package com.jamesstapleton.com.bems;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize
@Value.Style(
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        deepImmutablesDetection = true)
public @interface Model {
}
