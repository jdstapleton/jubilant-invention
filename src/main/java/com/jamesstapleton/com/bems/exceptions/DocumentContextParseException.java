package com.jamesstapleton.com.bems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class DocumentContextParseException extends RuntimeException {
    public DocumentContextParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentContextParseException(String message) {
        super(message);
    }
}
