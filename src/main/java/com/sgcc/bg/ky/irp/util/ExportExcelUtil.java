package com.sgcc.bg.ky.irp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class ExportExcelUtil {
	/**
	 * @description HttpServletResponse对象
	 */
	private HttpServletResponse response;
	/**
	 * @description 文件名称
	 */
	private String fileName;
	/**
	 * @description 工作簿名称
	 */
	private String sheetName;
	/**
	 * @description 工作簿标题列名称
	 */
	private String[] sheetColsTitleName;
	/**
	 * @description 工作簿数据列字段名称
	 */
	private String[] sheetColsField;
	/**
	 * @description 工作簿数据
	 */
	private List<Map<String,Object>> sheetColsDataList;
	/**
	 * @description 工作簿名称 多个
	 */
	private List<String> sheetNameList; 
	/**
	 * @description 工作簿标题列名称 多个
	 */
	private List<List<String[]>> sheetColsTitleNameList;
	/**
	 * @description 工作簿数据列字段名称 多个
	 */
	private List<String[]> sheetColsFieldList;
	/**
	 * @description 工作簿数据 多个
	 */
	private List<List<Map<String,Object>>> sheetsColsDataList;
	/**
	 * @description 创建工作簿对象
	 */ 
	private	HSSFWorkbook workbook;
	
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}
	/**
	 * @description 导出单个工作簿
	 * @param fileName 文件名 
	 * @param sheetColsTitleName 工作簿标题列名称
	 * @param sheetColsField 工作簿数据列字段名称
	 * @param sheetColsDataList 工作簿数据
	 * @param response HttpServletResponse对象
	 */
	public ExportExcelUtil(String fileName,String[] sheetColsTitleName,String[] sheetColsField,List<Map<String,Object>> sheetColsDataList,HttpServletResponse response) {
		this.fileName = fileName;
		this.sheetColsTitleName = sheetColsTitleName;
		this.sheetColsField = sheetColsField;
		this.sheetColsDataList = sheetColsDataList;
		this.response = response;
	}
	/**
	 * @description 导出单个工作簿
	 * @param fileName 文件名 
	 * @param sheetName 工作簿名称
	 * @param sheetColsTitleName 工作簿标题列名称
	 * @param sheetColsField 工作簿数据列字段名称
	 * @param sheetColsDataList 工作簿数据
	 * @param response HttpServletResponse对象
	 */
	public ExportExcelUtil(String fileName,String sheetName,String[] sheetColsTitleName,String[] sheetColsField,List<Map<String,Object>> sheetColsDataList,HttpServletResponse response) {
		this.fileName = fileName;
		this.sheetName = sheetName;
		this.sheetColsTitleName = sheetColsTitleName;
		this.sheetColsField = sheetColsField;
		this.sheetColsDataList = sheetColsDataList;
		this.response = response;
	}
	/**
	 * @description 导出多个工作簿
	 * @param fileName 文件名 
	 * @param sheetColsTitleName 工作簿标题列名称
	 * @param sheetColsField 工作簿数据列字段名称
	 * @param sheetColsDataList 工作簿数据
	 * @param response HttpServletResponse对象
	 */
	public ExportExcelUtil(String fileName,List<List<String[]>> sheetColsTitleNameList,List<String[]> sheetColsFieldList,List<List<Map<String,Object>>> sheetsColsDataList,HttpServletResponse response) {
		this.fileName = fileName;
		this.sheetColsTitleNameList = sheetColsTitleNameList;
		this.sheetColsFieldList = sheetColsFieldList;
		this.sheetsColsDataList = sheetsColsDataList;
		this.response = response;
	}
	/**
	 * @description 导出多个工作簿
	 * @param fileName 文件名 
	 * @param sheetName 工作簿名称
	 * @param sheetColsTitleName 工作簿标题列名称
	 * @param sheetColsField 工作簿数据列字段名称
	 * @param sheetColsDataList 工作簿数据
	 * @param response HttpServletResponse对象
	 */
	public ExportExcelUtil(String fileName,List<String> sheetNameList,List<List<String[]>> sheetColsTitleNameList,List<String[]> sheetColsFieldList,List<List<Map<String,Object>>> sheetsColsDataList,HttpServletResponse response) {
		this.fileName = fileName;
		this.sheetNameList = sheetNameList;
		this.sheetColsTitleNameList = sheetColsTitleNameList;
		this.sheetColsFieldList = sheetColsFieldList;
		this.sheetsColsDataList = sheetsColsDataList;
		this.response = response;
	}
	/**
	 * 
	 * @description 导出单个工作簿明细
	 * @throws Exception
	 */
	public void exportSheet() throws Exception {
		/*********************创建工作簿 *************************/
		// 创建工作簿对象
		workbook = new HSSFWorkbook(); 
		// 创建工作表
		HSSFSheet sheet = null; 
		if(null!=sheetName&&sheetName.length()>0){
			sheet = workbook.createSheet(sheetName); 
		}else{
			sheet = workbook.createSheet(); 
		}
		
		createSheet(workbook,sheet,true,true);
		
		writeSheet();
	}
	
	/**
	 * 
	 * @description 导出单个工作簿明细
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列  true 自动生成
	 * @param isDefultBgColor 是否生成背景 true 生成默认背景
	 * @throws Exception
	 */
	public void exportSheet(boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		/*********************创建工作簿 *************************/
		// 创建工作簿对象
		workbook = new HSSFWorkbook(); 
		// 创建工作表
		HSSFSheet sheet = null; 
		if(null!=sheetName&&sheetName.length()>0){
			sheet = workbook.createSheet(sheetName); 
		}else{
			sheet = workbook.createSheet(); 
		}
		
		createSheet(workbook,sheet,isCreateIndex,isDefultBgColor);
		
		writeSheet();
	}
	/**
	 * 
	 * @description 导出多个工作簿明细
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列  true 自动生成
	 * @param isDefultBgColor 是否生成背景 true 生成默认背景
	 * @throws Exception
	 */
	public void exportSheets(boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		/*********************创建工作簿 *************************/
		// 创建工作簿对象
		workbook = new HSSFWorkbook(); 
		for(int i=0;i<sheetNameList.size();i++){
			String sheetNameTmp = sheetNameList.get(i);
			// 创建工作表
			HSSFSheet sheet = workbook.createSheet(sheetNameTmp); 
			
			List<String[]> sheetColsTitleName = sheetColsTitleNameList.get(i);
			String[] sheetColsField = sheetColsFieldList.get(i);
			List<Map<String,Object>> sheetColsDataList = sheetsColsDataList.get(i);
			
			createSheets(workbook,sheet,sheetColsTitleName,sheetColsField,sheetColsDataList,isCreateIndex,isDefultBgColor);
		}
		writeSheet();
	}
	/**
	 * 
	 * @description 导出单个工作簿
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列
	 * @param isDefultBgColor 是否生成背景
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private void createSheet(HSSFWorkbook workbook,HSSFSheet sheet,boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		try {
			/*********************创建标题行*************************/
			//获取标题列样式
			HSSFCellStyle columnStyle = getColumnStyle(workbook,isDefultBgColor);
			//获取列长			
			int columnNum = sheetColsTitleName.length;
			//在索引0的位置创建行
			HSSFRow firstLine = sheet.createRow(0); 
			firstLine.setHeight((short) 450);
			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				// 创建列头对应个数的单元格
				HSSFCell cellRowName = firstLine.createCell(n); 
				// 设置列头单元格的数据类型
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); 
				HSSFRichTextString text = new HSSFRichTextString(sheetColsTitleName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnStyle); // 设置列头单元格样式
			}
			/*********************创建数据行*************************/
			//获取单元格样式
			HSSFCellStyle style = getStyle(workbook); 
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < sheetColsDataList.size(); i++) {
				// 创建所需的行数
				HSSFRow row = sheet.createRow(i + 1);
				// 遍历每个对象
				Map<String,Object> obj = sheetColsDataList.get(i);
				for (int j = 0; j < sheetColsField.length; j++) {
					String field = sheetColsField[j];
					// 创建对应个数的单元格
					HSSFCell cell = row.createCell(j); 	
					// 设置单元格的数据类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
					// 设置单元格的值
					String text = "";	
					if(isCreateIndex){
						if (j == 0) {
							text =  "" + (i + 1);
						} else {						
							text = obj.get(field) == null?"":obj.get(field).toString();
						}	
					}
					else{
						text = obj.get(field) == null?"":obj.get(field).toString();
					}
					
					cell.setCellValue(text); 					
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			/*********************整体样式调整*************************/
			// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow = null;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = 0;
							try {
								length = currentCell.getStringCellValue().getBytes().length;
							} catch (Exception e) {
								continue;
							}
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 255>30000?30000:(columnWidth - 2) * 255);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 255>30000?30000:(columnWidth + 4) * 255);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description 导出单个工作簿明细
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列  true 自动生成
	 * @param isDefultBgColor 是否生成背景 true 生成默认背景
	 * @throws Exception
	 */
	public void createSheetForDeptTotal(boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		/*********************创建工作簿 *************************/
		// 创建工作簿对象
		workbook = new HSSFWorkbook(); 
		// 创建工作表
		HSSFSheet sheet = null; 
		if(null!=sheetName&&sheetName.length()>0){
			sheet = workbook.createSheet(sheetName); 
		}else{
			sheet = workbook.createSheet(); 
		}
		
		createSheet2(workbook,sheet,isCreateIndex,isDefultBgColor);
		
		writeSheet();
	}
	/**
	 * 
	 * @description 导出单个工作簿
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列
	 * @param isDefultBgColor 是否生成背景
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private void createSheet2(HSSFWorkbook workbook,HSSFSheet sheet,boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		try {
			/*********************创建标题行*************************/
			//获取标题列样式
			HSSFCellStyle columnStyle = getColumnStyle(workbook,isDefultBgColor);
			//获取列长			
			int columnNum = sheetColsTitleName.length;
			//在索引0的位置创建行
			HSSFRow firstLine = sheet.createRow(0); 
			firstLine.setHeight((short) 1600);
			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				// 创建列头对应个数的单元格
				HSSFCell cellRowName = firstLine.createCell(n); 
				// 设置列头单元格的数据类型
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); 
				HSSFRichTextString text = new HSSFRichTextString(sheetColsTitleName[n]);
				cellRowName.setCellValue(text); // 设置列头单元格的值
				cellRowName.setCellStyle(columnStyle); // 设置列头单元格样式
			}
			/*********************创建数据行*************************/
			//获取单元格样式
			HSSFCellStyle style = getStyle(workbook); 
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < sheetColsDataList.size(); i++) {
				// 创建所需的行数
				HSSFRow row = sheet.createRow(i + 1);
				// 遍历每个对象
				Map<String,Object> obj = sheetColsDataList.get(i);
				for (int j = 0; j < sheetColsField.length; j++) {
					String field = sheetColsField[j];
					// 创建对应个数的单元格
					HSSFCell cell = row.createCell(j); 	
					// 设置单元格的数据类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
					// 设置单元格的值
					String text = "";	
					if(isCreateIndex){
						if (j == 0) {
							text =  "" + (i + 1);
						} else {						
							text = obj.get(field) == null?"":obj.get(field).toString();
						}	
					}
					else{
						text = obj.get(field) == null?"":obj.get(field).toString();
					}
					
					cell.setCellValue(text); 					
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			/*********************整体样式调整*************************/
			// 让列宽随着导出的列长自动适应
			/*for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow = null;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = 0;
							try {
								length = currentCell.getStringCellValue().getBytes().length;
							} catch (Exception e) {
								continue;
							}
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 255>30000?30000:(columnWidth - 2) * 255);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 255>30000?30000:(columnWidth + 4) * 255);
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description 导出多个工作簿
	 * @param isCreateIndex 是否生成行号，此时需要标题内定义序号列
	 * @param isDefultBgColor 是否生成背景
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private void createSheets(HSSFWorkbook workbook,HSSFSheet sheet,List<String[]> sheetColsTitleNameList,String[] sheetColsField,List<Map<String,Object>> sheetColsDataList,boolean isCreateIndex,boolean isDefultBgColor) throws Exception {
		try {
			/*********************创建标题行*************************/
			//获取标题列样式
			HSSFCellStyle columnStyle = getColumnStyle(workbook,isDefultBgColor);
			int startCol = 0;
			String sheetName = sheet.getSheetName();
			if(sheetName.equals("各单位完成值统计")){
				sheet.setColumnWidth(1, "列名列名".getBytes().length*2*256);
			}else if(sheetName.equals("全院知识产权完成值统计")){
				sheet.setColumnWidth(0, "列名列名".getBytes().length*2*256);
			}
			
			if(sheetName.equals("各单位完成值统计")){
				//合并
				String[] colsMegr = sheetColsTitleNameList.get(0);
				int v = 0;
				boolean isCountStart = true;
				for(int i=0;i<colsMegr.length;i++){
					String nameTemp = colsMegr[i];
					if(isCountStart){
						startCol = i+1;
					}
					if(nameTemp!=null&&nameTemp.trim().length()>0){
						isCountStart = false;
						// 合并单元格  
				        CellRangeAddress cra =new CellRangeAddress(0, 0, startCol+8*v-1, startCol+8*v+7-1); // 起始行, 终止行, 起始列, 终止列  
				        sheet.addMergedRegion(cra);  
				        // 使用RegionUtil类为合并后的单元格添加边框  
				        RegionUtil.setBorderBottom(1, cra, sheet); // 下边框  
				        RegionUtil.setBorderLeft(1, cra, sheet); // 左边框  
				        RegionUtil.setBorderRight(1, cra, sheet); // 有边框  
				        RegionUtil.setBorderTop(1, cra, sheet); // 上边框  
				        v++;
					}
				}
			}
			
			//获取列长			
			int columnNum = 0;
			int rowIndex = 0;
			//在索引0的位置创建行
			for(int i=0;i<sheetColsTitleNameList.size()-1;i++){
				String[] sheetColsTitleName =  sheetColsTitleNameList.get(i);
				columnNum = sheetColsTitleName.length;
				HSSFRow titleLine = sheet.createRow(rowIndex); 
				titleLine.setHeight((short) 450);
				// 将列头设置到sheet的单元格中
				for (int n = 0; n < columnNum; n++) {
					// 创建列头对应个数的单元格
					HSSFCell cellRowName = null;
					if(sheet.getSheetName().equals("各单位完成值统计")&&startCol-1<=n){
						int cellNum = startCol+(n-(startCol-1))*8-1;
						cellRowName = titleLine.createCell(cellNum); 
					}
					else{
						cellRowName = titleLine.createCell(n); 
					}
					// 设置列头单元格的数据类型
					String name = sheetColsTitleName[n];
					cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); 
					HSSFRichTextString text = new HSSFRichTextString(name);
					cellRowName.setCellValue(text); // 设置列头单元格的值
					cellRowName.setCellStyle(columnStyle); // 设置列头单元格样式
				}
				rowIndex++;
			}
			
			//在索引0的位置创建行
			for(int i=1;i<sheetColsTitleNameList.size();i++){
				String[] sheetColsTitleName =  sheetColsTitleNameList.get(i);
				columnNum = sheetColsTitleName.length;
				HSSFRow titleLine = sheet.createRow(rowIndex); 
				titleLine.setHeight((short) 1000);
				// 将列头设置到sheet的单元格中
				for (int n = 0; n < columnNum; n++) {
					// 创建列头对应个数的单元格
					HSSFCell cellRowName = titleLine.createCell(n); 
					// 设置列头单元格的数据类型
					String name = sheetColsTitleName[n];
					cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); 
					HSSFRichTextString text = new HSSFRichTextString(name);
					cellRowName.setCellValue(text); // 设置列头单元格的值
					cellRowName.setCellStyle(columnStyle); // 设置列头单元格样式
				}
				rowIndex++;
			}
			/*********************创建数据行*************************/
			//获取单元格样式
			HSSFCellStyle style = getStyle(workbook); 
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < sheetColsDataList.size(); i++) {
				// 创建所需的行数
				HSSFRow row = sheet.createRow(i + rowIndex);
				// 遍历每个对象
				Map<String,Object> obj = sheetColsDataList.get(i);
				for (int j = 0; j < sheetColsField.length; j++) {
					String field = sheetColsField[j];
					// 创建对应个数的单元格
					HSSFCell cell = row.createCell(j); 	
					// 设置单元格的数据类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
					// 设置单元格的值
					String text = "";	
					if(isCreateIndex){
						if (j == 0) {
							text =  "" + (i + 1);
						} else {						
							text = obj.get(field) == null?"":obj.get(field).toString();
						}	
					}
					else{
						text = obj.get(field) == null?"":obj.get(field).toString();
					}
					
					cell.setCellValue(text); 					
					cell.setCellStyle(style); // 设置单元格样式
				}
			}
			/*********************整体样式调整*************************/
			// 让列宽随着导出的列长自动适应
			/*for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 1 ; rowNum <= sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow = null;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = 0;
							try {
								length = currentCell.getStringCellValue().getBytes().length;
							} catch (Exception e) {
								continue;
							}
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 255>30000?30000:(columnWidth - 2) * 255);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 255>30000?30000:(columnWidth + 4) * 255);
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeSheet(){
		/*********************导出数据*************************/
		if (workbook != null) {
			OutputStream out = null;
			try {
				String fileNameExport = fileName +"_"+ String.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
				response.setContentType("APPLICATION/OCTET-STREAM");
				response.setHeader("Content-Disposition", "attachment; filename= " + URLEncoder.encode(fileNameExport, "UTF-8") + ".xls");
				out = response.getOutputStream();
				workbook.write(out);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(workbook!=null){
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @description 列头单元格样式
	 * @param workbook
	 * @param isExistbackColor  是否生成背景
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private HSSFCellStyle getColumnStyle(HSSFWorkbook workbook,boolean isDefultBgColor) {

		// 设置字体
		HSSFFont font = workbook.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 11);
		// 字体加粗
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体名字
		font.setFontName("Courier New");
		// 设置样式;
		HSSFCellStyle style = workbook.createCellStyle();
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
		if(isDefultBgColor){
			style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);//列名行背景颜色
		}
		else{
			style.setFillForegroundColor(HSSFColor.WHITE.index);//列名行背景颜色
		}
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setWrapText(true);//自动幻皇

		return style;

	}	

	/**
	 * 
	 * @description 列数据信息单元格样式
	 * @param workbook
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
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
}
