package org.jstrava.authenticator;


import org.jstrava.entities.Athlete;

public class ExchangeResponse {
    Athlete athlete;
    String token_type;
    Long expires_at;
    Long expires_in;
    String refresh_token;
    String access_token;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Long expires_at) {
        this.expires_at = expires_at;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Athlete getAthlete() { return athlete; }
    public void setAthlete(Athlete athlete) { this.athlete = athlete; }

    @Override
    public String toString() {
        return "ExchangeResponse{" +
                "athlete=" + athlete +
                ", token_type='" + token_type + '\'' +
                ", expires_at=" + expires_at +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }
}