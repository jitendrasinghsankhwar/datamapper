package utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class JSONConvertor {
    private XSSFWorkbook WORKBOOK;
    private XSSFSheet EXCEL_SHEET;
    private CellStyle STYLE;

    public JSONConvertor(String companyName){
        WORKBOOK= new XSSFWorkbook();
        EXCEL_SHEET = WORKBOOK.createSheet(companyName);
        XSSFFont font= WORKBOOK.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLUE_GREY.getIndex());
        font.setBold(true);
        font.setItalic(false);
        STYLE = EXCEL_SHEET.getWorkbook().createCellStyle();
        STYLE.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        STYLE.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        STYLE.setAlignment(HorizontalAlignment.CENTER);
        STYLE.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        STYLE.setRightBorderColor(IndexedColors.BLACK.getIndex());
        STYLE.setTopBorderColor(IndexedColors.BLACK.getIndex());
        STYLE.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        STYLE.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        STYLE.setBorderTop(HSSFCellStyle.BORDER_THIN);
        STYLE.setBorderRight(HSSFCellStyle.BORDER_THIN);
        STYLE.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        STYLE.setFont(font);
    }

    private static final String FILE_NAME = "conf/ConvertedExcel.xlsx";

    public  void createExcel(JSONObject convertObject) {
        int depth=iterateJsonObject(convertObject,0);
        int[] num = new int[]{0, 0, depth};
        List<Row> rowObj = new ArrayList<Row>();
        Row keyRow = EXCEL_SHEET.createRow(num[0]);
        rowObj.add(keyRow);
        Row valueRow = EXCEL_SHEET.createRow(num[2]);
        jsonObjectConverter(convertObject, num, rowObj, valueRow);
        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            WORKBOOK.write(outputStream);
            WORKBOOK.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private int findMaxNumberOfKey(JSONObject objectData){
        Set<String> keySet = objectData.keySet();
        int maxKey=keySet.size();
        for (String key : keySet) {
            if(objectData.get(key) instanceof JSONObject){
                int nestedObjectMaxKey= findMaxNumberOfKey((JSONObject) objectData.get(key));
                maxKey+=nestedObjectMaxKey-1;
            }else if(objectData.get(key) instanceof JSONArray){
                JSONArray arrayObj=(JSONArray)  objectData.get(key);
                maxKey=(arrayObj.length())>maxKey?(arrayObj.length()):maxKey;
                for (int i=0;i<arrayObj.length();i++){
                    if(arrayObj.get(i) instanceof JSONObject){
                        int nestedObjectMaxKey= findMaxNumberOfKey((JSONObject) arrayObj.get(i));
                        maxKey=nestedObjectMaxKey>maxKey?nestedObjectMaxKey:maxKey;
                    }
                }
            }
        }
        return maxKey;
    }

    private int[] jsonValueType(Object value, int[] num, String key, List<Row> rowObjList, Row valueRow) {
        if(!(value instanceof JSONArray)){
            Cell  cell;
            if(value instanceof JSONObject){
                int maxKeyValue=findMaxNumberOfKey((JSONObject) value );
                if(maxKeyValue>1){
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(num[0],num[0],num[1],(num[1]+maxKeyValue-1));
                    EXCEL_SHEET.addMergedRegion(cellRangeAddress);
                }
                cell=(rowObjList.get(num[0]++)).createCell(num[1]);
            }else {
                cell = (rowObjList.get(num[0])).createCell(num[1]);
            }
            cell.setCellValue(key);
            cell.setCellStyle(STYLE);
        }
        if (value instanceof String) {
            valueRow.createCell(num[1]++).setCellValue((String) value);
        } else if (value instanceof Boolean) {
            valueRow.createCell(num[1]++).setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            valueRow.createCell(num[1]++).setCellValue((Double) value);
        } else if (value instanceof Date) {
            valueRow.createCell(num[1]++).setCellValue((Date) value);
        } else if (value instanceof Integer) {
            valueRow.createCell(num[1]++).setCellValue((Integer) value);
        }  else if (value instanceof JSONObject) {
            if (num[0] >= rowObjList.size()) {
                Row keyRow = EXCEL_SHEET.createRow(num[0]);
                rowObjList.add(keyRow);
            }
            jsonObjectConverter((JSONObject) value, num, rowObjList, valueRow);
            num[0]--;
        } else if (value instanceof JSONArray) {
            JSONArray arrayValue = (JSONArray) value;
            for (int i = 0; i < arrayValue.length(); i++) {
                jsonValueType(arrayValue.get(i), num, key + "_" + (i + 1), rowObjList, valueRow);
            }
        } else {
            valueRow.createCell(num[1]++).setCellValue("");
        }
        return num;
    }

    private void jsonObjectConverter(JSONObject convert, int[] num, List<Row> rowObjList, Row valueRow) {
        Set<String> keySet = convert.keySet();
        for (String key : keySet) {
            num = jsonValueType(convert.get(key), num, key, rowObjList, valueRow);
        }


    }

    private int iterateJsonObject(JSONObject objectData, int depth){
        Set<String> keySet = objectData.keySet();
        if(keySet.isEmpty()){
            return depth;
        }
        int newDepth=depth+1;
        for (String key : keySet) {
            int keyDepth=checkJsonObjectRec(objectData.get(key),depth+1);
            newDepth=keyDepth>newDepth?keyDepth:newDepth;
        }
        return newDepth;
    }

    private int checkJsonObjectRec(Object value, int depth){
        if (value instanceof JSONObject) {
            return iterateJsonObject((JSONObject)value,depth);
        }
        else if(value instanceof JSONArray){
            int newDepth=depth;
            JSONArray arrayValue = (JSONArray) value;
            for (int i = 0; i < arrayValue.length(); i++) {
                int arrayDepth= checkJsonObjectRec(arrayValue.get(0),depth);
                newDepth=arrayDepth>newDepth?arrayDepth:newDepth;
            }
            return newDepth;
        }
        return depth;
    }
}
