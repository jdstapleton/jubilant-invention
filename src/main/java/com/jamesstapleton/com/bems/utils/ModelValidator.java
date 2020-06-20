package com.jamesstapleton.com.bems.utils;

import com.jamesstapleton.com.bems.exceptions.FieldValidationException;
import com.jamesstapleton.com.bems.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModelValidator implements AutoCloseable {
    private final String contextName;
    private final List<FieldValidationException> validationExceptions = new ArrayList<>();

    public ModelValidator(String contextName) {
        this.contextName = contextName;
    }

    public void validate(String fieldName, ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            validationExceptions.add(new FieldValidationException(fieldName, e));
        }
    }

    public void close() throws ValidationException {
        if (!validationExceptions.isEmpty()) {
            throw new ValidationException(
                    "Invalid model: " + contextName,
                    Collections.unmodifiableCollection(validationExceptions));
        }
    }
}
