package com.jamesstapleton.com.bems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FieldValidationException extends RuntimeException {
    private final String fieldName;

    public FieldValidationException(String fieldName, Exception wrapped) {
        super(fieldName + ": " + wrapped.getMessage());
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
