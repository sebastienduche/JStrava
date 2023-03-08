package org.jstrava.util;

import org.apache.commons.net.util.Base64;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Base64Util {
    public static void encode(String value, String file) {
        byte[] to = Base64.encodeBase64(value.getBytes());
        try {
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(to);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decode(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String sBuf = reader.readLine();
            String value = new String(Base64.decodeBase64(sBuf.getBytes()));
            reader.close();
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
