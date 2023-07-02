package org.jstrava;

public class StravaException extends Exception {

    int httpStatusCode = -1;
    public StravaException(String s, Exception e) {
        super(s, e);
    }
    public StravaException(int httpStatusCode, String s) {
        super(s);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
