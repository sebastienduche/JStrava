package org.jstrava;

public class StravaException extends Exception {
    public StravaException(String s, Exception e) {
        super(s, e);
    }
    public StravaException(String s) {
        super(s);
    }
}
