package com.sgcc.bg.yszx.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.StringUtil;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.YSZXMapper;
import com.sgcc.bg.yszx.Utils.NumberValidationUtil;
import com.sgcc.bg.yszx.Utils.Pinyin;
import com.sgcc.bg.yszx.bean.IdeaInfo;
import com.sgcc.bg.yszx.bean.VisitInfo;
import com.sgcc.bg.yszx.bean.CompanyUserInfo;
import com.sgcc.bg.yszx.bean.CompanyLeaderInfo;
import com.sgcc.bg.yszx.service.IdeaInfoService;

@Service
public class IdeaInfoServiceImpl implements IdeaInfoService {
	@Autowired
	private   YSZXMapper yszxMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	private static Logger bgServiceLog =  LoggerFactory.getLogger(IdeaInfoServiceImpl.class);

	@Override
	public String addIdeaInfo(HttpServletRequest request) {
	   bgServiceLog.info("演示中心参观预定的添加----->开始" );
	   String visitLevel = request.getParameter("visitLevel" ) == null ? "" : request.getParameter("visitLevel").toString(); //提交|保存
	   Map<String,String>  userInfoMap=userInfo();
	   //演示中心参观预定的添加数据的验证
	   String  ideaforRW=checkIdeaInfo(request);
	   if(!ideaforRW.equals("null")){
		   return ideaforRW;
	   } 
	   //参观人信息参观预定的添加数据的验证
	   String  visitforRW=checkVisitInfo(request);
	   if(!ideaforRW.equals("null")){
		   return ideaforRW;
	   } 
	   //陪同领导人员信息添加数据的验证
	   String  CompanyLeadershipforRW=checkCompanyLeaderInfo(request);
	   if(!CompanyLeadershipforRW.equals("null") ){
		   return CompanyLeadershipforRW;
	   }
	   //陪同部门人员信息添加数据的验证
	   String CompanyDeptforRW =checkCompanyUserInfo(request);
	   if(!CompanyDeptforRW.equals("null")){
		   return CompanyDeptforRW;
	   }
	   IdeaInfo  ideaInfo=new IdeaInfo();
	   String id=Rtext.getUUID();
	   ideaInfo.setId(id);
	   ideaInfo =IdeaInfo(request,ideaInfo,userInfoMap); //演示中心参观预定的添加数据实体的返回
	   if(visitLevel.equals("save")){//保存
		   ideaInfo.setStatus("SAVE");
		   yszxMapper.addIdeaInfo(ideaInfo);
	   }else if(visitLevel.equals("submit")){//提交
		   ideaInfo.setStatus("DEPT_HEAD_CHECK");
		   yszxMapper.addIdeaInfo(ideaInfo);
	   }
	   if(!"0".equals(visitforRW)){
		   //演示中心参观人员信息的添加
		  visitInfo(request, id,userInfoMap); 
	   }
	   if(!"0".equals(CompanyLeadershipforRW)){
		   //演示中心陪同领导人员信息的添加
		   CompanyLeaderInfo(request,id,userInfoMap);
	   }  
	   if(!"0".equals(CompanyDeptforRW)){
		 //演示中心陪同部门人员信息的添加
		   CompanyUserInfo(request,id,userInfoMap);
	   }
	   
	    
	   return "";
		//return yszxMapper.addIdeaInfo(ideaInfo);
	}
	/**
	 * 演示中心--参观预定的添加--主页数据的验证
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String  checkIdeaInfo(HttpServletRequest request){
		 bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo数据的验证" );
		String contactUser = request.getParameter("contactUser" ) == null ? "" : request.getParameter("contactUser").toString(); //联系人名称
		String contactPhone = request.getParameter("contactPhone" ) == null ? "" : request.getParameter("contactPhone").toString(); //联系人电话
		String stateDate = request.getParameter("stateDate" ) == null ? "" : request.getParameter("stateDate").toString(); //参观开始时间
		String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //参观结束时间
		String remark = request.getParameter("remark" ) == null ? "" : request.getParameter("remark").toString(); //备注
		String visitUnitType = request.getParameter("visitUnitType" ) == null ? "" : request.getParameter("visitUnitType").toString(); //参观单位性质
		String visitUnitName = request.getParameter("visitUnitName" ) == null ? "" : request.getParameter("visitUnitName").toString(); //参观单位名称
		String visitorNumber = request.getParameter("visitorNumber" ) == null ? "" : request.getParameter("visitorNumber").toString(); //参观人数
		String companyUserNumber = request.getParameter("companyUserNumber" ) == null ? "" : request.getParameter("companyUserNumber").toString(); //陪同人数
		ResultWarp rw =  null;
		//联系人名称名称的验证
		if(contactUser==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"联系人名称不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			//联系人名称为中文或英文
			boolean flag=StringUtil.checkNotNoOrC(contactUser);
			if(!flag){
				  rw = new ResultWarp(ResultWarp.FAILED ,"联系人名称只能为中文或英文");
				  return JSON.toJSONString(rw); 
			}
			
		}
		//联系人电话的验证
		if(contactPhone==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"联系人电话不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			//联系人电话为手机格式和座机格式
			boolean flag=StringUtil.checkPhone(contactPhone);
			if(!flag){
				 rw = new ResultWarp(ResultWarp.FAILED ,"联系人电话错误");
				 return JSON.toJSONString(rw); 
			}		
			
		}
		//参观开始时间和参观结束时间的验证
		String  checkreturn=checkData(stateDate,endDate);
		if(checkreturn!=null){
			return checkreturn;
		}
		//备注
		if(remark!=""){
			if(remark.length()>200){
				  rw = new ResultWarp(ResultWarp.FAILED ,"备注不超过200个字");
				  return JSON.toJSONString(rw);  
			}
		}
		//参观单位性质
		if(visitUnitType==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观单位性质不能为空");
			  return JSON.toJSONString(rw);  
		}
		//参观单位名称
		if(visitUnitName==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观单位名称不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			 if(visitUnitName.length()>150){
				 rw = new ResultWarp(ResultWarp.FAILED ,"参观单位名称不超过150个字");
				  return JSON.toJSONString(rw); 
			 }
		}
		//参观人数
		if(visitorNumber==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观人数不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			boolean flag=NumberValidationUtil.isPositiveInteger(visitorNumber);
			if(!flag){
				 rw = new ResultWarp(ResultWarp.FAILED ,"参观人数必须为数字");
				 return JSON.toJSONString(rw); 
			}	
		}
		//陪同人数
		if(companyUserNumber==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"陪同人数不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			boolean flag=NumberValidationUtil.isPositiveInteger(companyUserNumber);
			if(!flag){
				 rw = new ResultWarp(ResultWarp.FAILED ,"陪同人数必须为数字");
				 return JSON.toJSONString(rw); 
			}	
		}
		 return JSON.toJSONString(rw);  
	}
	
	/**
	 * 演示中心--参观预定的添加--返回实体数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public IdeaInfo IdeaInfo(HttpServletRequest request, IdeaInfo  ideaInfo,Map<String,String>  userInfoMap){
		bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo实体返回" );
		String contactUser = request.getParameter("contactUser" ) == null ? "" : request.getParameter("contactUser").toString(); //联系人名称
		String contactPhone = request.getParameter("contactPhone" ) == null ? "" : request.getParameter("contactPhone").toString(); //联系人电话
		String stateDate = request.getParameter("stateDate" ) == null ? "" : request.getParameter("stateDate").toString(); //参观开始时间
		String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //参观结束时间
		String remark = request.getParameter("remark" ) == null ? "" : request.getParameter("remark").toString(); //备注
		String visitUnitType = request.getParameter("visitUnitType" ) == null ? "" : request.getParameter("visitUnitType").toString(); //参观单位性质
		String visitUnitName = request.getParameter("visitUnitName" ) == null ? "" : request.getParameter("visitUnitName").toString(); //参观单位名称
		String visitorNumber = request.getParameter("visitorNumber" ) == null ? "" : request.getParameter("visitorNumber").toString(); //参观人数
		String companyUserNumber = request.getParameter("companyUserNumber" ) == null ? "" : request.getParameter("companyUserNumber").toString(); //陪同人数
		String visitLevel = request.getParameter("visitLevel" ) == null ? "" : request.getParameter("visitLevel").toString(); //陪同人数
		
		
		String applyDept= userInfoMap.get("deptId");
		ideaInfo.setApplyDept(applyDept);
		ideaInfo.setStateDate(stateDate );
		ideaInfo.setEndDate(endDate);
		ideaInfo.setRemark(remark);
		ideaInfo.setContactUser(contactUser);
		ideaInfo.setContactPhone(contactPhone);
		ideaInfo.setVisitUnitType(visitUnitType);
		ideaInfo.setVisitUnitName(visitUnitName);
		ideaInfo.setVisitorNumber(Integer.parseInt(visitorNumber));
		ideaInfo.setCompanyUserNumber(Integer.parseInt(companyUserNumber));
		String name= userInfoMap.get("name");
		String applyId=applyId(name);
		ideaInfo.setApplyId(applyId);
		ideaInfo.setVisitLevel(visitLevel);
		ideaInfo.setValId("1");
		String createUserId= userInfoMap.get("userId");
		ideaInfo.setCreateUser(createUserId);
		ideaInfo.setCreateTime(new Date());
		ideaInfo.setUpdateUser(createUserId);
		ideaInfo.setUpdateTime(new Date());
		return ideaInfo;  
	}
	
	
	/**
	 * 演示中心--参观预定的添加--申请单号的生成
	 * 需求：
	 * 演示中心首字母＋申请人姓名＋申请日期＋序号（序号是唯一值，按年度开始和结束排号）
	 * 生成为：YSZX-ZHANGMOUMOU-20190212-001
	 * @param
	 * @param
	 * @return
	 */
	public static   String   applyId(String name){
		name=Pinyin.getPinYin(name);
	    String serrialNum="001";
		String applyId="YSZX-"+name.toUpperCase()+"-"+DateUtil.getDays()+"-"+serrialNum;
		return applyId;
	}
	/**
	 * 演示中心--参观预定的添加--查询用户信息
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public   Map<String ,String >   userInfo(){
		Map<String,String>  map=new HashMap<String,String>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userid=  currentUser.getUserId();
		map.put("userId", userid);
		String userAlias=  currentUser.getUserAlias();
		map.put("name", userAlias );
		String deptId=currentUser.getDeptId();
		map.put("deptId", deptId);
		return map;
	}
	/**
	 * 演示中心--参观预定的添加--时间的验证  参观开始时间 参观结束时间
	 * 需求：
	 * 参观日期和时间
	 * 参观年度和时间只能选取当前年、当天以及以后，可跨年度申请，不能选取过往年度和天。
	 * 上午可预定时间为8:30-11:30，
	 * 下午可预定时间为13:30-16:30，
	 * 系统需控制用户上一场结束时间和下一场申请开始时间间隔30分钟。
	 * @param
	 * @param
	 * @return
	 */
    public    String  checkData( String stateDate,String endDate) {
     ResultWarp rw =  null;
     String	localTime=DateUtil.getTime();
   	 if(stateDate==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观开始时间不能为空");
			  return JSON.toJSONString(rw);  
	  }else{
			boolean flag=DateUtil.isCheckTime(stateDate);
			if(!flag){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观开始时间错误，格式:yyyy-MM-dd HH:mm"); 
			  return JSON.toJSONString(rw);  
		    }
			boolean flags;
			try {
				flags = DateUtil.compareTime(localTime,stateDate);
				if(!flags){
					  rw = new ResultWarp(ResultWarp.FAILED ,"当前时间为"+localTime+",您预定的参观开始时间已过"); 
					  return JSON.toJSONString(rw);  
				} 	
			} catch (ParseException e) {
				 rw = new ResultWarp(ResultWarp.FAILED ,"系统异常，请联系管理员");
				 return JSON.toJSONString(rw);  
			}
			
	  }
   	 
	 if(endDate==""){
			 rw = new ResultWarp(ResultWarp.FAILED ,"参观结束时间不能为空"); 
	  }else{
			boolean flag=DateUtil.isCheckTime(endDate);
			if(!flag){
			 rw = new ResultWarp(ResultWarp.FAILED ,"参观结束时间格式错误，格式:yyyy-MM-dd HH:mm"); 
			 return JSON.toJSONString(rw);  
		    } 
			boolean flags;
			try {
				flags = DateUtil.compareTime(localTime,endDate);
				if(!flags){
					  rw = new ResultWarp(ResultWarp.FAILED ,"当前时间为"+localTime+",您预定的参观结束时间已过"); 
					  return JSON.toJSONString(rw);  
				} 	
			} catch (ParseException e) {
				 rw = new ResultWarp(ResultWarp.FAILED ,"系统异常，请联系管理员");
				 return JSON.toJSONString(rw);  
			}
			
	  }
	 try {
			boolean flags=DateUtil.compareTime(stateDate,endDate);
			if(!flags){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观开始时间不能大于参观结束时间");
				  return JSON.toJSONString(rw);  
			 } 	
			List<Map<String, Object>> list=yszxMapper.selectForIdeaDate();
			 if(list!=null){
				 for(Map<String, Object> map:list){
					String  startdate=String.valueOf(map.get("START_DATE"));
					String  newEndDate=DateUtil.minutes(endDate,30);
				 
					flags=DateUtil.compareTime(startdate,newEndDate);
					System.out.println(newEndDate+"<"+startdate);
					if(!flags){
						  rw = new ResultWarp(ResultWarp.FAILED ,"用户上一场结束时间和下一场申请开始时间间隔30分钟");
						  return JSON.toJSONString(rw);  
					 } 
				
					 
				 
				 
					
					 
					
				 }
				
				
				  
			 }
			 
		} catch (ParseException e) {
			  rw = new ResultWarp(ResultWarp.FAILED ,"系统异常，请联系管理员"); 
			  return JSON.toJSONString(rw);  
		}
	 
	 
	 
	 
	return  JSON.toJSONString(rw);
	   
    }
    
    
    
  
	public String  checkVisitInfo(HttpServletRequest request){
    	bgServiceLog.info("演示中心参观人员信息----->VisitInfo数据的验证" );
    	ResultWarp rw =  null;
    	String visitInfo = Rtext.toStringTrim(request.getParameter("visitInfo"),"");
    	if(visitInfo==""){
    		return "0";
    	}
		List<HashMap> list = JSON.parseArray(visitInfo, HashMap.class);
		for (Map<String, String> visitInfoMap : list) {
			String userName=visitInfoMap.get("userName")== null ? "" : visitInfoMap.get("userName").toString().trim(); //参观人员名称
			String position=visitInfoMap.get("position")== null ? "" : visitInfoMap.get("position").toString().trim(); //参观职务
			String userLevel=visitInfoMap.get("userLevel")== null ? "" : visitInfoMap.get("userLevel").toString().trim(); //级别
			//参观人员名称
			if(userName==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观人员名称不能为空");
				  return JSON.toJSONString(rw);  
			}else{
				//参观人员名称为中文或英文
				boolean flag=StringUtil.checkNotNoOrC(userName);
				if(!flag){
					  rw = new ResultWarp(ResultWarp.FAILED ,"参观人员名称只能为中文或英文");
					  return JSON.toJSONString(rw); 
				}
			}
			//参观人职务
			if(position==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观人职务不能为空");
				  return JSON.toJSONString(rw);  
			}else{
				if(position.length()>100){
					  rw = new ResultWarp(ResultWarp.FAILED ,"参观人职务不超过200个字");
					  return JSON.toJSONString(rw);  
				}	
			}
			//参观人级别
			if(userLevel==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观人级别不能为空");
				  return JSON.toJSONString(rw);  
			} 
		   
		}
		return JSON.toJSONString(rw); 
    	
    }
    public void  visitInfo(HttpServletRequest request ,String ideaId, Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心参观人员信息----->visitInfo实体" );
    	String visitInfo = Rtext.toStringTrim(request.getParameter("visitInfo"),"");
		List<HashMap> list = JSON.parseArray(visitInfo, HashMap.class);
		for (Map<String, String> visitInfoMap : list) {
			String userName=visitInfoMap.get("userName")== null ? "" : visitInfoMap.get("userName").toString().trim(); //参观人员名称
			String position=visitInfoMap.get("position")== null ? "" : visitInfoMap.get("position").toString().trim(); //参观职务
			String userLevel=visitInfoMap.get("userLevel")== null ? "" : visitInfoMap.get("userLevel").toString().trim(); //级别
			String sortId=visitInfoMap.get("sortId")== null ? "" : visitInfoMap.get("sortId").toString().trim(); //级别
			VisitInfo  visit=new VisitInfo();
			visit.setUserName(userName);
			visit.setPosition(position);
			visit.setUserLevel(userLevel);
			String visitId=Rtext.getUUID();
			visit.setId(visitId);
			visit.setIdeaId(ideaId);
			visit.setSortId(Integer.parseInt(sortId));
			visit.setRemark("");
			visit.setValId("1");
			String createUser= userInfoMap.get("userId");
			visit.setCreateUser(createUser);
			visit.setCreateTime(new Date());
			visit.setUpdateUser(createUser);
			visit.setUpdateTime(new Date());
			yszxMapper.addVisitInfo(visit);
        }
    }
    public String  checkCompanyLeaderInfo(HttpServletRequest request){
    	bgServiceLog.info("演示中心陪同领导人员信息----->CompanyLeadershipInfo数据的验证" );
    	ResultWarp rw =  null;
    	String companyLeaderInfo = Rtext.toStringTrim(request.getParameter("companyLeaderInfo"),"");
    	if(companyLeaderInfo==""){
    		return "0";
    	}
		List<HashMap> list = JSON.parseArray(companyLeaderInfo, HashMap.class);
		for (Map<String, String> CompanyLeaderInfo : list) {
			String userId=CompanyLeaderInfo.get("userId")== null ? "" : CompanyLeaderInfo.get("userId").toString(); //陪同人员名称
			 //陪同领导人员名称
			if(userId==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员名称不能为空");
				  return JSON.toJSONString(rw);  
			} 
		}
		return JSON.toJSONString(rw); 	
    }
    public  void  CompanyLeaderInfo(HttpServletRequest request ,String ideaId,Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心参观人员信息----->CompanyLeadershipInfo实体" );
    	String companyLeaderInfo = Rtext.toStringTrim(request.getParameter("companyLeaderInfo"),"");
		List<HashMap> list = JSON.parseArray(companyLeaderInfo, HashMap.class);
		for (Map<String, String> CompanyLeaderInfo : list) {
			String companyUserId=CompanyLeaderInfo.get("userId")== null ? "" : CompanyLeaderInfo.get("userId").toString(); //陪同人员名称
			CompanyLeaderInfo  companyInfo=new CompanyLeaderInfo();
			String companyLeadershipId=Rtext.getUUID();
			companyInfo.setId(companyLeadershipId);
			companyInfo.setIdeaId(ideaId);
			companyInfo.setRemark("");
			companyInfo.setValid("1");
			companyInfo.setUserId(companyUserId);
			String userId= userInfoMap.get("userId");
			companyInfo.setCreateUser(userId);
			companyInfo.setCreateTime(new Date());
			companyInfo.setUpdateUser(userId);
			companyInfo.setUpdateTime(new Date());
			yszxMapper.addCompanyLeaderInfo(companyInfo);
			 
		}
		
    }
    public  String  checkCompanyUserInfo(HttpServletRequest request){
    	bgServiceLog.info("演示中心陪同部门人员信息----->CompanyDeptInfo数据的验证" );
    	ResultWarp rw =  null;
    	String companyUserInfo = Rtext.toStringTrim(request.getParameter("companyUserInfo"),"");
    	if(companyUserInfo==""){
    		return "0";
    	}
		List<HashMap> list = JSON.parseArray(companyUserInfo, HashMap.class);
		for (Map<String, String> CompanyUserInfo : list) {
			String userId=CompanyUserInfo.get("userId")== null ? "" : CompanyUserInfo.get("userId").toString(); //陪同人员名称
			//查询
			Map<String ,Object> userInfo=yszxMapper.getUserId(userId);
			if(userInfo==null){
				  rw = new ResultWarp(ResultWarp.FAILED ,"该陪同人信息异常，请联系管理员");
				  return JSON.toJSONString(rw);  
			}
		}
		return JSON.toJSONString(rw); 	
    }
    public  void CompanyUserInfo(HttpServletRequest request,String  ideaId,Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心陪同部门人员信息----->CompanyDeptInfo数据的验证" );
    	String companyUserInfo = Rtext.toStringTrim(request.getParameter("companyUserInfo"),"");
		List<HashMap> list = JSON.parseArray(companyUserInfo, HashMap.class);
		for (Map<String, String> CompanyUserInfo : list) {
			String userid=CompanyUserInfo.get("userId")== null ? "" : CompanyUserInfo.get("userId").toString(); //陪同人员名称
			//查询
			Map<String ,Object> userInfo=yszxMapper.getUserId(userid);
			CompanyUserInfo  companyUser=new CompanyUserInfo();
			companyUser.setUserId(userid);
			String companyUserId=Rtext.getUUID();
			companyUser.setId(companyUserId);
			companyUser.setIdeaId(ideaId);
			String userId= userInfoMap.get("userId");
			companyUser.setCreateUser(userId);
			companyUser.setCreateTime(new Date());
			companyUser.setUpdateUser(userId);
			companyUser.setUpdateTime(new Date());
			companyUser.setValid("1");
			yszxMapper.addCompanyUserInfo(companyUser);
		}
		 
    }
    
}