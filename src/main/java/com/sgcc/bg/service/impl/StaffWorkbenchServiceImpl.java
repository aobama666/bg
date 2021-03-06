package com.sgcc.bg.service.impl;

import com.sgcc.bg.mapper.BgConfigEfficacyMapper;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.sgcc.bg.common.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.mapper.BGMapper;
import com.sgcc.bg.mapper.StaffWorkbenchMaper;
import com.sgcc.bg.model.ProcessRecordPo;
import com.sgcc.bg.model.WorkHourInfoPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IStaffWorkbenchService;

@Service
public class StaffWorkbenchServiceImpl implements IStaffWorkbenchService{
	@Autowired
	private BGMapper bgMapper;
	@Autowired
	private StaffWorkbenchMaper SWMapper;
	@Autowired
	private BgConfigEfficacyMapper bgConfigEfficacyMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private DataDictionaryService dict;


	private static Logger SWServiceLog =  LoggerFactory.getLogger(StaffWorkbenchServiceImpl.class);

	@Override
	public List<Map<String, String>> getWorkingHourInfo(String selectedDate) {
		/*if(Rtext.isEmpty(selectedDate)){
			selectedDate=DateUtil.getFormatDateString(new Date(),"yyyy-MM-dd");
		}*/

		//取月初和月末
		Map<String,String> dateMap = dateBeginEnd(selectedDate);
		String dataBegin = dateMap.get("dateBegin");
		String dataEnd = dateMap.get("dateEnd");

		CommonUser commonUser = webUtils.getCommonUser();
		String currentUsername = commonUser.getUserName();
		String currentHrcode = commonUser.getSapHrCode();
		List<Map<String, String>>  list=SWMapper.getWorkingHourInfo(dataBegin,dataEnd,currentUsername);
		//获取默认审核人
		Map<String, String> approver = getDefaultApprover();
		String approverHrcode = approver==null?"":approver.get("hrcode");
		for (Map<String, String> map : list) {
			String category = map.get("CATEGORY");
			String proUserHrcode = map.get("PRO_USER_HRCODE");
			
			if("BP,CG,NP".indexOf(category)!=-1){
				map.put("EDITABLE", "true");
			}else if(currentHrcode.equals(proUserHrcode) && !currentHrcode.equals(approverHrcode)){
				map.put("EDITABLE", "true");
			}else{
				map.put("EDITABLE", "false");
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> workingHoursMap(String selectedDate) {
		//取一天工作时长
		Integer TIME = Integer.parseInt(ConfigUtils.getConfig("KQ_everyday_workDate"));
		//取月初和月末
		Map<String,String> dateMap = dateBeginEnd(selectedDate);
		String dataBegin = dateMap.get("dateBegin");
		String dataEnd = dateMap.get("dateEnd");
		CommonUser commonUser = webUtils.getCommonUser();
		String currentUsername = commonUser.getUserName();
		String currentHrcode = commonUser.getSapHrCode();
		Double fillSum = SWMapper.fillWorkingHour(dataBegin,dataEnd,currentUsername);
		Map map = new HashMap();
		map.put("fillSum",fillSum);//已填报工时

		String[] str= selectedDate.split("-");
		String year=	String.valueOf(str[0]);
		String month=	String.valueOf(str[1]);
		Map<String ,Object> efficacyInfo=new HashMap<>();
		efficacyInfo.put("year",year);
		efficacyInfo.put("month",month);
		List<Map<String ,Object>> efficacyList=bgConfigEfficacyMapper.selectForConfigEfficacy(efficacyInfo);
        if(!efficacyList.isEmpty()){
			String  isEfficacy=efficacyList.get(0).get("IS_EFFICACY").toString();
			if(isEfficacy.equals("0")){
				selectedDate=DateUtil.getFormatStringToString(selectedDate,"yyyy-MM");
				Map<String ,Object> efficacyMap=bgConfigEfficacyMapper.selectForBcxx(selectedDate);
				String    fillSumKQ=   efficacyMap.get("FILLSUMKQ").toString();
				map.put("fillSumKQ", fillSumKQ);//月度工时=8h*工作日+36h
				return map;
			}
		}
		Map<String,Object> fillSumKQ = SWMapper.fillKQWorkingHour(dataBegin,dataEnd,currentUsername);
		BigDecimal fullTime = ((BigDecimal) fillSumKQ.get("fullTime")).multiply(BigDecimal.valueOf(TIME));
		BigDecimal overTime = (BigDecimal) fillSumKQ.get("overTime");
		map.put("fillSumKQ",fullTime.add(overTime));//月度工时=考勤工时
		return map;
	}

	@Override
	public List<Map<String, String>> getProjectsByDate(String selectedDate,String proName,String proNumber) {
		String username = webUtils.getUsername();
		CommonCurrentUser user = userUtils.getCommonCurrentUserByUsername(username);
		String deptId = user==null?"":user.getDeptId();

		//取月初和月末
		Map<String,String> dateMap = dateBeginEnd(selectedDate);
		String dataBegin = dateMap.get("dateBegin");
		String dataEnd = dateMap.get("dateEnd");

        //去重（查询出符合填报条件的项目信息 如果时间段内有多个负责人会查询出相同的信息  这时去重 ）
		List<Map<String, String>>  list=SWMapper.getProjectsByDate(dataBegin,username,deptId,proName,proNumber,dataEnd);
		List<Map<String,String>> listMap = new ArrayList<>();
		Set<String> set = new HashSet();
		Map<String,Map> msp = new HashMap<>();
		for(Map<String,String> map : list){
			String id = map.get("ID");
			//map.remove("ID");
			msp.put(id,map);
        }
        set = msp.keySet();
        for(String key : set){
        	Map newMap = msp.get(key);
        	newMap.put(set,key);
			listMap.add(newMap);
		}
		//List<Map<String,String>> listMap = new ArrayList<>(set);
		return listMap;
	}

	@Override
	public int addWorkHourInfo(WorkHourInfoPo wp) {
		return SWMapper.addWorkHourInfo(wp);
	}

	@Override
	public int updateWorkHourInfo(WorkHourInfoPo wp) {
		return SWMapper.updateWorkHourInfoById(wp);
	}
	
	@Override 
	public boolean isConmmited(String id){
		String status=SWMapper.getFieldOfWorkHourById(id, "status");
		if("1".equals(status) || "3".equals(status)){
			return true;
		}
		return false;
	}
	
	@Override 
	public boolean isExamined(String id){
		String status=SWMapper.getFieldOfWorkHourById(id, "status");
		if("2".equals(status) || "3".equals(status)){
			return true;
		}
		return false;
	}
	
	@Override 
	public boolean canExamine(String id){
		String status=SWMapper.getFieldOfWorkHourById(id, "status");
		if("1".equals(status)){
			return true;
		}
		return false;
	}
	@Override
	public boolean canExamined(String id){
		String status=SWMapper.getFieldOfWorkHourById(id, "status");
		if("3".equals(status)){
			return true;
		}
		return false;
	}
	
	@Override
	public void changeWorkHourInfoStatus(String id, String status) {
		SWMapper.setFieldOfWorkHourById(id, "status", status,webUtils.getUsername(),new Date());
	}

	@Override
	public int deleteWorkHourInfoById(String id) {
		return SWMapper.InvalidWorkHourInfoById(id,webUtils.getUsername(),new Date());
	}
	
	@Override
	public String exportDIYItems(String startDate, String endDate, String proIds, HttpServletResponse response) {
		List<Map<String, String>> dataList=new ArrayList<Map<String, String>>();
		Calendar calendar1 = Calendar.getInstance();// 开始日期
		Calendar calendar2 = Calendar.getInstance();// 结束的日期
		calendar1.setTime(DateUtil.fomatDateMonth(startDate));
		calendar2.setTime(DateUtil.fomatDateMonth(endDate));
		String[] ids=proIds.split(",");
		String currentUsername = webUtils.getUsername();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(currentUsername);
		String currentHrcode=currentUser==null?"":currentUser.getHrCode();
		String currentDeptId = currentUser==null?"":currentUser.getDeptId();
		Map<String, String> approverMap=getDefaultApprover();

		while (calendar1.compareTo(calendar2)<=0) {
			String dataStr=DateUtil.getFormatDateString(calendar1.getTime(),"yyyy-MM");

			//取月初和月末
			Map<String,String> dateMap = dateBeginEnd(dataStr);
			String dataBegin = dateMap.get("dateBegin");
			String dataEnd = dateMap.get("dateEnd");

			//过滤掉节假日
			//int dayType=SWMapper.getDayType(dataStr);
			//SWServiceLog.info(dataStr+" 类型： "+dayType);
			
			//if(dayType==0){
			for (int i = 0; i < ids.length; i++) {
				String proId=ids[i];
				//校验用户是否有指定项目的提报资格
				int result=SWMapper.validateSelectedDateAndDeptId(proId,currentUsername,dataBegin,currentDeptId,dataEnd);
				if(result>0){
					//Map<String, String> proMap=SWMapper.getProInfoByProId(proId);//如果查询的proid相同，则返回上一个map
					Map<String, String> proMap=SWMapper.getProInfoByProIdDate(proId,dataBegin,dataEnd);//如果查询的proid相同，则返回上一个map
					if(proMap!=null) {
						Map<String, String> dataMap = new HashMap<>();
						dataMap.putAll(proMap);
						//如果负责人为空或者本人即负责人
						if (Rtext.isEmpty(proMap.get("HRCODE")) || (currentHrcode).equals(proMap.get("HRCODE"))) {
							dataMap.put("HRCODE", approverMap == null ? "" : approverMap.get("hrcode"));
							dataMap.put("PRINCIPAL", approverMap == null ? "" : approverMap.get("name"));
						}
						dataMap.put("DATE", dataStr);
						dataList.add(dataMap);
					}
				}
			}
			
			calendar1.add(Calendar.MONTH, 1);// 把日期往后增加一天
		}
		Object[][] title = { 
							 { "填报月度\r\n（必填，格式：YYYY-MM）", "DATE","nowrap"},
							 { "类型\r\n（必填）", "CATEGORY" ,"nowrap"},
							 { "任务编号\r\n（项目工作必填，非项目工作不填）", "PROJECT_NUMBER" ,"nowrap"}, 
							 { "任务名称\r\n（选填）","PROJECT_NAME","nowrap"},
							 { "工作内容简述\r\n（选填，200字以内）",""}, 
							 { "投入工时(h)\r\n(必填，数字 ）","","nowrap"},
							 { "审核人员姓名\r\n（选填）","PRINCIPAL","nowrap"}, 
							 { "审核人员员工编号\r\n（项目前期、常规工作必填，工作任务负责人必填）","HRCODE","nowrap"} 
							};
		ExportExcelHelper.getExcel(response, "工时填报定制模板", title, dataList, "normal");
		return "success";
	}

	@Override
	public String[] parseWorkHourExcel(InputStream in) {
		HSSFWorkbook wb = null;
		//保存通过验证的工时信息
		List<WorkHourInfoPo> whList=new ArrayList<>();
		//保存出错信息
		List<Map<String,Object>> errorList = new ArrayList<>();
		//存储在ftp出错文件名称
		String errorUUID = "";
		try{
			POIFSFileSystem fs = null;
			HSSFSheet sheet;
			HSSFRow row;
			HSSFCell cell;
			// 获取POI操作对象
			fs = new POIFSFileSystem(in);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
			// 得到sheet内总行数
			int rows = sheet.getLastRowNum();
			
			//从数据字典中获取项目类型
			Map<String,String> dictMap=dict.getDictDataByPcode("category100002");
			
			//获取所有项目编号存入一个集合
			List<String> list=bgMapper.getAllBgNumbers();
			String regex = "^([1-9]+0*|[1-9]*\\.[05]|0\\.5)$";
			SWServiceLog.info("项目信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[9];
			for (int i = 1; i <=rows; i++) {// 从第2行开始是正式数据
				// 获取正式数据并封装进cellValue数组中
				StringBuffer checkStr = new StringBuffer();
				SWServiceLog.info("第" + (i + 1) + "行");
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				for (int c = 0; c < cellValue.length; c++) {
					cell= row.getCell(c);// StringUtils.trimToEmpty(sheet.getCell(c,
					if (cell != null) {
						cellValue[c]=ExcelUtil.getStringCellValueForOnLine(cell);
					}else{
						cellValue[c]="";
						SWServiceLog.info("cell is null");
					}
					checkStr.append(cellValue[c]);
					SWServiceLog.info("cellValue is " + cellValue[c]);
				}
				if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {// 校验此行是否为空
					StringBuffer errorInfo = new StringBuffer();
					Set<Integer> errorNum = new HashSet<Integer>();
					String proId="";//项目id
					Map<String, String>  proMap=null;//项目信息
					String principal="";//项目负责人
					String currentUsername = webUtils.getUsername();
					CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(currentUsername);
					String currentUserHrcode=currentUser==null?"":currentUser.getHrCode();//当前登录人hrcode
					String currentUserId = currentUser==null?"":currentUser.getUserId();//当前登录人的id
					String currentDeptId = currentUser==null?"":currentUser.getDeptId();//当前登录人所处处室（部门）id
					CommonCurrentUser approverUser=null;

					//取月初和月末
					Map<String,String> dateMap = dateBeginEnd(cellValue[1]);
					String dataBegin = dateMap.get("dateBegin");
					String dataEnd = dateMap.get("dateEnd");

					boolean isNP = ("常规工作".equals(cellValue[2]) && Rtext.isEmpty(cellValue[3]))?true:false;//判断是否非项目
					
					//获取当前登录人
					// 对要导入的文件内容进行校验
					// 填报月度 必填;格式
					if (cellValue[1] == null || "".equals(cellValue[1])) {
						errorInfo.append("填报月度不能为空！ ");
						errorNum.add(1);
					} else if (!DateUtil.isValidDate(cellValue[1]) && !DateUtil.isValidDateYearMonth(cellValue[1])) {
						errorInfo.append("填报月度填写有误！ ");
						errorNum.add(1);
					}
					// 项目类型校验 必填
					if (cellValue[2] == null || "".equals(cellValue[2])) {
						errorInfo.append("项目类型不能为空！ ");
						errorNum.add(2);
					}else if(!dictMap.containsValue(cellValue[2])){
						errorInfo.append("无此项目类型！ ");
						errorNum.add(2);
					}
					// 校验项目编号(无论是什么类型，只要不是非项目，就校验项目编号)
					if(!isNP){
						//如果项目类型不为非项目工作则校验其wbs编号
						if (cellValue[3] == null || "".equals(cellValue[3])) {
							errorInfo.append("项目编号不能为空！ ");
							errorNum.add(3);
						}else if(!list.contains(cellValue[3])){
							errorInfo.append("项目中不存在此项目编号！ ");
							errorNum.add(3);
						}else{
							proId=bgMapper.getProIdByBgNmuber(cellValue[3]);
							proMap = bgMapper.getProInfoByProId(proId);
							//principal = SWMapper.getPrincipalByProId(proId);
							principal = SWMapper.getPrincipalByProIdDate(proId,dataBegin,dataEnd);
							principal = principal==null?"":principal;//如果是项目前期和常规项目则获取不到负责人
						}
					}
					
					//项目存在
					if(!Rtext.isEmpty(proId)){
						//验证项目类型是否一致
						String category = proMap.get("category");
						category = dictMap.get(category);
						if(!errorNum.contains(2) && !cellValue[2].equals(category)){
							errorInfo.append("项目类型与项目不符！ ");
							errorNum.add(2);
						}
						
						//验证项目状态是否处于进行中
						String projectStatus = proMap.get("projectStatus");
						String[] staArr = {"未启动","已启动","已暂停","已完成","已废止","状态未知"};
						if(!"1".equals(projectStatus)){
							errorInfo.append("项目"+staArr[Rtext.ToInteger(projectStatus,5)]+"，无法填报！ ");
							errorNum.add(3);
						}
						
						if(!errorNum.contains(2)){
							if(!"项目前期".equals(cellValue[2]) && !"常规工作".equals(cellValue[2])){
								//如项目存在并且不为项目前期和常规工作，则校验其是否存在于该项目
								int result=SWMapper.validateStaff(proId,currentUsername);
								if(result==0){
									errorInfo.append("提报人不属于该项目！ ");
									errorNum.add(3);
								}
							}else{
								//项目前期和常规工作，则校验其是是否属于属于项目指定的处室或部门
								int result = SWMapper.validateDeptId(proId,currentDeptId);
								if(result==0){
									errorInfo.append("提报人不属于项目所属组织！ ");
									errorNum.add(3);
								}
							}
						}
						
						//如果项目存在则再校验填报日期是否超出范围
						if(!errorNum.contains(1)){
							//取月初和月末
							/*Map<String,String> dateMap = dateBeginEnd(cellValue[1]);
							String dataBegin = dateMap.get("dateBegin");
							String dataEnd = dateMap.get("dateEnd");*/

							int result=SWMapper.validateSelectedDateScope(proId,currentUsername,dataBegin,dataEnd);
							//int result=SWMapper.validateSelectedDate(proId,currentUsername,cellValue[1]);
							if(result==0){
								errorInfo.append("填报月度不在项目周期或参与周期内！ ");
								errorNum.add(1);
							}
						}
					}
					
					//非项目工作的名称是直接存入表中，校验其长度不能大于50个字
					if(isNP && !Rtext.isEmpty(cellValue[4]) && cellValue[4].length()>50){
						errorInfo.append("项目名称不能大于50个字！ ");
						errorNum.add(4);
					}
					
					// 工作内容校验
					if (cellValue[5] == null || "".equals(cellValue[5])) {
						/*errorInfo.append("工作内容不能为空！");
						errorNum.add(5);*/
						//暂时不做工作内容的必填校验
					} else if (cellValue[5].length() > 200) {
						errorInfo.append("工作内容不能超过200字！ ");
						errorNum.add(5);
					}
					
					// 投入工时 必填 数字
					if (cellValue[6] == null || "".equals(cellValue[6])) {
						errorInfo.append("投入工时不能为空！ ");
						errorNum.add(6);
					}else if (!cellValue[6].matches(regex)) {
						errorInfo.append("计划投入工时填写有误！ ");
						errorNum.add(6);
					}
					
					// 审核人员员工编号，项目前期、常规工作必填，工作任务负责人必填
					if(!errorNum.contains(2)){
						if("项目前期".equals(cellValue[2]) || "常规工作".equals(cellValue[2]) || principal.equals(currentUsername)){
							if(cellValue[8] == null || "".equals(cellValue[8])){
								errorInfo.append("项目前期、常规工作、工作任务负责人的审核人员员工编号不能为空！ ");
								errorNum.add(8);
							}else{
								approverUser=userUtils.getCommonCurrentUserByHrCode(cellValue[8]);
								if(approverUser==null){
									errorInfo.append("审核人员员工编号错误！ ");
									errorNum.add(8);
								}else{
									if(cellValue[8].equals(currentUserHrcode)){//审核人是自己
										String subType=SWMapper.getTopSubmitType(currentUserId);
										int subTypeNum=Rtext.ToInteger(subType, 0);
										if(subTypeNum>5){//等级不够默认通过
											errorInfo.append("审核人员不具备审核权限！ ");
											errorNum.add(8);
										}
									}else{
										List<Map<String,String>> approverList=getApproverList(currentUsername);
										boolean containsApprover=false;
										for (Map<String, String> map : approverList) {
											if(approverUser.getHrCode().equals(map.get("hrcode"))){
												containsApprover=true;
												break;
											}
										}
										if(!containsApprover){
											errorInfo.append("审核人员不具备审核权限！ ");
											errorNum.add(8);
										}
									}
								}
							}
						}
					}

					//效验累计工时是否超过月度工时
					/*if(!errorNum.contains(1) && !errorNum.contains(6)) {
						Map<String,Object> workingHoursMap = workingHoursMap(cellValue[1]);
						Double fillSum = (Double) workingHoursMap.get("fillSum");
						BigDecimal fillSumKQ = (BigDecimal) workingHoursMap.get("fillSumKQ");
						fillSum+=Double.parseDouble(cellValue[6]);
						BigDecimal sum = BigDecimal.valueOf(fillSum);
						int res = sum.compareTo(fillSumKQ);
						if(res==1){
							errorInfo.append("填报工时超出月度工时！ ");
							errorNum.add(6);
						}
					}*/
					// 校验结束，分流数据
					if ("".equals(errorInfo.toString())) {// 通过校验
						WorkHourInfoPo wh=new WorkHourInfoPo();
						wh.setId(Rtext.getUUID());
						if(isNP){
							wh.setProId("");
							wh.setApprover(approverUser.getUserName());
							wh.setCategory("NP");
							wh.setProName(cellValue[4]);
						}else{
							//如果为项目工作，根据项目id获取项目负责人
							wh.setProId(proId);
							//如果提报人就是项目负责人，则以审核人是所填审核人；如果是参与人或者是非项目，则默认时项目负责人
							if(Rtext.isEmpty(principal) || principal.equals(currentUsername)) principal = approverUser.getUserName();
							wh.setApprover(principal);
							//如果为非项目工作，项目类型、名称与项目编号保持一致
							if("项目前期".equals(cellValue[2]) || "常规工作".equals(cellValue[2])){
								wh.setCategory("项目前期".equals(cellValue[2])?"BP":"CG");
								wh.setProName(cellValue[4]);
							}else{
								wh.setCategory(proMap.get("category"));
								wh.setProName(proMap.get("projectName"));
							}
						}

						wh.setWorkTimeEnd(DateUtil.fomatDate(dataEnd));
						wh.setWorkTimeBegin(DateUtil.fomatDate(dataBegin));
						//wh.setWorkTime(DateUtil.fomatDate(dataBegin));
						wh.setJobContent(cellValue[5]);
						wh.setWorkHour(Double.parseDouble(cellValue[6]));
						wh.setWorker(currentUsername);
						//获取填报人填报日期时的信息
						//CommonCurrentUser user=userUtils.getCommonCurrentUserByUsername(currentUsername,cellValue[1]);
						CommonCurrentUser user=userUtils.getCommonCurrentUserByUsernameScope(currentUsername,dataBegin,dataEnd);
						wh.setDeptId(user==null?"":user.getpDeptId());
						wh.setLabId(user==null?"":user.getDeptId());
						wh.setStatus("0");
						wh.setValid("1");
						wh.setCreateUser(currentUsername);
						wh.setCreateTime(new Date());
						wh.setUpdateUser(currentUsername);
						wh.setUpdateTime(new Date());
						wh.setSrc("1");
						//保存正确数据   
						whList.add(wh);
					} else {// 未通过校验
						Map<String,Object> map=new HashMap<>();
						map.put("SQNUM",cellValue[0]);
						map.put("DATE",cellValue[1].substring(0,7));
						map.put("CATEGORY",cellValue[2]);
						map.put("PROJECT_NUMBER",cellValue[3]);
						map.put("PROJECT_NAME",cellValue[4]);
						map.put("JOB_CONTENT",cellValue[5]);
						map.put("WORKING_HOUR",cellValue[6]);
						map.put("PRINCIPAL",cellValue[7]);
						map.put("HRCODE",cellValue[8]);
						map.put("errInfo",errorInfo.toString());
						map.put("errSet",errorNum);
						errorList.add(map);
					}
				}
			}

			// 返回错误数据
			if (errorList.size() > 0) {
				SWServiceLog.info("出错的项目： " + errorList);
				// 生成错误信息文件
				Object[][] title = { 
						 { "序号\r\n（选填）", "SQNUM","nowrap"},
						 { "填报月度 \r\n（必填，格式：YYYY-MM）", "DATE","nowrap"},
						 { "项目类型\r\n（必填）", "CATEGORY","nowrap" },
						 { "工作任务编号\r\n（常规工作如果没有可不填）", "PROJECT_NUMBER","nowrap" }, 
						 { "项目名称\r\n（选填）","PROJECT_NAME","nowrap"},
						 { "工作内容\r\n（必填 200字以内）","JOB_CONTENT"}, 
						 { "投入工时（h）\r\n（必填 数字 h）","WORKING_HOUR","nowrap"},
						 { "审核人员姓名\r\n（选填）","PRINCIPAL","nowrap"}, 
						 { "审核人员员工编号\r\n（项目前期、常规工作必填，\r\n工作任务负责人必填）","HRCODE","nowrap"},
						 { "错误说明","errInfo"}
						};
				
				errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
				SWServiceLog.info("errorUUID: " + errorUUID);
			}
		}catch (Exception e) {
			e.printStackTrace();
			String[] object = {"Error:项目信息导入失败，请重新导入！",""};
			return object;
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (WorkHourInfoPo wh : whList) {
			SWMapper.addWorkHourInfo(wh);
		}
		String[] object = {"成功导入项目信息"+whList.size()+"条，失败"+errorList.size()+"条",errorUUID};
		return object;
	}

	@Override
	public String checkWorkHour(String selectedDate,double totalHours) {
		Date date=DateUtil.fomatDate(selectedDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int start=calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int end=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//获取当月已提交加班总工时数(不含当天)
		double monthOvertime=SWMapper.getPeriodOvertime(
				webUtils.getUsername(),
				selectedDate,
				selectedDate.substring(0,(selectedDate.lastIndexOf("-")+1))+start,
				selectedDate.substring(0,(selectedDate.lastIndexOf("-")+1))+end);
		SWServiceLog.info("目前当月已提交（通过）加班工时："+monthOvertime+"(h)");
		
		//获取当天已提交总工时数
		double submitedWorkHour=SWMapper.todaySubmitedWorkHour(webUtils.getUsername(),selectedDate);
		double dayWorkHour=totalHours+submitedWorkHour;
		SWServiceLog.info("目前当天已提交总工时（不只加班）："+dayWorkHour+"(h)");
		
		//判断某天的类型，0工作日，1节假日
		double totalOvertime=monthOvertime;
		int dayType=SWMapper.getDayType(selectedDate);
		SWServiceLog.info(selectedDate+" 类型： "+dayType);
		if(dayType==0){
			if(dayWorkHour>11){
				return "工作日允许录入工时不能超过11小时";
			}else if(dayWorkHour>8){
				totalOvertime+=(dayWorkHour-8);
			}
		}else if(dayType==1){
			if(dayWorkHour>8){
				return "周末、节假日允许录入工时不能超过8小时";
			}else{
				totalOvertime+=dayWorkHour;
			}
		}
		if(totalOvertime>36){
			return "每月加班不能超过36小时，本月已加班"+monthOvertime+"小时，当天加班时长不能超过"+(36-monthOvertime)+"小时";
		}
		return "";
	}

	@Override
	public List<Map<String, String>> getAllProjects(String startDate,String endDate,String proName,String proNumber) {
		List<Map<String, String>> dataList=new ArrayList<Map<String, String>>();
		
		Calendar calendar1 = Calendar.getInstance();// 开始日期
		Calendar calendar2 = Calendar.getInstance();// 结束的日期
		calendar1.setTime(DateUtil.fomatDateMonth(startDate));
		calendar2.setTime(DateUtil.fomatDateMonth(endDate));
		
		String username=webUtils.getUsername();
		CommonCurrentUser user = userUtils.getCommonCurrentUserByUsername(username);
		String deptId = user==null?"":user.getDeptId();
		
		//获取登录人名下所有已启动（不含结束和废止）项目项目
		//List<Map<String, String>> proList=SWMapper.getAllProjects(webUtils.getUsername());
//		for (Map<String, String> map : proList) {
//			String proId=map.get("ID");
//			while (calendar1.compareTo(calendar2)<=0) {
//				String dataStr=DateUtil.getFormatDateString(calendar1.getTime(),"yyyy-MM-dd");
//				//校验指定日期下，指定用户是否有指定项目的提报资格
//				System.out.println("校验项目"+map.get("PROJECT_NAME")+"/日期："+dataStr);
//				int result=SWMapper.validateSelectedDate(dataStr,proId,username);
//				if(result>0){
//					dataList.add(map);
//					break;
//				}
//				calendar1.add(Calendar.DATE, 1);// 把日期往后增加一天
//			}
//			calendar1.setTime(DateUtil.fomatDate(startDate));//重置开始日期
//		}
		Set<String> proNumberSet = new HashSet<>();
		while (calendar1.compareTo(calendar2)<=0) {
			String dataStr=DateUtil.getFormatDateString(calendar1.getTime(),"yyyy-MM");
			String dataStrEnd=DateUtil.getFormatDateString(calendar2.getTime(),"yyyy-MM");

			//取开始时间月初
			Map<String,String> dateMapBegin = dateBeginEnd(dataStr);
			String dataBegin = dateMapBegin.get("dateBegin");
			//取结束时间月末
			Map<String,String> dateMapEnd = dateBeginEnd(dataStrEnd);
			String dataEnd = dateMapEnd.get("dateEnd");
			//String dataEnd = dateMap.get("dateEnd");

			//获取指定日期下可填报的项目信息
			List<Map<String, String>> availableProjects = SWMapper.getProjectsByDate(dataBegin, username, deptId , proName, proNumber,dataEnd);
			
			for (Map<String, String> map : availableProjects) {
				if(proNumberSet.add(map.get("PROJECT_NUMBER"))) dataList.add(map);
			}
			
			calendar1.add(Calendar.MONTH, 1);// 把日期往后增加一天
		}
		
		return dataList;
	}

	@Override
	public int recallWorkHour(String id) {
		String currentUsername=webUtils.getUsername();
		//记录表中的记录设为失效
		//SWMapper.setFieldOfProcessRecordById(processId,"valid","0");
		//添加撤回操作记录
		String processId = addRecallRecord(id, currentUsername);
		//工时表中状态改为未提交，更新记录id
		return SWMapper.recallWorkHour(id,currentUsername,new Date(),processId);
	}

	/**
	 * 添加操作记录到流程记录表
	 * @param processId 如果为null则新增记录，否则执行更新
	 * @param bussinessId
	 * @param processUsername
	 * @param result
	 * @param note
	 * @return
	 */
	private String addProcessRecord(String processId,String bussinessId,String processUsername, String result, String note) {
		ProcessRecordPo pr=new ProcessRecordPo();
		pr.setId((processId==null)?Rtext.getUUID():processId);
		pr.setBussinessId(bussinessId);
		//获取处理人当前信息
		CommonCurrentUser processUser=userUtils.getCommonCurrentUserByUsername(processUsername);
		pr.setProcessUserId(processUsername);
		pr.setProcessDeptId(processUser==null?"":processUser.getpDeptId());
		pr.setProcessLabtId(processUser==null?"":processUser.getDeptId());
		pr.setProcessResult(result);
		pr.setProcessNote(note);
		pr.setProcessCreateTime(new Date());
		pr.setProcessUpdateTime(new Date());
		//pr.setProcessStep("");
		pr.setValid(1);
		
		int res;
		if(processId==null){
			res= SWMapper.addProcessRecord(pr);
		}else{
			res= SWMapper.updateProcessRecord(pr);
		}
		
		if(res==1){
			return pr.getId();
		}
		return "";
	}
	
	//审批状态result : 0 已提交 1 待审批 2 已通过 3 已驳回 4 已撤回
	@Override
	public String addSubmitRecord(String bussinessId,String processUsername){
		addProcessRecord(null,bussinessId,processUsername,"0","");
		return addProcessRecord(null,bussinessId,processUsername,"1","");
	}
	
	@Override
	public String addRecallRecord(String bussinessId,String processUsername){
		String processId=SWMapper.getFieldOfWorkHourById(bussinessId, "process_id");
		return addProcessRecord(processId,bussinessId,processUsername,"4","");
	}
	@Override
	public String addExamineRecord(String bussinessId,String processUsername, String result, String note){
		String processId=SWMapper.getFieldOfWorkHourById(bussinessId, "process_id");
		return addProcessRecord(processId,bussinessId,processUsername,result,note);
	}
	
	@Override
	public	List<Map<String,String>> getApproverList(String username){
		/*CommonUser user=webUtils.getCommonUser();
		String username=user.getUserName();*/
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(username);
		
		if(currentUser==null) return new ArrayList<>();
		
		String userId=currentUser.getUserId();
		String deptId=currentUser.getDeptId();//获取当前提报人当前所在部门
		return SWMapper.getApproverList(userId,deptId);
	}

	@Override
	public Map<String, String> getDefaultApprover() {
		CommonUser user=webUtils.getCommonUser();
		String username=user.getUserName();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(username);
		Map<String,String> approver=new HashMap<>();
		
		if(currentUser==null){
			approver.put("hrcode", "");
			approver.put("name", "");
			return approver;
		}
		
		String hrcode=currentUser.getHrCode();
		String useralias=currentUser.getUserAlias();
		String userId=currentUser.getUserId();
		String deptId=currentUser.getDeptId();//获取当前提报人当前所在部门
		//获取当前提报人提交类型，取最大(数值最小的)
		/*院领导	    SUT1	1
		分管院领导		SUT2	2
		院助理副总师	SUT3	3
		部门行政正职	SUT4	4
		部门副职		SUT5	5
		部门助理副总师	SUT6	6
		技术专家		SUT7	7
		处室正职		SUT8	8
		处室副职		SUT9	9
		专业通道员工	SUT10	10
		*/
		String subType=SWMapper.getTopSubmitType(userId);
		int subTypeNum=Rtext.ToInteger(subType, 0);
		//如果审核类型在部门副职及以上，则默认审批通过，返回自己
		if(subTypeNum>5){
			Map<String, String> resMap = SWMapper.getDefaultApprover(subType,deptId);
			approver.put("hrcode", resMap==null?"":resMap.get("hrcode"));
			approver.put("name", resMap==null?"":resMap.get("name"));
		}else{
			approver.put("hrcode", hrcode);
			approver.put("name", useralias);
		}
		return approver;
	}
	
	@Override
	public String getApproverById(String id) {
		return SWMapper.getFieldOfWorkHourById(id, "approver");
	}

	/**
	 * 取一个月的月初和月末
	 * @param selectedDate
	 * @return
	 */
	private Map<String,String> dateBeginEnd(String selectedDate){
		Map<String,String> map = new HashMap();
		String[] str= selectedDate.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		//每月月初
		String dateBegin = DateUtil.getFirstDayOfMonth1(year,month);
		//每月月末
		String dateEnd = DateUtil.getLastDayOfMonth1(year,month);
		map.put("dateBegin",dateBegin);
		map.put("dateEnd",dateEnd);
		return map;
	}
}
