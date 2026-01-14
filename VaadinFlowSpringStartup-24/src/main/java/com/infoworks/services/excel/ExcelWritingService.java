package com.infoworks.services.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelWritingService {

    private static Logger LOG = Logger.getLogger(ExcelWritingService.class.getSimpleName());

    public void write(boolean xssf, OutputStream outputStream, String sheetName, Map<Integer, List<String>> data) throws Exception {
        AsyncWriter writer = new AsyncWriter(xssf, outputStream);
        writer.write(sheetName, data, false);
        writer.flush();
        writer.close();
    }

    public void write(OutputStream outputStream, String sheetName, Map<Integer, List<String>> data) throws Exception {
        AsyncWriter writer = new StreamWriter(100, outputStream);
        writer.write(sheetName, data, false);
        writer.flush();
        writer.close();
    }

    public AsyncWriter createWriter(boolean xssf, String outFileName, boolean replace) {
        try {
            if(outFileName == null || outFileName.isEmpty()) return null;
            if (replace) removeIfExist(outFileName);
            return new AsyncWriter(xssf, outFileName);
        } catch (IOException e) {LOG.log(Level.WARNING, e.getMessage());}
        return null;
    }

    public AsyncWriter createWriter(boolean xssf, OutputStream outputStream) {
        try {
            return new AsyncWriter(xssf, outputStream);
        } catch (IOException e) {LOG.log(Level.WARNING, e.getMessage());}
        return null;
    }

    public StreamWriter createStreamWriter(int rowSize, String outFileName, boolean replace) {
        try {
            if(outFileName == null || outFileName.isEmpty()) return null;
            if (replace) removeIfExist(outFileName);
            return new StreamWriter(rowSize, outFileName);
        } catch (IOException e) {LOG.log(Level.WARNING, e.getMessage());}
        return null;
    }

    private boolean removeIfExist(String outFileName){
        try {
            File outFile = new File(outFileName);
            if (outFile.exists() && outFile.isFile()){
                return outFile.delete();
            }
        } catch (Exception e) {LOG.log(Level.WARNING, e.getMessage());}
        return false;
    }
}