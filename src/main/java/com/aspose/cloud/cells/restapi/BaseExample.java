package com.aspose.cloud.cells.restapi;

import javax.json.JsonStructure;
import java.io.File;

public abstract class BaseExample implements Runnable {
    public static final String HTTP_GET = "GET";
    public static final String HTTP_POST = "POST";
    public static final String HTTP_PUT = "PUT";
    public static final String HTTP_DELETE = "DELETE";

    /**
     * Should be one of GET POST PUT DELETE
     */
    public abstract String getHttpMethod();

    /**
     * Should not include base API URL. Use {@code getBaseUrl} instead.
     * Should not start with /.
     * Should not contain any query string parameters. Use {@code getQueryString} instead.
     */
    public abstract String getRequestUrl();

    /**
     * Path to input file that we want to upload. Must be null when {@code getRequestJson} returns non-null.
     *
     * @return Default implementation always returns {@code null}.
     */
    public File getRequestFile() {
        return null;
    }

    /**
     * JSON data that we want to send with request. Must be null when {@code getRequestFile} returns non-null.
     *
     * @return Default implementation always returns {@code null}.
     */
    public JsonStructure getRequestJson() {
        return null;
    }

    /**
     * Should not start or end with ? and &
     *
     * @return Default implementation always returns {@code null}.
     */
    public String getQueryString() {
        return null;
    }

    public String getBaseUrl() {
        return "http://api.aspose.com/v1.1/";
    }

    public File getOutputDir() {
        File dir = new File(System.getProperty("user.dir"), "Output");
        dir.mkdirs();
        return dir;
    }
}
