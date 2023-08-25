package org.jstrava.exception;

public class StravaRequestException extends StravaException {
    private final int httpStatusCode;
    private final String message;

    public StravaRequestException(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
