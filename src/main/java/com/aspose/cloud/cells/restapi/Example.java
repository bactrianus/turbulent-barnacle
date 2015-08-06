package com.aspose.cloud.cells.restapi;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public abstract class Example extends BaseExample {

    public static final String URL_SIGNING_ALGORITHM = "HmacSHA1";

    /**
     * Validate that the overriding methods are doing everything as documented.
     *
     * @return False when validation fails
     */
    public boolean isValid() {
        if (getAppSID() == null) {
            System.err.println("AppSID is null");
            return false;
        }

        if (getAppSID().isEmpty()) {
            System.err.println("AppSID is empty");
            return false;
        }

        if (getAppKey() == null) {
            System.err.println("AppKey is null");
            return false;
        }

        if (getAppKey().isEmpty()) {
            System.err.println("AppKey is null");
            return false;
        }

        if (getRequestJson() != null && getRequestFile() != null) {
            // only one of them should be non-null
            return false;
        }

        return true;
    }

    public JsonStructure getResponseJson() {
        try (JsonReader reader = Json.createReader(getResponseStream())) {
            return reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getResponseStream() throws IOException {
        if (!isValid()) {
            throw new IllegalStateException("Example is not valid");
        }

        URL url = null;
        try {
            url = new URL(getSignedUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setDoOutput(true);

        http.setRequestMethod(getHttpMethod());
        http.setRequestProperty("Accept", "application/json");

        if (getRequestFile() != getRequestFile()) {
            http.setRequestProperty("Content-Type", "application/octet-stream");
            try (InputStream in = new BufferedInputStream(new FileInputStream(getRequestFile()))) {
                try (OutputStream out = new BufferedOutputStream(http.getOutputStream())) {
                    while (in.available() > 0) {
                        out.write(in.read());
                    }
                }
            }
        } else if (getRequestJson() != getRequestFile()) {
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (JsonWriter writer = Json.createWriter(http.getOutputStream())) {
                writer.write(getRequestJson());
            }
        }

        String d = http.getResponseMessage();
        System.out.println(d);

        return http.getInputStream();
    }

    public void writeStream(InputStream i, File f) {
        try (InputStream in = new BufferedInputStream(i)) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
                while (in.available() > 0) {
                    out.write(in.read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSignedUrl() {
        StringBuilder url = new StringBuilder(getBaseUrl()).append(getRequestUrl());
        if (getQueryString() == null || getQueryString().isEmpty()) {
            url.append("?appSID=").append(getAppSID());
        } else {
            url.append('?').append(getQueryString()).append("&appSID=").append(getAppSID());
        }

        SecretKeySpec key = new SecretKeySpec(getAppKey().getBytes(), URL_SIGNING_ALGORITHM);

        Mac mac = null;
        try {
            mac = Mac.getInstance(URL_SIGNING_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        try {
            mac.init(key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

        // compute the hmac on input data bytes
        mac.update(url.toString().getBytes());
        byte[] rawHmac = mac.doFinal();
        String signature = Base64.getEncoder().withoutPadding().encodeToString(rawHmac);

        url.append("&signature=").append(signature);
        return url.toString();
    }

    public String getAppSID() {
        String s = null;

        if (s == null) {
            s = System.getProperty("com.aspose.cloud.appsid");
            if (s == null) {
                s = System.getenv("ASPOSE_CLOUD_APPSID");
            }
        }

        return s;
    }

    public String getAppKey() {
        String s = null;

        if (s == null) {
            s = System.getProperty("com.aspose.cloud.appkey");
            if (s == null) {
                s = System.getenv("ASPOSE_CLOUD_APPKEY");
            }
        }

        return s;
    }
}
