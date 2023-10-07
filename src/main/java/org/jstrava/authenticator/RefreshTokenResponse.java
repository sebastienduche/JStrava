package org.jstrava.authenticator;


public class RefreshTokenResponse {
    String access_token;

    String refresh_token;

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() { return access_token; }
    public void setAccess_token(String token) { this.access_token = token; }


    @Override
    public String toString() {
        return "AuthResponse{" +
                "access_token='" + access_token + '\'' +
                ", refresh_token=" + refresh_token +
                '}';
    }
}