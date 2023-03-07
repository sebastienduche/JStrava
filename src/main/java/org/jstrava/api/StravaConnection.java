package org.jstrava.api;

import org.jstrava.authenticator.ExchangeResponse;
import org.jstrava.authenticator.RefreshTokenResponse;
import org.jstrava.authenticator.StravaAuthenticator;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.net.URL;

public class StravaConnection {

    private final int clientId;
    private static final String PARAM_CODE = "$code=";
    private static final String PARAM_SCOPE = "&scope=";

    private final JStravaV3 jStravaV3;
    private final StravaAuthenticator stravaAuthenticator;
    private final static String URL_GET_CODE = "https://www.strava.com/oauth/authorize?client_id=%d&response_type=code&redirect_uri=http://localhost/exchange_token&approval_prompt=force&scope=activity:read";

    public StravaConnection(int clientId, String clientSecret) {
        this.clientId = clientId;
        this.jStravaV3 = new JStravaV3();
        stravaAuthenticator = new StravaAuthenticator(clientId, "http://localhost/", clientSecret);
        init();
    }

    private void init() {
        String codeUrl = getCodeUrl();
        try {
            Desktop.getDesktop().browse(new URL(codeUrl).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = JOptionPane.showInputDialog("Please enter the URL after accepting the authentication");

        int indexCode = value.indexOf(PARAM_CODE);
        int indexStart = indexCode + PARAM_CODE.length();
        int indexEnd = value.indexOf(PARAM_SCOPE);
        String code = value.substring(indexStart, indexEnd);
        tokenExchange(code);
    }

    public JStravaV3 getStrava() {
        return jStravaV3;
    }

    // call the URL to get the code in the response
    public void tokenExchange(String code) {
        ExchangeResponse exchangeResponse = stravaAuthenticator.tokenExchange(code);
        System.out.println(exchangeResponse.getAccess_token());
        System.out.println(exchangeResponse);
        jStravaV3.setAccessToken(exchangeResponse.getAccess_token());
        jStravaV3.setRefreshToken(exchangeResponse.getRefresh_token());
    }

    public void refreshToken() {
        RefreshTokenResponse authResponse = stravaAuthenticator.refreshToken(jStravaV3.getRefreshToken());
        System.out.println(authResponse);
        jStravaV3.setAccessToken(authResponse.getAccess_token());
        jStravaV3.setRefreshToken(authResponse.getRefresh_token());
    }

    public String getCodeUrl() {
        return String.format(URL_GET_CODE, clientId);
    }
}
