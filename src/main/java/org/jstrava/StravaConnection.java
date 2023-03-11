package org.jstrava;

import org.jstrava.api.JStravaV3;
import org.jstrava.authenticator.ExchangeResponse;
import org.jstrava.authenticator.RefreshTokenResponse;
import org.jstrava.authenticator.StravaAuthenticator;
import org.jstrava.user.IdentificationStorage;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class StravaConnection {

    private final IdentificationStorage identificationStorage;
    private static final String PARAM_CODE = "$code=";
    private static final String PARAM_SCOPE = "&scope=";

    private final JStravaV3 jStravaV3;
    private final StravaAuthenticator stravaAuthenticator;
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
        } else {
            requestCode();
        }
    }

    public void requestCode() throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URL(getCodeUrl()).toURI());
        String value = JOptionPane.showInputDialog("Please enter the URL after accepting the authentication");

        int indexCode = value.indexOf(PARAM_CODE);
        int indexStart = indexCode + PARAM_CODE.length();
        int indexEnd = value.indexOf(PARAM_SCOPE);
        String code = value.substring(indexStart, indexEnd);
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
            return;
        }
        RefreshTokenResponse refreshTokenResponse = stravaAuthenticator.refreshToken(jStravaV3.getRefreshToken());
        jStravaV3.setAccessToken(refreshTokenResponse.getAccess_token());
        jStravaV3.setRefreshToken(refreshTokenResponse.getRefresh_token());
        identificationStorage.setAccessToken(refreshTokenResponse.getAccess_token());
        identificationStorage.setRefreshToken(refreshTokenResponse.getRefresh_token());
        identificationStorage.save();
    }

    public String getCodeUrl() {
        return String.format(URL_GET_CODE, identificationStorage.getClientId());
    }

    private void tokenExchange(String code) {
        ExchangeResponse exchangeResponse = stravaAuthenticator.tokenExchange(code);
        System.out.println(exchangeResponse.getAccess_token());
        System.out.println(exchangeResponse);
        jStravaV3.setAccessToken(exchangeResponse.getAccess_token());
        jStravaV3.setRefreshToken(exchangeResponse.getRefresh_token());
        identificationStorage.setAccessToken(exchangeResponse.getAccess_token());
        identificationStorage.setRefreshToken(exchangeResponse.getRefresh_token());
        identificationStorage.save();
    }
}
