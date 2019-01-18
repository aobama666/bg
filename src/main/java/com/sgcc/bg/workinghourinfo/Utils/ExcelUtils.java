package com.sgcc.bg.workinghourinfo.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
 

public class ExcelUtils {
	/**
	 * 获取Excel数据并转成集合
	 * 仅支持xls格式
	 * 
	 * @param is
	 *            文件流
	 * @param sheetNum
	 *            第几页数据
	 * @param beginRowNum
	 *            从第几行开始取
	 * @param cellIndex
	 *            取第几列
	 * @param keys
	 *            存入Map的key叫什么 下标需和 cellIndex相对应
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("resource")
	public static List<Map<String, String>> getExcelDate(InputStream is, int sheetNum, int beginRowNum, int[] cellIndex,
			String[] keys)
			throws Exception {
		//文件流不能为空
		if(is==null){
			throw new Exception("InputStream is null");
		}
		//sheet页码和开始行不能小于0
		if(sheetNum<0||beginRowNum<0){
			throw new Exception("sheetNum or beginRowNum not less 0");
		}
		//要取的单元格坐标和key值 不能为空和长度等于0
		if(cellIndex==null||cellIndex.length<=0||keys==null||keys.length<=0){
			throw new Exception("cellIndex or keys array length error");
		}
		//单元格坐标个数和key值个数应相等
		if(cellIndex.length!=keys.length){
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
		for (int i = beginRowNum; i <= rowCountNum+1; i++) {
			// 获取当期行
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, String> tempMap = new HashMap<String, String>();
			for (int j = 0; j < cellIndex.length; j++) {
				cell = row.getCell(cellIndex[j]);
				tempMap.put(keys[j], getStringCellValueForOnLine(cell));
			}
			list.add(tempMap);
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public static String getStringCellValueForOnLine(HSSFCell cell) {
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
	/*
	 * 列头单元格样式
	 */
	public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		// 设置字体
				HSSFFont font = workbook.createFont();
				font.setColor(HSSFColor.WHITE.index);
				// 设置字体大小
				font.setFontHeightInPoints((short) 10);
				// 字体加粗
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				// 设置字体名字
				font.setFontName("宋体");
				// 设置样式;
				HSSFCellStyle style = workbook.createCellStyle();
				//声明背景颜色
				String color="008080";//国网绿
				int r=Integer.parseInt(color.substring(0,2), 16);
				int g=Integer.parseInt(color.substring(2,4), 16);
				int b=Integer.parseInt(color.substring(4,6), 16);
				HSSFPalette palette=workbook.getCustomPalette();
				palette.setColorAtIndex((short)11, (byte)r, (byte)g, (byte)b);
				//设置 背景颜色
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setFillForegroundColor((short)11);
				// 在样式用应用设置的字体;
				style.setFont(font);
				// 设置底边框;
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				// 设置底边框颜色;
				style.setBottomBorderColor(HSSFColor.BLACK.index);
				// 设置左边框;
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				// 设置左边框颜色;
				style.setLeftBorderColor(HSSFColor.BLACK.index);
				// 设置右边框;
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// 设置右边框颜色;
				style.setRightBorderColor(HSSFColor.BLACK.index);
				// 设置顶边框;
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				// 设置顶边框颜色;
				style.setTopBorderColor(HSSFColor.BLACK.index);
				// 设置自动换行;
				style.setWrapText(true);
				// 设置水平对齐的样式为居中对齐;
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 设置垂直对齐的样式为居中对齐;
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

				return style;

	}
	/*
	 * 列数据信息单元格样式
	 */
	@SuppressWarnings("deprecation")
	public static HSSFCellStyle getStyle(HSSFWorkbook workbook, String isErrorText) {
		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		// font.setFontHeightInPoints((short)10);
		// 字体加粗
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
		if ("err".equals(isErrorText)) {
			style.setFillForegroundColor(HSSFColor.YELLOW.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}
		// 设置底边框;
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// 设置底边框颜色;
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		// 设置左边框;
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// 设置左边框颜色;
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		// 设置右边框;
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// 设置右边框颜色;
		style.setRightBorderColor(HSSFColor.BLACK.index);
		// 设置顶边框;
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// 设置顶边框颜色;
		style.setTopBorderColor(HSSFColor.BLACK.index);
		// 在样式用应用设置的字体;
		style.setFont(font);
		// 设置自动换行;
		style.setWrapText(false);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;
	}
public static HSSFWorkbook PaddingExcel(LinkedHashMap<String,String> headermap,List<Map<String,Object>> rowlsit,List<Map<String,String>> headlsit){
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		
		if(headermap==null||headermap.isEmpty()){
			try {
				throw new Exception("Excel 头不能为空!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//创建Excel头部
		
		//判断Map对应的value放到哪一列的Map
		Map<String,Integer> valueColumnMap = new LinkedHashMap<String,Integer>();
		//创建ExcelHeader头
		HSSFRow headerrow = sheet.createRow(0);
		sheet.setDefaultColumnWidth(20);
		//设置头部样式
	 
		HSSFCellStyle cellStyle=getColumnTopStyle(workbook);
		 
		 
		int column = 0;
		for(Entry<String, String> header:headermap.entrySet()){
			String headervalue = header.getValue();
			//做一个key对应的列的Map用于处理数据的Key对应的列
			valueColumnMap.put(header.getKey(), column);
			HSSFCell cell = headerrow.createCell(column);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(headervalue);
			column++;
		}
		
		// 合并单元格
		for(int i=0;i<headlsit.size();i++){
			if(headlsit.get(i)==null){
				continue;
			}
			int rowindex = i+headlsit.size();
			HSSFRow valueRow = sheet.createRow(rowindex);
			 
			 
			for(Entry<?, ?> entry:headlsit.get(i).entrySet()){
				String key = entry.getKey()+"";
				String value = entry.getValue()==null?"":entry.getValue()+"";
				//根据Key匹配获取对应的值属于哪一列。获取列索引
				Integer valueColumn = valueColumnMap.get(key);
				if(valueColumn==null){
					continue;
				}
				HSSFCell valueCell = valueRow.createCell(valueColumn);
				valueCell.setCellStyle(cellStyle);
				valueCell.setCellValue(value);
			}
		}
				 
		
		//创建Excel对应的行
		for(int i=0;i<rowlsit.size();i++){
			if(rowlsit.get(i)==null){
				continue;
			}
			int rowindex = i+1+headlsit.size();
			HSSFCellStyle style = getStyle(workbook, "");
			HSSFRow valueRow = sheet.createRow(rowindex);
			for(Entry<?, ?> entry:rowlsit.get(i).entrySet()){
				String key = entry.getKey()+"";
				String value = entry.getValue()==null?"":entry.getValue()+"";
				//根据Key匹配获取对应的值属于哪一列。获取列索引
				Integer valueColumn = valueColumnMap.get(key);
				if(valueColumn==null){
					continue;
				}
				HSSFCell valueCell = valueRow.createCell(valueColumn);
				valueCell.setCellStyle(style);
				valueCell.setCellValue(value);
			}
		}
		mergerow(sheet);
		return workbook;
	}
    /**
     * 合并的行和列
     * */
    public static void   mergerow(HSSFSheet sheet){
    	
    	  
        //合并单元格
    	
    	
        CellRangeAddress region = new CellRangeAddress(//序号
        		0, // first row 
                1, // last row
                0, // first column
                0 // last column
        );
        CellRangeAddress region1 = new CellRangeAddress(//项目工作投入工时统计
        		0, // first row
                1, // last row
                1, // first column
                1 // last column
        );
        
        CellRangeAddress region2 = new CellRangeAddress(//非项目工作投入工时统计
        		0, // first row
                1, // last row
                2, // first column
                2 // last column
        );
        CellRangeAddress region3 = new CellRangeAddress(//投入总工时（h）
        		0, // first row
                0, // last row
                3, // first column
                4 // last column
        );
        CellRangeAddress region4 = new CellRangeAddress(//
        		0, // first row
                0, // last row
                5, // first column
                7 // last column
        );
        /*CellRangeAddress region5 = new CellRangeAddress(//
        		0, // first row
                1, // last row
                7, // first column
                7 // last column
        );
        CellRangeAddress region6 = new CellRangeAddress(//工作饱和度
        		0, // first row
                1, // last row
                8, // first column
                8 // last column
        );*/
       
        sheet.addMergedRegion(region);
        sheet.addMergedRegion(region1); 
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
       /* sheet.addMergedRegion(region5);
        sheet.addMergedRegion(region6);*/
      
        
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    	
		 
		LinkedHashMap<String,String> headermap0 = new LinkedHashMap<>(); 
		headermap0.put("XH", "序号");
		headermap0.put("StartAndEndData", "统计周期");
		headermap0.put("TotalHoursNum", "投入总工时（h）");
		headermap0.put("ProjectTotalHoursNum", "项目工作投入工时统计");
		headermap0.put("ProjectTotalHoursNumBF", "");
		headermap0.put("NoProjectTotalHoursNum", "非项目工作投入工时统计");
		headermap0.put("NoProjectTotalHoursNumBF", "");
		headermap0.put("BZGS", "标准工时");
		headermap0.put("GZBHD", "工作饱和度");
	 
		List<Map<String,String>>  headermaplist=new ArrayList<Map<String,String>>();
		LinkedHashMap<String,String> headermap1 = new LinkedHashMap<>(); 
		headermap1.put("XH", "");
		headermap1.put("StartAndEndData", "");
		headermap1.put("TotalHoursNum", "");
		headermap1.put("ProjectTotalHoursNum", "项目投入总工时（h）");
		headermap1.put("ProjectTotalHoursNumBF", "工时占比（%）");
		headermap1.put("NoProjectTotalHoursNum", "非项目投入总工时（h）");
		headermap1.put("NoProjectTotalHoursNumBF", "工时占比（%）");
		headermap1.put("BZGS", "");
		headermap1.put("GZBHD", "");
		headermaplist.add(headermap1);
		List<Map<String,Object>>  valueList=new ArrayList<Map<String,Object>>();
		 
		 Map<String,Object> valuemap0= new  HashMap<>(); 
		valuemap0.put("XH", "fffff");
		valuemap0.put("StartAndEndData", "fffff");
		valuemap0.put("TotalHoursNum", "fffff");
		valuemap0.put("ProjectTotalHoursNum", "ffff");
		valuemap0.put("ProjectTotalHoursNumBF", "fffff");
		valuemap0.put("NoProjectTotalHoursNum", "ffff");
		valuemap0.put("NoProjectTotalHoursNumBF", "fffff");
		valuemap0.put("BZGS", "fffff");
		valuemap0.put("GZBHD", "ffffff");
		valueList.add(valuemap0);
		//获取Excel数据信息
		HSSFWorkbook workbook = PaddingExcel(headermap0,valueList,headermaplist);
		workbook.write(new FileOutputStream("F:/table6.xls"));
    }
}
