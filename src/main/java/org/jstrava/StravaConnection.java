package org.jstrava;

import org.jstrava.api.JStravaV3;
import org.jstrava.authenticator.ExchangeResponse;
import org.jstrava.authenticator.RefreshTokenResponse;
import org.jstrava.authenticator.StravaAuthenticator;
import org.jstrava.user.IdentificationStorage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class StravaConnection {

    private final IdentificationStorage identificationStorage;
    private final JStravaV3 jStravaV3;
    private final StravaAuthenticator stravaAuthenticator;

    private static final String PARAM_CODE = "&code=";
    private static final String PARAM_SCOPE = "&scope=";
    private final static String URL_GET_CODE = "https://www.strava.com/oauth/authorize?client_id=%d&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=activity:read";

    public StravaConnection(IdentificationStorage identificationStorage) throws IOException, URISyntaxException {
        this.identificationStorage = identificationStorage;
        this.jStravaV3 = new JStravaV3();
        identificationStorage.load();
        stravaAuthenticator = new StravaAuthenticator(identificationStorage.getClientId(), "http://localhost/", identificationStorage.getClientSecret());
        jStravaV3.setAccessToken(identificationStorage.getAccessToken());
        jStravaV3.setRefreshToken(identificationStorage.getRefreshToken());
        if (identificationStorage.getRefreshToken() != null) {
            refreshToken();
        }
    }

    public void requestCode() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URL(getCodeUrl()).toURI());
    }

    public void parseUrl(String url) {
        int indexCode = url.indexOf(PARAM_CODE);
        if (indexCode == -1) {
            throw new RuntimeException("The provided URL doesn't contain " + PARAM_CODE + " \n" + url);
        }
        int indexStart = indexCode + PARAM_CODE.length();
        int indexEnd = url.indexOf(PARAM_SCOPE);
        String code = url.substring(indexStart, indexEnd);
        tokenExchange(code);
    }

    public JStravaV3 getStrava() {
        if (jStravaV3.getAccessToken() == null) {
            throw new RuntimeException("The method requestCode must be called to initialise the access token!");
        }
        return jStravaV3;
    }

    public void refreshToken() {
        if (jStravaV3.getRefreshToken() == null) {
            throw new RuntimeException("This method can't be called without refresh token!. The method requestCode must be called");
        }
        RefreshTokenResponse refreshTokenResponse = stravaAuthenticator.refreshToken(jStravaV3.getRefreshToken());
        saveTokens(refreshTokenResponse.getAccess_token(), refreshTokenResponse.getRefresh_token());
    }

    public String getCodeUrl() {
        return String.format(URL_GET_CODE, identificationStorage.getClientId());
    }

    private void tokenExchange(String code) {
        ExchangeResponse exchangeResponse = stravaAuthenticator.tokenExchange(code);
        saveTokens(exchangeResponse.getAccess_token(), exchangeResponse.getRefresh_token());
    }

    private void saveTokens(String accessToken, String refreshToken) {
        jStravaV3.setAccessToken(accessToken);
        jStravaV3.setRefreshToken(refreshToken);
        identificationStorage.setAccessToken(accessToken);
        identificationStorage.setRefreshToken(refreshToken);
        identificationStorage.save();
    }
}
