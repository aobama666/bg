package com.sgcc.bg.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.mapper.HandleSyncMapper;
import com.sgcc.bg.model.ProjectInfoPo;
import com.sgcc.bg.service.HandleSyncService;
import com.sgcc.bg.service.IBGService;

import ch.qos.logback.classic.Logger;

@Service
public class HandleSyncServiceImpl implements HandleSyncService {
	
	private Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncServiceImpl.class);
	
	@Autowired
	private HandleSyncMapper handleMapper;
	
	@Autowired
	private IBGService bgService;
	
	@Override
	public void copyKY() {
		handleMapper.addKYToProTemp();
		handleMapper.cutProTemp(3);
		
		handleMapper.addKYToEmpTemp();
		handleMapper.cutEmpTemp(3);
	}
	
	@Override
	public void handleKY() {
		List<Map<String, Object>> dataList = handleMapper.getAllProjects("","");
		for (Map<String, Object> map : dataList) {
			
			String KYId = Rtext.toString(map.get("uuid"));
			String projectName = Rtext.toString(map.get("projectName"));
			String category = Rtext.toString(map.get("category"));
			String wbsNumber = Rtext.toString(map.get("wbsNumber"));
			String projectIntroduce = Rtext.toString(map.get("introduce"));
			String startDateStr  = Rtext.toString(map.get("startDate"));
			String endDateStr = Rtext.toString(map.get("endDate"));
			
			String errorInfo = "";
			
			if(projectName.isEmpty()) errorInfo += "项目名称为空；";
			
			if(projectName.length()>50) errorInfo += "项目名称超50字；";
			
			if(category.isEmpty()) errorInfo += "项目类型为空；";
			
			if(wbsNumber.isEmpty()) errorInfo += "wbs编号为空；";
			 
			Date startDate = DateUtil.getFormatDate(startDateStr, "yyyy-MM-dd");
			Date endDate = DateUtil.getFormatDate(endDateStr, "yyyy-MM-dd");
			
			if(startDate==null) errorInfo += "项目开始日期出错；";
			
			if(endDate==null) errorInfo += "项目结束日期出错；";
			
			if(errorInfo.isEmpty()){//字段校验通过
				
				Map<String,Object> record = handleMapper.getProRelationById("",KYId);
				
				if(record!=null){//已存在同步记录，更新(项目类型、项目名称、WBS)
					ProjectInfoPo pro = bgService.getProPoByProId(Rtext.toString(record.get("BG_ID")));
					pro.setCategory(category);
					pro.setProjectName(projectName);
					pro.setWBSNumber(wbsNumber);
					pro.setUpdateUser("HandleSyncJob");
					pro.setUpdateDate(new Date());
					bgService.addProInfo(pro);
				}else{//不存在同步记录，新增
					//存入项目信息业务表
					ProjectInfoPo pro = new ProjectInfoPo();
					String proId = Rtext.getUUID();
					pro.setId(proId);
					pro.setProjectNumber(bgService.getBGNumber());
					pro.setProjectName(projectName);
					pro.setCategory("KY");
					pro.setWBSNumber(wbsNumber);
					pro.setProjectIntroduce(projectIntroduce);
					pro.setDecompose("0");
					pro.setSrc("2");
					pro.setProjectStatus("1");
					pro.setStartDate(startDate);
					pro.setEndDate(endDate);
					pro.setPlanHours(null);
					pro.setOrganInfo(null);
					pro.setCreateDate(new Date());
					pro.setUpdateDate(new Date());
					pro.setCreateUser("HandleSyncJob");
					pro.setUpdateUser("HandleSyncJob");
					bgService.addProInfo(pro);
					
					//存入项目关联表
					handleMapper.addProRelation(proId,KYId);
				}
			}else{//字段合法性校验未通过，插入错误记录表
				handleMapper.addErrorRecord("KY",KYId,errorInfo);
			}
		}
	}

}
