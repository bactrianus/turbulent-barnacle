package com.aspose.cells.cloud.examples.chart;

import com.aspose.cells.cloud.examples.Common;
import java.io.IOException;
import java.nio.file.Path;

public class ConvertChartToImage {

    public static void main(String... args) throws IOException {
        Path inputFile = Common.getPath(ConvertChartToImage.class, "Sample.xlsx");
        Path outputFile = Common.getPath(ConvertChartToImage.class, "Chart.png");

        Common.uploadFile(inputFile, null);

        String url = Common.signRequestUrl(Common.buildRequestUrl(
                "cells/" + inputFile.getFileName().toString() + "/worksheets/Sheet1/charts/1",
                "format=png"
        ));
        Common.downloadFile(url, outputFile);
    }
}
