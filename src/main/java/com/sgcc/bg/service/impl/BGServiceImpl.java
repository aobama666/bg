package com.sgcc.bg.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExcelUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BGMapper;
import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectInfoVali;
import com.sgcc.bg.model.ProjectUserPo;
import com.sgcc.bg.model.ProjectUserVali;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.IBGService;
import com.alibaba.fastjson.JSONObject;

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
	public List<Map<String, String>> getAllProjects(String proName,String proStatus,Integer page,Integer limit) {
		String start="0";
		String end="30";
		if(page!=null && limit!=null){
			start=String.valueOf((page-1)*limit);
			end=String.valueOf(page*limit);
		}
		bgServiceLog.info("page="+page+"/limit="+limit);
		List<Map<String, String>> list=bgMapper.getAllProjects(webUtils.getUsername(),proName, proStatus,start,end);
		return list;
	}

	@Override
	public int addProInfo(ProjectInfoPo pro) {
		pro.setStatus("1");
		pro.setProjectStatus("0");
		pro.setCreateDate(new Date());
		pro.setUpdateDate(new Date());
		pro.setUpdateUser(webUtils.getUsername());
		pro.setCreateUser(webUtils.getUsername());
		return bgMapper.addProInfo(pro);
	}

	@Override
	public int addProUser(ProjectUserPo proUser) {
		proUser.setStatus("1");
		proUser.setCreateDate(new Date());
		proUser.setUpdateDate(new Date());
		proUser.setCreateUser(webUtils.getUsername());
		proUser.setUpdateUser(webUtils.getUsername());
		return bgMapper.addProUser(proUser);
	}

	@Override
	public int updateProInfo(ProjectInfoPo pro) {
		//只需要更新人信息
		pro.setUpdateUser(webUtils.getUsername());
		pro.setUpdateDate(new Date());
		return bgMapper.updateProInfo(pro);
	}

	@Override
	public int deleteProUsersByProId(String proId) {
		return bgMapper.deleteProUsersByProId(proId);
	}

	@Override
	public Map<String, String> getProInfoByProId(String proId) {
		Map<String, String> proMap=bgMapper.getProInfoByProId(proId);
		return proMap;
	}

	@Override
	public List<Map<String, String>> getProUsersByProId(String proId) {
		List<Map<String, String>> userList=bgMapper.getProUsersByProId(proId);
		return userList;
	}

	@Override
	public int deleteProjectByProId(String proId) {
		//删除该项目的同时也把其所属人员信息删除
		//删除项目信息为逻辑删
		int affectedRows_1=bgMapper.deleteProjectByProId(proId,webUtils.getUsername(),new Date());
		int affectedRows_2=bgMapper.deleteProUsersByProId(proId);
		return affectedRows_1;
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
			int count=0;
			for (Map<String, String> emp : empList) {
				if("1".equals(emp.get("ROLE"))){
					count++;
				}
			}
			if(count==0){
				return "目前项目下不存在负责人，请添加负责人后启动。";//存在人员但无负责人
			}else if(count>1){
				return "目前项目下存在多个负责人，指定一名负责人。";//负责人重复
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

	@Override
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
	}

	@Override
	public String checkUniqueness(String wbsNumber) {
		int affectedRows=bgMapper.checkUniqueness(wbsNumber);
		if(affectedRows==0){
			return "true";
		}else{
			return "false";
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
			//项目类型
			String categoryStr="[科研项目],[横向项目],[技术服务项目]";
			wbsCodeSet.addAll(list);
			bgServiceLog.info("项目信息excel表格最后一行： " + rows);
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
						errorInfo.append("项目名称不能为空！");
						errorNum.add(1);
					} else if (cellValue[1].length() > 50) {
						errorInfo.append("项目名称不能超过50字！");
						errorNum.add(1);
					}
					// 项目分类校验 必填
					if (cellValue[2] == null || "".equals(cellValue[2])) {
						errorInfo.append("项目类型不能为空！");
						errorNum.add(2);
					}else if(!categoryStr.contains("["+cellValue[2]+"]")){
						errorInfo.append("无此项目类型！");
						errorNum.add(2);
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
					if (("科研项目".equals(cellValue[2]) || "横向项目".equals(cellValue[2]))
							&& Rtext.isEmpty(cellValue[3])) {
						errorInfo.append("wbs编号不能为空！");
						errorNum.add(3);
					}else if (!Rtext.isEmpty(cellValue[3]) && !repeatChecker.add(cellValue[3])) {
						errorInfo.append("wbs编号重复！");
						errorNum.add(3);
					}else if(!Rtext.isEmpty(cellValue[3]) && wbsCodeSet.contains(cellValue[3])){
						errorInfo.append("系统中已经存此wbs编号！");
						errorNum.add(3);
					}
				
					// 项目说明长度200以内
					if (cellValue[4].length() > 200) {
						errorInfo.append("项目说明不能超过200字！");
						errorNum.add(4);
					}
					// 发布日期 必填;格式
					if (cellValue[5] == null || "".equals(cellValue[5])) {
						errorInfo.append("项目开始日期不能为空！");
						errorNum.add(5);
					} else if (!DateUtil.isValidDate(cellValue[5], "yyyy-MM-dd")) {
						errorInfo.append("项目开始日期填写有误！");
						errorNum.add(5);
					}
					// 发布日期 必填;格式
					if (cellValue[6] == null || "".equals(cellValue[6])) {
						errorInfo.append("项目结束日期不能为空！");
						errorNum.add(6);
					} else if (!DateUtil.isValidDate(cellValue[6], "yyyy-MM-dd")) {
						errorInfo.append("项目结束日期填写有误！");
						errorNum.add(6);
					}
					//如果日期均正确 ，则验证技术服务项目是否跨年
					if(!errorNum.contains(5) && !errorNum.contains(6)){
						//如果日期均正确 ，则验证技术服务项目是否跨年
						if("技术服务项目".equals(cellValue[2])){
							if(!(cellValue[5].substring(0,4)).equals(cellValue[6].substring(0,4))){
								errorInfo.append("技术服务项目不能跨年！");
								errorNum.add(5);
								errorNum.add(6);
							}
						}
						//验证日期是否符合先后逻辑顺序
						if(DateUtil.fomatDate(cellValue[6]).getTime()<=DateUtil.fomatDate(cellValue[5]).getTime()){
							errorInfo.append("项目结束日期必须大于项目开始日期！");
							errorNum.add(6);
						}
					}
					// 组织信息 技术服务项目为必填项(如果不填则默认登录人所在处室)
					if("技术服务项目".equals(cellValue[2])){
						if(!Rtext.isEmpty(cellValue[7])){
							if(Rtext.isEmpty(getDeptIdByDeptCode(cellValue[7]))){
								errorInfo.append("组织信息填写错误！"); 
								errorNum.add(7);
							}
						}
					}
					// 计划投入工时 必填;数字
					if (cellValue[8] == null || "".equals(cellValue[8])) {
						errorInfo.append("计划投入工时不能为空！");
						errorNum.add(8);
					} else if (!cellValue[8].matches(regex)) {
						errorInfo.append("计划投入工时填写有误！");
						errorNum.add(8);
					} else if (cellValue[8].length() > 8) {
						errorInfo.append("计划投入工时不能超过8位数！");
						errorNum.add(8);
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
						if("科研项目".equals(cellValue[2])){
							pro.setCategory("KY");
						}else if("横向项目".equals(cellValue[2])){
							pro.setCategory("HX");
						}else if("技术服务项目".equals(cellValue[2])){
							pro.setCategory("JS");
						}
						pro.setWBSNumber(cellValue[3]);
						pro.setProjectIntroduce(cellValue[4]);
						pro.setStartDate(DateUtil.fomatDate(cellValue[5]));
						pro.setEndDate(DateUtil.fomatDate(cellValue[6]));
						if("技术服务项目".equals(cellValue[2])){
							String OrganDeptId="";
							if(Rtext.isEmpty(cellValue[7])){
								CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(currentUsername);
								String deptCode=currentUser.getDeptCode();
								OrganDeptId=getDeptIdByDeptCode(deptCode);
							}else{
								OrganDeptId=getDeptIdByDeptCode(cellValue[7]);
							}
							pro.setOrganInfo(OrganDeptId);
						}
						pro.setPlanHours(Integer.parseInt(cellValue[8]));
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
						//保存正确数据
						proList.add(pro);
					} else {// 未通过校验
						ProjectInfoVali prov = new ProjectInfoVali();
						prov.setSqnum(cellValue[0]);
						prov.setProjectName(cellValue[1]);
						prov.setCategory(cellValue[2]);
						prov.setWBSNumber(cellValue[3]);
						prov.setProjectIntroduce(cellValue[4]);
						prov.setStartDate(cellValue[5]);
						prov.setEndDate(cellValue[6]);
						prov.setOrganInfo(cellValue[7]);
						prov.setPlanHours(cellValue[8]);
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
						errorInfo.append("项目编号不能为空！");
						errorNum.add(1);
					}else if(!list.contains(cellValue[1])){
						errorInfo.append("项目中不存在此项目编号！");
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
							errorInfo.append("该项目已完成或废止，无法添加人员！");
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
						errorInfo.append("人员编号不能为空！");
						errorNum.add(3);
					}else{
						user=userUtils.getCommonCurrentUserByHrCode(cellValue[3]);
						if(user==null){
							errorInfo.append("人员编号错误！");
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
						errorInfo.append("项目开始日期填写有误！");
						errorNum.add(4);
					}
					// 项目结束日期 必填(如果不填则默认项目结束时间);格式
					if (!Rtext.isEmpty(cellValue[5]) && !DateUtil.isValidDate(cellValue[5], "yyyy-MM-dd")) {
						errorInfo.append("项目结束日期填写有误！");
						errorNum.add(5);
					}
					//如果日期正确 ，则验证是否超出项目日期
					if(!Rtext.isEmpty(cellValue[4]) && !Rtext.isEmpty(startDate) && !errorNum.contains(4)){
						if(!DateUtil.compareDate(cellValue[4], startDate) || !DateUtil.compareDate(endDate,cellValue[4])){
							errorInfo.append("项目开始日期超出项目周期！");
							errorNum.add(4);
						}
					}
					if(!Rtext.isEmpty(cellValue[5]) && !Rtext.isEmpty(endDate) && !errorNum.contains(5)){
						if(!DateUtil.compareDate(cellValue[5], startDate) || !DateUtil.compareDate(endDate,cellValue[5])){
							errorInfo.append("项目结束日期超出项目周期！");
							errorNum.add(5);
						}
					}
					//验证日期是否符合先后逻辑顺序
					if(!Rtext.isEmpty(cellValue[4]) && !Rtext.isEmpty(cellValue[5]) && !errorNum.contains(4) && !errorNum.contains(5)){
						if(DateUtil.fomatDate(cellValue[5]).getTime()<DateUtil.fomatDate(cellValue[4]).getTime()){
							errorInfo.append("结束日期必须大于开始日期！");
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
									errorInfo.append("与已存在参与人"+user.getUserAlias()+"日期("+dateMap.get("startDate")+"至"+dateMap.get("endDate")+")重叠!");
									errorNum.add(4);
									errorNum.add(5);
									break;
								}
							}
						}
					}
					
					// 角色 必填
					if (Rtext.isEmpty(cellValue[6])) {
						errorInfo.append("角色不能为空！");
						errorNum.add(6);
					}else if(!roleStr.contains("["+cellValue[6]+"]")){
						errorInfo.append("不存在此角色！");
						errorNum.add(6);
					}else if(!Rtext.isEmpty(proId)){//项目存在
						//获取map中指定项目的项目负责人数量
						int currentValue=roleMap.get(proId);
						if("项目负责人".equals(cellValue[6])){
							int principalCount=bgMapper.getPrincipalCountByProId(proId);
							if(principalCount>0 || currentValue>0){
								errorInfo.append("项目已存在负责人！");
								errorNum.add(6);
							}
						}
					}
					
					//校验项目中负责人只能存在一条
					if(!Rtext.isEmpty(proId) && !errorNum.contains(3) && !errorNum.contains(6)){//项目存在，此人存在且角色存在并不冲突
						//获取当前项目中人员信息
						List<Map<String, String>> empList=bgMapper.getProUsersByProId(proId);
						for (ProjectUserPo po : userList) {
							if(po.getProjectId().equals(proId) && po.getHrcode().equals(cellValue[3])){
								Map<String,String> map=new HashMap<>();
								map.put("ROLE",po.getRole());
								map.put("HRCODE",po.getHrcode());
								empList.add(map);
							}
						}
						if("项目负责人".equals(cellValue[6])){//此人存在且为负责人时，项目中如果已存在测人记录，则不允许添加
							for (Map<String, String> map : empList) {
								if(cellValue[3].equals(map.get("HRCODE"))){
									errorInfo.append("此人已存在项目中，不允许添加为负责人！");
									errorNum.add(6);
									break;
								}
							}
						}else{//此人存在且为参与人时，项目中此人如果已经作为负责人，则不允许添加
							for (Map<String, String> map : empList) {
								if(cellValue[3].equals(map.get("HRCODE")) && "1".equals(map.get("ROLE"))){
									errorInfo.append("此人为当前项目负责人，请勿重复添加！");
									errorNum.add(6);
									break;
								}
							}
						}
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
						proUser.setStatus("1");
						proUser.setCreateDate(new Date());
						proUser.setUpdateDate(new Date());
						proUser.setCreateUser(webUtils.getUsername());
						proUser.setUpdateUser(webUtils.getUsername());
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
						pruv.setErrorInfo(errorInfo.toString());
						pruv.setErrSet(errorNum);
						errorList.add(pruv);
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
		String statusStr="0未启动/1进行中/2暂停/3已结束/4废止/";
		List<Map<String,String>> dataList=new ArrayList<Map<String,String>>();
		if(ids.indexOf("{")!=-1){
			Map<String,String> map=JSONObject.parseObject(ids,Map.class);
			String proStatus = Rtext.toStringTrim(map.get("proStatus"), "");
			String proName = Rtext.toStringTrim(map.get("proName"), "");
			//复用页面查询方法，把分页范围调大
			dataList=bgMapper.getAllProjects(webUtils.getUsername(), proName, proStatus, "0","100000000");
		}else{
			String[] idArr=ids.split(",");
			Map<String,String> proMap = new HashMap<String,String>();
			for (int i = 0; i < idArr.length; i++) {
				proMap=bgMapper.getProInfoByProId(idArr[i]);
				Map<String,String> dictMap=dict.getDictDataByPcode("category100002");
				proMap.put("category", dictMap.get(proMap.get("category")));
				dataList.add(proMap);
			}
		}
		
		for (int i = 0; i < dataList.size(); i++) {
			Map<String,String> map=dataList.get(i);
			String proStatus=map.get("projectStatus");
			int statusSndex=statusStr.indexOf(proStatus);
			if(statusSndex!=-1){
				int fromIndex=statusStr.indexOf("/", statusSndex);
				proStatus=statusStr.substring(statusSndex+1, fromIndex);
			}
			map.put("projectStatus", proStatus);
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
	public int updateProInfoField(String proId,String field, String value) {
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
	
}