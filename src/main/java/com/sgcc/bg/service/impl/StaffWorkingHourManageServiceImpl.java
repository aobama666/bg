package com.sgcc.bg.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExcelUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BGMapper;
import com.sgcc.bg.mapper.StaffWorkbenchMaper;
import com.sgcc.bg.mapper.StaffWorkingHourManageMaper;
import com.sgcc.bg.model.WorkHourInfoPo;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IStaffWorkbenchService;
import com.sgcc.bg.service.IStaffWorkingHourManageService;
import com.sgcc.bg.service.OrganStuffTreeService;

@Service
public class StaffWorkingHourManageServiceImpl implements IStaffWorkingHourManageService {
	@Autowired
	private StaffWorkingHourManageMaper smMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private BGMapper bgMapper;
	@Autowired
	private StaffWorkbenchMaper SWMapper;
	@Autowired
	private OrganStuffTreeService organStuffTreeService;
	@Autowired
	private DataDictionaryService dict;
	@Autowired
	private IStaffWorkbenchService swService;
	
	
	private static Logger smServiceLog =  LoggerFactory.getLogger(StaffWorkingHourManageServiceImpl.class);
	
	/**
	 * 获取用户有专责权限的部门（处室）
	 * @param username
	 * @param deptCode 
	 * @return 如果limit为空，则返回''
	 */
	@Override
	public String getLimitDeptIds(String username, String deptCode){
		String limit="";
		if(Rtext.isEmpty(deptCode)){//如果没有指定，则默认全院范围
			deptCode="41000001";
		}
		//List<Map<String, Object>> organList=organStuffTreeService.queryDeptByCurrentUserPriv(deptCode, "2", username);
		List<Map<String, Object>> organList=organStuffTreeService.getUserAuthoryOrgan(username, deptCode);
		if(organList!=null&&organList.size()>0){
			StringBuffer sb = new StringBuffer();
			for(Map<String, Object> obj:organList){
				sb.append("'").append(obj.get("deptId").toString()).append("',");
			}
			limit=sb.toString();
			limit = limit.substring(0, limit.lastIndexOf(","));
		}
		return Rtext.isEmpty(limit)?"''":limit;
	}
	
	@Override
	public List<Map<String, String>> searchForWorkHourInfo(String startDate, String endDate, String deptCode,
			String category, String proName, String empName, String status) {
		String deptIds=getLimitDeptIds(webUtils.getUsername(),deptCode);
		smServiceLog.info("得到的权限部门ids： "+deptIds);
		List<Map<String, String>> list=smMapper.getWorkHourInfoByCondition(
				deptIds,startDate,endDate,category,proName,empName,status);
		return list;
	}

	/*@Override
	public int getItemCount(String startDate, String endDate, String deptCode, String category, String proName,
			String empName, String status) {
		String deptIds=getLimitDeptIds(webUtils.getUsername(),deptCode);
		return smMapper.getItemCount(deptIds,startDate, endDate, category, proName, empName, status);
	}*/

	@Override
	public Map<String, String> getWorkHourInfoById(String whId) {
		Map<String, String> whMap=smMapper.getWorkHourInfoById(whId);
		return whMap;
	}

	@Override
	public String checkWorkHour(String username,String selectedDate,double totalHours) {
		Date date=DateUtil.fomatDate(selectedDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int start=calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int end=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		//获取当月已提交加班总工时数(不含当天)
		double monthOvertime=smMapper.getPeriodOvertime(
				username,
				selectedDate,
				selectedDate.substring(0,(selectedDate.lastIndexOf("-")+1))+start,
				selectedDate.substring(0,(selectedDate.lastIndexOf("-")+1))+end);
		smServiceLog.info("目前当月已提交"+monthOvertime+"(h)");
		
		//获取当天已提交总工时数
		double submitedWorkHour=smMapper.todaySubmitedWorkHour(username,selectedDate);
		double dayWorkHour=totalHours+submitedWorkHour;
		smServiceLog.info("目前当天已提交"+dayWorkHour+"(h)");
		
		//判断某天的类型，0工作日，1节假日
		int dayType=smMapper.getDayType(selectedDate);
		smServiceLog.info(selectedDate+" 类型： "+dayType);
		if(dayType==0){
			if(dayWorkHour>11){
				return "工作日允许录入工时不能超过11小时";
			}else if(dayWorkHour>8){
				monthOvertime+=dayWorkHour-8;
			}
		}else if(dayType==1){
			if(dayWorkHour>8){
				return "周末、节假日允许录入工时不能超过8小时";
			}else{
				monthOvertime+=totalHours;
			}
		}
		if(monthOvertime>36){
			return "每月加班不能超过36小时，本月已加班"+monthOvertime+"小时，当天加班时长不能超过"+(monthOvertime-36)+"小时";
		}
		return "";
	}

	@Override
	public int updateWorkHourInfo(WorkHourInfoPo wp) {
		return smMapper.updateWorkHourInfoById(wp);
	}

	@Override
	public void changeWorkHourInfoStatus(String id, String status) {
		smMapper.changeWorkHourInfoStatus(id,status);
	}

	@Override
	public int deleteWorkHourInfoById(String id) {
		return smMapper.InvalidWorkHourInfoById(id,webUtils.getUsername(),new Date());
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
			//Set<String> repeatChecker = new HashSet<String>();
			//从数据字典中获取项目类型
			Map<String,String> dictMap=dict.getDictDataByPcode("category100002");
			
			//获取所有项目编号存入一个集合
			List<String> list=bgMapper.getAllBgNumbers();
			String regex = "^([1-9]+|[1-9]*\\.[05]|0\\.5)$";
			smServiceLog.info("项目信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[11];
			for (int i = 1; i <=rows; i++) {// 从第2行开始是正式数据
				// 获取正式数据并封装进cellValue数组中
				StringBuffer checkStr = new StringBuffer();
				smServiceLog.info("第" + (i + 1) + "行");
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
						smServiceLog.info("cell is null");
					}
					checkStr.append(cellValue[c]);
					smServiceLog.info("cellValue is " + cellValue[c]);
				}
				if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {// 校验此行是否为空
					StringBuffer errorInfo = new StringBuffer();
					Set<Integer> errorNum = new HashSet<Integer>();
					String proId="";//项目id
					Map<String, String>  proMap=null;//项目信息
					String principal="";//项目负责人
					String currentUsername=webUtils.getUsername();
					CommonCurrentUser approverUser=null;
					CommonCurrentUser worker=null;
					// 对要导入的文件内容进行校验
					// 填报日期 必填;格式
					if (cellValue[1] == null || "".equals(cellValue[1])) {
						errorInfo.append("填报日期不能为空！ ");
						errorNum.add(1);
					} else if (!DateUtil.isValidDate(cellValue[1], "yyyy-MM-dd")) {
						errorInfo.append("填报日期填写有误！ ");
						errorNum.add(1);
					}
					// 项目类型校验 必填
					if (cellValue[2] == null || "".equals(cellValue[2])) {
						errorInfo.append("项目类型不能为空！ ");
						errorNum.add(2);
					}else if(!dictMap.containsValue(cellValue[2])){
						errorInfo.append("无此项目类型 ！");
						errorNum.add(2);
					}
					// 校验项目编号(无论是什么类型，只要是不是非项目，就校验项目编号)
					if(!"非项目工作".equals(cellValue[2])){
						//如果项目类型不为非项目工作则校验其项目编号
						if (cellValue[3] == null || "".equals(cellValue[3])) {
							errorInfo.append("项目编号不能为空！ ");
							errorNum.add(3);
						}else if(!list.contains(cellValue[3])){
							errorInfo.append("项目中不存在此项目编号！ ");
							errorNum.add(3);
						}else{
							proId=bgMapper.getProIdByBgNmuber(cellValue[3]);
							proMap = bgMapper.getProInfoByProId(proId);
							principal =SWMapper.getPrincipalByProId(proId);
						}
					}
					
					//项目存在
					if(!Rtext.isEmpty(proId)){
						//验证项目状态是否处于进行中
						String projectStatus = proMap.get("projectStatus");
						String[] staArr = {"已新建","已启动","已暂停","已完成","已废止","状态未知"};
						if(!"1".equals(projectStatus)){
							errorInfo.append("项目"+staArr[Rtext.ToInteger(projectStatus,5)]+"，无法填报！ ");
							errorNum.add(3);
						}
						
						//验证项目类型是否一致
						String category=proMap.get("category");
						category = dictMap.get(category);
						if(!errorNum.contains(2) && !cellValue[2].equals(category)){
							errorInfo.append("项目类型与项目不符！ ");
							errorNum.add(2);
						}
					}
					
					//非项目工作的名称是直接存入表中，校验其长度不能大于50个字
					if("非项目工作".equals(cellValue[2]) && !Rtext.isEmpty(cellValue[4]) && cellValue[4].length()>50){
						errorInfo.append("项目名称不能大于50个字！ ");
						errorNum.add(4);
					}
					
					// 工作内容校验
					if (cellValue[5] == null || "".equals(cellValue[5])) {
						/*errorInfo.append("工作内容不能为空！");
						errorNum.add(5); 暂不做工作内容必填校验*/
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
					//填报人员工编号校验
					if(cellValue[8] == null || "".equals(cellValue[8])){
						errorInfo.append("填报人员工编号不能为空！ ");
						errorNum.add(8);
					}else{
						worker=userUtils.getCommonCurrentUserByHrCode(cellValue[8]);
						if(worker==null){
							errorInfo.append("填报人员工编号错误！ ");
							errorNum.add(8);
						}else{
							//如填报人正确且项目存在
							//校验其是否存在于该项目
							if(!"非项目工作".equals(cellValue[2]) && !Rtext.isEmpty(proId)){
								int result=SWMapper.validateStaff(proId,worker.getUserName());
								if(result==0){
									errorInfo.append("提报人不属于该项目！  ");
									errorNum.add(8);
								}
							}
							//校验其是否属于当前专责管辖
							String currentLabId=worker.getDeptId();//报工人当时所处科室id
							String limitDdptIds=getLimitDeptIds(webUtils.getUsername(),"41000001");//当前专责有权限的所有部门id
							if(!limitDdptIds.contains(currentLabId)){
								errorInfo.append("提报人不在专责范围内！ ");
								errorNum.add(8);
							}
						}
						
					}
					
					//如果存在wbs/项目编号,且填报人真实存在则再次校验填报日期是否超出范围
					if(!"非项目工作".equals(cellValue[2]) && !Rtext.isEmpty(proId) && !errorNum.contains(1) && !errorNum.contains(8)){
						
						int result=SWMapper.validateSelectedDate(cellValue[1],proId,worker.getUserName());
						if(result==0){
							errorInfo.append("填报日期不在项目周期或参与周期内！ ");
							errorNum.add(1);
						}
					}
					// 审核人员员工编号，非项目工作或项目工作负责人为必填项
					if ("非项目工作".equals(cellValue[2]) || principal.equals(worker==null?"":worker.getUserName())) {
						if(cellValue[10] == null || "".equals(cellValue[10])){
							errorInfo.append("非项目工作、项目工作负责人的审核人员员工编号不能为空！ ");
							errorNum.add(10);
						}else{
							approverUser=userUtils.getCommonCurrentUserByHrCode(cellValue[10]);
							if(approverUser==null){
								errorInfo.append("审核人员员工编号错误！ ");
								errorNum.add(10);
							}else if(worker!=null){
								if(cellValue[10].equals(worker.getHrCode())){//审核人是自己
									String subType=SWMapper.getTopSubmitType(worker.getUserId());
									int subTypeNum=Rtext.ToInteger(subType, 0);
									if(subTypeNum>5){//等级不够默认通过
										errorInfo.append("审核人员不具备审核权限！ ");
										errorNum.add(10);
									}
								}else{
									List<Map<String,String>> approverList=swService.getApproverList(worker.getUserName());
									boolean containsApprover=false;
									for (Map<String, String> map : approverList) {
										if(approverUser.getHrCode().equals(map.get("hrcode"))){
											containsApprover=true;
											break;
										}
									}
									if(!containsApprover){
										errorInfo.append("审核人员不具备审核权限！ ");
										errorNum.add(10);
									}
								}
							}
						}
					}
					// 校验结束，分流数据
					if ("".equals(errorInfo.toString())) {// 通过校验
						WorkHourInfoPo wh=new WorkHourInfoPo();
						wh.setId(Rtext.getUUID());
						if("非项目工作".equals(cellValue[2])){
							wh.setProId("");
							wh.setApprover(approverUser.getUserName());
							wh.setCategory("NP");
							wh.setProName(cellValue[4]);
						}else{
							//如果为项目工作，根据项目id获取项目负责人
							wh.setProId(proId);
							//如果提报人就是项目负责人，则以审核人是所填审核人；如果是参与人，则默认时项目负责人
							wh.setApprover(principal.equals(worker.getUserName())?approverUser.getUserName():principal);
							//如果为非项目工作，项目类型、名称与项目编号保持一致
							wh.setCategory(proMap.get("category"));
							wh.setProName(proMap.get("project_name"));
						}
						wh.setWorkTime(DateUtil.fomatDate(cellValue[1]));
						wh.setJobContent(cellValue[5]);
						wh.setWorkHour(Double.parseDouble(cellValue[6]));
						//获取填报人填报日期时的信息
						CommonCurrentUser thenWorker=userUtils.getCommonCurrentUserByHrCode(cellValue[8],cellValue[1]);
						wh.setWorker(thenWorker.getUserName());
						wh.setDeptId(thenWorker.getpDeptId());
						wh.setLabId(thenWorker.getDeptId());
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
						map.put("DATE",cellValue[1]);
						map.put("CATEGORY",cellValue[2]);
						map.put("PROJECT_NUMBER",cellValue[3]);
						map.put("PROJECT_NAME",cellValue[4]);
						map.put("JOB_CONTENT",cellValue[5]);
						map.put("WORKING_HOUR",cellValue[6]);
						map.put("WORKER",cellValue[7]);
						map.put("WORKER_HRCODE",cellValue[8]);
						map.put("PRINCIPAL",cellValue[9]);
						map.put("PRINCIPAL_HRCODE",cellValue[10]);
						map.put("errInfo",errorInfo.toString());
						map.put("errSet",errorNum);
						errorList.add(map);
					}
				}
			}

			// 返回错误数据
			if (errorList.size() > 0) {
				smServiceLog.info("出错的项目： " + errorList);
				// 生成错误信息文件
				Object[][] title = { 
						 { "序号\r\n（选填）", "SQNUM" ,"nowrap"},
						 { "填报日期\r\n（必填，格式：YYYY-MM-DD）", "DATE" ,"nowrap"},
						 { "项目类型\r\n（必填）", "CATEGORY","nowrap"},
						 { "项目编号\r\n（项目工作必填，非项目工作不填）", "PROJECT_NUMBER","nowrap" }, 
						 { "项目名称\r\n（选填）","PROJECT_NAME","nowrap"},
						 { "工作内容\r\n（必填 200字以内）","JOB_CONTENT"}, 
						 { "投入工时（h）\r\n（必填 数字 ）","WORKING_HOUR","nowrap"},
						 { "填报人员姓名\r\n（选填）","WORKER","nowrap"}, 
						 { "填报人员编号\r\n（必填）","WORKER_HRCODE","nowrap"},
						 { "审核人员姓名\r\n（选填）","PRINCIPAL","nowrap"}, 
						 { "审核人员员工编号\r\n（非项目工作必填，项目工作负责人必填）","PRINCIPAL_HRCODE","nowrap"},
						 { "错误说明","errInfo"}
						};
				
				errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
				smServiceLog.info("errorUUID: " + errorUUID);
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
			smMapper.addWorkHourInfo(wh);
		}
		String[] object = {"成功导入项目信息"+whList.size()+"条，失败"+errorList.size()+"条",errorUUID};
		return object;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String exportSelectedItems(String ids, HttpServletResponse response) {
		List<Map<String,String>> dataList=new ArrayList<Map<String,String>>();
		if(ids.indexOf("{")!=-1){
			Map<String,String> map=JSONObject.parseObject(ids,Map.class);
			String startDate = Rtext.toStringTrim(map.get("startDate"), "");
			String endDate = Rtext.toStringTrim(map.get("endDate"), "");
			String deptCode = Rtext.toStringTrim(map.get("deptCode"), "");
			String category = Rtext.toStringTrim(map.get("category"), "");
			String proName = Rtext.toStringTrim(map.get("proName"), "");
			String empName = Rtext.toStringTrim(map.get("empName"), "");
			String status = Rtext.toStringTrim(map.get("status"), "");
			//服用页面查询方法，把分页范围调大
			dataList=searchForWorkHourInfo(
					startDate, endDate,deptCode,category,proName,empName,status);
		}else{
			String[] idArr=ids.split(",");
			Map<String,String> map = new HashMap<String,String>();
			for (int i = 0; i < idArr.length; i++) {
				map=smMapper.getSelectedWorkHourInfoById(idArr[i]);
				dataList.add(map);
			}
		}
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,String> map=dataList.get(i);
			Map<String,String> dictMap=dict.getDictDataByPcode("cstatus100003");
			map.put("STATUS", dictMap.get(map.get("STATUS")));
			dataList.set(i, map);
		}
		
		Object[][] title = { 
							 { "日期", "WORK_TIME","nowrap"},
							 { "部门（单位）", "DEPT" ,"nowrap"},
							 { "处室","LAB","nowrap"},
							 { "人员编号", "HRCODE" ,"nowrap"}, 
							 { "人员姓名","USERNAME","nowrap"}, 
							 { "类型","CATEGORY","nowrap"},
							 { "项目名称","PROJECT_NAME","nowrap"}, 
							 { "工作内容","JOB_CONTENT"},
							 { "投入工时（h）","WORKING_HOUR","nowrap"},
							 { "审核人","APPROVER","nowrap"},
							 { "状态","STATUS","nowrap"}
							};
		ExportExcelHelper.getExcel(response, "报工管理-员工工时管理-"+DateUtil.getDays(), title, dataList, "normal");
		return "";
	}
}
