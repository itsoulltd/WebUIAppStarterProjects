package com.infoworks.services.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class AsyncWriter implements AutoCloseable {

    protected Workbook workbook;
    protected OutputStream outfile;

    public AsyncWriter() {
    }

    public AsyncWriter(boolean xssf, OutputStream outputStream) throws IOException {
        this.workbook = WorkbookFactory.create(xssf);
        this.outfile = outputStream;
    }

    public AsyncWriter(boolean xssf, String fileNameToWrite) throws IOException {
        this(xssf, new FileOutputStream(fileNameToWrite, true));
    }

    public void flush() throws IOException {
        if (workbook != null)
            workbook.write(outfile);
    }

    @Override
    public void close() throws Exception {
        if (workbook != null) {
            if (outfile != null) {
                outfile.close();
                outfile = null;
            }
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
            workbook.close();
            workbook = null;
        }
    }

    public void write(String sheetName, Map<Integer, List<String>> data, boolean skipZeroIndex) {
        //DoTheMath:
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) sheet = workbook.createSheet(sheetName);
        int rowIndex = 0;
        for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
            Row row = sheet.createRow((skipZeroIndex) ? entry.getKey() : rowIndex);
            int cellIndex = 0;
            for (String cellVal : entry.getValue()) {
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(cellVal);
                if (sheet instanceof XSSFSheet)
                    sheet.autoSizeColumn(cellIndex);
                cellIndex++;
            }
            rowIndex++;
        }
    }

    public static Map<Integer, List<String>> convert(List<Map> response, int startIndex, String... keys) {
        List<String> keysList = Arrays.stream(keys).toList();
        Map<Integer, List<String>> result = new HashMap<>();
        int index = Math.max(startIndex, 0);
        for (Map row : response) {
            List<String> rowList = keysList.stream()
                    .map(key -> Optional.ofNullable(row.get(key)).orElse("").toString())
                    .toList();
            result.put(index++, rowList);
        }
        return result;
    }

    public OutputStream getOutfile() {
        return outfile;
    }

    public Workbook getWorkbook() {
        return workbook;
    }
}
