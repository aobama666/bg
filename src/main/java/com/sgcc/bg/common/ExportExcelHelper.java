package com.sgcc.bg.common;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import common.Logger;
public final class ExportExcelHelper {

	private static final Logger log = Logger.getLogger(ExportExcelHelper.class);
  
	
	public static <T> void getExcels(HttpServletResponse response,HSSFWorkbook workbook,String fileName ){
		OutputStream os = null;
		try {
			 
			response.reset();
			response.setContentType("application/x-download");
        	response.setCharacterEncoding("UTF-8"); 
        	response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(fileName,"utf-8")+".xls");
			os = response.getOutputStream();
            workbook.write(os);
            os.flush();
		} catch (Exception e) {
			log.error("getExcel()",e);
			e.printStackTrace();
		} finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static <T> void getExcel(HttpServletResponse response, String fileName, Object[][] title, List<T> dataList,String type){
		OutputStream os = null;
		try {
			HSSFWorkbook workbook=getExcelFile(title, dataList,type);
			response.reset();
			response.setContentType("application/x-download");
        	response.setCharacterEncoding("UTF-8"); 
        	response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(fileName,"utf-8")+".xls");
			os = response.getOutputStream();
            workbook.write(os);
            os.flush();
		} catch (Exception e) {
			log.error("getExcel()",e);
			e.printStackTrace();
		} finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static <T> HSSFWorkbook getExcelFile(Object[][] title, List<T> dataList, String type) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();// 建立工作簿
		try {
			int pageSize = 20000;// 20000条数据分一个sheet页
			// 分页
			int pageCount = new BigDecimal(dataList.size()).divide(new BigDecimal(pageSize), 0, BigDecimal.ROUND_UP)
					.intValue();
			for (int i = 0; i < pageCount || i == 0; i++) {
				HSSFSheet sheet = workbook.createSheet("sheet" + (i + 1));
				int end = (i + 1) * pageSize;
				if ("normal".equals(type)) {
					getSheet(workbook,sheet, title, dataList.subList(i * pageSize, end < dataList.size() ? end : dataList.size()));
				} else {
					getSheet4ErrorExcel(workbook,sheet, title,dataList.subList(i * pageSize, end < dataList.size() ? end : dataList.size()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getExcelFile", e);
		} 
		return workbook;
	}

	private static <T> void getSheet(HSSFWorkbook workbook,HSSFSheet sheet, Object[][] title, List<T> dataList) throws Exception {
		// 获取列头样式对象
		HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);
		// 设置行宽 和 写入表头信息
		HSSFRow rowTitle = sheet.createRow(0);
		rowTitle.setHeight((short) 1000);
		HSSFCell cellTiltle = rowTitle.createCell(0);
		cellTiltle.setCellStyle(columnTopStyle);
		cellTiltle.setCellValue(new HSSFRichTextString("序号"));
		for (int i = 0; i < title.length; i++) {
			cellTiltle = rowTitle.createCell(i+1);
			cellTiltle.setCellStyle(columnTopStyle);
			cellTiltle.setCellValue(new HSSFRichTextString((String) title[i][0]));
		}
		// 写入正文信息
		int i = 1;
		HSSFCellStyle style = getStyle(workbook, "");
		for (Object cbo : dataList) {
			HSSFRow rowText = sheet.createRow(i);
			// 正文数据
			HSSFCell cellText = rowText.createCell(0);
			cellText.setCellStyle(style);
			cellText.setCellValue(i);
			for (int j = 0; j < title.length; j++) {
				cellText = rowText.createCell(j+1);
				cellText.setCellStyle(style);
				cellText.setCellValue(String.valueOf(getBeanValueByMethodName(cbo, (String) title[j][1])));
			}
			i++;
		}
		//设置列宽
		for (int k = 0; k < title.length+1; k++) {
			adjustColumnWidth(sheet, k);
		}
	}

	@SuppressWarnings({ "unchecked"})
	private static <T> void getSheet4ErrorExcel(HSSFWorkbook workbook,HSSFSheet sheet, Object[][] title, List<T> dataList) throws Exception {
		// 获取列头样式对象
		HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);
		// 设置行宽 和 写入表头信息
		HSSFRow rowTitle = sheet.createRow(0);
		rowTitle.setHeight((short) 1000);
		for (int i = 0; i < title.length; i++) {
			HSSFCell cellTiltle = rowTitle.createCell(i);
			cellTiltle.setCellStyle(columnTopStyle);
			cellTiltle.setCellValue(new HSSFRichTextString((String) title[i][0]));
		}
		// 写入正文信息
		int i = 1;
		HSSFCellStyle style = getStyle(workbook, "");
		HSSFCellStyle errorStyle = getStyle(workbook, "err");
		for (Object cbo : dataList) {
			Set<Integer> set = (Set<Integer>) getBeanValueByMethodName(cbo, "errSet");
			HSSFRow rowText = sheet.createRow(i);
			// 正文数据
			for (int j = 0; j < title.length; j++) {
				HSSFCell cellText = rowText.createCell(j);
				if (set.contains(j)) {
					cellText.setCellStyle(errorStyle);
				} else {
					cellText.setCellStyle(style);
				}
				cellText.setCellValue(String.valueOf(getBeanValueByMethodName(cbo, (String) title[j][1])));
			}
			i++;
		}
		//设置列宽
		for (int k = 0; k < title.length; k++) {
			adjustColumnWidth(sheet, k);
			/*if(title[k].length>=3 && "nowrap".equals(title[k][2])){
				adjustColumnWidth(sheet, k);
			}else{
				sheet.autoSizeColumn(k);
			}*/
		}
	}

	/**
	 * 使用反射动态获取bean对象的属性值
	 * @param dto
	 * @param name
	 * @return
	 * @throws Exception
	 */
	private static Object getBeanValueByMethodName(Object dto, String name) throws Exception {
		if(dto instanceof HashMap){
			Map map=(HashMap)dto;
			return map.get(name)== null ? "" : map.get(name);
		}
		Method[] m = dto.getClass().getMethods();
		for (int i = 0; i < m.length; i++) {
			if (("get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()))
					.equals(m[i].getName())) {
				return m[i].invoke(dto) == null ? "" : m[i].invoke(dto);
			}
		}
		
		log.error("There is not the property of '" + name + "' in " + dto.getClass().toString());
		Exception e = new Exception();
		throw new RuntimeException(e);
	}

	/**
	 * 创建错误数据excel
	 * @param path 出错信息存放路径
	 * @param title
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	public static <T> String createErrorExcel(String path, Object[][] title, List<T> dataList) throws Exception {
/*		String errorexcelPath = path + "errorexcel" + File.separator;
		File errorfilePath = new File(errorexcelPath);
		if (!errorfilePath.isDirectory()) {
			errorfilePath.mkdirs();
		}*/
//		File file = new File(errorexcelPath + uuid + ".xls");
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		HSSFWorkbook workbook=getExcelFile(title, dataList, "error");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);        
		byte[] content = os.toByteArray();
        InputStream in = new ByteArrayInputStream(content);
        FtpUtils.uploadFile(in,path+uuid+".xls");
		return uuid;
	}

	/*
	 * 列头单元格样式
	 */
	@SuppressWarnings("deprecation")
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
		font.setFontHeightInPoints((short)10);
		// 字体加粗
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
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
		style.setWrapText(true);
		// 设置水平对齐的样式为居中对齐;
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 设置垂直对齐的样式为居中对齐;
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		return style;
	}
	
	public static String toUtf8String(String s){ 
	     StringBuffer sb = new StringBuffer(); 
	       for (int i=0;i<s.length();i++){ 
	          char c = s.charAt(i); 
	          if (c >= 0 && c <= 255){sb.append(c);} 
	        else{ 
	        byte[] b; 
	         try { b = Character.toString(c).getBytes("utf-8");} 
	         catch (Exception ex) { 
	             //System.out.println(ex); 
	                  b = new byte[0]; 
	         } 
	            for (int j = 0; j < b.length; j++) { 
	             int k = b[j]; 
	              if (k < 0) k += 256; 
	              sb.append("%" + Integer.toHexString(k).toUpperCase()); 
	              } 
	     } 
	  } 
	  return sb.toString(); 
	}
	
	/**
	 * 调整某一列的列宽
	 * @param sheet
	 * @param colNum
	 */
	@SuppressWarnings("deprecation")
	private static void adjustColumnWidth(HSSFSheet sheet,int colNum){
		String reg="[!#$%&'()*+,-./:;<=>?@\\^_`{|}~a-zA-Z0-9]";
		int columnWidth = sheet.getColumnWidth(colNum) / 256;
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
			HSSFRow currentRow;
			// 当前行未被使用过
			if (sheet.getRow(rowNum) == null) {
				currentRow = sheet.createRow(rowNum);
			} else {
				currentRow = sheet.getRow(rowNum);
			}
			if (currentRow.getCell(colNum) != null) {
				HSSFCell currentCell = currentRow.getCell(colNum);
				if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					String str=currentCell.getStringCellValue();
					String[] strArr=str.split("\r\n");
					for (String string : strArr) {
						int length = string.getBytes().length+getRegexCount(reg,string);//为单字节字符增加部分宽度
						columnWidth = Math.max(columnWidth, length);
						columnWidth = columnWidth>50?50:columnWidth;
					}
				}
				
			}
		}
		sheet.setColumnWidth(colNum, columnWidth * 256);
		/*if (colNum == 0) {
			sheet.setColumnWidth(colNum, columnWidth * 256>25000?25000:columnWidth* 256);
		} else {
			sheet.setColumnWidth(colNum, columnWidth* 256>25000?25000:columnWidth* 256);
		}*/
	}
	
	/**
	 * 获取匹配正则的数量
	 * @param reg
	 * @param str
	 * @return
	 */
	private static int getRegexCount(String reg,String str){
		Pattern p=Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
		Matcher m=p.matcher(str);
		int count=0;
		while(m.find()){
			count++;
		}
		return count;
	}

}
