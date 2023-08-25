package org.jstrava.exception;

public class GenericStravaException extends StravaException {
    private final String message;
    private final Exception exception;
    public GenericStravaException(String s, Exception e) {
        this.message = s;
        this.exception = e;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
