package com.sgcc.bg.webservice;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.service.BgInterfaceService;

@Service
public class BgWebService {
	private static Logger log = LoggerFactory.getLogger(BgWebService.class);
	//有效的期间
	private static String periodDic = "Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12";
	@Autowired
	private BgInterfaceService bgInterfaceService;
	
	/**
	 * 创建请求批次号     yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	private synchronized String getBatch(){
		//每次请求等待1010毫秒
		try {
			Thread.sleep(1010);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String batchTime = sdf.format(new Date());	
		return batchTime;
	}




	/**
	 * 根据人资系统请求参数，返回工时信息
	 * @param xml
	 * <![CDATA[<?xml version="1.0" encoding="utf-8"?>
	 * 	<ROOT>
	 * 		<SYS_KEY>bsp</SYS_KEY >
	 * 		<SYS_VALUE>sBu5jn3AE00IdN12MF</SYS_VALUE>
	 * 		<BG_YEAR>2018</BG_YEAR>   -- yyyy
	 * 		<BG_PERIOD>S1</BG_PERIOD> -- Y（年度），S1-S4（季度）,M1-M12（月份）
	 * 	</ROOT> ]]>
	 * @return
	 */
	public String getBgData(String xml){
		log.info("---------人资系统请求参数:"+xml);
		//生成时间戳
		String batchTime = getBatch();		
		String year = "";
		String period = "";
		log.info("---------[batch:"+batchTime+"]:开始解析请求参数！");
		//解析请求参数
		try{
			//请求参数非空校验      空请求存在内容为'?'情况，需过滤
			if(xml==null||xml.trim().length()==0||xml.equals("?")){
				return returnErrorMessage("请求参数不能为空！");
			}
			
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			if(root==null){
				return returnErrorMessage("根节点对象获取失败！");
			}
			//系统名称
			String key = root.elementTextTrim("SYS_KEY");
			if(key==null||key.length()==0){
				return returnErrorMessage("系统名称（SYS_KEY）不能为空！");
			}
			if(!key.equals("bsp")){
				return returnErrorMessage("系统名称（SYS_KEY）填写错误！ SYS_KEY= "+key);
			}
			//系统密钥
			String value = root.elementTextTrim("SYS_VALUE");
			if(value==null||value.length()==0){
				return returnErrorMessage("系统密钥（SYS_VALUE）不能为空！");
			}
			if(!value.equals("sBu5jn3AE00IdN12MF")){
				return returnErrorMessage("系统密钥（SYS_VALUE）填写错误！ SYS_VALUE= "+value);
			 }
			//年份  BG_YEAR
			year = root.elementTextTrim("BG_YEAR");
			if(year==null||year.length()==0){
				return returnErrorMessage("年份（BG_YEAR）不能为空！");
			}
			//年份格式验证  yyyy
			if(!isValidYearDate(year)){
				return returnErrorMessage("年份（BG_YEAR）填写错误,应该为：4位数字！ BG_YEAR= "+year);
			}
			//统计期间   BG_PERIOD Y（年度），S1-S4（季度）,M1-M12（月份）
			period = root.elementTextTrim("BG_PERIOD");
			if(period==null||period.length()==0){
				return returnErrorMessage("统计期间（BG_PERIOD）不能为空！");
			}
			if(periodDic.indexOf(period)==-1){
				return returnErrorMessage("统计期间（BG_PERIOD）填写错误,应该为：Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12！ BG_PERIOD= "+period);
			}
		}catch(Exception e){
			log.info("---------[batch:"+batchTime+"]:请求参数解析异常:"+e.getMessage());
			e.printStackTrace();
			return returnErrorMessage("请求参数解析异常！");
		}

		//获取报工数据
		String startDate = getStartDate(year, period);
		String endDate = getEndDate(year, period);
		String monthName = getMonthName(year, period);
		//根据年，季，月获取统计报表的最后一天
		/**
		 * 获取基本信息
		 * @param WT_SEASON 期间    Y,S1,S2,S3,S4,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
		 * @param yearName  期间为Y使用：年份  yyyy
		 * @param startDate 期间为季度使用：S1,S2,S3,S4  开始时间  yyyy-MM-dd
		 * @param endDate   期间为季度使用：S1,S2,S3,S4   结束时间 yyyy-MM-dd
		 * @param monthName 期间为月份使用：M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12  yyyy-MM
		 * @return
		 */
		String  newEndData="";
		if(period.equals("Y")){ newEndData=year+"-12-01"; }
		else if("S1".equals(period)){newEndData = year +"-03-31";}
		else if("S2".equals(period)){newEndData = year +"-06-30";}
		else if("S3".equals(period)){newEndData = year +"-09-30";}
		else if("S4".equals(period)){newEndData = year +"-12-31";}
		else if("M1".equals(period)){newEndData = year +"-01-31";}
		else if("M2".equals(period)){newEndData = year +"-02-29";}
		else if("M3".equals(period)){newEndData = year +"-03-31";}
		else if("M4".equals(period)){newEndData = year +"-04-30";}
		else if("M5".equals(period)){newEndData = year +"-05-31";}
		else if("M6".equals(period)){newEndData = year +"-06-30";}
		else if("M7".equals(period)){newEndData = year +"-07-31";}
		else if("M8".equals(period)){newEndData = year +"-08-31";}
		else if("M9".equals(period)){newEndData = year +"-09-30";}
		else if("M10".equals(period)){newEndData = year +"-10-31";}
		else if("M11".equals(period)){newEndData = year +"-11-30";}
		else if("M12".equals(period)){newEndData = year +"-12-31";}


		log.info("---------[batch:"+batchTime+"]:开始获取基本信息，验证本期间是否记录！");
		//获取基本信息
		List<Map<String,Object>> baseDate = bgInterfaceService.getInterfaceBaseData(period, year, startDate, endDate, monthName );
		if(baseDate==null||baseDate.size()==0){
			return returnSucessMessage("本期间没有记录！");
		}
		
		log.info("---------[batch:"+batchTime+"]:开始获取基本信息,包含负责人没有工时数据！");
		//获取基本信息
		List<Map<String,Object>> baseInfo = bgInterfaceService.getInterfaceBaseInfo(period, year, startDate, endDate, monthName ,newEndData);
		if(baseInfo==null||baseInfo.size()==0){
			return returnSucessMessage("本期间没有记录,包含负责人没有工时数据！");
		}
		/*List<Map<String,Object>> baseInfo = new ArrayList<>();
		Set<String> set = new HashSet();
		Map<String,Map> msp = new HashMap<>();
		for(Map<String,Object> map : baseInfoList){
			String id = String.valueOf(map.get("PROJECT_ID"));
			//map.remove("ID");
			msp.put(id,map);
		}
		set = msp.keySet();
		for(String key : set){
			Map newMap = msp.get(key);
			newMap.put(set,key);
			baseInfo.add(newMap);
		}*/

		for(Map<String,Object>   base:baseInfo){
			String  projectid  =String.valueOf(base.get("PROJECT_ID"));
			List<Map<String,Object>> ProjectUserInfo =	bgInterfaceService.selectForProjectUser(projectid,newEndData);
			if(ProjectUserInfo.isEmpty()){
				base.put("PROJECT_LEADER","");
				base.put("LEADER_USERNAME","");
			}else {
				Object project_leaders=ProjectUserInfo.get(0).get("PROJECT_LEADER");
				Object leader_usernames=ProjectUserInfo.get(0).get("LEADER_USERNAME");
				base.put("PROJECT_LEADER",project_leaders);
				base.put("LEADER_USERNAME",leader_usernames);
			}
		}




		log.info("---------[batch:"+batchTime+"]:开始获取员工总工时！");
		//获取员工总工时
		List<Map<String,Object>> totalBgUser = bgInterfaceService.getInterfaceTotalByUser(period, year, startDate, endDate, monthName);
		if(totalBgUser!=null&&totalBgUser.size()>0){
			for(Map<String,Object> m:baseInfo){
				String hrcode = m.get("EMP_CODE")==null?null:m.get("EMP_CODE").toString();
				String role = m.get("ROLE")==null?null:m.get("ROLE").toString();
				if(hrcode!=null){
					for(Map<String,Object> n:totalBgUser){
						String hr = n.get("EMP_CODE")==null?null:n.get("EMP_CODE").toString();
						String ro = n.get("ROLE")==null?null:n.get("ROLE").toString();
						if(hr!=null&&hr.equals(hrcode) && ro!=null && ro.equals(role)){
							String hour = n.get("WORKING_HOUR")==null?"0":n.get("WORKING_HOUR").toString();
							m.put("TOTAL_INPUT_TIME", hour);
						}
					}
				}
			}
		}
		log.info("---------[batch:"+batchTime+"]:开始获取员工项目工时！");

		//获取员工项目工时
		List<Map<String,Object>> totalBgProj = bgInterfaceService.getInterfaceTotalByProj(period, year, startDate, endDate, monthName);
		if(totalBgUser!=null&&totalBgUser.size()>0){
			for(Map<String,Object> m:baseInfo){
				String hrcode = m.get("EMP_CODE")==null?null:m.get("EMP_CODE").toString();
				String project = m.get("PROJECT_ID")==null?null:m.get("PROJECT_ID").toString();
				String role = m.get("ROLE")==null?null:m.get("ROLE").toString();
				if(hrcode!=null){
					for(Map<String,Object> n:totalBgProj){
						String hr = n.get("EMP_CODE")==null?null:n.get("EMP_CODE").toString();
						String proj = n.get("PROJECT_ID")==null?null:n.get("PROJECT_ID").toString();
						String ro = n.get("ROLE")==null?null:n.get("ROLE").toString();
						if(hr!=null&&hr.equals(hrcode)&&proj!=null&&proj.equals(project) && ro!=null && ro.equals(role)){
							String hour = n.get("WORKING_HOUR")==null?"0":n.get("WORKING_HOUR").toString();
							m.put("INPUT_TIME", hour);
							break;
						}
						else if(hr!=null&&hr.equals(hrcode)&&proj==null&&project==null){
							String hour = n.get("WORKING_HOUR")==null?"0":n.get("WORKING_HOUR").toString();
							m.put("INPUT_TIME", hour);
							break;
						}
					}
				}
			}
		}
		log.info("---------[batch:"+batchTime+"]:开始计算占比！");

		//计算占比
		for(Map<String,Object> m:baseInfo){
			String TOTAL_INPUT_TIME = m.get("TOTAL_INPUT_TIME")==null?null:m.get("TOTAL_INPUT_TIME").toString();
			String INPUT_TIME = m.get("INPUT_TIME")==null?null:m.get("INPUT_TIME").toString();
			
			String INPUT_PERCENTAGE = getInputPercentage(TOTAL_INPUT_TIME, INPUT_TIME);
			m.put("INPUT_PERCENTAGE", INPUT_PERCENTAGE);
		}
		log.info("---------[batch:"+batchTime+"]:生成返回数据！");

		List<Map<String,Object>> baseInfoList = new ArrayList<>();
		List<Map<String,Object>> list2 = new ArrayList<>();
		/*for(Map<String,Object> m:baseInfo){
			String projectId = m.get("PROJECT_ID")==null?null:m.get("PROJECT_ID").toString();
			String userCode = m.get("EMP_CODE")==null?null:m.get("EMP_CODE").toString();
			String start = m.get("START_DATE")==null?null:m.get("START_DATE").toString();
			String end = m.get("END_DATE")==null?null:m.get("END_DATE").toString();

			for(Map<String,Object> n:baseInfo){
				String proId = n.get("PROJECT_ID")==null?null:n.get("PROJECT_ID").toString();
				String uCode = n.get("EMP_CODE")==null?null:n.get("EMP_CODE").toString();
				String sta = n.get("START_DATE")==null?null:n.get("START_DATE").toString();
				String e = n.get("END_DATE")==null?null:n.get("END_DATE").toString();
				if(projectId!=null && proId!=null && projectId.equals(proId) && userCode.equals(uCode)){
					if(start!=null && sta!=null) {
						int i = start.compareTo(sta);
						if (i < 0) {
							m = n;
						}
					}
				}
				if(projectId!=null && proId!=null && projectId.equals(proId) && userCode.equals(uCode)){
					if(end!=null && e!=null){
						int q = end.compareTo(newEndData);
						int w = e.compareTo(newEndData);
						int a = end.compareTo(e);
						if(q<0 && w<0 && a<0){
							m=n;
						}else {
							list2.add(n);
						}
					}
				}
			}
			baseInfoList.add(m);
		}
		List<Map<String,Object>> list = new ArrayList<>();*/

		for(int i=0;i<baseInfo.size();i++){
			Map<String,Object> m = baseInfo.get(i);
			String projectId = m.get("PROJECT_ID")==null?null:m.get("PROJECT_ID").toString();
			String userCode = m.get("EMP_CODE")==null?null:m.get("EMP_CODE").toString();
			String start = m.get("START_DATE")==null?null:m.get("START_DATE").toString();
			String end = m.get("END_DATE")==null?null:m.get("END_DATE").toString();
			for(int j=i+1;j<baseInfo.size();j++){
				Map<String,Object> n = baseInfo.get(j);
				String proId = n.get("PROJECT_ID")==null?null:n.get("PROJECT_ID").toString();
				String uCode = n.get("EMP_CODE")==null?null:n.get("EMP_CODE").toString();
				String sta = n.get("START_DATE")==null?null:n.get("START_DATE").toString();
				String e = n.get("END_DATE")==null?null:n.get("END_DATE").toString();
				if(projectId!=null && proId!=null && projectId.equals(proId) && userCode.equals(uCode)){
					if(end!=null && e!=null){
						int q = end.compareTo(newEndData);
						int q2 = start.compareTo(newEndData);
						int w = e.compareTo(newEndData);
						int w2 = sta.compareTo(newEndData);
						int a = end.compareTo(e);
						if(w>=0 && w2<0  && a<0){
							m=n;
						}else {
							list2.add(n);
						}
					}
				}
			}
			baseInfoList.add(m);
		}

		for(int i=0;i<baseInfo.size();i++){
			String projectId = baseInfo.get(i).get("PROJECT_ID")==null?null: baseInfo.get(i).get("PROJECT_ID").toString();
			String userCode =  baseInfo.get(i).get("EMP_CODE")==null?null: baseInfo.get(i).get("EMP_CODE").toString();
			String role =  baseInfo.get(i).get("ROLE")==null?null: baseInfo.get(i).get("ROLE").toString();
			for(int j=i+1;j<baseInfo.size();j++){
				String proId = baseInfo.get(j).get("PROJECT_ID")==null?null:baseInfo.get(j).get("PROJECT_ID").toString();
				String uCode = baseInfo.get(j).get("EMP_CODE")==null?null:baseInfo.get(j).get("EMP_CODE").toString();
				String ro = baseInfo.get(j).get("ROLE")==null?null:baseInfo.get(j).get("ROLE").toString();
				if(projectId!=null && proId!=null && ro!=null && role!=null && projectId.equals(proId) && userCode.equals(uCode) &&role.equals(ro) ){
					list2.add(baseInfoList.get(i));
					break;
				}
			}
		}

		for(Map<String,Object> map :list2){
			baseInfoList.remove(map);
		}


		//生成返回数据
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ROOT>");
		sb.append("<RESULT>");
		sb.append("<STATUS>SUCCESS</STATUS>");		
		sb.append("<MESSAGE>").append("").append("</MESSAGE>");
		sb.append("</RESULT>");
		sb.append("<ITEMS>");
		for(Map<String,Object> m:baseInfoList){
			
			String WT_YEAR = year;
			String WT_SEASON = period;
			String EMP_CODE = m.get("EMP_CODE")==null?"":m.get("EMP_CODE").toString();
			String WT_TYPE = m.get("WT_TYPE")==null?"":m.get("WT_TYPE").toString();
			String WBS_CODE = m.get("WBS_CODE")==null?"":m.get("WBS_CODE").toString();
			String WBS_NAME = m.get("WBS_NAME")==null?"":m.get("WBS_NAME").toString();
			String TOTAL_INPUT_TIME = m.get("TOTAL_INPUT_TIME")==null?"0":m.get("TOTAL_INPUT_TIME").toString();
			String INPUT_TIME = m.get("INPUT_TIME")==null?"0":m.get("INPUT_TIME").toString();
			String INPUT_PERCENTAGE = m.get("INPUT_PERCENTAGE")==null?"0.00":m.get("INPUT_PERCENTAGE").toString();
			String PROJECT_LEADER = m.get("PROJECT_LEADER")==null?"":m.get("PROJECT_LEADER").toString();
			String PROJECT_ROLE = m.get("PROJECT_ROLE")==null?"":m.get("PROJECT_ROLE").toString();
			
			sb.append("<ITEM>");
			sb.append("<WT_YEAR>").append(WT_YEAR).append("</WT_YEAR>");
			sb.append("<WT_SEASON>").append(WT_SEASON).append("</WT_SEASON>");
			sb.append("<EMP_CODE>").append(EMP_CODE).append("</EMP_CODE>");
			sb.append("<WT_TYPE>").append(WT_TYPE).append("</WT_TYPE>");
			sb.append("<WBS_CODE>").append(WBS_CODE).append("</WBS_CODE>");
			sb.append("<WBS_NAME>").append(WBS_NAME).append("</WBS_NAME>");
			sb.append("<TOTAL_INPUT_TIME>").append(TOTAL_INPUT_TIME).append("</TOTAL_INPUT_TIME>");
			sb.append("<INPUT_TIME>").append(INPUT_TIME).append("</INPUT_TIME>");
			sb.append("<INPUT_PERCENTAGE>").append(INPUT_PERCENTAGE).append("</INPUT_PERCENTAGE>");
			sb.append("<PROJECT_LEADER>").append(PROJECT_LEADER).append("</PROJECT_LEADER>");
			sb.append("<PROJECT_ROLE>").append(PROJECT_ROLE).append("</PROJECT_ROLE>");
			sb.append("</ITEM>");
		}
		sb.append("</ITEMS>");
		sb.append("</ROOT>");
		
		log.info("---------[batch:"+batchTime+"]:生成保存接口数据！");

		//保存接口数据
		for(Map<String,Object> m:baseInfoList){
			String WT_YEAR = year;
			String WT_SEASON = period;
			String EMP_CODE = m.get("EMP_CODE")==null?"":m.get("EMP_CODE").toString();
			String WT_TYPE = m.get("WT_TYPE")==null?"":m.get("WT_TYPE").toString();
			String PROJECT_ID = m.get("PROJECT_ID")==null?"":m.get("PROJECT_ID").toString();
			String WBS_CODE = m.get("WBS_CODE")==null?"":m.get("WBS_CODE").toString();
			String WBS_NAME = m.get("WBS_NAME")==null?"":m.get("WBS_NAME").toString();
			String TOTAL_INPUT_TIME = m.get("TOTAL_INPUT_TIME")==null?"0":m.get("TOTAL_INPUT_TIME").toString();
			String INPUT_TIME = m.get("INPUT_TIME")==null?"0":m.get("INPUT_TIME").toString();
			String INPUT_PERCENTAGE = m.get("INPUT_PERCENTAGE")==null?"0.00":m.get("INPUT_PERCENTAGE").toString();
			String PROJECT_LEADER = m.get("PROJECT_LEADER")==null?"":m.get("PROJECT_LEADER").toString();
			String PROJECT_ROLE = m.get("PROJECT_ROLE")==null?"":m.get("PROJECT_ROLE").toString();
			String update_time = batchTime;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("WT_YEAR", WT_YEAR);
			map.put("WT_SEASON", WT_SEASON);
			map.put("EMP_CODE", EMP_CODE);
			map.put("WT_TYPE", WT_TYPE);
			map.put("PROJECT_ID", PROJECT_ID);
			map.put("WBS_CODE", WBS_CODE);
			map.put("WBS_NAME", WBS_NAME);
			map.put("TOTAL_INPUT_TIME", TOTAL_INPUT_TIME);
			map.put("INPUT_TIME", INPUT_TIME);
			map.put("INPUT_PERCENTAGE", INPUT_PERCENTAGE);
			map.put("PROJECT_LEADER", PROJECT_LEADER);
			map.put("PROJECT_ROLE", PROJECT_ROLE);
			map.put("update_time", update_time);
			
			bgInterfaceService.addInterfaceBspData(map);
		}
		log.info("---------[batch:"+batchTime+"]:开始保存接口明细数据！");

		//保存接口明细数据
		bgInterfaceService.addInterfaceBspDetailData(period, year, startDate, endDate, monthName,batchTime);
		log.info("---------[batch:"+batchTime+"]:完成本次请求！返回数据："+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 返回错误信息
	 * @param message 信息提示 
	 * @return
	 */
	private String returnErrorMessage(String message){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ROOT>");
		sb.append("<RESULT>");
		sb.append("<STATUS>FALSE</STATUS>");
		if(message==null){
			message = "";
		}
		sb.append("<MESSAGE>").append(message).append("</MESSAGE>");
		sb.append("</RESULT>");
		sb.append("</ROOT>");
		return sb.toString();
	}
	/**
	 * 返回正确信息   没有有效数据
	 * @param message 信息提示 
	 * @return
	 */
	private String returnSucessMessage(String message){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ROOT>");
		sb.append("<RESULT>");
		sb.append("<STATUS>SUCCESS</STATUS>");
		if(message==null){
			message = "";
		}
		sb.append("<MESSAGE>").append(message).append("</MESSAGE>");
		sb.append("</RESULT>");
		sb.append("</ROOT>");
		return sb.toString();
	}
	/**
	 * 获取月份   yyyy-MM
	 * @param year yyyy
	 * @param period M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12
	 * @return
	 */
	private String getMonthName(String year,String period){
		String monthName = null;
		if("M1".equals(period)){monthName = year +"-01";}
		else if("M2".equals(period)){monthName = year +"-02";}
		else if("M3".equals(period)){monthName = year +"-03";}
		else if("M4".equals(period)){monthName = year +"-04";}
		else if("M5".equals(period)){monthName = year +"-05";}
		else if("M6".equals(period)){monthName = year +"-06";}
		else if("M7".equals(period)){monthName = year +"-07";}
		else if("M8".equals(period)){monthName = year +"-08";}
		else if("M9".equals(period)){monthName = year +"-09";}
		else if("M10".equals(period)){monthName = year +"-10";}
		else if("M11".equals(period)){monthName = year +"-11";}
		else if("M12".equals(period)){monthName = year +"-12";}		
		return monthName;
	}
	/**
	 * 获取开始时间   yyyy-MM
	 * @param year yyyy
	 * @param period S1,S2,S3,S4
	 * @return
	 */
	private String getStartDate(String year,String period){
		String startDate = null;
		if("S1".equals(period)){startDate = year +"-01-01";}
		else if("S2".equals(period)){startDate = year +"-04-01";}
		else if("S3".equals(period)){startDate = year +"-07-01";}
		else if("S4".equals(period)){startDate = year +"-10-01";}
		
		return startDate;
	}
	/**
	 * 获取结束时间   yyyy-MM
	 * @param year yyyy
	 * @param period S1,S2,S3,S4
	 * @return
	 */
	private String getEndDate(String year,String period){
		String endDate = null;
		if("S1".equals(period)){endDate = year +"-03-31";}
		else if("S2".equals(period)){endDate = year +"-06-30";}
		else if("S3".equals(period)){endDate = year +"-09-30";}
		else if("S4".equals(period)){endDate = year +"-12-31";}
		
		return endDate;
	}
	/**
	 * 计算占比 input/total
	 * @param total 分母（被除数）
	 * @param input 分子（除数）
	 * @return
	 */
	private String getInputPercentage(String total,String input){
		if(total==null||total.length()==0||total.equals("0")||input==null||input.length()==0){
			return "0.00";
		}
		String result = "";
		try{
			Double divisor = Double.valueOf(input);
			Double dividend = Double.valueOf(total);
			
			result = new BigDecimal((divisor/dividend)*100).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 验证年份是否合法     true合法   false不合法
	 * @param dateString  
	 * @return
	 */
	private static boolean isValidYearDate(String dateString){
		boolean convertSuccess = true;
		try{
			Pattern p = Pattern.compile("\\d{4}");
			Matcher m = p.matcher(dateString);
			if(!m.matches()){
				convertSuccess = false;
			}
		}catch(Exception e){
//			e.printStackTrace();
			convertSuccess = false;
		}
		return convertSuccess;
	}

}
