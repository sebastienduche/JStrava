package org.jstrava.user;

import org.jstrava.util.Base64Util;

import java.io.File;

public class FileIdentificationStorage extends IdentificationStorage {

    private final File file;

    public FileIdentificationStorage(File file) {
        this.file = file;
    }

    public FileIdentificationStorage(int clientId, String clientSecret, File file) {
        super(clientId, clientSecret);
        this.file = file;
    }

    public FileIdentificationStorage(int clientId, String clientSecret, String accessToken, String refreshToken, File file) {
        super(clientId, clientSecret, accessToken, refreshToken);
        this.file = file;
    }

    @Override
    public void save() {
        String values = String.join("-", Integer.toString(getClientId()), getClientSecret(), getAccessToken(), getRefreshToken());
        Base64Util.encode(values, file);
    }

    @Override
    public void load() {
        if (!file.exists()) {
            return;
        }
        String decode = Base64Util.decode(file);
        if (decode == null) {
            throw new RuntimeException("No content in the file " + file);
        }
        String[] values = decode.split("-");
        if (values.length < 2) {
            throw new RuntimeException("The file '" + file + "' doesn't contain clientId and secret");
        }
        setClientId(Integer.parseInt(values[0]));
        setClientSecret(values[1]);
        if (values.length == 4) {
            setAccessToken(values[2]);
            setRefreshToken(values[3]);
        }

    }
}
