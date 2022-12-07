package org.springframework.core;

public class NestedIOException extends RuntimeException{

    public NestedIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
