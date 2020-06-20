package com.jamesstapleton.com.bems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ValidationException extends RuntimeException {
    public final String[] fieldNames;

    public ValidationException(
            String message,
            Collection<FieldValidationException> exceptions) {

        super(message);

        this.fieldNames = exceptions.stream()
                .map(FieldValidationException::getFieldName)
                .toArray(String[]::new);

        exceptions.forEach(this::addSuppressed);
    }
}
