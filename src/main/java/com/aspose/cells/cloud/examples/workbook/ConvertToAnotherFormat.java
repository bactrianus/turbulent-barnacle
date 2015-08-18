package com.aspose.cells.cloud.examples.workbook;

import com.aspose.cells.cloud.examples.Common;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ConvertToAnotherFormat {

    public static void main(String... args) throws IOException {
        String inputFile = "Workbook1.xlsx";
        String outputFile = "Workbook1.pdf";

        String requestUrl1 = Common.buildRequestUrl("storage/file/" + inputFile, null);
        String signedUrl1 = Common.signRequestUrl(requestUrl1);
        Path file1 = Common.getPath(ConvertToAnotherFormat.class, inputFile);
        Common.executeRequest("PUT", signedUrl1, Common.readFile(file1));

        String requestUrl2 = Common.buildRequestUrl("cells/" + inputFile, "format=pdf");
        String signedUrl2 = Common.signRequestUrl(requestUrl2);
        Path file2 = Common.getPath(ConvertToAnotherFormat.class, outputFile);

        InputStream responseStream2 = Common.executeRequest("GET", signedUrl2, true);
        Common.writeFile(responseStream2, file2);

    }
}
