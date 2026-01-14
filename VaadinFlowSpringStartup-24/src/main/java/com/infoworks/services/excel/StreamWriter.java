package com.infoworks.services.excel;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StreamWriter extends AsyncWriter {

    public StreamWriter(int rowSize, OutputStream outputStream) {
        if (rowSize <= 0) rowSize = 100;
        this.workbook = new SXSSFWorkbook(rowSize);
        this.outfile = outputStream;
    }

    public StreamWriter(int rowSize, String fileNameToWrite) throws IOException {
        this(rowSize, new FileOutputStream(fileNameToWrite, true));
    }
}
