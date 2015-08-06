package com.aspose.cloud.cells.examples;

import com.aspose.cloud.cells.restapi.Example;

import javax.json.JsonObject;
import java.util.Random;

public class CreateEmptyWorkbook extends Example {
    @Override
    public String getHttpMethod() {
        return HTTP_PUT;
    }

    @Override
    public String getRequestUrl() {
        String filename = "Workbook-" + new Random().nextInt() + ".xlsx";
        return "cells/" + filename;
    }

    @Override
    public void run() {
        JsonObject json = (JsonObject) getResponseJson();
        if (json.getString("Status").equals("OK")) {
            System.out.println("Workbook created");
        }

        System.out.println(json.toString());
    }

    public static void main(String... args) {
        new CreateEmptyWorkbook().run();
    }
}
