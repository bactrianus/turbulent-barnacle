package com.aspose.cells.cloud.examples;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

public class Common {

    private static final Logger LOGGER = Logger.getLogger(Common.class.getName());
    public static final String BASE_PATH = "http://api.aspose.com/v1.1/";
    public static final String APP_SID = null;
    public static final String APP_KEY = null;
    public static final String STORAGE = null;
    public static final String URL_SIGNING_ALGORITHM = "HmacSHA1";

    public static String buildRequestUrl(String path, String query) {
        StringBuilder builder = new StringBuilder(BASE_PATH).append(path);
        if (STORAGE != null && !STORAGE.isEmpty()) {
            builder.append(builder.indexOf("?") < 0 ? '?' : '&')
                    .append("storage=")
                    .append(STORAGE);
        }

        if (query != null && !query.isEmpty()) {
            builder.append(builder.indexOf("?") < 0 ? '?' : '&')
                    .append(query);
        }

        LOGGER.log(Level.INFO, "Request URL: {0}", builder.toString());
        return builder.toString();
    }

    public static String signRequestUrl(String requestUrl) {
        if (APP_SID == null || APP_KEY == null) {
            throw new RuntimeException("Both APP_SID and APP_KEY must have valid values");
        }

        StringBuilder url = new StringBuilder(requestUrl);

        if (requestUrl.contains("?")) {
            url.append('&').append("appSID=").append(APP_SID);
        } else {
            url.append('?').append("appSID=").append(APP_SID);
        }

        SecretKeySpec key = new SecretKeySpec(APP_KEY.getBytes(), URL_SIGNING_ALGORITHM);

        Mac mac;// = null;
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

        LOGGER.log(Level.INFO, "Signed URL: {0}", url.toString());
        return url.toString();
    }

    public static InputStream executeRequest(String httpMethod, String signedUrl, byte[] requestStream, boolean returnRawStream) throws IOException {
        URL url = new URL(signedUrl);

        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setDoOutput(true);

        http.setRequestMethod(httpMethod);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");

        if (requestStream != null) {
            try (OutputStream out = new BufferedOutputStream(http.getOutputStream())) {
                out.write(requestStream);
            }
        }

        LOGGER.info(String.format("HTTP Request: %s %s", httpMethod, signedUrl));
        http.connect();

        if (http.getErrorStream() != null) {
            return http.getErrorStream();
        } else {
            return http.getInputStream();
        }
    }

    public static InputStream executeRequest(String httpMethod, String signedUrl, JsonStructure requestJson, boolean returnRawStream) throws IOException {
//        ByteArrayOutputStream requestData = null;
//        if (requestJson != null) {
//            requestData = new ByteArrayOutputStream();
//            try (JsonWriter writer = Json.createWriter(requestData)) {
//                writer.write(requestJson);
//            }
//        }

        return executeRequest(httpMethod, signedUrl, requestJson.toString().getBytes(), true);
    }

    public static JsonStructure executeRequest(String httpMethod, String signedUrl, byte[] requestData) throws IOException {
        InputStream responseStream = executeRequest(httpMethod, signedUrl, requestData, true);
        try (JsonReader reader = Json.createReader(responseStream)) {
            return reader.read();
        }
    }

    public static JsonStructure executeRequest(String httpMethod, String signedUrl, JsonStructure requestJson) throws IOException {
//        ByteArrayOutputStream requestStream = null;
//        if (requestJson != null) {
//            requestStream = new ByteArrayOutputStream();
//            try (JsonWriter writer = Json.createWriter(requestStream)) {
//                writer.write(requestJson);
//            }
//        }

        byte[] buf = null;
        if (requestJson != null) {
            buf = requestJson.toString().getBytes();
        }
        InputStream responseStream = executeRequest(httpMethod, signedUrl, buf, true);
        try (JsonReader reader = Json.createReader(responseStream)) {
            return reader.read();
        }
    }

    public static JsonStructure executeRequest(String httpMethod, String signedUrl) throws IOException {
        return executeRequest(httpMethod, signedUrl, (byte[]) null);
    }

    public static InputStream executeRequest(String httpMethod, String signedUrl, boolean returnRawStream) throws IOException {
        return executeRequest(httpMethod, signedUrl, (byte[]) null, true);
    }

    public static byte[] readFile(Path p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Files.copy(p, out);
        return out.toByteArray();
    }

    public static void writeFile(InputStream i, Path p) throws IOException {
        Files.copy(i, p, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void uploadFile(Path source, String destination) throws IOException {
        if (destination == null) {
            destination = source.getFileName().toString();
        }
        String url = buildRequestUrl("storage/file/" + destination, null);
        url = signRequestUrl(url);
        byte[] buf = Common.readFile(source);
        JsonObject result = (JsonObject) Common.executeRequest("PUT", url, buf);
        if (result.getJsonNumber("Code").intValue() == 200) {
            LOGGER.info("Upload successful " + url);
        } else {
            LOGGER.info("Upload failed " + url);
        }
    }

    public static void downloadFile(String signedUrl, Path destination) throws IOException {
        writeFile(executeRequest("GET", signedUrl, true), destination);
    }

    public static Path getPath(Class example, String filename) {
        Path p = FileSystems.getDefault().getPath(System.getProperty("user.dir"), "src", "main", "resources");
        p = FileSystems.getDefault().getPath(p.toString(), example.getName().split("\\."));
        p = FileSystems.getDefault().getPath(p.toString(), filename);

        LOGGER.info(String.format("Using file %s", p.toString()));
        return p;
    }
}
