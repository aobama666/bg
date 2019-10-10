package com.sgcc.bg.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.mapper.BGMapper;
import com.sgcc.bg.mapper.HandleSyncMapper;
import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.model.ProjectUserPo;
import com.sgcc.bg.service.HandleSyncService;

import ch.qos.logback.classic.Logger;

@Service
public class HandleSyncServiceImpl implements HandleSyncService {
	
	private Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncServiceImpl.class);
	
	@Autowired
	private HandleSyncMapper handleMapper;
	
	@Autowired
	private BGMapper bgMapper;
	
	@Autowired
	private UserUtils userUtils;
	
	//报工系统转存表删除几天前的数据
	private final static int DAYS = 2;
	
/**********************************************处理科研******************************************************/
	
	@Override
	public void copyFromKY() {
		logger.info("转存项目信息表");
		handleMapper.addKYToProTemp();
		handleMapper.cutKYProTemp(DAYS);
		
		logger.info("转存参与人员表");
		handleMapper.addKYToEmpTemp();
		handleMapper.cutKYEmpTemp(DAYS);
	}
	
	@Override
	public void validateKY() {
		logger.info("删除科研中间表项目信息");
		handleMapper.truncateTable("BG_SYNC_KY_PROJECT");
//		字段名称	英文名称	类型	是否必推字段	要求
//		项目ID	PROJECT_ID	VARCHAR2(50)	是	科研项目ID，唯一
//		项目分类	PROJECT_TYPE	VARCHAR2(20)	是	科研项目，编码：KYXM
//		项目名称	PROJECT_NAME	VARCHAR2(100 CHAR)	是	
//		WBS编号	PROJECT_WBS	VARCHAR2(50)	是	
//		项目说明	PROJECT_REMARK	VARCHAR2(200 CHAR )	否	项目摘要
//		项目开始时间	PROJECT_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		项目结束时间	PROJECT_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
		logger.info("获取所有的项目信息");
		List<Map<String, Object>> proList = handleMapper.getAllSyncProFromKY();
		logger.info("处理获取到的项目信息");
		for (Map<String, Object> proMap : proList) {
			if(validPro(proMap,"KY")){
				//校验通过存入报工中间表
				handleMapper.saveProFromKY(proMap);
			}
		}
		
		logger.info("删除科研中间表人员信息");
		handleMapper.truncateTable("BG_SYNC_KY_EMP");
//		字段名称	英文名称	类型	是否必推字段	要求
//		人员姓名	USER_NAME	VARCHAR2(100 CHAR)	是	
//		人员编号	USER_HRCODE	VARCHAR2(20)	是	人资编号，必须是院内人员，唯一
//		开始时间	JOIN_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		结束时间	JOIN_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		角色	PROJECT_ROLE	VARCHAR2(2)	是	纵向项目中的项目执行人默认为项目负责人；
//		其他人默认为项目参与人;
//		1 项目负责人 0 项目参与人
//		投入工时	WORK_TIME	NUMBER	是	单位：月
		logger.info("获取所有参与人员");
		List<Map<String, Object>> empList = handleMapper.getAllSyncEmpFromKY();
		logger.info("处理获取到的参与人员信息");
		for (Map<String, Object> empMap : empList) {
			if(valiEmp(empMap,"KY")){
				//通过校验的参与人员信息保存到报工系统人员中间表
				handleMapper.saveEmpFromKY(empMap);
			}
		}
		
		
		
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updateFromKY() {
		//获取所有已关联的科研系统项目关联关系
		List<Map<String, Object>>  proRelList = bgMapper.getProRelation(null,null,"KY");
 		for (Map<String, Object> map : proRelList) {
			String bgProId = Rtext.toString(map.get("BG_ID"));
			String kyProId = Rtext.toString(map.get("SYNC_ID"));
			
			if(bgProId.isEmpty() || kyProId.isEmpty()) continue;
			
			//更新已经关联到报工系统的项目信息的部分字段（项目类型，项目名称，wbs编号）
			Map<String, Object> kyProMap = bgMapper.getProInfoByProIdFromKY(kyProId);
			
			String proName = Rtext.toString(kyProMap.get("PROJECT_NAME"));
			String wbsCode = Rtext.toString(kyProMap.get("WBS_NUMBER"));

			ProjectInfoPo pro = bgMapper.getProPoByProId(bgProId);
			pro.setProjectName(proName);
			pro.setWBSNumber(wbsCode);
			pro.setUpdateDate(new Date());
			pro.setUpdateUser("HandleSyncJob");
			
			bgMapper.updateProInfo(pro);
			
			/*************************************************************************/
			
			//更新参与人员
			Date startDate = pro.getStartDate();
			Date endDate = pro.getEndDate();

			List<Map<String, String>> bgEmpList = bgMapper.getProUsersByProId(bgProId);
			List<HashMap> kyEmpList = bgMapper.getEmpByProIdFromKY(kyProId);
			
			//遍历报工系统中的参与人员获取必要信息
			Set<String> bgEmpSet = new HashSet<>();
			boolean existsPrincipal = false;//是否存在项目负责人
			for (Map<String, String> bgEmp : bgEmpList) {
				String bgHrCode = bgEmp.get("HRCODE");
				String role = bgEmp.get("ROLE");
				bgEmpSet.add(bgHrCode);
				if("1".equals(role)) existsPrincipal = true;
			}
			
			for (HashMap kyEmp : kyEmpList) {
				String kyHrCode = (String) kyEmp.get("hrcode");
				
				if(!bgEmpSet.contains(kyHrCode)){//如果报工系统中不存在则同步到报工系统并添加关联
					ProjectUserPo proUser = new ProjectUserPo();
					String empId = Rtext.getUUID();
					proUser.setId(empId);
					proUser.setRole(existsPrincipal?"0":Rtext.toString(kyEmp.get("role")));
					proUser.setProjectId(bgProId);
					proUser.setHrcode(kyHrCode);
					proUser.setEmpName(Rtext.toString(kyEmp.get("stuffName")));
					
					proUser.setStartDate(startDate);
					proUser.setEndDate(endDate);
					proUser.setTask(null);
					Double planHours = Rtext.ToDouble(kyEmp.get("planHours"),0d);
					proUser.setPlanHours(planHours*22*24);
					proUser.setSrc("2");
					proUser.setStatus("1");
					proUser.setCreateDate(new Date());
					proUser.setUpdateDate(new Date());
					proUser.setCreateUser("HandleSyncJob");
					proUser.setUpdateUser("HandleSyncJob");
					
					bgMapper.addProUser(proUser);
					bgMapper.addEmpRelation(empId, bgProId, kyHrCode, "KY");
				}
			}
		}
	}
	
	
	
//	@Override
//	public List<Map<String, String>>  handleAllSyncProFromKY() {
//		return handleMapper.getAllSyncProFromKY();
//	}
//	
//	@Override
//	public List<Map<String, String>>  handleAllSyncEmpFromKY() {
//		return handleMapper.getAllSyncEmpFromKY();
//	}
//	
	
	/**
	 * 校验科研数据,不通过的做记录
	 * @param proMap
	 * @return 返回项目信息实体类，不通过则为null
	 */
	private boolean validPro(Map<String, Object> proMap,String src){
//		字段名称	英文名称	类型	是否必推字段	要求
//		项目ID	PROJECT_ID	VARCHAR2(50)	是	科研项目ID，唯一
//		项目分类	PROJECT_TYPE	VARCHAR2(20)	是	科研项目，编码：KYXM
//		项目名称	PROJECT_NAME	VARCHAR2(100 CHAR)	是	
//		WBS编号	PROJECT_WBS	VARCHAR2(50)	是	
//		项目说明	PROJECT_REMARK	VARCHAR2(200 CHAR )	否	项目摘要
//		项目开始时间	PROJECT_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		项目结束时间	PROJECT_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
		
		String KYId = Rtext.toString(proMap.get("PROJECT_ID"));
		String category = Rtext.toString(proMap.get("PROJECT_TYPE"));
		String projectName = Rtext.toString(proMap.get("PROJECT_NAME"));
		String wbsNumber = Rtext.toString(proMap.get("PROJECT_WBS"));
		String projectIntroduce = Rtext.toString(proMap.get("PROJECT_REMARK"));
		String startDateStr  = Rtext.toString(proMap.get("PROJECT_START_DATE"));
		String endDateStr = Rtext.toString(proMap.get("PROJECT_END_DATE"));
		
		String errorInfo = "";
		
		if(KYId.isEmpty()) errorInfo += "科研项目id为空；";
		
		if(category.isEmpty()) errorInfo += "项目类型为空；";
		
		if("KYXM".equals(category)){
			category="KY";
		}else if("HXXM".equals(category)){
			category="HX";
		}else{
			errorInfo += "项目类型错误；";
		}
		
		if(projectName.isEmpty()) errorInfo += "项目名称为空；";
		
		if(projectName.length()>50) errorInfo += "项目名称超50字；";
		
		if(wbsNumber.isEmpty()){
			errorInfo += "wbs编号为空；";
		}/*else{
			String  result = bgService.checkUniqueness(wbsNumber);
			if(!result.equalsIgnoreCase("true")) errorInfo += "系统中已存在此wbs编号；";;
		}*/
		
		if(projectIntroduce.length()>200) errorInfo += "项目介绍超过200字；"; 
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		
		if(startDateStr.isEmpty()){
			errorInfo += "项目开始日期为空；";
		}else{
			try {
				startDate = dateFormat.parse(startDateStr);
			} catch (ParseException e) {
				errorInfo += "项目开始日期出错；";
			}
		}
		
		if(endDateStr.isEmpty()){
			errorInfo += "项目结束日期为空；";
		}else{
			try {
				endDate = dateFormat.parse(endDateStr);
			} catch (ParseException e) {
				errorInfo += "项目结束日期出错；";
			}
		}
		
		if(startDate!=null && endDate!=null && startDate.after(endDate)) errorInfo += "日期顺序出错；";
		
		if(errorInfo.isEmpty()){//字段校验通过
			return true;
		}else{//字段合法性校验未通过，插入错误记录表
			logger.info("字段验证失败： "+errorInfo);
			handleMapper.addErrorRecord(src,"pro",KYId,errorInfo);
		}
		return false;
	}
	
	private boolean valiEmp(Map<String,Object> empMap,String src){
//		字段名称	英文名称	类型	是否必推字段	要求
//		人员姓名	USER_NAME	VARCHAR2(100 CHAR)	是	
//		人员编号	USER_HRCODE	VARCHAR2(20)	是	人资编号，必须是院内人员，唯一
//		开始时间	JOIN_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		结束时间	JOIN_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		角色	PROJECT_ROLE	VARCHAR2(2)	是	纵向项目中的项目执行人默认为项目负责人；
//		其他人默认为项目参与人;
//		1 项目负责人 0 项目参与人
//		投入工时	WORK_TIME	NUMBER	是	单位：月
		String syncProId = Rtext.toString(empMap.get("PROJECT_ID"));
		String empName = Rtext.toString(empMap.get("USER_NAME"));
		String hrCode = Rtext.toString(empMap.get("USER_HRCODE"));
		String startDateStr  = Rtext.toString(empMap.get("JOIN_START_DATE"));
		String endDateStr = Rtext.toString(empMap.get("JOIN_END_DATE"));
		String role = Rtext.toString(empMap.get("PROJECT_ROLE"));
		String planHours = Rtext.toString(empMap.get("WORK_TIME"));

		String errorInfo = "";
		
		if(syncProId.isEmpty()){
			errorInfo += "所在项目id为空；";
		}/*else{
			Map<String,Object> proRecord = handleMapper.getProRelationById("",syncProId);
			if(proRecord==null){
				errorInfo += "不存在该项目；";
			}else{
				bgProId = Rtext.toString(proRecord.get("BG_ID"));
				pro = bgService.getProPoByProId(bgProId);
			}
		}*/
		
		//if(empName.isEmpty()) errorInfo += "人员姓名为空；";
		
		if(hrCode.isEmpty()) {
			errorInfo += "人员编号为空；";
		}else{
			CommonCurrentUser user = userUtils.getCommonCurrentUserByHrCode(hrCode);
			if(user==null){
				errorInfo += "人员编号错误！ ";
			}/*else{
				if(!empName.isEmpty() && !empName.equals(user.getUserAlias())) errorInfo += "人员编号与姓名不匹配！ ";
			}*/
		}
		
//		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
//		Date startDate = null;
//		Date endDate = null;
//		
//		if(startDateStr.isEmpty()){
//			errorInfo += "参与开始日期为空；";
//		}else{
//			try {
//				startDate = dateFormat.parse(startDateStr);
//			} catch (ParseException e) {
//				errorInfo += "参与开始日期出错；";
//			}
//		}
//		
//		if(endDateStr.isEmpty()){
//			errorInfo += "参与结束日期为空；";
//		}else{
//			try {
//				endDate = dateFormat.parse(endDateStr);
//			} catch (ParseException e) {
//				errorInfo += "参与结束日期出错；";
//			}
//		}
//		
//		if(startDate!=null && endDate!=null && startDate.after(endDate)) errorInfo += "日期顺序出错；";
		
		//角色的转换
		if(role.isEmpty()) errorInfo += "角色为空；";
		
		if(!"0".equals(role) && !"1".equals(role)) errorInfo += "无此角色类型；";
		
		if(planHours.isEmpty()){
			//errorInfo += "投入工时为空；";
		}else{
			try {
				Double.parseDouble(planHours);
			} catch (NumberFormatException e) {
				errorInfo += "投入工时出错；";
			}
		}
		
		if(errorInfo.isEmpty()){//字段校验通过
			return true;
		}else{//字段合法性校验未通过，插入错误记录表
			logger.info("字段验证失败： "+errorInfo);
			handleMapper.addErrorRecord(src,"emp",syncProId+"("+hrCode+")",errorInfo);
		}
		
		return false;
	}

/**********************************************处理横向******************************************************/
	
	@Override
	public void copyFromHX() {
		logger.info("转存项目信息表");
		handleMapper.addHXToProTemp();
		handleMapper.cutHXProTemp(DAYS);
		
		logger.info("转存参与人员表");
		handleMapper.addHXToEmpTemp();
		handleMapper.cutHXEmpTemp(DAYS);
	}

	@Override
	public void validateHX() {
		logger.info("删除横向中间表项目信息");
		handleMapper.truncateTable("BG_SYNC_HX_PROJECT");
//		字段名称	英文名称	类型	是否必推字段	要求
//		项目ID	PROJECT_ID	VARCHAR2(50)	是	科研项目ID，唯一
//		项目分类	PROJECT_TYPE	VARCHAR2(20)	是	科研项目，编码：KYXM
//		项目名称	PROJECT_NAME	VARCHAR2(100 CHAR)	是	
//		WBS编号	PROJECT_WBS	VARCHAR2(50)	是	
//		项目说明	PROJECT_REMARK	VARCHAR2(200 CHAR )	否	项目摘要
//		项目开始时间	PROJECT_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		项目结束时间	PROJECT_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
		logger.info("获取所有的横向项目信息");
		List<Map<String, Object>> proList = handleMapper.getAllSyncProFromHX();
		logger.info("处理获取到的项目信息");
		for (Map<String, Object> proMap : proList) {
			if(validPro(proMap,"HX")){
				//校验通过存入报工中间表
				handleMapper.saveProFromHX(proMap);
			}
		}
		
		logger.info("删除横向中间表人员信息");
		handleMapper.truncateTable("BG_SYNC_HX_EMP");
//		字段名称	英文名称	类型	是否必推字段	要求
//		人员姓名	USER_NAME	VARCHAR2(100 CHAR)	是	
//		人员编号	USER_HRCODE	VARCHAR2(20)	是	人资编号，必须是院内人员，唯一
//		开始时间	JOIN_START_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		结束时间	JOIN_END_DATE	VARCHAR2(20)	是	格式：yyyy-MM-dd
//		角色	PROJECT_ROLE	VARCHAR2(2)	是	纵向项目中的项目执行人默认为项目负责人；
//		其他人默认为项目参与人;
//		1 项目负责人 0 项目参与人
//		投入工时	WORK_TIME	NUMBER	是	单位：月
		logger.info("获取所有参与人员");
		List<Map<String, Object>> empList = handleMapper.getAllSyncEmpFromHX();
		logger.info("处理获取到的参与人员信息");
		for (Map<String, Object> empMap : empList) {
			if(valiEmp(empMap,"HX")){
				//通过校验的参与人员信息保存到报工系统人员中间表
				handleMapper.saveEmpFromHX(empMap);
			}
		}
		
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updateFromHX() {
		//获取所有已关联的横向系统项目关联关系
		List<Map<String, Object>>  proRelList = bgMapper.getProRelation(null,null,"HX");
		for (Map<String, Object> map : proRelList) {
			String bgProId = Rtext.toString(map.get("BG_ID"));
			String hxProId = Rtext.toString(map.get("SYNC_ID"));
			
			if(bgProId.isEmpty() || hxProId.isEmpty()) continue;
			
			//更新已经关联到报工系统的项目信息的部分字段（项目类型，项目名称，wbs编号）
			Map<String, Object> hxProMap = bgMapper.getProInfoByProIdFromHX(hxProId);
			
			String proName = Rtext.toString(hxProMap.get("PROJECT_NAME"));
			String wbsCode = Rtext.toString(hxProMap.get("WBS_NUMBER"));

			ProjectInfoPo pro = bgMapper.getProPoByProId(bgProId);
			pro.setProjectName(proName);
			pro.setWBSNumber(wbsCode);
			pro.setUpdateDate(new Date());
			pro.setUpdateUser("HandleSyncJob");
			
			bgMapper.updateProInfo(pro);
			
			/*************************************************************************/
			
			//更新参与人员
			Date startDate = pro.getStartDate();
			Date endDate = pro.getEndDate();

			List<Map<String, String>> bgEmpList = bgMapper.getProUsersByProId(bgProId);
			List<HashMap> hxEmpList = bgMapper.getEmpByProIdFromHX(hxProId);
			
			//遍历报工系统中的参与人员获取必要信息
			Set<String> bgEmpSet = new HashSet<>();
			boolean existsPrincipal = false;//是否存在项目负责人
			for (Map<String, String> bgEmp : bgEmpList) {
				String bgHrCode = bgEmp.get("HRCODE");
				String role = bgEmp.get("ROLE");
				bgEmpSet.add(bgHrCode);
				if("1".equals(role)) existsPrincipal = true;
			}
			
			for (HashMap hxEmp : hxEmpList) {
				String hxHrCode = (String) hxEmp.get("hrcode");
				
				if(!bgEmpSet.contains(hxHrCode)){//如果报工系统中不存在则同步到报工系统并添加关联
					ProjectUserPo proUser = new ProjectUserPo();
					String empId = Rtext.getUUID();
					proUser.setId(empId);
					proUser.setRole(existsPrincipal?"0":Rtext.toString(hxEmp.get("role")));
					proUser.setProjectId(bgProId);
					proUser.setHrcode(hxHrCode);
					proUser.setEmpName(Rtext.toString(hxEmp.get("stuffName")));
					
					proUser.setStartDate(startDate);
					proUser.setEndDate(endDate);
					proUser.setTask(null);
					Double planHours = Rtext.ToDouble(hxEmp.get("planHours"),0d);
					proUser.setPlanHours(planHours*22*24);
					proUser.setSrc("2");
					proUser.setStatus("1");
					proUser.setCreateDate(new Date());
					proUser.setUpdateDate(new Date());
					proUser.setCreateUser("HandleSyncJob");
					proUser.setUpdateUser("HandleSyncJob");
					
					bgMapper.addProUser(proUser);
					bgMapper.addEmpRelation(empId, bgProId, hxHrCode, "HX");
				}
			}
		}
	}

	@Override
	public void cutErrorRecord() {
		handleMapper.cutErrorRecord(DAYS);
	}

}
