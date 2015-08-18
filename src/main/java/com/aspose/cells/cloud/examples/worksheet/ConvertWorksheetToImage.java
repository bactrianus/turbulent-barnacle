package com.aspose.cells.cloud.examples.worksheet;

import com.aspose.cells.cloud.examples.Common;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ConvertWorksheetToImage {

    public static void main(String... args) throws IOException {
        Path inputFile = Common.getPath(ConvertWorksheetToImage.class, "Workbook1.xlsx");
        Path outputFile = Common.getPath(ConvertWorksheetToImage.class, "Sheet1.png");
        String url = Common.buildRequestUrl(
                "cells/" + inputFile.getFileName().toString() + "/worksheets/Sheet1",
                "format=png"
        );
        url = Common.signRequestUrl(url);

        Common.uploadFile(inputFile, null);

        InputStream str = Common.executeRequest("GET", url, true);
        Common.writeFile(str, outputFile);
    }

}
