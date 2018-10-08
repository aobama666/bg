package com.sgcc.bg.ky.irp.service.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.ky.irp.mapper.IrpCountMapper;
import com.sgcc.bg.ky.irp.service.IrpCountService;
import com.sgcc.bg.ky.irp.util.ExportExcelUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class IrpCountServiceImpl implements IrpCountService{

	@Autowired
	private IrpCountMapper irpCountMapper;
	@Autowired
	private WebUtils webUtils; 
	
	public List<Map<String, Object>> queryIrpDataCountForAll(String countMonth) {

		List<Map<String, Object>> dataList = getDataCountForAll(countMonth);
		
		return dataList;
	}

	public List<Map<String, Object>> queryDeptIrpDataCountForAll(String countMonth) {
		
		List<Map<String, Object>> dataList = getDeptDataCountForAll(countMonth);
		
		return dataList;
	}
	
	public List<Map<String, Object>> queryIrpDataCountForDept(HttpServletRequest request) {
		
		List<Map<String, Object>> dataList = getIrpDataCountForDept(request);
		
		return dataList;
	}
	
	public void exportIrpTotal(HttpServletRequest request,HttpServletResponse response) {
		//模板类型
		String mType=request.getParameter("mType");
		if(mType==null){
			return;
		}
		/*****************************导出word**********************************/
		//科技部  总计   word
		if(mType.equals("irpT01")){
			exportWord(request, response);		
			return;
		}
		//科技部  总计   excel
		if(mType.equals("irpT02")){
			exporExceltDataCountForAll(request, response);		
			return;
		}
		/*****************************导出excel 各部门统计数据**********************************/
		if(mType.equals("irpT03")){
			exportDeptDataCountExcel(request, response);
			return;
		}
		/*****************************导出excel 专利、论文、软著、专著、海外专利明细**********************************/
		if(mType.equals("irpD01")||mType.equals("irpD02")||mType.equals("irpD03")
				||mType.equals("irpD04")||mType.equals("irpD05")||mType.equals("irpD06")){
			exportDetailExcel(request, response);	
			return;
		}
	}	
	/**
	 * @desciption 获取统计数据 全院  科技部
	 * @param curDate  统计时间
	 * @return
	 */
	private List<Map<String, Object>> getDataCountForAll(String curDate){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try{
			/***********************************定义固定行属性***************************************/
			StringBuffer rowStr = new StringBuffer();
			rowStr.append("PATENS_APPLY-申请专利-项,");
			rowStr.append("PATENS_APPLY_INVENT-申请发明专利-项,");
			rowStr.append("PATENS_ACCNOTI-专利授权-项,");
			rowStr.append("PATENS_ACCNOTI_INVENT-发明专利授权-项,");
			rowStr.append("OUT_PATENS-海外专利申请-项,");
			rowStr.append("SOFTCPR-登记软件著作权-项,");
			rowStr.append("PAPER-科技论文-篇,");
			rowStr.append("MONOGRAPH-科技专著-部");
			
			StringBuffer filed = new StringBuffer();
			String[] rowArr = rowStr.toString().split(",");
			for(int i=0;i<rowArr.length;i++){
				String[] temp = rowArr[i].split("-");
				String seq = ""+(i+1);
				if((i+1)<10){
					seq = "0" + (i+1);
				}
				//filed.append("ROW_ID==PATENS_APPLY-SORT_ID==1-ITEM_NAME==申请专利-ITEM_UNIT==项-ITEM_MONTH_VALUE=MONTH01-ITEM_YEAR_VALUE=YEAR01,");
				filed.append("ROW_ID").append("==").append(temp[0]);
				filed.append("-").append("SORT_ID").append("==").append(i+1);
				filed.append("-").append("ITEM_NAME").append("==").append(temp[1]);
				filed.append("-").append("ITEM_UNIT").append("==").append(temp[2]);
				filed.append("-").append("ITEM_MONTH_VALUE").append("=").append("MONTH").append(seq);
				filed.append("-").append("ITEM_YEAR_VALUE").append("=").append("YEAR").append(seq);
				if(i<rowArr.length-1){
					filed.append(","); 
				}
			}
			/***********************************定义查询参数***************************************/
			//当前年
			String curYear = curDate.substring(0, curDate.indexOf("-"));
			//去年12月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = new Date();
			try {
				date = sdf.parse(curDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, -1);
			Date lastYearDate = calendar.getTime();
			String lastYearDateString = sdf.format(lastYearDate);
			String lastDate = lastYearDateString.substring(0, lastYearDateString.indexOf("-"))+"-12";		
			//本年起始月
			String startDate = curYear+"-01";
			/***********************************获取数据结果***************************************/
			//数据 
			List<Map<String, Object>> list = irpCountMapper.getDataTotalForAll(curDate, startDate, curYear, lastDate);
			Map<String, Object> map = new HashMap<String, Object>();
			if(list!=null){
				for(int k=0;k<list.size();k++){
					Map<String, Object> m = list.get(k);
					String item_name = m.get("ITEM_NAME").toString();
					String item_count = m.get("ITEM_COUNT").toString();
					map.put(item_name, item_count);
				}
			}
			
			String[] rowLine  = filed.toString().split(",");
			for(int i=0;i<rowArr.length;i++){
				String tmp = rowLine[i];
				Map<String, Object> one = formatDateCountListForAll(map, tmp);
				result.add(one);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * @desciption 解析数据行 
	 * @param map  数据值
	 * @param filed 列表行字段
	 * @return
	 */
	private Map<String, Object> formatDateCountListForAll(Map<String,Object> map,String filed) {
		Map<String, Object> result = new HashMap<>();			
		String[] filedArr = filed.split(",");
		for(int i=0;i<filedArr.length;i++){
			String[] oField = filedArr[i].split("-");
			for(int k=0;k<oField.length;k++){
				String vField = oField[k];
				if(vField.indexOf("==")!=-1){//A==a A:字段名  a:字段值，需要充map中获取
					String[] arr = vField.split("==");
					result.put(arr[0], arr[1]);
				}else if(vField.indexOf("=")!=-1){//A=a A:字段名  a:字段值
					String[] arr = vField.split("=");
					result.put(arr[0], map.get(arr[1]));
				}
			}
		}
		return result;
	}
	/**
	 * @desciption  获取统计数据 全院-部门  科技部
	 * @param curDate  统计时间
	 * @return
	 */
	private List<Map<String, Object>> getDeptDataCountForAll(String curDate){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try{
			/***********************************定义固定行属性***************************************/
			StringBuffer rowStr = new StringBuffer();
			rowStr.append("PATEN01-系统所,");
			rowStr.append("PATEN02-高压所,");
			rowStr.append("PATEN03-电自所,");
			rowStr.append("PATEN04-保护所,");
			rowStr.append("PATEN05-工程所,");
			rowStr.append("PATEN06-信通所,");
			rowStr.append("PATEN09-计量所,");
			rowStr.append("PATEN07-配电所,");
			rowStr.append("PATEN08-用能所,");
			rowStr.append("PATEN10-新能源中心,");
			rowStr.append("PATEN11-电工所,");
			rowStr.append("PATEN12-战略中心,");
			rowStr.append("PATEN13-武汉检测中心,");
			rowStr.append("PATEN14-电网质监中心,");
			rowStr.append("PATEN15-其他,");
			rowStr.append("PATEN16-全院总计");
			
			StringBuffer filed = new StringBuffer();
			String[] rowArr = rowStr.toString().split(",");
			for(int i=0;i<rowArr.length;i++){
				String[] temp = rowArr[i].split("-");				
//				filed.append("ROW_ID==PATEN01-SORT_ID==1-ITEM_NAME==系统所-ITEM_PATENS=PATEN01-ITEM_PATENS_YEAR=YPATEN01-ITEM_INVENT=INVENT01-ITEM_INVENT_YEAR=YINVENT01,");
				filed.append("ROW_ID").append("==").append(temp[0]);
				filed.append("-").append("SORT_ID").append("==").append(i+1);
				filed.append("-").append("ITEM_NAME").append("==").append(temp[1]);
				
				filed.append("-").append("ITEM_PATENS").append("=").append("MONTH01");
				filed.append("-").append("ITEM_PATENS_INVENT").append("=").append("MONTH02");
				filed.append("-").append("ITEM_ACC_PATENS").append("=").append("MONTH03");
				filed.append("-").append("ITEM_ACC_PATENS_INVENT").append("=").append("MONTH04");
				filed.append("-").append("ITEM_OUT_PATENS").append("=").append("MONTH05");
				filed.append("-").append("ITEM_SOFTCPR").append("=").append("MONTH06");
				filed.append("-").append("ITEM_PAPER").append("=").append("MONTH07");
				filed.append("-").append("ITEM_MONOGRAPH").append("=").append("MONTH08");
				
				filed.append("-").append("ITEM_PATENS_YEAR").append("=").append("YEAR01");
				filed.append("-").append("ITEM_PATENS_INVENT_YEAR").append("=").append("YEAR02");
				filed.append("-").append("ITEM_ACC_PATENS_YEAR").append("=").append("YEAR03");
				filed.append("-").append("ITEM_ACC_PATENS_INVENT_YEAR").append("=").append("YEAR04");
				filed.append("-").append("ITEM_OUT_PATENS_YEAR").append("=").append("YEAR05");
				filed.append("-").append("ITEM_SOFTCPR_YEAR").append("=").append("YEAR06");
				filed.append("-").append("ITEM_PAPER_YEAR").append("=").append("YEAR07");
				filed.append("-").append("ITEM_MONOGRAPH_YEAR").append("=").append("YEAR08");
				if(i<rowArr.length-1){
					filed.append(",");
				}
			}
			
			/***********************************定义查询参数***************************************/
			//当前年
			String curYear = curDate.substring(0, curDate.indexOf("-"));
			//去年12月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = new Date();
			try {
				date = sdf.parse(curDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, -1);
			Date lastYearDate = calendar.getTime();
			String lastYearDateString = sdf.format(lastYearDate);
			String lastDate = lastYearDateString.substring(0, lastYearDateString.indexOf("-"))+"-12";		
			//本年起始月
			String startDate = curYear+"-01";
			//单位列表
			List<Map<String, Object>> deptList = irpCountMapper.getDeptList();
			/***********************************获取数据结果***************************************/
			String[] line = filed.toString().split(",");
			//统计合计值
			int MONTH01 = 0;
			int MONTH02 = 0;
			int MONTH03 = 0;
			int MONTH04 = 0;
			int MONTH05 = 0;
			int MONTH06 = 0;
			int MONTH07 = 0;
			int MONTH08 = 0;
			
			int YEAR01 = 0;
			int YEAR02 = 0;
			int YEAR03 = 0;
			int YEAR04 = 0;
			int YEAR05 = 0;
			int YEAR06 = 0;
			int YEAR07 = 0;
			int YEAR08 = 0;
			for(int i=0;i<rowArr.length;i++){
				String[] temp = rowArr[i].split("-");		
				String deptCode = temp[0];
				//合计行
				if(deptCode.equals("PATEN16")){
					continue;
				}
				
				String DFlag = "dept";
				String deptId = "other";
				//其他行   非业务部门
				if(deptCode.equals("PATEN15")){
					DFlag = "other";
				}
				else{	
					//获取部门id
					for(int k=0;k<deptList.size();k++){
						Map<String, Object> objMap = deptList.get(k);
						String code = objMap.get("CODENAME").toString();
						String val = objMap.get("DEPTID").toString();
						if(code.equals(deptCode)){
							deptId = val;
							break;
						}
					}
				}
				
				List<Map<String, Object>> list = irpCountMapper.getDataTotalForAllByMonth(curDate, startDate, curYear, lastDate, DFlag, deptId);
				Map<String, Object> map = new HashMap<String, Object>();
				if(list!=null){
					for(int k=0;k<list.size();k++){
						Map<String, Object> m = list.get(k);
						String item_name = m.get("ITEM_NAME").toString();
						String item_count = m.get("ITEM_COUNT").toString();
						map.put(item_name, item_count);
					}
					MONTH01 += Integer.valueOf(map.get("MONTH01").toString());
					MONTH02 += Integer.valueOf(map.get("MONTH02").toString());
					MONTH03 += Integer.valueOf(map.get("MONTH03").toString());
					MONTH04 += Integer.valueOf(map.get("MONTH04").toString());
					MONTH05 += Integer.valueOf(map.get("MONTH05").toString());
					MONTH06 += Integer.valueOf(map.get("MONTH06").toString());
					MONTH07 += Integer.valueOf(map.get("MONTH07").toString());
					MONTH08 += Integer.valueOf(map.get("MONTH08").toString());
					
					YEAR01 += Integer.valueOf(map.get("YEAR01").toString());
					YEAR02 += Integer.valueOf(map.get("YEAR02").toString());
					YEAR03 += Integer.valueOf(map.get("YEAR03").toString());
					YEAR04 += Integer.valueOf(map.get("YEAR04").toString());
					YEAR05 += Integer.valueOf(map.get("YEAR05").toString());
					YEAR06 += Integer.valueOf(map.get("YEAR06").toString());
					YEAR07 += Integer.valueOf(map.get("YEAR07").toString());
					YEAR08 += Integer.valueOf(map.get("YEAR08").toString());
				}
				
				Map<String, Object> one = formatDateCountListForAll(map, line[i]);
				result.add(one);
			}
			
			//合计行
			Map<String, Object> lastMap = new HashMap<String, Object>();
			lastMap.put("ROW_ID", "PATEN16");
			lastMap.put("SORT_ID", "16");
			lastMap.put("ITEM_NAME", "全院总计");
			lastMap.put("ITEM_PATENS",MONTH01);
			lastMap.put("ITEM_PATENS_INVENT",MONTH02);
			lastMap.put("ITEM_ACC_PATENS",MONTH03);
			lastMap.put("ITEM_ACC_PATENS_INVENT",MONTH04);
			lastMap.put("ITEM_OUT_PATENS",MONTH05);
			lastMap.put("ITEM_SOFTCPR",MONTH06);
			lastMap.put("ITEM_PAPER",MONTH07);
			lastMap.put("ITEM_MONOGRAPH",MONTH08);
			
			lastMap.put("ITEM_PATENS_YEAR",YEAR01);
			lastMap.put("ITEM_PATENS_INVENT_YEAR",YEAR02);
			lastMap.put("ITEM_ACC_PATENS_YEAR",YEAR03);
			lastMap.put("ITEM_ACC_PATENS_INVENT_YEAR",YEAR04);
			lastMap.put("ITEM_OUT_PATENS_YEAR",YEAR05);
			lastMap.put("ITEM_SOFTCPR_YEAR",YEAR06);
			lastMap.put("ITEM_PAPER_YEAR",YEAR07);
			lastMap.put("ITEM_MONOGRAPH_YEAR",YEAR08);
			
			result.add(lastMap);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * @description 科技部  总计   word
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	private void exportWord(HttpServletRequest request,HttpServletResponse response){
		//模板类型
		String mType=request.getParameter("mType");
		if(mType==null||!mType.equals("irpT01")){
			return;
		}
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		
		Map<String,Object> map = getExportWordDataCountForAllMap(countMonth);
		
		String fileName = "知识产权全院数据统计-"+countMonth+"_"+String.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))+".doc";//文件名称
		String templateName = "irpDataTotal.ftl";
		
		OutputStream os = null;
		Writer out = null;
		
		try {
			//创建配置实例
			Configuration con = null ;
			con = new Configuration();
			
			//设置编码
			con.setDefaultEncoding("UTF-8");
			//ftp模板文件所在位置
			con.setClassForTemplateLoading(IrpCountServiceImpl.class, "/freemark/ky/irp/");
			//获取模板
			Template template = con.getTemplate(templateName);
			
			os = response.getOutputStream();
			
			response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName,"UTF-8")+"");
			
			//将模板和数据模型合并生成文件
			out = new BufferedWriter(new OutputStreamWriter(os,"utf-8"));
			//生成文件
			template.process(map, out);
			//关闭流
			out.flush();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * @description 获取统计数据  导出Word 
	 * @param curDate  统计时间
	 * @return
	 */
	private Map<String, Object> getExportWordDataCountForAllMap(String curDate){
		Map<String,Object> map = new HashMap<String,Object>();
		String curTime = curDate.replaceFirst("-", "年")+"月";
		
		//统计时间
		map.put("totalDate", curTime);	
		//全院统计
		List<Map<String, Object>> dataTotalListForAll = getDataCountForAll(curDate);
		map.put("totalList", dataTotalListForAll);	
		//全院各部门统计
		List<Map<String, Object>> dataTotalListForDeptAll = getDeptDataCountForAll(curDate);
		map.put("deptTotalList", dataTotalListForDeptAll);	
				
		return map;
	}
	/**
	 * @description 院各项指标统计  导出execl
	 * @param curDate  统计时间
	 * @return
	 */
	private void exporExceltDataCountForAll(HttpServletRequest request,HttpServletResponse response){
		//模板类型
		String mType=request.getParameter("mType");
		//统计月份
		String curDate=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		
		String fileName = null;
		List<String> sheetNameList = new ArrayList<String>(); 
		List<List<String[]>> sheetColsTitleNameList = new ArrayList<List<String[]>>(); 
		List<String[]> sheetColsFieldList = new ArrayList<String[]>(); 
		List<List<Map<String,Object>>> sheetsColsDataList = new ArrayList<List<Map<String,Object>>>(); 
		
		if(mType.equals("irpT02")){//全院数据统计 excel
			String month = curDate.substring(curDate.indexOf("-")+1);
			String curYear = curDate.substring(0, curDate.indexOf("-"));
			if(month.substring(0, 1).equals("0")){
				month = month.substring(1);
			}
			int loopTime = Integer.valueOf(month);//生成月份次数			
			fileName = "知识产权全院数据统计-"+curDate;//文件名称
			/***********************************sheetNameList*****************************/
			//工作簿名称
			String sheetName1 = "全院知识产权完成值统计";
			String sheetName2 = "各单位完成值统计";
			sheetNameList.add(sheetName1);
			sheetNameList.add(sheetName2);
			/***********************************sheetColsTitleNameList*****************************/
			//sheet1 标题
			List<String[]> titleNameList1 = new ArrayList<String[]>();
			StringBuffer tn1 = new StringBuffer();
			tn1.append(" ").append(",");
			tn1.append(" ").append(",");
			tn1.append(" ").append(",");
			for(int i=0;i<loopTime;i++){
				tn1.append(i+1).append("月,");
			}
			tn1.append(" ");
			titleNameList1.add(tn1.toString().split(","));
			
			StringBuffer tn2 = new StringBuffer();
			tn2.append("科技指标,单位,考核指标,");
			for(int i=0;i<loopTime;i++){
				tn2.append("累计完成值,");
			}
			tn2.append("同比增长（%）");
			titleNameList1.add(tn2.toString().split(","));
			
			sheetColsTitleNameList.add(titleNameList1);
			//sheet2标题
			List<String[]> titleNameList2 = new ArrayList<String[]>();
			StringBuffer tn3 = new StringBuffer();
			tn3.append(" ").append(",");
			tn3.append(" ").append(",");			
			for(int i=0;i<loopTime;i++){
				tn3.append(i+1).append("月");
				if(i<loopTime-1){
					tn3.append(",");
				}
			}
			titleNameList2.add(tn3.toString().split(","));
			
			StringBuffer tn4 = new StringBuffer();
			tn4.append("序号,单位,");
			for(int i=0;i<loopTime;i++){
				tn4.append("申请专利（项）,申请发明专利(项),专利授权（项）,发明专利授权(项),海外专利申请(项),登记软件著作权(项),科技论文(项),科技专著(项)");
				if(i<loopTime-1){
					tn4.append(",");
				}
			}			
			titleNameList2.add(tn4.toString().split(","));
			sheetColsTitleNameList.add(titleNameList2);
			/***********************************sheetColsFieldList*****************************/
			//列编码
			StringBuffer tc1 = new StringBuffer();
			tc1.append("ITEM_NAME").append(",");
			tc1.append("ITEM_UNIT").append(",");
			tc1.append("ITEM_BASE").append(",");
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				tc1.append("MONTH").append(seq).append(",");
			}
			tc1.append("ITEM_ADD").append("");
			
			sheetColsFieldList.add(tc1.toString().split(","));
			
			StringBuffer tc2 = new StringBuffer();
			tc2.append("SORT_ID").append(",");
			tc2.append("ITEM_NAME").append(",");
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				for(int p=0;p<8;p++){
					tc2.append("MONTH").append(seq).append(p+1).append(",");					
				}	
				
			}
			String tc2Str = tc2.toString();
			tc2Str = tc2Str.substring(0, tc2Str.lastIndexOf(","));
			sheetColsFieldList.add(tc2Str.split(","));
			/***********************************sheetsColsDataList*****************************/
			StringBuffer rowStr = new StringBuffer();
			rowStr.append("PATENS_APPLY-申请专利-项,");
			rowStr.append("PATENS_APPLY_INVENT-申请发明专利-项,");
			rowStr.append("PATENS_ACCNOTI-专利授权-项,");
			rowStr.append("PATENS_ACCNOTI_INVENT-发明专利授权-项,");
			rowStr.append("OUT_PATENS-海外专利申请-项,");
			rowStr.append("SOFTCPR-登记软件著作权-项,");
			rowStr.append("PAPER-科技论文-篇,");
			rowStr.append("MONOGRAPH-科技专著-部");
			
			List<Map<String,Object>> dataList1 = new ArrayList<Map<String,Object>>();
			String[] rowArr = rowStr.toString().split(",");
			for(int i=0;i<rowArr.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				String[] row =  rowArr[i].split("-");
				String ITEM_NAME = row[1];
				String ITEM_UNIT = row[2];
				String ITEM_BASE = "";
				String ITEM_ADD = "";
				map.put("ITEM_NAME", ITEM_NAME);
				map.put("ITEM_UNIT", ITEM_UNIT);
				map.put("ITEM_BASE", ITEM_BASE);
				map.put("ITEM_ADD", ITEM_ADD);
				for(int k=0;k<loopTime;k++){
					String seq = ""+(k+1);		
					if((i+1)<10){
						seq = "0"+(k+1);
					}
					map.put("MONTH"+seq,"");
				}
				dataList1.add(map);
			}
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				String date = curYear +"-"+ seq;
				List<Map<String,Object>>  monthData = getDataCountForAll(date);
				for(int m=0;m<dataList1.size();m++){					
					Map<String,Object> mapTemp = dataList1.get(m);
					if(monthData!=null){
						for(int k=0;k<monthData.size();k++){
							Map<String, Object> o = monthData.get(k);
							String item_name = o.get("ITEM_NAME").toString();
							if(item_name.equals(mapTemp.get("ITEM_NAME"))){
								String item_count = o.get("ITEM_YEAR_VALUE").toString();
								mapTemp.put("MONTH"+seq, item_count);
								break;
							}
						}
					}
				}
			}
			sheetsColsDataList.add(dataList1);
			rowStr = new StringBuffer();
			rowStr.append("PATEN01-系统所,");
			rowStr.append("PATEN02-高压所,");
			rowStr.append("PATEN03-电自所,");
			rowStr.append("PATEN04-保护所,");
			rowStr.append("PATEN05-工程所,");
			rowStr.append("PATEN06-信通所,");
			rowStr.append("PATEN09-计量所,");
			rowStr.append("PATEN07-配电所,");
			rowStr.append("PATEN08-用能所,");
			rowStr.append("PATEN10-新能源中心,");
			rowStr.append("PATEN11-电工所,");
			rowStr.append("PATEN12-战略中心,");
			rowStr.append("PATEN13-武汉检测中心,");
			rowStr.append("PATEN14-电网质监中心,");
			rowStr.append("PATEN15-其他,");
			rowStr.append("PATEN16-全院总计");
			
			List<Map<String,Object>> dataList2 = new ArrayList<Map<String,Object>>();
			rowArr = rowStr.toString().split(",");
			for(int i=0;i<rowArr.length;i++){
				Map<String,Object> map = new HashMap<String,Object>();
				String[] row =  rowArr[i].split("-");
				String ITEM_NAME = row[1];
				String SORT_ID = ""+(i+1);
				if(i==rowArr.length-1){
					SORT_ID = "";
				}
				map.put("SORT_ID", SORT_ID);
				map.put("ITEM_NAME", ITEM_NAME);
				for(int k=0;k<loopTime;k++){
					String seq = ""+(k+1);		
					if((i+1)<10){
						seq = "0"+(k+1);
					}
					for(int p=0;p<8;p++){
						map.put("MONTH"+seq+(p+1),"");
					}	
				}
				dataList2.add(map);
			}
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				String date = curYear +"-"+ seq;
				List<Map<String,Object>>  monthData = getDeptDataCountForAll(date);
				for(int m=0;m<dataList2.size();m++){
					Map<String,Object> mapTemp = dataList2.get(m);
					if(monthData!=null){
						for(int k=0;k<monthData.size();k++){
							Map<String, Object> o = monthData.get(k);
							String item_name = o.get("ITEM_NAME").toString();
							if(item_name.equals(mapTemp.get("ITEM_NAME"))){
								String MONTH01 = o.get("ITEM_PATENS").toString();
								String MONTH02 = o.get("ITEM_PATENS_INVENT").toString();
								String MONTH03 = o.get("ITEM_ACC_PATENS").toString();
								String MONTH04 = o.get("ITEM_ACC_PATENS_INVENT").toString();
								String MONTH05 = o.get("ITEM_OUT_PATENS").toString();
								String MONTH06 = o.get("ITEM_SOFTCPR").toString();
								String MONTH07 = o.get("ITEM_PAPER").toString();
								String MONTH08 = o.get("ITEM_MONOGRAPH").toString();
								
								String YEAR01 = o.get("ITEM_PATENS_YEAR").toString();
								String YEAR02 = o.get("ITEM_PATENS_INVENT_YEAR").toString();
								String YEAR03 = o.get("ITEM_ACC_PATENS_YEAR").toString();
								String YEAR04 = o.get("ITEM_ACC_PATENS_INVENT_YEAR").toString();
								String YEAR05 = o.get("ITEM_OUT_PATENS_YEAR").toString();
								String YEAR06 = o.get("ITEM_SOFTCPR_YEAR").toString();
								String YEAR07 = o.get("ITEM_PAPER_YEAR").toString();
								String YEAR08 = o.get("ITEM_MONOGRAPH_YEAR").toString();								
								
								mapTemp.put("MONTH"+seq+"1", MONTH01);
								mapTemp.put("MONTH"+seq+"2", MONTH02);
								mapTemp.put("MONTH"+seq+"3", MONTH03);
								mapTemp.put("MONTH"+seq+"4", MONTH04);
								mapTemp.put("MONTH"+seq+"5", MONTH05);
								mapTemp.put("MONTH"+seq+"6", MONTH06);
								mapTemp.put("MONTH"+seq+"7", MONTH07);
								mapTemp.put("MONTH"+seq+"8", MONTH08);
								
								mapTemp.put("MONTH"+seq+"1", YEAR01);
								mapTemp.put("MONTH"+seq+"2", YEAR02);
								mapTemp.put("MONTH"+seq+"3", YEAR03);
								mapTemp.put("MONTH"+seq+"4", YEAR04);
								mapTemp.put("MONTH"+seq+"5", YEAR05);
								mapTemp.put("MONTH"+seq+"6", YEAR06);
								mapTemp.put("MONTH"+seq+"7", YEAR07);
								mapTemp.put("MONTH"+seq+"8", YEAR08);

								
								break;
							}
						}
					}
				}
			}
			sheetsColsDataList.add(dataList2);
			
		}else{
			return;
		}
		
		try {
			boolean isCreateIndex = false;
			boolean isDefultBgColor = false;
			new ExportExcelUtil(fileName, sheetNameList, sheetColsTitleNameList, sheetColsFieldList, sheetsColsDataList, response).exportSheets(isCreateIndex,isDefultBgColor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @description 各单位数据统计 导出execl
	 * @param curDate  统计时间
	 * @return
	 */
	private void exportDeptDataCountExcel(HttpServletRequest request,HttpServletResponse response){
		//模板类型
		String mType=request.getParameter("mType");
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		
		String fileName = null;
		String sheetName = null;
		String[] sheetColsTitleName = null;
		String[] sheetColsField = null;
		List<Map<String,Object>> sheetColsDataList = null;
		
		if(mType.equals("irpT03")){//单位数据统计 excel
			String userName = webUtils.getUsername();//登录人ID
			String deptName = "";
			//获取部门ID
			List<Map<String,Object>>  userInfoList = irpCountMapper.getUserInfoByUserName(userName);
			
			if(userInfoList!=null&&userInfoList.size()==1){
				deptName = "-"+userInfoList.get(0).get("pdeptname".toUpperCase()).toString();
			}
			
			fileName = "知识产权单位数据统计"+deptName+"-"+countMonth;//文件名称
			sheetName = "单位数据统计";
			sheetColsDataList = getIrpDataCountForDept(request);
		}else{
			return;
		}
		
		sheetColsTitleName = getIrpTitleName(mType);
		sheetColsField = getIrpColsField(mType);
		
		try {
			boolean isCreateIndex = false;
			boolean isDefultBgColor = false;
			new ExportExcelUtil(fileName, sheetName, sheetColsTitleName, sheetColsField, sheetColsDataList, response).createSheetForDeptTotal(isCreateIndex,isDefultBgColor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @desciption 获取各单位统计数据   所专责
	 * @param request
	 * @return
	 */
	private List<Map<String, Object>> getIrpDataCountForDept(HttpServletRequest request) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try{
			/***********************************定义固定行属性***************************************/
			String curDate=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
			
			//动态控制 生成月份数量
			String month = curDate.substring(curDate.indexOf("-")+1);
			if(month.substring(0, 1).equals("0")){
				month = month.substring(1);
			}
			int loopTime = Integer.valueOf(month);//生成月份次数
			
			StringBuffer filed = new StringBuffer();
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				
//				filed.append("ROW_ID==MONTH01-SORT_ID==1月-ITEM_PATENS=PT101-ITEM_PATENS_YEAR=PT201-ITEM_INVENT=VT101-ITEM_INVENT_YEAR=VT201");
				filed.append("ROW_ID").append("==").append("MONTH").append(seq);
				filed.append("-").append("SORT_ID").append("==").append(i+1).append("月");
				
				filed.append("-").append("ITEM_PATENS").append("=").append("MONTH01");
				filed.append("-").append("ITEM_PATENS_INVENT").append("=").append("MONTH02");
				filed.append("-").append("ITEM_ACC_PATENS").append("=").append("MONTH03");
				filed.append("-").append("ITEM_ACC_PATENS_INVENT").append("=").append("MONTH04");
				filed.append("-").append("ITEM_OUT_PATENS").append("=").append("MONTH05");
				filed.append("-").append("ITEM_SOFTCPR").append("=").append("MONTH06");
				filed.append("-").append("ITEM_PAPER").append("=").append("MONTH07");
				filed.append("-").append("ITEM_MONOGRAPH").append("=").append("MONTH08");
				
				filed.append("-").append("ITEM_PATENS_YEAR").append("=").append("YEAR01");
				filed.append("-").append("ITEM_PATENS_INVENT_YEAR").append("=").append("YEAR02");
				filed.append("-").append("ITEM_ACC_PATENS_YEAR").append("=").append("YEAR03");
				filed.append("-").append("ITEM_ACC_PATENS_INVENT_YEAR").append("=").append("YEAR04");
				filed.append("-").append("ITEM_OUT_PATENS_YEAR").append("=").append("YEAR05");
				filed.append("-").append("ITEM_SOFTCPR_YEAR").append("=").append("YEAR06");
				filed.append("-").append("ITEM_PAPER_YEAR").append("=").append("YEAR07");
				filed.append("-").append("ITEM_MONOGRAPH_YEAR").append("=").append("YEAR08");
				if(i<loopTime-1){
					filed.append(",");
				}
			}
			
			/***********************************定义查询参数***************************************/
			//登录人ID
			String userName = webUtils.getUsername();
			List<Map<String,Object>>  userInfoList = irpCountMapper.getUserInfoByUserName(userName);
			String deptId= "";
			if(userInfoList!=null&&userInfoList.size()==1){
				deptId = userInfoList.get(0).get("pdeptid".toUpperCase()).toString();
			}
			
			//当前年
			String curYear = curDate.substring(0, curDate.indexOf("-"));
			//去年12月
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = new Date();
			try {
				date = sdf.parse(curDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, -1);
			Date lastYearDate = calendar.getTime();
			String lastYearDateString = sdf.format(lastYearDate);
			String lastDate = lastYearDateString.substring(0, lastYearDateString.indexOf("-"))+"-12";		
			//本年起始月
			String startDate = curYear+"-01";
			/***********************************获取数据***************************************/
			String[] line = filed.toString().split(",");
			
			String DFlag = "dept";
			
			for(int i=0;i<loopTime;i++){
				String seq = ""+(i+1);		
				if((i+1)<10){
					seq = "0"+(i+1);
				}
				String curDateTemp = curYear +"-"+ seq;
				Map<String, Object> map = new HashMap<String, Object>();
				
				List<Map<String, Object>> list = irpCountMapper.getDataTotalForAllByMonth(curDateTemp, startDate, curYear, lastDate, DFlag, deptId);
				if(list!=null){
					for(int k=0;k<list.size();k++){
						Map<String, Object> m = list.get(k);
						String item_name = m.get("ITEM_NAME").toString();
						String item_count = m.get("ITEM_COUNT").toString();
						map.put(item_name, item_count);
					}
				}
				
				Map<String, Object> one = formatDateCountListForAll(map, line[i]);
				result.add(one);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * @description 专利、论文、软著、专著、海外专利明细
	 * @param request
	 * @param response
	 */
	private void exportDetailExcel(HttpServletRequest request,HttpServletResponse response){
		//模板类型
		String mType=request.getParameter("mType")==null?"":request.getParameter("mType");
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		String patensType=request.getParameter("patensType")==null?"":request.getParameter("patensType").trim();
		String YFlag=request.getParameter("YFlag")==null?"":request.getParameter("YFlag").trim();//no 当月数据  yes 年度累计  
		String DFlag=request.getParameter("DFlag")==null?"":request.getParameter("DFlag").trim();//dept 业务部门  
		String deptId=request.getParameter("deptId")==null?"":request.getParameter("deptId").trim();
		
		String fileName = null;
		String sheetName = null;
		String[] sheetColsTitleName = null;
		String[] sheetColsField = null;
		List<Map<String,Object>> sheetColsDataList = null;
		
		String userName = webUtils.getUsername();//登录人ID
		String deptName = "";
		//获取部门ID
		if(DFlag!=null&&DFlag.equals("dept")){
			//科技部
			if(deptId.length()>0&&("PATEN01,PATEN02,PATEN03,PATEN04,PATEN05,PATEN06,PATEN07,PATEN08,PATEN09,PATEN10,PATEN11,PATEN12,PATEN13,PATEN14").indexOf(deptId)!=-1){
				List<Map<String,Object>>  deptList = irpCountMapper.getDeptList();
				if(deptList!=null){
					//获取部门id
					for(int k=0;k<deptList.size();k++){
						Map<String, Object> objMap = deptList.get(k);
						String code = objMap.get("CODENAME").toString();
						if(code.equals(deptId)){
							deptId = objMap.get("DEPTID").toString();
							deptName = "-"+objMap.get("DEPTNAME").toString();
							break;
						}
					}
				}
			}else{//各部门
				List<Map<String,Object>>  userInfoList = irpCountMapper.getUserInfoByUserName(userName);
				
				if(userInfoList!=null&&userInfoList.size()==1){
					deptId = userInfoList.get(0).get("pdeptid".toUpperCase()).toString();
					deptName = "-"+userInfoList.get(0).get("pdeptname".toUpperCase()).toString();
				}
			}
		}
		
		if(mType.equals("irpD01")){//专利申请明细 excel/
			if(patensType!=null&&patensType.equals("1301")){//专利发明申请
				fileName = "知识产权专利申请发明当月明细"+deptName+"-"+countMonth;//文件名称
				if(YFlag.equals("yes")){//累计
					fileName = "知识产权专利申请发明年度累计明细"+deptName+"-"+countMonth;//文件名称
				}
				sheetName = "专利申请发明";
			}else{
				fileName = "知识产权专利申请当月明细"+deptName+"-"+countMonth;//文件名称
				if(YFlag.equals("yes")){//累计
					fileName = "知识产权专利申请年度累计明细"+deptName+"-"+countMonth;//文件名称
				}
				sheetName = "专利申请";
			}
			
		}else if(mType.equals("irpD02")){//专利授权当月明细 excel
			if(patensType.equals("1301")){
				fileName = "知识产权专利授权发明当月明细"+deptName+"-"+countMonth;//文件名称
				if(YFlag.equals("yes")){//累计
					fileName = "知识产权专利授权发明年度累计明细"+deptName+"-"+countMonth;//文件名称
				}
				sheetName = "专利授权发明";
			}else{
				fileName = "知识产权专利授权当月明细"+deptName+"-"+countMonth;//文件名称
				if(YFlag.equals("yes")){//累计
					fileName = "知识产权专利授权年度累计明细"+deptName+"-"+countMonth;//文件名称
				}
				sheetName = "专利授权";
			}
		}else if(mType.equals("irpD03")){//论文当月明细 excel
			fileName = "知识产权论文当月明细"+deptName+"-"+countMonth;//文件名称
			if(YFlag.equals("yes")){//累计
				fileName = "知识产权论文年度累计明细"+deptName+"-"+countMonth;//文件名称
			}
			sheetName = "论文";
		}else if(mType.equals("irpD04")){//软著当月明细 excel
			fileName = "知识产权软著当月明细"+deptName+"-"+countMonth;//文件名称
			if(YFlag.equals("yes")){//累计
				fileName = "知识产权软著年度累计明细"+deptName+"-"+countMonth;//文件名称
			}
			sheetName = "软著";
		}else if(mType.equals("irpD05")){//专著当月明细 excel
			fileName = "知识产权专著当月明细"+deptName+"-"+countMonth;//文件名称
			if(YFlag.equals("yes")){//累计
				fileName = "知识产权专著年度累计明细"+deptName+"-"+countMonth;//文件名称
			}
			sheetName = "专著";
		}else if(mType.equals("irpD06")){//海外专利当月明细 excel
			fileName = "知识产权海外专利当月明细"+deptName+"-"+countMonth;//文件名称
			if(YFlag.equals("yes")){//累计
				fileName = "知识产权海外专利年度累计明细"+deptName+"-"+countMonth;//文件名称
			}
			sheetName = "海外专利";
		}else{
			return;
		}
		
		sheetColsTitleName = getIrpTitleName(mType);
		sheetColsField = getIrpColsField(mType);
		sheetColsDataList = getIrpDataList(mType, countMonth, patensType, YFlag, DFlag, deptId);
		
		try {
			new ExportExcelUtil(fileName, sheetName, sheetColsTitleName, sheetColsField, sheetColsDataList, response).exportSheet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private String[] getIrpTitleName(String mType){
		StringBuffer sb = new StringBuffer();
		
		if(mType.equals("irpD01")){//专利明细 excel
			sb.append("序号,专利名称,公司编码,归口管理部门编码,专利类型,申请地,申请人,申请日期,申请号,使用保管人,发明/设计人,专利权人,完成单位,申请单位,专利权人与上报单位的关系,上报年份,上报季度,上报单位,技术领域(一级),技术领域(二级),技术领域(三级),智能电网环节,知识产权增加方式,初始申请费累计总额,初始年费累计总额,项目类型,项目编码,项目名称,项目出资单位,项目承担单位,项目竣工验收时间,项目工程预算,竣工决算金额,项目资本化金额,项目费用化金额,附属载体,设备分类(一级),设备分类(二级),应用阶段(成熟度),重要性,获奖情况,获奖年份,国内市场前景,国内市场前景说明,国外市场前景,国外市场前景说明,产品名称及型号,产品数量,产品使用单位及地点,产品备注,是否已纳入相关标准,相关标准名称,标准文号,标准性质,相关标准条款内容");
		}else if(mType.equals("irpD02")){//专利授权明细 excel
			sb.append("序号,专利名称,公司编码,归口管理部门编码,专利类型,申请地,申请人,申请日期,申请号,授权公告日,使用保管人,发明/设计人,专利权人,完成单位,申请单位,专利权人与上报单位的关系,上报年份,上报季度,上报单位,技术领域(一级),技术领域(二级),技术领域(三级),智能电网环节,知识产权增加方式,初始申请费累计总额,初始年费累计总额,项目类型,项目编码,项目名称,项目出资单位,项目承担单位,项目竣工验收时间,项目工程预算,竣工决算金额,项目资本化金额,项目费用化金额,附属载体,设备分类(一级),设备分类(二级),应用阶段(成熟度),重要性,获奖情况,获奖年份,国内市场前景,国内市场前景说明,国外市场前景,国外市场前景说明,产品名称及型号,产品数量,产品使用单位及地点,产品备注,是否已纳入相关标准,相关标准名称,标准文号,标准性质,相关标准条款内容");
		}else if(mType.equals("irpD03")){//论文明细 excel
			sb.append("序号,论文名称,作者姓名,第一作者单位,刊物名称,刊号,刊期,刊物级别,第一作者单位与上报单位的关系,技术领域(一级),技术领域(二级),技术领域(三级),上报年份,上报季度,系统内排名,备注");
		}else if(mType.equals("irpD04")){//软著明细 excel
			sb.append("序号,软件著作权名称,证书编号,公司编码,归口管理部门编码,使用保管人,技术领域(一级),技术领域(二级),技术领域(三级),登记号,申请人,著作权人,权力获得方式,权力范围,首次发表日期,授权否,著作权人与上报单位的关系,上报年份,上报季度,上报单位,知识产权增加方式,初始申请费累计总额,初始年费累计总额,项目编码,项目名称,项目类型,项目出资单位,项目承担单位,项目竣工验收时间,项目工程预算,竣工决算金额,项目资本化金额,项目费用化金额");
		}else if(mType.equals("irpD05")){//专著明细 excel
			sb.append("序号,论著名称,主要作者,作者单位,书号,出版社,出版日期,作者单位与上报单位的关系,技术领域(一级),技术领域(二级),技术领域(三级),备注,上报年份,上报季度,上报单位,系统内排名");
		}else if(mType.equals("irpD06")){//海外专利明细 excel
			sb.append("序号,专利名称,是否通过海外评审,公司编码,归口管理部门编码,专利类型,申请人,国际申请号,国际检索报告日,国际申请日,使用保管人,发明/设计人,专利权人,完成单位,申请单位,专利权人与上报单位的关系,上报年份,上报季度,上报单位,技术领域(一级),技术领域(二级),技术领域(三级),智能电网环节,知识产权增加方式,初始申请费累计总额,初始年费累计总额,项目类型,项目编码,项目名称,项目出资单位,项目承担单位,项目竣工验收时间,项目工程预算,竣工决算金额,项目资本化金额,项目费用化金额");
		}else if(mType.equals("irpT03")){//单位数据统计 excel
			sb.append("月度,当月申请专利（项）,年度累计申请专利（项）,当月申请发明专利(项),年度累计申请发明专利(项),当月专利授权（项）,年度累计专利授权（项）,当月发明专利授权(项),年度累计发明专利授权(项),当月海外专利申请（项）,年度累计海外专利申请（项）,当月登记软件著作权（项）,年度累计登记软件著作权（项）,当月科技论文（项）,年度累计科技论文（项）,当月科技专著（项）,年度累计科技专著（项）");
		}		
		
		return sb.toString().split(",");
	}
	private String[] getIrpColsField(String mType){
		
		StringBuffer sb = new StringBuffer();
		
		if(mType.equals("irpD01")){//专利明细 excel
			sb.append("PATENS_ID,PATENS_NAME,ORG_CODE,MANAGEDEPT_CODE,PATENS_TYPE,");
			sb.append("APPLY_ADDR,APPLY_USER,APPLY_DATE,APPLY_NO, USESAVE_USER,");
			sb.append("DESIGN_USER,PATENS_OWNER,APPLY_ORG,FILL_UNIT_NAME,AUTH_ORGREL,");
			sb.append("SEND_YEAR,SEND_QUARTER,SEND_ORG,TECH_FIELD,SECOND_TECHFIELD,");
			sb.append("THREED_TECHFIELD,CAPACITY_TACHE,IRP_ADDWAY,");
			sb.append("A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,A13,A14,A15,A16,");
			sb.append("A17,A18,A19,A20,A21,A22,A23,A24,A25,A26,A27,A28,A29,A30,A31,A32");
		}else if(mType.equals("irpD02")){//专利授权明细 excel
			sb.append("PATENS_ID,PATENS_NAME,ORG_CODE,MANAGEDEPT_CODE,PATENS_TYPE,APPLY_ADDR,");
			sb.append("APPLY_USER,APPLY_DATE,APPLY_NO,ACCNOTI_DATE,USESAVE_USER,DESIGN_USER,");
			sb.append("PATENS_OWNER,APPLY_ORG,FILL_UNIT_NAME,AUTH_ORGREL,SEND_YEAR,SEND_QUARTER,");
			sb.append("SEND_ORG,TECH_FIELD,SECOND_TECHFIELD,THREED_TECHFIELD,CAPACITY_TACHE,");
			sb.append("IRP_ADDWAY,A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12,A13,A14,A15,A16,");
			sb.append("A17,A18,A19,A20,A21,A22,A23,A24,A25,A26,A27,A28,A29,A30,A31,A32");
		}else if(mType.equals("irpD03")){//论文明细 excel
			sb.append("PAPER_ID,PAPER_NAME,AUTH_NAME,AUTH_ORG,PERIODICL_NAME,");
			sb.append("PERIODICL_CODE,PERIODICL_PERIOD,PERIODICL_LEVEL,AUTH_ORGREL,");
			sb.append("TECH_FIELD,SECOND_TECHFIELD,THREED_TECHFIELD,SEND_YEAR,");
			sb.append("SEND_QUARTER,SYSTEM_ORDER,PAPER_REMARK");
		}else if(mType.equals("irpD04")){//软著明细 excel
			sb.append("SOFTCPR_ID,SOFTCPR_NAME,CERTIFICATE_NO,ORG_CODE,MANAGEDEPT_CODE,");
			sb.append("SAFEKEEP_USER,TECH_FIELD,SECOND_TECHFIELD,THREED_TECHFIELD,");
			sb.append("REGISTER_NO,PROPOSER_USER,COPYRIGHT_USER,AUTHORITY_WAY,");
			sb.append("AUTHORITY_RANGE,FIRSTPUB_DATE,ISAUTHOR,COPYRIGHT_ORGREF,");
			sb.append("SEND_YEAR,SEND_QUARTER,FILL_UNIT_NAME,IRP_ADDWAY,");
			sb.append("A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12");
		}else if(mType.equals("irpD05")){//专著明细 excel
			sb.append("GRAPH_ID,GRAPH_NAME,INAUTH_NAME,AUTH_ORGNAME,BOOK_NO,");
			sb.append("PRESSING_HOUSE,PUB_DATE,AUTH_ORGREL,TECH_FIELD,");
			sb.append("SECOND_TECHFIELD,THREED_TECHFIELD,REMARK,");
			sb.append("SEND_YEAR,SEND_QUARTER,FILL_UNIT_NAME,SYSTEM_ORDER");
		}else if(mType.equals("irpD06")){//海外专利明细 excel
			sb.append("OUTPAT_ID,PATENS_NAME,ISPASS_OUTER,ORG_CODE,MANAGEDEPT_CODE,");
			sb.append("PATENS_TYPE,APPLY_USER,OUTPAT_NO,CHECK_DATE,OUTAPP_DATE,");
			sb.append("USESAVE_USER,DESIGN_USER,PATENS_OWNER,INORG_NAME,FILL_UNIT_NAME,");
			sb.append("AUTH_ORGREL,SEND_YEAR,SEND_QUARTER,SEND_ORG,TECH_FIELD,SECOND_TECHFIELD,");
			sb.append("THREED_TECHFIELD,CAPACITY_TACHE,IRP_ADDWAY,");
			sb.append("A1,A2,A3,A4,A5,A6,A7,A8,A9,A10,A11,A12");
		}else if(mType.equals("irpT03")){//单位数据统计 excel
			sb.append("SORT_ID,ITEM_PATENS,ITEM_PATENS_YEAR,ITEM_PATENS_INVENT,");
			sb.append("ITEM_PATENS_INVENT_YEAR,ITEM_ACC_PATENS,ITEM_ACC_PATENS_YEAR,");
			sb.append("ITEM_ACC_PATENS_INVENT,ITEM_ACC_PATENS_INVENT_YEAR,");
			sb.append("ITEM_OUT_PATENS,ITEM_OUT_PATENS_YEAR,ITEM_SOFTCPR,");
			sb.append("ITEM_SOFTCPR_YEAR,ITEM_PAPER,ITEM_PAPER_YEAR,ITEM_MONOGRAPH,ITEM_MONOGRAPH_YEAR");
		}
		
		return sb.toString().split(",");
	}
	private List<Map<String, Object>> getIrpDataList(String mType,String curDate,String patensType,String YFlag,String DFlag,String deptId){
		//当前年
		String curYear = curDate.substring(0, curDate.indexOf("-"));
		//去年12月
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		try {
			date = sdf.parse(curDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		String lastYearDateString = sdf.format(lastYearDate);
		String lastDate = lastYearDateString.substring(0, lastYearDateString.indexOf("-"))+"-12";	
		//起始时间
		String startDate = curYear + "-01";
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		if(mType.equals("irpD01")){//专利明细 excel
			dataList = irpCountMapper.getPatensDetailList(patensType, curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}else if(mType.equals("irpD02")){//专利授权明细 excel
			dataList = irpCountMapper.getPatensAuthDetailList(patensType, curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}else if(mType.equals("irpD03")){//论文明细 excel
			dataList = irpCountMapper.getPaperDetailList(curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}else if(mType.equals("irpD04")){//软著明细 excel
			dataList = irpCountMapper.getSoftDetailList(curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}else if(mType.equals("irpD05")){//专著明细 excel
			dataList = irpCountMapper.getGraphDetailList(curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}else if(mType.equals("irpD06")){//海外专利明细 excel
			dataList = irpCountMapper.getOutPatensDetailList(curDate, startDate, curYear, lastDate, YFlag, DFlag,deptId);
		}
		
		return dataList;
	}
}


