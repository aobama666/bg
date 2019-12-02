package com.sgcc.bg.common;

 

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Font;


public class newExcelUtil {
    /**
     * 获取Excel数据并转成集合
     * 仅支持xls格式
     *
     * @param is          文件流
     * @param sheetNum    第几页数据
     * @param beginRowNum 从第几行开始取
     * @param cellIndex   取第几列
     * @param keys        存入Map的key叫什么 下标需和 cellIndex相对应
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static List<Map<String, String>> getExcelDate(InputStream is, int sheetNum, int beginRowNum, int[] cellIndex,
                                                         String[] keys)
            throws Exception {
        //文件流不能为空
        if (is == null) {
            throw new Exception("InputStream is null");
        }
        //sheet页码和开始行不能小于0
        if (sheetNum < 0 || beginRowNum < 0) {
            throw new Exception("sheetNum or beginRowNum not less 0");
        }
        //要取的单元格坐标和key值 不能为空和长度等于0
        if (cellIndex == null || cellIndex.length <= 0 || keys == null || keys.length <= 0) {
            throw new Exception("cellIndex or keys array length error");
        }
        //单元格坐标个数和key值个数应相等
        if (cellIndex.length != keys.length) {
            throw new Exception("cellIndex and keys length inequality");
        }
        POIFSFileSystem fs = null;
        HSSFWorkbook wb = null;
        HSSFSheet sheet;
        HSSFRow row;
        HSSFCell cell;
        // 获取POI操作对象
        fs = new POIFSFileSystem(is);
        wb = new HSSFWorkbook(fs);
        // 创建存储数据集合 用来存放取出的数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        sheet = wb.getSheetAt(sheetNum);
        // 得到sheet内总行数
        int rowCountNum = sheet.getLastRowNum();
        // 正文内容应该从beginRowNum行开始
        for (int i = beginRowNum; i <= rowCountNum + 1; i++) {
            // 获取当期行
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Map<String, String> tempMap = new HashMap<String, String>();
            for (int j = 0; j < cellIndex.length; j++) {
                cell = row.getCell(cellIndex[j]);
                if (cell == null) {
                    tempMap.put(keys[j], "");
                } else {
                    tempMap.put(keys[j], getStringCellValueForOnLine(cell));
                }
            }
            list.add(tempMap);
        }
        return list;
    }

    /**
     * 获取Excel数据并转成集合
     * 仅支持xls格式
     *
     * @param wb          POI工作簿对象
     * @param sheetNum    第几页数据
     * @param beginRowNum 从第几行开始取
     * @param cellIndex   取第几列
     * @param keys        存入Map的key叫什么 下标需和 cellIndex相对应
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static List<Map<String, String>> getExcelDate(HSSFWorkbook wb, int sheetNum, int beginRowNum, int[] cellIndex,
                                                         String[] keys)
            throws Exception {
        //文件流不能为空
        if (wb == null) {
            throw new Exception("HSSFWorkbook is null");
        }
        //sheet页码和开始行不能小于0
        if (sheetNum < 0 || beginRowNum < 0) {
            throw new Exception("sheetNum or beginRowNum not less 0");
        }
        //要取的单元格坐标和key值 不能为空和长度等于0
        if (cellIndex == null || cellIndex.length <= 0 || keys == null || keys.length <= 0) {
            throw new Exception("cellIndex or keys array length error");
        }
        //单元格坐标个数和key值个数应相等
        if (cellIndex.length != keys.length) {
            throw new Exception("cellIndex and keys length inequality");
        }
        HSSFSheet sheet;
        HSSFRow row;
        HSSFCell cell;
        // 创建存储数据集合 用来存放取出的数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        sheet = wb.getSheetAt(sheetNum);
        // 得到sheet内总行数
        int rowCountNum = sheet.getLastRowNum();
        // 正文内容应该从beginRowNum行开始
        for (int i = beginRowNum; i <= rowCountNum + 1; i++) {
            // 获取当期行
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            Map<String, String> tempMap = new HashMap<String, String>();
            for (int j = 0; j < cellIndex.length; j++) {
                cell = row.getCell(cellIndex[j]);
                if (cell == null) {
                    tempMap.put(keys[j], "");
                } else {
                    tempMap.put(keys[j], getStringCellValueForOnLine(cell));
                }
            }
            list.add(tempMap);
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    private static String getStringCellValueForOnLine(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_FORMULA:
                try {
                    strCell = String.valueOf(cell.getStringCellValue());
                } catch (IllegalStateException e) {
                    strCell = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    strCell = dateFormat.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                } else if (cell.getCellStyle().getDataFormatString().indexOf("%") != -1) {
                    strCell = cell.getNumericCellValue() * 100 + "%";
                } else {
                    DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                    strCell = nf.format(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 生成Excel
     *
     * @param headermap Excel头 头数据Map中的key要和rowlist中的MapKey一一对应
     * @param rowlsit   行数据
     * @return
     */
    public static HSSFWorkbook PaddingExcel(LinkedHashMap<String, String> headermap, List<Map<String, Object>> rowList) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        if (headermap == null || headermap.isEmpty()) {
            try {
                throw new Exception("Excel 头不能为空!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //判断Map对应的value放到哪一列的Map
        Map<String, Integer> valueColumnMap = new LinkedHashMap<>();
        //创建ExcelHeader头
        HSSFRow headerrow = sheet.createRow(0);
        //设置头部样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);

        //设置数据样式
        HSSFCellStyle dataCellStyle = workbook.createCellStyle();
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 11);
        dataCellStyle.setFont(dataFont);


        //根据值的长度获取Excel格子的宽度Key为 表头 value 为数据值的最大长度
        Map<String, Integer> valueLenMap = getCellValueWidth(rowList);

        int column = 0;
        for (Entry<String, String> header : headermap.entrySet()) {
            String headervalue = header.getValue();
            //做一个key对应的列的Map用于处理数据的Key对应的列
            valueColumnMap.put(header.getKey(), column);

            Integer cellValueLentgh = valueLenMap.get(header.getKey()) == null ? 1 : valueLenMap.get(header.getKey());
            if (cellValueLentgh < header.getValue().length()) {
                cellValueLentgh = header.getValue().length();
            }

            int excelLength = (cellValueLentgh + 1) * 800;

            if (excelLength > 10000) {
                excelLength = 10000;
            }
            sheet.setColumnWidth(column, excelLength);
            HSSFCell cell = headerrow.createCell(column);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(headervalue);
            column++;
        }
        //创建Excel对应的行
        for (int i = 0; i < rowList.size(); i++) {
            if (rowList.get(i) == null) {
                continue;
            }
            int rowindex = i + 1;
            HSSFRow valueRow = sheet.createRow(rowindex);
            for (Entry<?, ?> entry : rowList.get(i).entrySet()) {
                String key = entry.getKey() + "";
                String value = entry.getValue() == null ? "" : entry.getValue() + "";
                //根据Key匹配获取对应的值属于哪一列。获取列索引
                Integer valueColumn = valueColumnMap.get(key);
                if (valueColumn == null) {
                    continue;
                }
                HSSFCell valueCell = valueRow.createCell(valueColumn);
                valueCell.setCellStyle(dataCellStyle);
                valueCell.setCellValue(value);
            }
        }

        sheet.setDefaultRowHeight((short) 400);

        return workbook;
    }

    /**
     * 根据key获取相应的
     *
     * @param rowlsit excel值
     * @return
     */
    private static Map<String, Integer> getCellValueWidth(List<Map<String, Object>> rowlsit) {
        Map<String, Integer> maxLenMap = new HashMap<>();
        for (Map<String, Object> mpdata : rowlsit) {
            for (Entry<String, Object> entry : mpdata.entrySet()) {
                String mpdataKey = entry.getKey();
                int mpdataLenValue = 0;
                if (entry.getValue() != null && !"".equals(entry.getValue()))
                    mpdataLenValue = String.valueOf(entry.getValue()).length();
                if (!maxLenMap.containsKey(mpdataKey)) {
                    maxLenMap.put(mpdataKey, mpdataLenValue);
                } else {
                    Integer maxLenValue = maxLenMap.get(mpdataKey);
                    if (maxLenValue < mpdataLenValue) {
                        maxLenMap.put(mpdataKey, mpdataLenValue);
                    }
                }

            }
        }
        return maxLenMap;
    }


}
