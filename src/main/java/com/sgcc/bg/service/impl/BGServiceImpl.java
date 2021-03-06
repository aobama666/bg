package com.sgcc.bg.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.mapper.BGMapper;
import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectInfoVali;
import com.sgcc.bg.model.ProjectUserPo;
import com.sgcc.bg.model.ProjectUserVali;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IBGService;

@Service
public class BGServiceImpl implements IBGService {
	@Autowired
	private BGMapper bgMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private DataDictionaryService dict;
	
	private static Logger bgServiceLog =  LoggerFactory.getLogger(BGServiceImpl.class);

	@Override
	public List<Map<String, String>> getAllProjects(String proName,String proStatus) {
		List<Map<String, String>> list=bgMapper.getAllProjects(webUtils.getUsername(),proName, proStatus);
		for(Map<String,String> map : list){
			Double hourSum = bgMapper.hourSum(map.get("id"));
			Double qianSum = bgMapper.qianQiSum(map.get("id"));
			String sum = String.valueOf(hourSum+qianSum);
			if(sum.equals("0.0")){
				sum = "0";
			}
			map.put("hourSum", sum);
		}
		return list;
	}

	@Override
	public int addProInfo(ProjectInfoPo pro) {
		return bgMapper.addProInfo(pro);
	}

	@Override
	public int updateProInfo(ProjectInfoPo pro) {
		return bgMapper.updateProInfo(pro);
	}

	@Override
	public Map<String, String> getProInfoByProId(String proId) {
		Map<String, String> proMap=bgMapper.getProInfoByProId(proId);
		return proMap;
	}
	
	@Override
	public ProjectInfoPo getProPoByProId(String proId) {
		return bgMapper.getProPoByProId(proId);
	}

	@Override
	public List<Map<String, String>> getProUsersByProId(String proId) {
		List<Map<String, String>> userList=bgMapper.getProUsersByProId(proId);
		for(Map<String,String> map : userList){
			Double workingHour = bgMapper.workingHour(proId,map.get("HRCODE"));
			String sum = String.valueOf(workingHour);
			if(sum.equals("0.0")){
				sum = "0";
			}
			map.put("workingHour", sum);
		}
		return userList;
	}

	@Override
	public int deleteProjectByProId(String proId) {
		//删除该项目的同时也把其所属人员信息删除
		deleteEmpAndRelation(proId);
		
		//删除该项目的同时也把其关联的项目前期删除
		bgMapper.updateProInfoFieldByField("RELATED_PROJECT_ID",proId,null);
		
		//删除项目信息为逻辑删,删除人员信息为物理删
		int affectedRows = deleteProject(proId);
		
		return affectedRows;
	}

	private int deleteProject(String proId){
		String username = webUtils.getUsername();
		Date sysDate = new Date();
		int affectedRows = bgMapper.deleteProjectByProId(proId,username,sysDate);
		bgServiceLog.info("成功删除项目： "+proId);
		bgMapper.deleteProRelation(proId);
		
		return affectedRows;
	}
	
	/**
	 * 根据项目id删除参与人员以及关联记录
	 * @param proId
	 */
	private void deleteEmpAndRelation(String proId){
		// 删除旧的项目下所有人员
		int affectedRows = bgMapper.deleteProUsersByProId(proId);
		bgServiceLog.info("成功删除" + affectedRows + "人！");
		bgMapper.deleteEmpRelation(proId,null);
	}
	
	@Override
	public String changeProStatusById(String proId, String operation) {
		String proStatus="";
		//如果是启动，结束或废止项目，则把项目结束时间改为操作时间(暂不执行)
		if("start".equals(operation)){
			proStatus="1";
			//bgMapper.updateProInfoField(proId, "start_date",DateUtil.getDay());
			//查询此时数据库中当前项目是否有人员，是否有唯一负责人等
			List<Map<String, String>>  empList=bgMapper.getProUsersByProId(proId);
			//count和size只能大于或者等于0
			if(empList.size()==0){
				return "目前项目下不存在参与人员，请添加人员后启动。";//没有人员
			}
			//负责人负责的时间总和
			long sumDate = 0 ;
			int count=0;
			//统计负责人数
			for (Map<String, String> emp : empList) {
				if("1".equals(emp.get("ROLE"))){
					count++;
				}
			}
			String[] startDate = new String[count];
			String[] endDate = new String[count];
			/*for (Map<String, String> emp : empList) {
				if("1".equals(emp.get("ROLE"))){
					startDate[count] = emp.get("START_DATE");
					endDate[count] = emp.get("END_DATE");
					count++;
					sumDate += DateUtil.getDaySub(emp.get("START_DATE"),emp.get("END_DATE"));
					sumDate++;
				}
			}*/
			for(int i=0;i<empList.size();i++){
				if("1".equals(empList.get(i).get("ROLE"))){
					startDate[i] = empList.get(i).get("START_DATE");
					endDate[i] = empList.get(i).get("END_DATE");
					sumDate += DateUtil.getDaySub(empList.get(i).get("START_DATE"),empList.get(i).get("END_DATE"));
					sumDate++;
				}
			}
			if(count==0){
				return "目前项目下不存在负责人，请添加负责人后启动。";//存在人员但无负责人
			}/*else if(count>1){
				return "目前项目下存在多个负责人，指定一名负责人。";//负责人重复
			}*/
			Arrays.sort(startDate);
			Arrays.sort(endDate);
			for(int i=1;i<startDate.length;i++){
				if(startDate[i] != null && startDate[i] != "") {
					if (DateUtil.getFormatDate(startDate[i], "yyyy-MM-dd").getTime() < DateUtil.getFormatDate(endDate[i - 1], "yyyy-MM-dd").getTime()) {
						return "项目负责人多个，日期交叉";
					}
				}
			}

			Map<String ,String> map = bgMapper.getProjectsInfo(proId);
			if(map!=null){
				long infoDate = DateUtil.getDaySub(map.get("startDate"),map.get("endDate"))+1;
				if(sumDate>infoDate){
					//return "项目负责人参与时间不能晚于项目周期";
					return "负责人参与累计时间大于项目时间";
				}
				if(sumDate<infoDate){
					return "负责人参与累计时间小于项目时间";
				}
			}

		}else if("pause".equals(operation)){
			proStatus="2";
		}else if("finish".equals(operation)){
			proStatus="3";
			//bgMapper.updateProInfoField(proId, "end_date",DateUtil.getDay());
		}else if("discard".equals(operation)){
			proStatus="4";
			//bgMapper.updateProInfoField(proId, "end_date",DateUtil.getDay());
		}
		bgMapper.changeProStatusById(proId,proStatus);
		return "done";
	}

	/*@Override
	public int getProjectCount(String proName,String proStatus) {
		if("未启动".equals(proStatus)){
			proStatus="0";
		}else if("进行中".equals(proStatus)){
			proStatus="1";
		}else if("暂停".equals(proStatus)){
			proStatus="2";
		}else if("已结束".equals(proStatus)){
			proStatus="3";
		}else if("废止".equals(proStatus)){
			proStatus="4";
		}
		return bgMapper.getProjectCount(webUtils.getUsername(),proName,proStatus);
	}*/

	@Override
	public String checkUniqueness(String wbsNumber) {
		int affectedRows = bgMapper.checkUniqueness(wbsNumber);
		if(affectedRows>0){
			return "false";
		}else{
			return "true";
		}
	}

	@Override
	public String[] parseProExcel(InputStream in) {
		HSSFWorkbook wb = null;
		//获取通过验证的项目信息
		List<ProjectInfoPo> proList=new ArrayList<>();
		//出错项目信息
		List<ProjectInfoVali> errorList = new ArrayList<ProjectInfoVali>();
		//存储在ftp的错误文件名称
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
			Set<String> repeatChecker = new HashSet<String>();
			String regex ="^[1-9][0-9]*$";
			//获取所有项目编号存入一个集合
			Set<String> wbsCodeSet = new HashSet<String>();
			List<String> list=bgMapper.getAllWbsNumbers();
			
			//定义可接受目类型
			Map<String,String> dictMap = new HashMap<>();
			dictMap.put("技术服务项目" , "JS");
			dictMap.put("其他" , "QT");
			//获取级别
			Map<String,String> rank=dict.getDictDataByPcode("project_rank");
			
			wbsCodeSet.addAll(list);
			bgServiceLog.info("项目信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[11];
			for (int i = 1; i <=rows; i++) {// 从第2行开始是正式数据
				// 获取正式数据并封装进cellValue数组中
				StringBuffer checkStr = new StringBuffer();
				bgServiceLog.info("第" + (i + 1) + "行");
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
						bgServiceLog.info("第"+(i + 1)+"行 第"+(c+1)+"列 is null");
					}
					checkStr.append(cellValue[c]);
					bgServiceLog.info("cellValue is " + cellValue[c]);
				}
				if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {// 校验此行是否为空
					StringBuffer errorInfo = new StringBuffer();
					Set<Integer> errorNum = new HashSet<Integer>();
					String currentUsername=webUtils.getUsername();
					// 对要导入的文件内容进行校验
					// 项目名称校验
					if (cellValue[1] == null || "".equals(cellValue[1])) {
						errorInfo.append("项目名称不能为空！ ");
						errorNum.add(1);
					} else if (cellValue[1].length() > 50) {
						errorInfo.append("项目名称不能超过50字！ ");
						errorNum.add(1);
					}

					//项目级别验证
                    if(cellValue[2]==null || "".equals(cellValue[2])){
					    errorInfo.append("项目级别不能为空！ ");
					    errorNum.add(2);
                    }else if (cellValue[2].length()>20){
					    errorInfo.append("项目级别不能超过20个字！ ");
                    }

					// 项目分类校验 必填
					if (cellValue[3] == null || "".equals(cellValue[3])) {
						errorInfo.append("项目类型不能为空！ ");
						errorNum.add(3);
					}else if(!dictMap.containsKey(cellValue[3])){
						errorInfo.append("无此项目类型！ ");
						errorNum.add(3);
					}
					
					/*if(!"技术服务项目".equals(cellValue[2])){
						// 非技术服务项目，校验wbs编号是否在该excel表以及数据库中已经存在
						if (cellValue[3] == null || "".equals(cellValue[3])) {
							errorInfo.append("wbs编号/项目编号不能为空！");
							errorNum.add(3);
						}else if (!repeatChecker.add(cellValue[3])) {
							errorInfo.append("wbs编号重复！");
							errorNum.add(3);
						}else if(wbsCodeSet.contains(cellValue[3])){
							errorInfo.append("系统中已经存此wbs编号！");
							errorNum.add(3);
						}
					}*/
					//校验wbs编号，当为科研或横向时必填，如果填了，则校验唯一
					if (("科研项目".equals(cellValue[3]) || "横向项目".equals(cellValue[3]))
							&& Rtext.isEmpty(cellValue[4])) {
						errorInfo.append("wbs编号不能为空！ ");
						errorNum.add(4);
					}else if(!Rtext.isEmpty(cellValue[4]) && cellValue[4].length()>50){
						errorInfo.append("wbs编号不能超过50字！ ");
						errorNum.add(4);
					}else if (!Rtext.isEmpty(cellValue[4]) && !repeatChecker.add(cellValue[4])) {
						errorInfo.append("wbs编号重复！ ");
						errorNum.add(4);
					}else if(!Rtext.isEmpty(cellValue[4]) && wbsCodeSet.contains(cellValue[4])){
						errorInfo.append("系统中已经存此wbs编号！ ");
						errorNum.add(4);
					}
				
					// 项目说明长度200以内
					if (cellValue[5].length() > 200) {
						errorInfo.append("项目说明不能超过200字！ ");
						errorNum.add(5);
					}
					// 发布日期 必填;格式
					if (cellValue[6] == null || "".equals(cellValue[6])) {
						errorInfo.append("项目开始日期不能为空！ ");
						errorNum.add(6);
					} else if (!DateUtil.isValidDate(cellValue[6], "yyyy-MM-dd")) {
						errorInfo.append("项目开始日期填写有误！ ");
						errorNum.add(6);
					}
					// 发布日期 必填;格式
					if (cellValue[7] == null || "".equals(cellValue[7])) {
						errorInfo.append("项目结束日期不能为空！ ");
						errorNum.add(7);
					} else if (!DateUtil.isValidDate(cellValue[7], "yyyy-MM-dd")) {
						errorInfo.append("项目结束日期填写有误！ ");
						errorNum.add(7);
					}
					//如果日期均正确 ，则验证技术服务项目是否跨年
					if(!errorNum.contains(6) && !errorNum.contains(7)){
						//如果日期均正确 ，则验证技术服务项目是否跨年
						if("技术服务项目".equals(cellValue[3])){
							if(!(cellValue[6].substring(0,4)).equals(cellValue[7].substring(0,4))){
								errorInfo.append("技术服务项目不能跨年！ ");
								errorNum.add(6);
								errorNum.add(7);
							}
						}
						//验证日期是否符合先后逻辑顺序
						if(DateUtil.fomatDate(cellValue[7]).getTime()<=DateUtil.fomatDate(cellValue[6]).getTime()){
							errorInfo.append("项目结束日期必须大于项目开始日期！ ");
							errorNum.add(7);
						}
					}
					// 组织信息 技术服务项目为必填项(如果不填则默认登录人所在处室)
					if("技术服务项目".equals(cellValue[3])){
						if(!Rtext.isEmpty(cellValue[8])){
							if(Rtext.isEmpty(getDeptIdByDeptCode(cellValue[8]))){
								errorInfo.append("组织信息填写错误！ "); 
								errorNum.add(8);
							}
						}
					}
					// 计划投入工时 必填;数字
					if (cellValue[9] == null || "".equals(cellValue[9])) {
						errorInfo.append("计划投入工时不能为空！ ");
						errorNum.add(9);
					} else if (!cellValue[9].matches(regex)) {
						errorInfo.append("计划投入工时填写有误！ ");
						errorNum.add(9);
					} else if (cellValue[9].length() > 8) {
						errorInfo.append("计划投入工时不能超过8位数！ ");
						errorNum.add(9);
					}
					// 是否分解
					/*if (cellValue[9] == null || "".equals(cellValue[9])) {
						errorInfo.append("是否分解不能为空！");
						errorNum.add(9);
					}*/

					// 校验结束，分流数据
					if ("".equals(errorInfo.toString())) {// 通过校验
						ProjectInfoPo pro=new ProjectInfoPo();
						pro.setId(Rtext.getUUID());
						pro.setProjectName(cellValue[1]);
						/*if("科研项目".equals(cellValue[2])){
							pro.setCategory("KY");
						}else if("横向项目".equals(cellValue[2])){
							pro.setCategory("HX");
						}else if("技术服务项目".equals(cellValue[2])){
							pro.setCategory("JS");
						}*/
						/*for (Map.Entry<String,String> entry : dictMap.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue();
							if(cellValue[2].equals(value)) pro.setCategory(key);
						}*/
						pro.setProjectGrade(rank.get(cellValue[2]));
						pro.setCategory(dictMap.get(cellValue[3]));
						pro.setWBSNumber(cellValue[4]);
						pro.setProjectIntroduce(cellValue[5]);
						pro.setStartDate(DateUtil.fomatDate(cellValue[6]));
						pro.setEndDate(DateUtil.fomatDate(cellValue[7]));
						
						if("技术服务项目".equals(cellValue[3])){
							String OrganDeptId="";
							if(Rtext.isEmpty(cellValue[8])){
								CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(currentUsername);
								String deptCode=currentUser==null?"":currentUser.getDeptCode();
								OrganDeptId=getDeptIdByDeptCode(deptCode);
							}else{
								OrganDeptId=getDeptIdByDeptCode(cellValue[8]);
							}
							pro.setOrganInfo(OrganDeptId);
						}
						pro.setPlanHours(Integer.parseInt(cellValue[9]));
						/*String decompose ="";
						if("否".equals(cellValue[9])){
							decompose="0";
						}else if("是".equals(cellValue[9])){
							decompose="1";
						}*/
						pro.setDecompose("0");//一期默认不分解
						pro.setCreateUser(currentUsername);
						pro.setCreateDate(new Date());
						pro.setUpdateUser(currentUsername);
						pro.setUpdateDate(new Date());
						pro.setStatus("1");
						pro.setProjectStatus("0");
						pro.setSrc("1");
						//保存正确数据
						proList.add(pro);
					} else {// 未通过校验
						ProjectInfoVali prov = new ProjectInfoVali();
						prov.setSqnum(cellValue[0]);
						prov.setProjectName(cellValue[1]);
						prov.setProjectGrade(cellValue[2]);
						prov.setCategory(cellValue[3]);
						prov.setWBSNumber(cellValue[4]);
						prov.setProjectIntroduce(cellValue[5]);
						prov.setStartDate(cellValue[6]);
						prov.setEndDate(cellValue[7]);
						prov.setOrganInfo(cellValue[8]);
						prov.setPlanHours(cellValue[9]);
						//prov.setDecompose(cellValue[9]);
						prov.setErrorInfo(errorInfo.toString());
						prov.setErrSet(errorNum);
						errorList.add(prov);
					}
				}
			}

			// 返回错误数据
			if (errorList.size() > 0) {
				bgServiceLog.info("出错的项目： " + errorList);
				// 生成错误信息文件
				Object[][] title = { 
						{ "序号\r\n（选填）", "sqnum","nowrap" }, 
						{ "项目名称\r\n（必填，50字以内）", "projectName","nowrap" },
                        { "项目级别\r\n(必填，20字以内)","projectGrade","nowrap"},
						{ "项目类型\r\n（必填）", "category" ,"nowrap"},
						{ "WBS编号\r\n（项目类型为科研项目、横向项目时必填）", "WBSNumber" ,"nowrap"}, 
						{ "项目说明\r\n（200字以内）","projectIntroduce"},
						{ "项目开始时间\r\n（必填，格式：YYYY-MM-DD）","startDate","nowrap"}, 
						{ "项目结束时间\r\n（必填，格式：YYYY-MM-DD）","endDate","nowrap"},
						{ "组织信息\r\n（项目类型为技术服务项目时必填，如不填则默认填报人处室）","organInfo","nowrap"}, 
						{ "计划投入工时(h)\r\n(必填，数字，8位数字）", "planHours","nowrap"},
						{ "错误说明","errorInfo"}
				};
				errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
				bgServiceLog.info("errorUUID: " + errorUUID);
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
		for (ProjectInfoPo pro : proList) {
			//项目编号自动生成
			pro.setProjectNumber(getBGNumber());
			bgMapper.addProInfo(pro);
		}
		String[] object = {"成功导入项目信息"+proList.size()+"条，失败"+errorList.size()+"条",errorUUID};
		return object;
	}
	
	@Override
	public String[] parseEmpExcel(InputStream in) {
		//beta
		HSSFWorkbook wb = null;
		//存放出错信息
		List<ProjectUserVali> errorList = new ArrayList<ProjectUserVali>();
		//存放正确导入的人员信息
		List<ProjectUserPo> userList = new ArrayList<ProjectUserPo>();
		//存放到ftp的文件名
		String errorUUID = "";
		//累加负责人角色数目
		Map<String,Integer> roleMap=new HashMap<>();
		//存放项目id
		Set<String> setId = new HashSet<String>();
		try{
			POIFSFileSystem fs = null;
			HSSFSheet sheet;
			HSSFRow row;
			HSSFCell cell;
			// 获取POI操作对象
			fs = new POIFSFileSystem(in);
			wb = new HSSFWorkbook(fs);
			// 创建存储数据集合 用来存放取出的数据
			sheet = wb.getSheetAt(0);
			// 得到sheet内总行数
			int rows = sheet.getLastRowNum();
			//获取所有项目编号存入一个集合
			List<String> list=bgMapper.getAllBgNumbers();
			//角色类型
			String roleStr="[项目负责人],[项目参与人]";
			//验证计划投入工时正则表达式
			String regex ="^(0\\.\\d|[1-9]+\\d*(\\.\\d)?)$";
			
			bgServiceLog.info("该参与人员信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[10];
			for (int i = 1; i <=rows; i++) {// 从第2行开始是正式数据
				// 获取正式数据并封装进cellValue数组中
				StringBuffer checkStr = new StringBuffer();
				bgServiceLog.info("第" + (i + 1) + "行");
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
						bgServiceLog.info("cell is null");
					}
					checkStr.append(cellValue[c]);
					bgServiceLog.info("cellValue is " + cellValue[c]);
				}
				if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {// 校验此行是否为空
					StringBuffer errorInfo = new StringBuffer();
					Set<Integer> errorNum = new HashSet<Integer>();
					String proId="";
					String startDate="";
					String endDate="";
					CommonCurrentUser user=null;
					// 对要导入的文件内容进行简单的校验
					// 项目编号 必填
					if (Rtext.isEmpty(cellValue[1])) {
						errorInfo.append("项目编号不能为空！ ");
						errorNum.add(1);
					}else if(!list.contains(cellValue[1])){
						errorInfo.append("项目中不存在此项目编号！ ");
						errorNum.add(1);
					}else{
						//如果存在项目编号，获取其项目id
						proId=bgMapper.getProIdByBgNmuber(cellValue[1]);
						//获取该项目开始日期以及结束日期
						startDate=bgMapper.getProInfoFieldByProId(proId,"start_date");
						endDate=bgMapper.getProInfoFieldByProId(proId,"end_date");
						if(Rtext.isEmpty(cellValue[4])){
							cellValue[4]=startDate;
						}
						if(Rtext.isEmpty(cellValue[5])){
							cellValue[5]=endDate;
						}
						//判断是否当前状态无法导入人员
						String projectStatus=bgMapper.getProInfoFieldByProId(proId, "project_status");
						//3完成，4废止状态不可以添加人员
						if("3".equals(projectStatus) || "4".equals(projectStatus)){
							errorInfo.append("该项目已完成或废止，无法添加人员！ ");
							errorNum.add(1);
						}
						//初始化roleMap
						if(roleMap.get(proId)==null){
							roleMap.put(proId, 0);
						}
					}
					
					// 人员姓名必填
					/*if (cellValue[2] == null || "".equals(cellValue[2])) {
						errorInfo.append("人员姓名不能为空！");
						errorNum.add(2);
					}*/
					// 人员编号必填
					if (Rtext.isEmpty(cellValue[3])) {
						errorInfo.append("人员编号不能为空！ ");
						errorNum.add(3);
					}else{
						user=userUtils.getCommonCurrentUserByHrCode(cellValue[3]);
						if(user==null){
							errorInfo.append("人员编号错误！ ");
							errorNum.add(3);
						}
					}
					
					//人员姓名和人员编号正确，则验证是否存在该人员
					/*if(!errorNum.contains(2) && !errorNum.contains(3)){
						CommonCurrentUser user=userUtils.getCommonCurrentUserByHrCode(cellValue[3]);
						if(user!=null){
							String userAlias=user.getUserAlias();
							if(!cellValue[2].equals(userAlias)){
								errorInfo.append("人员姓名不正确！");
								errorNum.add(2);
							}
						}else{
							errorInfo.append("人员编号不存在！");
							errorNum.add(3);
						}
					}*/
					// 项目开始日期 必填(若果不填则默认项目开始时间);格式
					if (!Rtext.isEmpty(cellValue[4]) && !DateUtil.isValidDate(cellValue[4], "yyyy-MM-dd")) {
						errorInfo.append("项目开始日期填写有误！ ");
						errorNum.add(4);
					}
					// 项目结束日期 必填(如果不填则默认项目结束时间);格式
					if (!Rtext.isEmpty(cellValue[5]) && !DateUtil.isValidDate(cellValue[5], "yyyy-MM-dd")) {
						errorInfo.append("项目结束日期填写有误！ ");
						errorNum.add(5);
					}
					//如果日期正确 ，则验证是否超出项目日期
					if(!Rtext.isEmpty(cellValue[4]) && !Rtext.isEmpty(startDate) && !errorNum.contains(4)){
						if(!DateUtil.compareDate(cellValue[4], startDate) || !DateUtil.compareDate(endDate,cellValue[4])){
							errorInfo.append("项目开始日期超出项目周期！ ");
							errorNum.add(4);
						}
					}
					if(!Rtext.isEmpty(cellValue[5]) && !Rtext.isEmpty(endDate) && !errorNum.contains(5)){
						if(!DateUtil.compareDate(cellValue[5], startDate) || !DateUtil.compareDate(endDate,cellValue[5])){
							errorInfo.append("项目结束日期超出项目周期！ ");
							errorNum.add(5);
						}
					}
					//验证日期是否符合先后逻辑顺序
					if(!Rtext.isEmpty(cellValue[4]) && !Rtext.isEmpty(cellValue[5]) && !errorNum.contains(4) && !errorNum.contains(5)){
						if(DateUtil.fomatDate(cellValue[5]).getTime()<DateUtil.fomatDate(cellValue[4]).getTime()){
							errorInfo.append("结束日期必须大于开始日期！ ");
							errorNum.add(4);
							errorNum.add(5);
						}else if(!errorNum.contains(3) && !Rtext.isEmpty(proId)){
							//验证已存在的人员日期是否重复
							List<Map<String,String>> dateList=bgMapper.getPartyDateByHrcode(proId,cellValue[3]);
							//从已通过的人员中获取日期
							for (ProjectUserPo po : userList) {
								if(po.getProjectId().equals(proId) && po.getHrcode().equals(cellValue[3])){
									Map<String,String> map=new HashMap<>();
									map.put("startDate", DateUtil.getFormatDateString(po.getStartDate(),"yyyy-MM-dd"));
									map.put("endDate", DateUtil.getFormatDateString(po.getEndDate(),"yyyy-MM-dd"));
									dateList.add(map);
								}
							}
							for (Map<String, String> dateMap : dateList) {
								if(DateUtil.fomatDate(cellValue[4]).getTime()>DateUtil.fomatDate(dateMap.get("endDate")).getTime()||
										DateUtil.fomatDate(cellValue[5]).getTime()<DateUtil.fomatDate(dateMap.get("startDate")).getTime()){
									//日期不重叠
								}else{
									errorInfo.append("与已存在参与人"+user.getUserAlias()+"日期("+dateMap.get("startDate")+"至"+dateMap.get("endDate")+")重叠! ");
									errorNum.add(4);
									errorNum.add(5);
									break;
								}
							}
						}
					}
					
					// 角色 必填
					if (Rtext.isEmpty(cellValue[6])) {
						errorInfo.append("角色不能为空！  ");
						errorNum.add(6);
					}else if(!roleStr.contains("["+cellValue[6]+"]")){
						errorInfo.append("不存在此角色！  ");
						errorNum.add(6);
					}/*else if(!Rtext.isEmpty(proId)){//项目存在
						//获取map中指定项目的项目负责人数量
						int currentValue=roleMap.get(proId);
						if("项目负责人".equals(cellValue[6])){
							int principalCount=bgMapper.getPrincipalCountByProId(proId);
							if(principalCount>0 || currentValue>0){
								errorInfo.append("项目已存在负责人！  ");
								errorNum.add(6);
							}
						}
					}*/
					

					if(!Rtext.isEmpty(proId) && !errorNum.contains(3) && !errorNum.contains(6)){//项目存在，此人存在且角色存在并不冲突
						//获取当前项目中人员信息
						List<Map<String, String>> empList=bgMapper.getProUsersByProId(proId);
						for (ProjectUserPo po : userList) {
							if(po.getProjectId().equals(proId) && po.getHrcode().equals(cellValue[3])){
								Map<String,String> map=new HashMap<>();
								map.put("ROLE",po.getRole());
								map.put("HRCODE",po.getHrcode());
								map.put("startDate",DateUtil.dateToStr(po.getStartDate()));
								map.put("endDate", DateUtil.dateToStr(po.getEndDate()));
								empList.add(map);
							}
						}
						if("项目负责人".equals(cellValue[6])){//此人存在且为负责人时，项目中如果已存在测人记录，则不允许添加
							for (Map<String, String> map : empList) {
								if(cellValue[3].equals(map.get("HRCODE"))){
									errorInfo.append("此人已存在项目中，不允许添加为负责人！  ");
									errorNum.add(6);
									break;
								}
							}
						}else{//此人存在且为参与人时，项目中此人如果已经作为负责人，则不允许添加
							for (Map<String, String> map : empList) {
								/*if(cellValue[3].equals(map.get("HRCODE")) && "1".equals(map.get("ROLE"))){
									errorInfo.append("此人为当前项目负责人，请勿重复添加！  ");
									errorNum.add(6);
									break;
								}*/
								if(cellValue[3].equals(map.get("HRCODE"))) {
									if (DateUtil.fomatDate(cellValue[4]).getTime() > DateUtil.fomatDate(map.get("endDate")).getTime() ||
											DateUtil.fomatDate(cellValue[5]).getTime() < DateUtil.fomatDate(map.get("startDate")).getTime()) {
										//日期不重叠
									} else {
										errorInfo.append("此人" + user.getUserAlias() + "日期(" + map.get("startDate") + "至" + map.get("endDate") + ")重叠! ");
										errorNum.add(4);
										errorNum.add(5);
										break;
									}
								}
							}
						}
					}

					//验证项目负责人 负责的时间段是否有交叉
					//取出该项目所有负责人的开始时间和结束时间
					//与已存在参与人"+user.getUserAlias()+"日期("+dateMap.get("startDate")+"至"+dateMap.get("endDate")+")重叠!
					if( ("项目负责人").equals(cellValue[6]) && !Rtext.isEmpty(cellValue[4]) && !Rtext.isEmpty(cellValue[5])
							&& !errorNum.contains(4) && !errorNum.contains(5)){
						List<Map<String, String>> listPrincipalDate = bgMapper.listPrincipalDate(proId);
						for (ProjectUserPo po : userList) {
							if(po.getProjectId().equals(proId)){
								Map<String,String> map=new HashMap<>();
								map.put("startDate",DateUtil.dateToStr(po.getStartDate()));
								map.put("endDate", DateUtil.dateToStr(po.getEndDate()));
								map.put("role",po.getRole());
								map.put("empName",po.getEmpName());
								listPrincipalDate.add(map);
							}
						}
						//验证负责人时间是否重叠和 负责人时间是否贯穿整个项目
						for (Map<String, String> principalDateMap : listPrincipalDate) {
							if (DateUtil.fomatDate(cellValue[4]).getTime() > DateUtil.fomatDate(principalDateMap.get("endDate")).getTime() ||
									DateUtil.fomatDate(cellValue[5]).getTime() < DateUtil.fomatDate(principalDateMap.get("startDate")).getTime()) {
								//日期不重叠
							} else {
								//errorInfo.append("与项目负责人" + principalDateMap.get("empName") + "日期(" + principalDateMap.get("startDate") + "至" + principalDateMap.get("endDate") + ")重叠! ");
								errorInfo.append("与项目负责人" + cellValue[3] + "日期(" + cellValue[4] + "至" + cellValue[5] + ")重叠! ");
								errorNum.add(4);
								errorNum.add(5);
							}
						}
					}


					if(!Rtext.isEmpty(cellValue[7]) && cellValue[7].length()>200){
						errorInfo.append("工作任务超过200字！  ");
						errorNum.add(7);
					}
					
					if(!Rtext.isEmpty(cellValue[8]) && !cellValue[8].matches(regex)){
						errorInfo.append("计划投入工时格式错误！  ");
						errorNum.add(8);
					}
					
					// 校验结束，分流数据
					if ("".equals(errorInfo.toString())) {
						// 通过校验 ,保存正确数据
						ProjectUserPo proUser = new ProjectUserPo();
						proUser.setId(Rtext.getUUID());
						proUser.setProjectId(proId);
						proUser.setEmpName(user==null?"":user.getUserAlias());
						proUser.setHrcode(cellValue[3]);
						proUser.setStartDate(DateUtil.fomatDate(cellValue[4]));
						proUser.setEndDate(DateUtil.fomatDate(cellValue[5]));
						//获取该项目下当前已添加的项目负责人数
						int currentValue=roleMap.get(proId);
						if ("项目负责人".equals(cellValue[6])) {
							proUser.setRole("1");
							currentValue++;
							roleMap.put(proId,currentValue);
						}else{
							proUser.setRole("0");
						}
						proUser.setTask(cellValue[7]);
						if(null!=cellValue[8]&&cellValue[8]!="") {
							proUser.setPlanHours(Double.parseDouble(cellValue[8]));
						}
						proUser.setStatus("1");
						proUser.setCreateDate(new Date());
						proUser.setUpdateDate(new Date());
						proUser.setCreateUser(webUtils.getUsername());
						proUser.setUpdateUser(webUtils.getUsername());
						proUser.setSrc("1");
						proUser.setSqnum(cellValue[0]);
						setId.add(proId);
						userList.add(proUser);
					} else {// 未通过校验
						ProjectUserVali pruv = new ProjectUserVali();
						pruv.setSqnum(cellValue[0]);
						pruv.setProjectNumber(cellValue[1]);
						pruv.setEmpName(cellValue[2]);
						pruv.setHrcode(cellValue[3]);
						pruv.setStartDate(cellValue[4]);
						pruv.setEndDate(cellValue[5]);
						pruv.setRole(cellValue[6]);
						pruv.setTask(cellValue[7]);
						pruv.setPlanHours(cellValue[8]);
						pruv.setErrorInfo(errorInfo.toString());
						pruv.setErrSet(errorNum);
						errorList.add(pruv);
					}
				}
			}

			//验证负责人是否负责整个项目周期
			//存放正确导入的人员信息
			List<ProjectUserPo> projectUserPoList = new ArrayList<>();
			List<Map<String, String>> listPrincipal = new ArrayList<>();
			for(String set : setId){
				for(ProjectUserPo po : userList) {
					if ((set).equals(po.getProjectId()) && ("1").equals(po.getRole())) {
						projectUserPoList.add(po);
					}
				}
				long sumDate = 0;
				StringBuffer errorInfo = new StringBuffer();
				Set<Integer> errorNum = new HashSet<Integer>();
				//导入数据
				for(ProjectUserPo up :projectUserPoList){
					if(("1").equals(up.getRole())){
						sumDate += DateUtil.getDaySub(DateUtil.dateToStr(up.getStartDate()),DateUtil.dateToStr(up.getEndDate()));
						sumDate++;
					}
				}
				//原来已经保存的数据
				listPrincipal = bgMapper.listPrincipalDate(String.valueOf(set));
				for(Map<String,String> map : listPrincipal){
					sumDate += DateUtil.getDaySub(map.get("startDate"),map.get("endDate"));
					sumDate++;
				}
				//查项目周期时间
				Map<String ,String> map = bgMapper.getProjectsInfo(String.valueOf(set));

				if(map!=null){
					long infoDate = DateUtil.getDaySub(map.get("startDate"),map.get("endDate"))+1;
					if(sumDate>infoDate){
						errorInfo.append("项目编号为"+map.get("projectNumber")+"负责人参与累计时间大于项目时间");
						errorNum.add(4);
						errorNum.add(5);
						for(ProjectUserPo up :projectUserPoList){
							userList.remove(up);

							ProjectUserVali pruv = new ProjectUserVali();
							pruv.setSqnum(up.getSqnum());
							pruv.setProjectNumber(map.get("projectNumber"));
							pruv.setEmpName(up.getEmpName());
							pruv.setHrcode(up.getHrcode());
							pruv.setStartDate(DateUtil.dateToStr(up.getStartDate()));
							pruv.setEndDate(DateUtil.dateToStr(up.getEndDate()));
							if(up.getRole().equals("1")){
								pruv.setRole("项目负责人");
							}else {
								pruv.setRole("项目参与人");
							}
							if(null != up.getPlanHours()) {
								pruv.setPlanHours(String.valueOf(up.getPlanHours()));
							}
							pruv.setTask(up.getTask());
							pruv.setErrorInfo(errorInfo.toString());
							pruv.setErrSet(errorNum);
							errorList.add(pruv);
						}
					}
					if(sumDate<infoDate){
						errorInfo.append("项目编号为"+map.get("projectNumber")+"负责人参与累计时间小于项目时间");
						errorNum.add(4);
						errorNum.add(5);
						for(ProjectUserPo up :projectUserPoList){
							userList.remove(up);

							ProjectUserVali pruv = new ProjectUserVali();
							pruv.setSqnum(up.getSqnum());
							pruv.setProjectNumber(map.get("projectNumber"));
							pruv.setEmpName(up.getEmpName());
							pruv.setHrcode(up.getHrcode());
							pruv.setStartDate(DateUtil.dateToStr(up.getStartDate()));
							pruv.setEndDate(DateUtil.dateToStr(up.getEndDate()));
							pruv.setRole(up.getRole());
							if(up.getRole().equals("1")){
								pruv.setRole("项目负责人");
							}else {
								pruv.setRole("项目参与人");
							}
							if(null != up.getPlanHours()) {
								pruv.setPlanHours(String.valueOf(up.getPlanHours()));
							}
							pruv.setTask(up.getTask());
							pruv.setErrorInfo(errorInfo.toString());
							pruv.setErrSet(errorNum);
							errorList.add(pruv);
						}
					}
				}
			}


			// 返回错误数据
			if (errorList.size() > 0) {
				bgServiceLog.info("出错的项目： " + errorList);
				// 生成错误信息文件
				Object[][] title = { 
						{ "序号\r\n（选填）", "sqnum","nowrap" }, 
						{ "项目编号\r\n（必填）", "projectNumber" ,"nowrap"}, 
						{ "人员姓名\r\n（选填）", "empName" ,"nowrap"},
						{ "人员编号\r\n（必填）", "hrcode","nowrap" },
						{ "项目开始时间\r\n（选填，格式：YYYY-MM-DD，如果不填写，系统默认项目开始日期）","startDate","nowrap"}, 
						{ "项目结束时间\r\n（选填，格式：YYYY-MM-DD，如果不填写，系统默认项目开始日期）","endDate","nowrap"},
						{ "角色\r\n（必填）","role","nowrap"},
						{ "工作任务\r\n（选填，200字以内）", "task" ,"nowrap"},
						{ "计划投入工时\r\n（选填，正数，\r\n最多精确到一位小数）", "planHours" ,"nowrap"},
						{ "错误说明","errorInfo"}
					};
				
				errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
				bgServiceLog.info("errorUUID: " + errorUUID);
			}
		}catch (Exception e) {
			e.printStackTrace();
			String[] object = {"Error:人员信息导入失败，请重新导入！",""};
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
		//如果执行未遇到错误，再保存通过验证的人员信息
		for (ProjectUserPo proUser : userList) {
			bgMapper.addProUser(proUser);
		}
		String proNames="";
		for (Entry<String,Integer> ele: roleMap.entrySet()) {
			String proId=ele.getKey();
			int addCount=ele.getValue();
			int principalCount=bgMapper.getPrincipalCountByProId(proId);
			if(principalCount==0 && addCount==0){
				proNames+="["+bgMapper.getProInfoFieldByProId(proId, "project_name")+"]、";
			}
		}
		String msg="成功导入人员信息"+userList.size()+"条，失败"+errorList.size()+"条。";
		if(!proNames.isEmpty()){
			msg+="项目： "+ proNames.substring(0,proNames.length()-1)+"  中未添加项目负责人，请及时添加。";
		}
		String[] object = {msg,errorUUID};
		return object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String exportSelectedItems(String ids, HttpServletResponse response) {
		Map<String,String> statusMap= dict.getDictDataByPcode("pstatus100001");
		Map<String,String> categoryMap=dict.getDictDataByPcode("category100002");
		
		List<Map<String,String>> dataList=new ArrayList<Map<String,String>>();
		if(ids.indexOf("{")!=-1){
			Map<String,String> map=JSONObject.parseObject(ids,Map.class);
			String proStatus = Rtext.toStringTrim(map.get("proStatus"), "");
			String proName = Rtext.toStringTrim(map.get("proName"), "");
			//复用页面查询方法
			dataList = getAllProjects(proName,proStatus);
		}else{
			String[] idArr=ids.split(",");
			Map<String,String> proMap = new HashMap<String,String>();
			for (int i = 0; i < idArr.length; i++) {
				proMap=bgMapper.getProInfoByProId(idArr[i]);
				proMap.put("category", categoryMap.get(proMap.get("category")));
				dataList.add(proMap);
			}
		}
		
		String[] srcArr = {"报工系统","报工系统","科研系统","横向系统"};
		String[] relArr = {"无","有"};
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,String> map=dataList.get(i);
			map.put("projectStatus", statusMap.get(map.get("projectStatus")));
			map.put("src", srcArr[Rtext.ToInteger(map.get("src"),0)]);
			map.put("isRelated", relArr[Rtext.ToInteger(map.get("isRelated"),0)]);
			dataList.set(i, map);
		}
		Object[][] title = { 
							 { "项目编号", "projectNumber","nowrap" },
							 { "WBS编号", "WBSNumber","nowrap" },
							 { "项目名称", "projectName","nowrap" },
							 { "项目说明","projectIntroduce"},
							 { "项目类型", "category","nowrap" },
							 { "项目开始时间","startDate","nowrap"}, 
							 { "项目结束时间","endDate","nowrap"},
							 { "计划投入工时(h)","planHours","nowrap"},
							 { "项目负责人","principal","nowrap"}, 
							 { "参与人数","amount","nowrap"},
							 { "项目来源","src","nowrap"},
							 { "项目前期","isRelated","nowrap"},
							 { "项目状态","projectStatus","nowrap"},
							 { "项目参与人","empName"}
							};
		ExportExcelHelper.getExcel(response, "报工管理-项目信息维护-"+DateUtil.getDays(), title, dataList, "normal");
		return "";
	}

	@Override
	public String getBGNumber() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String currentDateStr=sdf.format(new Date());
		String number=bgMapper.getMaxBgNumber(currentDateStr);
		int newNum=(Integer.parseInt(number)+1);
		String MaxJsNumber=String.format("%04d", newNum);
		return "BG"+currentDateStr+MaxJsNumber;
	}

	@Override
	public boolean updateProInfoField(String proId,String field, String value) {
		return bgMapper.updateProInfoField(proId,field,value);
	}

	@Override
	public String getDeptIdByDeptCode(String deptCode) {
		return bgMapper.getDeptIdByDeptCode(deptCode);
	}

	@Override
	public boolean isExistPrincipal(String proId) {
		if(bgMapper.getPrincipalCountByProId(proId)>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	@Override
	public int addProUser(ProjectUserPo proUser) {
		return bgMapper.addProUser(proUser);
	}
	
	@Override
	public int saveStuff(String proId ,String src,List<HashMap> list) {
		int count=0;
		for (HashMap<String, String> map : list) {
			String empName=Rtext.toStringTrim(map.get("stuffName"),"");
			String roleStr=Rtext.toStringTrim(map.get("role"),"");
			String hrCode=Rtext.toStringTrim(map.get("hrcode"),"");
			String startDateStr=Rtext.toStringTrim(map.get("startDate"),"");
			String endDateStr=Rtext.toStringTrim(map.get("endDate"),"");
			String taskStr=Rtext.toStringTrim(map.get("task"),"");
			String planHoursStr=Rtext.toStringTrim(map.get("planHours"),"");
			String role=roleStr;
			Double planHours=null;
			
			//校验数据
			if(Rtext.isEmpty(empName) || Rtext.isEmpty(hrCode) || Rtext.isEmpty(roleStr) 
					|| Rtext.isEmpty(startDateStr) || Rtext.isEmpty(endDateStr)){
				bgServiceLog.info("bgController 项目参与人员必填参数存在空值："+"empName:"+empName+"/"+"hrCode:"+hrCode+"/"+
						"roleStr:"+roleStr+"/"+"startDateStr:"+startDateStr+"/"+"endDateStr:"+endDateStr);
				continue;
			}
			if(!DateUtil.isValidDate(startDateStr)){
				bgServiceLog.info("开始日期格式错误");
				continue;
			}
			if(!DateUtil.isValidDate(endDateStr)){
				bgServiceLog.info("结束日期格式错误");
				continue;
			}
			if ("项目参与人".equals(roleStr)) {
				role="0";
			} else if ("项目负责人".equals(roleStr)) {
				/*if(isExistPrincipal(proId)){
					bgServiceLog.info("项目中已存在项目负责人");
					continue;
				}*/
				role="1";
				//记录负责人的处室到项目信息表bg_project_info的组织信息字段；原因：技术服务项目系统以科室为最小单位
				//CommonCurrentUser user=userUtils.getCommonCurrentUserByHrCode(hrCode);
				//bgService.updateProInfoField(proId,"organ_info",user.getDeptId());
			}
			if(taskStr.length()>200){
				bgServiceLog.info("工作任务超出200字！");
				continue;
			}
			
			if(!Rtext.isEmpty(planHoursStr)){
				try {
					planHours = Double.parseDouble(planHoursStr);
				} catch (Exception e) {
					bgServiceLog.info("计划投入工时格式不正确！");
					continue;
				}
			}
			
			ProjectUserPo proUser = new ProjectUserPo();
			String proUserId = Rtext.getUUID();
			proUser.setId(proUserId);
			proUser.setRole(role);
			proUser.setProjectId(proId);
			proUser.setHrcode(hrCode);
			proUser.setEmpName(empName);
			proUser.setStartDate(DateUtil.fomatDate(startDateStr));
			proUser.setEndDate(DateUtil.fomatDate(endDateStr));
			proUser.setTask(taskStr);
			proUser.setPlanHours(planHours);
			String srcFlag = "0";
			if("KY".equalsIgnoreCase(src)){
				srcFlag = "2";
			}else if("HX".equalsIgnoreCase(src)){
				srcFlag = "3";
			}
			proUser.setSrc(srcFlag);
			proUser.setStatus("1");
			proUser.setCreateDate(new Date());
			proUser.setUpdateDate(new Date());
			proUser.setCreateUser(webUtils.getUsername());
			proUser.setUpdateUser(webUtils.getUsername());
			// 注意事务
			int affectedRows = addProUser(proUser);
			count+=affectedRows;
		}
		
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String updateStuff(String proId,String src,List<HashMap> list) {
		//查询该项目是否已有报工记录
		//List<Map<String,String>> workerList=bgMapper.getBgWorkerByProId(proId);
		
		/*for (Map<String, String> stuffMap : list) {
			Iterator<Map<String, String>> iterator= workerList.iterator();
			while(iterator.hasNext()){
			Map<String, String> workerMap=iterator.next();
				if (stuffMap.get("hrcode").equals(workerMap.get("HRCODE"))
						&& DateUtil.compareDate(workerMap.get("WORK_TIME_BEGIN"), stuffMap.get("startDate"))
						&& DateUtil.compareDate(stuffMap.get("endDate"), workerMap.get("WORK_TIME_END"))) {
					bgServiceLog.info("updateStuff ：删除成功" + stuffMap.get("hrcode"));
					iterator.remove();
				}
			}
		}
		
		Map<String, String> resultMap = new HashMap<>();
		if(workerList.size()>0){
			resultMap.put("result", "fail");
			resultMap.put("failList", JSON.toJSONString(workerList, SerializerFeature.WriteMapNullValue));
			return JSON.toJSONString(resultMap);
		}*/
		Map<String, String> resultMap = new HashMap<>();
		//储存填报工时的人员信息
		Set<Map<String,String>> failList = new HashSet<>();
		//查看项目时间段中每月人员填报工时
		List<Map<String,String>> userWorker = bgMapper.userWorker(proId);
		//查项目员工填报日期
		//List<Map<String,String>> projectTime = bgMapper.projectTime(proId);

		if(null!= userWorker && userWorker.size()>0) {
			List<Map<String, String>> settleMap  = new ArrayList<>();
			// 1. 把新数据整理 如果一个参与人有多个数据 则整合到一条中 开始、结束时间用list存储
			for(int i=0; i<list.size() ; i++){
				String hrCode = Rtext.toStringTrim(list.get(i).get("hrcode"), "");
				String startDateStr = Rtext.toStringTrim(list.get(i).get("startDate"), "");
				String endDateStr = Rtext.toStringTrim(list.get(i).get("endDate"), "");
				List startDate  =new ArrayList();
				List endDate  =new ArrayList();
				startDate.add(startDateStr);
				endDate.add(endDateStr);
				for(int j=i+1 ; j<list.size() ; j++){
					String hr = Rtext.toStringTrim(list.get(j).get("hrcode"), "");
					String start = Rtext.toStringTrim(list.get(j).get("startDate"), "");
					String end = Rtext.toStringTrim(list.get(j).get("endDate"), "");
					if(hrCode.equals(hr)){
						startDate.add(start);
						endDate.add(end);
					}
				}
				Map m = new HashMap();
				m.put("hrCode",hrCode);
				m.put("startDate",startDate);
				m.put("endDate",endDate);
				settleMap.add(m);
			}

			// 2.去重 如果list的最后几条是同一个参与人 则会出现重复数据 settleMap的最后条数据可能重复
			List<Map<String, String>> settle  = new ArrayList<>();
			for(int i=0; i<settleMap.size() ; i++){
				for(int j=i+1 ;j <settleMap.size() ;j++){
					if(settleMap.get(i).get("hrCode").equals(settleMap.get(j).get("hrCode"))){
						List a = new ArrayList();
						List b = new ArrayList();
						a.addAll(Collections.singleton(Collections.singletonList(settleMap.get(i).get("startDate")).get(0)));
						b.addAll(Collections.singleton(Collections.singletonList(settleMap.get(j).get("startDate")).get(0)));
						a = (List) a.get(0);
						b = (List) b.get(0);
						/*if(a.size()>b.size()){
							settle.add(settleMap.get(i));
							break;
						}*/
						if(a.size()>b.size()){
							settle.add(settleMap.get(j));
							break;
						}
					}/*else {
						settle.add(settleMap.get(i));
						break;
					}*/
				}
			}
			settleMap.removeAll(settle);

			for (Map<String, String> map : settleMap) {
				List startDate = new ArrayList();
				List endDate = new ArrayList();
				String hrCode = map.get("hrCode");
				startDate.addAll( Collections.singletonList(Collections.singletonList(map.get("startDate")).get(0)));
				endDate.addAll( Collections.singletonList(Collections.singletonList(map.get("endDate")).get(0)));
				startDate =  (List) startDate.get(0);
				endDate =  (List) endDate.get(0);
				//临时储存setDateOld的值
				Set<Map<String, String>> listChangeDate = new HashSet<>();
				// 3.把原来时间段取出来
				List<Map<String, String>> userOld = bgMapper.userDateOld(hrCode, proId);
				//把原来时间按月分段
				List<KeyValueForDate> listDateOld = new ArrayList<>();
				//把listDateOld放到set（setDateOld）里
				Set<Map<String,String>> setDateOld = new HashSet<>();
				for (Map<String, String> userMap : userOld) {
					listDateOld .addAll( SplitDateUtil.getKeyValueForDate(userMap.get("START_DATE"), userMap.get("END_DATE")));
				}
				for(KeyValueForDate key : listDateOld){
					Map m = new HashMap();
					m.put("start",key.getStartDate());
					m.put("end",key.getEndDate());
					setDateOld.add(m);
				}
				// 4. 把现在时间取出来并按月分段
				List<KeyValueForDate> listDateNew = new ArrayList<>();
				for(int i=0 ; i<startDate.size(); i++) {
					listDateNew .addAll( SplitDateUtil.getKeyValueForDate(String.valueOf(startDate.get(i)), String.valueOf(endDate.get(i))));
				}
				Set<Map<String, String>> setDateNew = new HashSet<>();
				for(KeyValueForDate key : listDateNew){
					Map m = new HashMap();
					m.put("start",key.getStartDate());
					m.put("end",key.getEndDate());
					setDateNew.add(m);
				}

				// 5. 两时间对比取出变化的时间段
				listChangeDate.addAll(setDateOld);
				setDateOld.addAll(setDateNew);
				listChangeDate.retainAll(setDateNew);
				setDateOld.removeAll(listChangeDate);

				// 6. 判断改变的时间段中是否有工时
				for (Map<String,String> key : setDateOld) {
					String[] str = key.get("start").split("-");
					int year = Integer.parseInt(str[0]);
					int month = Integer.parseInt(str[1]);
					String start = DateUtil.getFirstDayOfMonth1(year, month);
					String end = DateUtil.getLastDayOfMonth1(year, month);
					List<Map<String, String>> userWorkingInfo = bgMapper.userWorkingInfo(start, end, proId, hrCode);
					if (null != userWorkingInfo && userWorkingInfo.size() > 0) {
						Map m = new HashMap();
						m.put("NAME", userWorkingInfo.get(0).get("USERALIAS"));
						m.put("WORK_TIME_BEGIN", userWorkingInfo.get(0).get("WORK_TIME_BEGIN"));
						m.put("WORK_TIME_END", userWorkingInfo.get(0).get("WORK_TIME_END"));
						failList.add(m);
					}
				}
			}
		}

		if(null != failList && failList.size()>0){
			resultMap.put("result", "fail");
			resultMap.put("failList", JSON.toJSONString(failList, SerializerFeature.WriteMapNullValue));
			return JSON.toJSONString(resultMap);
		}


		//删除旧的项目下所有人员
		int affectedRows = bgMapper.deleteProUsersByProId(proId);
		bgServiceLog.info("updateStuff ：删除成功"+affectedRows+"名参与人！");
		
		// 重新添加人员
		int count=saveStuff(proId,src,list);
		resultMap.put("result", "success");
		resultMap.put("count", count+"");
		resultMap.put("failCount", (list.size()-count)+"");
		return JSON.toJSONString(resultMap);
	}

	@Override
	public void changeEmpRole(String projectId,String in_hrCode,String ex_hrCode,String role) {
		bgMapper.changeEmpRoleByHrCode(projectId,in_hrCode,ex_hrCode,role);
	}

	
	@Override
	public void saveBeforePro(String proId, String[] idsArr) {
		//将所有关联项目id的项目前期的关联项目id清空
		bgMapper.updateProInfoFieldByField("RELATED_PROJECT_ID",proId,null);
		
		if(idsArr==null || idsArr.length==0) return;
		
		//为所有idsArr的项目前期id添加关联项目proId
		for (String bid : idsArr) {
			updateProInfoField(bid, "RELATED_PROJECT_ID", proId);
		}
	}

	@Override
	public Map<String, Object> getProDataByProIdAndSrc(String proId, String src) {
		if("KY".equals(src)){
			return bgMapper.getProInfoByProIdFromKY(proId);
		}else if("HX".equals(src)){
			return bgMapper.getProInfoByProIdFromHX(proId);
		}else if ("JS".equals(src)){
			return bgMapper.getProInfoByProIdFromJS(proId);
		}
		return null;
	}

	@Override
	public List<HashMap> getEmpDataByProIdAndSrc(String proId, String src) {
		if("KY".equals(src)){
			return bgMapper.getEmpByProIdFromKY(proId);
		}else if("HX".equals(src)){
			return bgMapper.getEmpByProIdFromHX(proId);
		}else if("JS".equals(src)){
			return bgMapper.getEmpByProIdFromKYJS(proId);
		}
		return new ArrayList<HashMap>() {
		};
	}

	@Override
	public List<Map<String, Object>> getProjectsBySrc(String src,String proName, String wbsNumber) {
		proName = Rtext.toStringTrim(proName,"");
		wbsNumber = Rtext.toStringTrim(wbsNumber,"");
		String username = webUtils.getUsername();
		
		if("KY".equals(src)){
			return bgMapper.getProjectsFromKY(username,proName,wbsNumber);
		}else if("HX".equals(src)){
			return bgMapper.getProjectsFromHX(username,proName,wbsNumber);
		}else if ("JS".equals(src)){
			return bgMapper.getProjectsFromJS(username,proName,wbsNumber);
		}
		return null;
	}

	/**
	 * 添加项目与来源系统的关联(如果存在proId则更新，否则新增)
	 * @param proId 报工系统的id
	 * @param srcProId 项目来源系统的项目id
	 * @param src 项目来源系统
	 */
	private void addProRelation(String proId, String srcProId, String src) {
		if(Rtext.isEmpty(proId) || Rtext.isEmpty(srcProId) || Rtext.isEmpty(src)){
			bgServiceLog.info("新增项目信息：addProRelation添加项目和来源系统项目id关联缺少参数！");
			return;
		}
		bgMapper.addProRelation(proId,srcProId,src);
	}

	@Override
	public void addEmpRelation(String proUserId, String proId, String hrCode , String src) {
		if(Rtext.isEmpty(proUserId) || Rtext.isEmpty(proId) || Rtext.isEmpty(hrCode) || Rtext.isEmpty(src)){
			bgServiceLog.info("新增项目信息：addEmpRelation添加项目和来源系统项目id关联缺少参数！");
			return;
		}
		bgMapper.addEmpRelation(proUserId,proId,hrCode,src);
	}
	
	@Override
	public String ajaxSaveProInfo(HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<>();
		ProjectInfoPo pro = new ProjectInfoPo();
		String proId = Rtext.toStringTrim(request.getParameter("proId"),"");
		String method = Rtext.toStringTrim(request.getParameter("method"),"");//要执行的操作方法，存在proId为更新，否则保存
		String projectName = Rtext.toStringTrim(request.getParameter("projectName"),"");
		String category =Rtext.toStringTrim(request.getParameter("category"),""); 
		String projectNumber =Rtext.toStringTrim(request.getParameter("projectNumber"),""); 
		String WBSNumber = Rtext.toStringTrim(request.getParameter("WBSNumber"),""); 
		String projectIntroduce = Rtext.toStringTrim(request.getParameter("projectIntroduce"),""); 
		String deptCode = Rtext.toStringTrim(request.getParameter("deptCode"),"");
		
		String src = Rtext.toStringTrim(request.getParameter("src"),"");//项目来源系统
		String srcProId = Rtext.toStringTrim(request.getParameter("srcProId"),"");//项目来源系统的项目id
		//是否分解是（一期默认为不分解）
		//String decompose = "否".equals(Rtext.toStringTrim(request.getParameter("decompose"),"")) ? "0" : "1";
		String decompose="0";
		String startDateStr=Rtext.toStringTrim(request.getParameter("startDate"),"");
		String endDateStr=Rtext.toStringTrim(request.getParameter("endDate"),"");
		String planHoursStr=Rtext.toStringTrim(request.getParameter("planHours"),"");
		String projectGrade=Rtext.toStringTrim(request.getParameter("projectGrade"),"");
		//项目编号后台生成
		if(projectNumber.indexOf("BG")==-1){//如果不存在项目编号
			projectNumber = getBGNumber();
		}
		//校验项目信息
		if(Rtext.isEmpty(projectName) 
				|| Rtext.isEmpty(category)
				|| Rtext.isEmpty(startDateStr) 
				|| Rtext.isEmpty(endDateStr) 
				|| Rtext.isEmpty(planHoursStr)
				|| Rtext.isEmpty(projectGrade)){
			bgServiceLog.info("bgController 项目必填参数存在空值："+"projectName:"+projectName+"/"+"category:"+category+"/"+
					"startDateStr:"+startDateStr+"/"+"endDateStr:"+endDateStr+"/"+
					"planHoursStr:"+planHoursStr+"/"+"projectGrade"+projectGrade);
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(("KY".equalsIgnoreCase(category) || "HX".equalsIgnoreCase(category)) 
				&& Rtext.isEmpty(WBSNumber)){
			bgServiceLog.info("科研或横向时，wbs编号为空！WBSNumber :"+WBSNumber);
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if("JS".equals(category) && Rtext.isEmpty(deptCode)){
			bgServiceLog.info("技术服务项目，组织信息为必填项");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(projectGrade.length()>20){
			bgServiceLog.info("项目级别超过20个字");
			resultMap.put("result","fail");
			return JSON.toJSONString(resultMap);
		}
		if(projectName.length()>50){
			bgServiceLog.info("项目名称超过50个字");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(projectIntroduce.length()>200){
			bgServiceLog.info("项目介绍超过200个字");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
			
		if(!DateUtil.isValidDate(startDateStr)){
			bgServiceLog.info("开始日期格式错误");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		
		if(!DateUtil.isValidDate(endDateStr)){
			bgServiceLog.info("结束日期格式错误");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		if(!ParamValidationUtil.isValidInt(planHoursStr) || planHoursStr.length()>8){
			bgServiceLog.info("计划投入工时不是8位整数");
			resultMap.put("result", "fail");
			return JSON.toJSONString(resultMap);
		}
		//如果项目时间修改则效验修改范围是否有工时 （当项目时间范围缩小：如果缩小时间段有工时则不能修改）
		Map<String,String> projectMap = bgMapper.projectMap(proId);
		if(null!= projectMap) {
            try {
                if (DateUtil.judgeDateNOEqu(projectMap.get("START_DATE"), startDateStr)) {
                    //取月初和月末
                    String[] str = projectMap.get("START_DATE").split("-");
                    String[] s = startDateStr.split("-");
                    int yearOld = Integer.parseInt(str[0]);
                    int monthOld = Integer.parseInt(str[1]);
                    int yearNow = Integer.parseInt(s[0]);
                    int monthNow = Integer.parseInt(s[1]);
                    //之前项目开始时间
                    String dateBegin = DateUtil.getFirstDayOfMonth1(yearOld, monthOld);
                    //现在项目开始时间
                    String dateEnd = DateUtil.getLastDayOfMonth1(yearNow, monthNow);
                    List<Map<String, String>> workingHours = bgMapper.userWorkingInfo(dateBegin, dateEnd, proId, null);
                    if (workingHours != null && workingHours.size() > 0) {
                        resultMap.put("result", "error");
                        resultMap.put("content", projectMap.get("START_DATE") + "到" + startDateStr + "时间段有工时填报，不可修改！");
                        return JSON.toJSONString(resultMap);
                    }
                }
                if (DateUtil.judgeDateNOEqu(endDateStr, projectMap.get("END_DATE"))) {
                    //取月初和月末
                    String[] str = projectMap.get("END_DATE").split("-");
                    String[] s = endDateStr.split("-");
                    int yearOld = Integer.parseInt(str[0]);
                    int monthOld = Integer.parseInt(str[1]);
                    int yearNow = Integer.parseInt(s[0]);
                    int monthNow = Integer.parseInt(s[1]);
                    //现在项目结束时间
                    String dateBegin = DateUtil.getFirstDayOfMonth1(yearNow, monthNow);
                    //之前项目结束时间
                    String dateEnd = DateUtil.getLastDayOfMonth1(yearOld, monthOld);
                    List<Map<String, String>> workingHours = bgMapper.userWorkingInfo(dateBegin, dateEnd, proId, null);
                    if (workingHours != null && workingHours.size() > 0) {
                        resultMap.put("result", "error");
                        resultMap.put("content", endDateStr + "到" + projectMap.get("END_DATE") + "时间段有工时填报，不可修改！");
                        return JSON.toJSONString(resultMap);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

		Date startDate = DateUtil.fomatDate(startDateStr);
		Date endDate = DateUtil.fomatDate(endDateStr);
		Integer planHours = Rtext.ToInteger(planHoursStr, 0);
		
		proId = proId.isEmpty()?Rtext.getUUID():proId;
		pro.setId(proId);
		pro.setProjectName(projectName);
		pro.setProjectNumber(projectNumber);
		pro.setCategory(category);
		pro.setWBSNumber(WBSNumber);
		pro.setProjectIntroduce(projectIntroduce);
		pro.setStartDate(startDate);
		pro.setEndDate(endDate);
		pro.setProjectGrade(projectGrade);
		if("JS".equals(category)){//当为技术服务项目时才保存或更新组织信息
			pro.setOrganInfo(getDeptIdByDeptCode(deptCode));
		}
		pro.setPlanHours(planHours);
		pro.setDecompose(decompose);
		
		String srcFlag = "0";
		if("KY".equalsIgnoreCase(src)){
			srcFlag = "2";
		}else if("HX".equalsIgnoreCase(src)){
			srcFlag = "3";
		}else if("JS".equalsIgnoreCase(src)){
			srcFlag = "4";
		}

		pro.setSrc(srcFlag);
		pro.setStatus("1");
		pro.setProjectStatus("0");
		pro.setCreateDate(new Date());
		pro.setUpdateDate(new Date());
		pro.setUpdateUser(webUtils.getUsername());
		pro.setCreateUser(webUtils.getUsername());
		
		int affectedRows;
		if("save".equals(method)){
			affectedRows = addProInfo(pro);
		}else{
			affectedRows = updateProInfo(pro);
		}
		
		
		//项目来源是否为关联的
		if(affectedRows==1 && !"BG".equalsIgnoreCase(src)){
			//是否已关联该项目
			List<Map<String, Object>>  proRelList = bgMapper.getProRelation(proId,srcProId,src);
			if(proRelList==null || proRelList.size()==0){//未关联
				//将其添加到关联表
				addProRelation(proId,srcProId,src);
				//将关联项目下的参与人保存到报工系统
				List<HashMap>  list = getEmpDataByProIdAndSrc(srcProId,src);
				int count = saveEmpAfterProSaved(proId,startDate, endDate, src, list);
				bgServiceLog.info("关联项目下的参与人， " + count+"人已添加到人员关联表！");
			}
		}
		
		if (affectedRows == 1) {
			resultMap.put("result", "success");
			resultMap.put("proId", pro.getId());
			resultMap.put("wbsNumber", WBSNumber);
			resultMap.put("proNumber", projectNumber);
		} else {
			resultMap.put("result", "fail");
		}
		bgServiceLog.info("保存项目信息返回字符串： " + JSON.toJSONString(resultMap));
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 保存项目后保存关联项目的参与人员
	 * @param proId
	 * @param src
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private int saveEmpAfterProSaved(String proId,Date startDate,Date endDate,String src,List<HashMap> list){
		//删除旧的项目下所有人员以及关联关系
		deleteEmpAndRelation(proId);
		
		// 重新添加人员
		int count=0;
		for (HashMap<String, String> map : list) {
			String empName=Rtext.toStringTrim(map.get("stuffName"),"");
			String roleStr=Rtext.toStringTrim(map.get("role"),"");
			String hrCode=Rtext.toStringTrim(map.get("hrcode"),"");
//			String startDateStr=Rtext.toStringTrim(map.get("startDate"),"");
//			String endDateStr=Rtext.toStringTrim(map.get("endDate"),"");
			String taskStr=Rtext.toStringTrim(map.get("task"),"");
			Double planHours = Rtext.ToDouble(map.get("planHours"), 0d);
			String role=roleStr;
			
			ProjectUserPo proUser = new ProjectUserPo();
			String proUserId = Rtext.getUUID();
			proUser.setId(proUserId);
			proUser.setRole(role);
			proUser.setProjectId(proId);
			proUser.setHrcode(hrCode);
			proUser.setEmpName(empName);
			proUser.setStartDate(startDate);//默认为项目开始日期
			proUser.setEndDate(endDate);//默认为项目结束日期
			proUser.setTask(taskStr);
			proUser.setPlanHours(planHours);
			
			String srcFlag = "0";
			if("KY".equalsIgnoreCase(src)){
				srcFlag = "2";
			}else if("HX".equalsIgnoreCase(src)){
				srcFlag = "3";
			}else if ("JS".equalsIgnoreCase(src)){
				srcFlag = "4";
			}
			proUser.setSrc(srcFlag);
			proUser.setStatus("1");
			proUser.setCreateDate(new Date());
			proUser.setUpdateDate(new Date());
			proUser.setCreateUser(webUtils.getUsername());
			proUser.setUpdateUser(webUtils.getUsername());
			// 注意事务
			int affectedRows = addProUser(proUser);
			
			//若果添加参与人成功，并且项目信息为关联项目，则添加人员信息到关联表
			if(affectedRows==1 && !"BG".equalsIgnoreCase(src)) addEmpRelation(proUserId,proId,hrCode,src);
			
			count+=affectedRows;
		}
		
		return count;
	}
	
	public static void main(String[] args) {
		/*String[] arr1 = new String[3];
		System.out.println(Arrays.toString(arr1));
		
		String[] arr = {"1","2","3"};
		System.out.println(Arrays.toString(arr1));*/
	}

	@Override
	public List<Map<String, Object>> getBeforeProjects(String username, String proName ,boolean isRelated,String relProId) {
		if(isRelated){
			return bgMapper.getBeforeProjects(null,proName,isRelated,relProId);
		}else{
			CommonCurrentUser user = userUtils.getCommonCurrentUserByUsername(username);
			String deptId = user==null?"":user.getDeptId();
			return bgMapper.getBeforeProjects(deptId,proName,isRelated,relProId);
		}
	}

	/*@Override
	public String deleteBeforePro(String idsStr) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if(Rtext.isEmpty(idsStr)){
			jsonMap.put("success", "false");
			return JSON.toJSONString(jsonMap);
		}
		
		String[] idsArr = idsStr.split(",");
		boolean res = bgMapper.deleteBeforeProById(idsArr);
		
		jsonMap.put("success", String.valueOf(res));
		return JSON.toJSONString(jsonMap);
	}*/
}