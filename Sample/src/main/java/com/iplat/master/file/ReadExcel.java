package com.iplat.master.file;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class ReadExcel {

    private final static int COLUMN_INDEX_TYPE = 9;
    private final static int COLUMN_INDEX_NAME = 14;
    private final static int COLUMN_INDEX_LENGTH = 21;
    private final static int COLUMN_INDEX_ACCURACY = 24;
    private final static int COLUMN_INDEX_REQUIRED = 28;


    private final static int HEADER_INDEX_TABLE_1 = 9;
    private final long MAX_ROW_SHEET = 10000;

    final String excelFilePath = "src\\main\\resources\\documents\\Mapper_Function_Template.xlsx";

    public ReadExcel() {}

    public List<SheetEntity> readFileExcel() throws IOException, java.io.IOException {


        List<SheetEntity> sheetEntityList = new ArrayList<>();

        InputStream inputStream = new FileInputStream(new File(excelFilePath));
        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        for(int i = 1; i < workbook.getNumberOfSheets(); i++) {
            int min_row_table_1 = HEADER_INDEX_TABLE_1 + 1;
            int max_row_table_1 = HEADER_INDEX_TABLE_1 + countRowTable(i, min_row_table_1);
            int min_row_table_2 = max_row_table_1 + 4;
            int max_row_table_2 = min_row_table_2 - 1 +  countRowTable(i, min_row_table_2);

            TypeTable typeTable_1;
            if(getValueCell(i, 7, 5) != null){
               typeTable_1 = TypeTable.OBJECT_PARAMETER;
            }
            else {
                typeTable_1 = TypeTable.PARAMETER;
            }


            TypeTable typeTable_2;
            if(max_row_table_2 < min_row_table_2) {
                typeTable_2 = TypeTable.SAMPLE_ENTITY;
            }
            else {
                typeTable_2 = TypeTable.SAMPLE;
            }
            sheetEntityList.add(createSheetList(i, HEADER_INDEX_TABLE_1 + 1, max_row_table_1, min_row_table_2, max_row_table_2, typeTable_1, typeTable_2));
        }

        for(int i = 1; i < workbook.getNumberOfSheets(); i++) {
             readEachSheet(workbook, i, sheetEntityList.get(i-1));
        }

        workbook.close();
        inputStream.close();

        return sheetEntityList;
    }

    public int countRowTable(int SheetNumber, int minRowTable) throws IOException {
        int rowNumberOfTable = 0;
        for(int i = minRowTable; i < MAX_ROW_SHEET; i++) {
            String value =  getValueCell(SheetNumber, i, 2);

            if(value != null) {
                ++ rowNumberOfTable;
            }
            else{
                return rowNumberOfTable;
            }
        }
        return rowNumberOfTable;
    }


    public SheetEntity createSheetList(int SheetNumber, int minRowTable_1, int maxRowTable_1, int minRowTable_2, int maxRowTable_2, TypeTable typeTable_1, TypeTable typeTable_2) {
        List<TableEntity> tableEntityList = new ArrayList<>();

        TableEntity tableEntity_1 = new TableEntity(minRowTable_1, maxRowTable_1, typeTable_1, new ArrayList<>());
        TableEntity tableEntity_2 = new TableEntity(minRowTable_2, maxRowTable_2, typeTable_2, new ArrayList<>());
        tableEntityList.add(tableEntity_1);
        tableEntityList.add(tableEntity_2);

        SheetEntity sheetEntity = new SheetEntity();
        sheetEntity.setNameMethod(new Coordinate(6, 5));
        sheetEntity.setInput(new Coordinate(7, 5));
        sheetEntity.setExport(new Coordinate(maxRowTable_1 + 1, 7));
        sheetEntity.setLocation(new Coordinate(maxRowTable_1 + 1, 17));
        sheetEntity.setSheetNumber(SheetNumber);
        sheetEntity.setTableEntities(tableEntityList);

        return sheetEntity;
    }

    public void readEachSheet(Workbook workbook, int numberSheet, SheetEntity sheetEntity) {


        for(int i = 0; i < sheetEntity.getTableEntities().size(); i++) {

            Sheet sheet = workbook.getSheetAt(numberSheet);

            Iterator<Row> iterator = sheet.rowIterator();
            readEachTableOfEachSheet(iterator, sheetEntity.getTableEntities().get(i));
        }
    }

    public void readEachTableOfEachSheet(Iterator<Row> iterator, TableEntity tableEntity) {

        if(tableEntity.getMinRow() > tableEntity.getMaxRow()) {
            return;
        }

        while (iterator.hasNext()) {
            Row row = iterator.next();

            if (row.getRowNum() < tableEntity.getMinRow()|| row.getRowNum() > tableEntity.getMaxRow()) {
                continue;
            }

            Iterator<Cell> cellIterator = row.cellIterator();

            FieldSample propertySimpleRequest = new FieldSample();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                Object cellValue = getCellValue(cell);

                if (cellValue == null || cellValue.toString().isEmpty()) {
                    continue;
                }

                int columnIndex = cell.getColumnIndex();

                switch (columnIndex) {
                    case COLUMN_INDEX_NAME:
                        propertySimpleRequest.setName(((String) getCellValue(cell)).toLowerCase());
                        break;
                    case COLUMN_INDEX_TYPE:
                        propertySimpleRequest.setType((String) getCellValue(cell));
                        break;
                    case COLUMN_INDEX_LENGTH:
                        propertySimpleRequest.setLength(new BigDecimal((double) cellValue).intValue());
                        break;
                    case COLUMN_INDEX_ACCURACY:
                        propertySimpleRequest.setAccuracy(new BigDecimal((double) cellValue).intValue());
                        break;
                    case COLUMN_INDEX_REQUIRED:
                        boolean required = getCellValue(cell).equals("Y");
                        propertySimpleRequest.setRequired(required);
                        break;
                    default:
                        break;
                }
            }
            if(propertySimpleRequest.getName() != null) {
                tableEntity.getFieldSamples().add(propertySimpleRequest);
            }
        }

    }
    public String getValueCell(int SheetNumber, int rowIndex, int columnIndex) throws IOException {
        InputStream inputStream = new FileInputStream(new File(excelFilePath));

        Workbook workbook = getWorkbook(inputStream, excelFilePath);

        Sheet sheet = workbook.getSheetAt(SheetNumber);

        Row row = sheet.getRow(rowIndex);

        Cell cell = row.getCell(columnIndex);

        workbook.close();
        inputStream.close();

        return (String) getCellValue(cell);
    }


    private Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException, java.io.IOException {
        Workbook workbook = null;

        if(excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        }
        else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else  {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    private Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        Object cellValue = null;

        switch (cellType) {
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }

        return cellValue;
    }

}
