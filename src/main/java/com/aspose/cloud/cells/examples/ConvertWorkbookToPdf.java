package com.aspose.cloud.cells.examples;

import com.aspose.cloud.cells.restapi.Example;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saqib
 */
public class ConvertWorkbookToPdf extends Example {

    @Override
    public String getHttpMethod() {
        return HTTP_GET;
    }

    @Override
    public String getRequestUrl() {
        String filename = "Workbook1.xlsx";
        return "cells/" + filename;
    }

    @Override
    public void run() {
        try (InputStream s = getResponseStream()) {
            
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

}
