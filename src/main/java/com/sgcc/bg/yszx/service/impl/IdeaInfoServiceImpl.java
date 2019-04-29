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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.StringUtil;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.common.getSet;
import com.sgcc.bg.mapper.YSZXMapper;
import com.sgcc.bg.yszx.Utils.NumberValidationUtil;
import com.sgcc.bg.yszx.Utils.Pinyin;
import com.sgcc.bg.yszx.bean.IdeaInfo;
import com.sgcc.bg.yszx.bean.VisitInfo;
import com.sgcc.bg.yszx.bean.CompanyUserInfo;
import com.sgcc.bg.yszx.bean.CompanyLeaderInfo;
import com.sgcc.bg.yszx.service.ApproveService;
import com.sgcc.bg.yszx.service.IdeaInfoService;

@Service
public class IdeaInfoServiceImpl implements IdeaInfoService {
	@Autowired
	private    YSZXMapper yszxMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private ApproveService approveService;
	 
	
	private static Logger bgServiceLog =  LoggerFactory.getLogger(IdeaInfoServiceImpl.class);
    
	
	/**
	 * 演示中心--参观预定的添加--申请单号的生成
	 * 需求：
	 * 演示中心首字母＋申请人姓名＋申请日期＋序号（序号是唯一值，按年度开始和结束排号）
	 * 生成为：YSZX-ZHANGMOUMOU-20190212-001
	 * @param
	 * @param
	 * @return
	 */
	public     Map<String,String>   applyId(String  ideaId){
		Map<String,String>  userInfoMap=userInfo();
		String name= userInfoMap.get("name");
        name=Pinyin.getPinYin(name);
		String apply="YSZX-"+name.toUpperCase()+"-"+DateUtil.getDays();
		 
		Integer year=Integer.valueOf(DateUtil.getYear()) ;
		List<Map<String, Object>>   ideaMap = yszxMapper.selectForApplyId(year);
		 int applyOrder=0;
		if("null".equals(ideaMap)){
			apply=apply+"-001" ;
		}else{
		   String    applySoid=   String.valueOf( ideaMap.get(0).get("applyOrder")) ;
		    applyOrder=Integer.valueOf(applySoid);
		    applyOrder++;
		   if(0<applyOrder &&  applyOrder<10){
			   apply=apply+"-00"+applyOrder;   
		   }else if(9<applyOrder && applyOrder<100){
			   apply=apply+"-0"+applyOrder;   
		   }else if(99<applyOrder && applyOrder<1000){
			   apply=apply+"-"+applyOrder;  
		   }
		}
		Map<String,String>  map=new HashMap<String,String>();
		map.put("applyNumber", apply);
		map.put("applyOrder", String.valueOf(applyOrder));
		map.put("applyYear",  String.valueOf(year));
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
//			List<Map<String, Object>> list=yszxMapper.selectForIdeaDate();
//			 if(list!=null){
//				 for(Map<String, Object> map:list){
//					String  startdate=String.valueOf(map.get("START_DATE"));
//					String  newEndDate=DateUtil.minutes(endDate,30);
//				 
//					flags=DateUtil.compareTime(startdate,newEndDate);
//					System.out.println(newEndDate+"<"+startdate);
//					if(!flags){
//						  rw = new ResultWarp(ResultWarp.FAILED ,"用户上一场结束时间和下一场申请开始时间间隔30分钟");
//						  return JSON.toJSONString(rw);  
//					 } 
//				
//					 
//				 
//				 
//					
//					 
//					
//				 }
//				
//				
//				  
//			 }
			 
		} catch (ParseException e) {
			  rw = new ResultWarp(ResultWarp.FAILED ,"系统异常，请联系管理员"); 
			  return JSON.toJSONString(rw);  
		}
	 
	 
	 
	 
	return  JSON.toJSONString(rw);
	   
    }
    //演示中心--参观预定的添加--主要参观领导最高级别 VISIT_LEVEL
    public    String  checkVisitLevel(Map<String, Object> paramsMap) {
    	String bigvisitLevel="";
		List<HashMap> list =  (List<HashMap>) paramsMap.get("visitinfo");
    	if(!list.isEmpty()){
    		List<String> levelList=new ArrayList<String>();
    		for (Map<String, String> visitInfoMap : list) {
    			String userLevel=visitInfoMap.get("userLevel")== null ? "" : visitInfoMap.get("userLevel").toString().trim(); //级别
    			levelList.add(userLevel);
    		}
    		List<Map<String, Object>>    level =yszxMapper.selectForDictionary("visitunit_levle", levelList);
    		bigvisitLevel= String.valueOf(level.get(0).get("code"));
    	}
		return bigvisitLevel; 
    }
    
    
	/**
	 * 演示中心--参观预定的添加--主页数据的验证
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String  checkIdeaInfo(Map<String, Object> paramsMap){
		 bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo数据的验证开始" );
		 String contactUser = paramsMap.get("contactUser") == null ? "" : paramsMap.get("contactUser").toString().trim(); //联系人名称
		 String contactPhone = paramsMap.get("contactPhone") == null ? "" : paramsMap.get("contactPhone").toString().trim(); //联系人电话
		 String stateDate = paramsMap.get("stateDate") == null ? "" : paramsMap.get("stateDate").toString().trim(); //参观开始时间
		 String endDate = paramsMap.get("endDate") == null ? "" : paramsMap.get("endDate").toString();  //参观结束时间
		 String remark = paramsMap.get("remark") == null ? "" : paramsMap.get("remark").toString();  //参观结束时间
		 String visitUnitType = paramsMap.get("visitUnitType") == null ? "" : paramsMap.get("visitUnitType").toString().trim(); //参观单位性质
		 String visitUnitName = paramsMap.get("visitUnitName") == null ? "" : paramsMap.get("visitUnitName").toString().trim(); //参观单位名称
		 String visitorNumber = paramsMap.get("visitorNumber") == null ? "" : paramsMap.get("visitorNumber").toString().trim(); //参观人数
		 String companyUserNumber = paramsMap.get("companyUserNumber") == null ? "" : paramsMap.get("companyUserNumber").toString().trim(); //陪同人数
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
		bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo数据的验证结束" );
		return JSON.toJSONString(rw);  
	}

	/**
	 * 演示中心--参观预定的添加--参观领导的验证
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String  checkVisitInfo(Map<String, Object> paramsMap){
    	bgServiceLog.info("演示中心参观人员信息----->VisitInfo数据的验证开始" );
    	ResultWarp rw =  null;
    	List<HashMap> list =  (List<HashMap>) paramsMap.get("visitinfo");
    	if(!list.isEmpty()){
    		//List<HashMap> list = JSON.parseArray(visitInfo, HashMap.class);
    		for (Map<String, String> visitInfoMap : list) {
    			String userName=visitInfoMap.get("visitUserName")== null ? "" : visitInfoMap.get("visitUserName").toString().trim(); //参观人员名称
    			String position=visitInfoMap.get("visitPosition")== null ? "" : visitInfoMap.get("visitPosition").toString().trim(); //参观职务
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
    	}
    	bgServiceLog.info("演示中心参观人员信息----->VisitInfo数据的验证结束" );
		return JSON.toJSONString(rw); 
    	
    }
	/**
	 * 演示中心--参观预定的添加--陪同领导人员验证
	 * 
	 * @param
	 * @param
	 * @return
	 */
	 public String  checkCompanyLeaderInfo(Map<String, Object> paramsMap){
	    	bgServiceLog.info("演示中心陪同领导人员信息----->CompanyLeadershipInfo数据的验证开始" );
	    	ResultWarp rw =  null;
	    	String companyLeaderInfo = Rtext.toStringTrim(paramsMap.get("companyLeaderName"),"");
	    	if(companyLeaderInfo!=""){
	    		String[] companyLeaderInfoArr = companyLeaderInfo.split(",");
				for (String companyLeaderName : companyLeaderInfoArr) {
					try {
						if(companyLeaderName==""){
							  rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员名称不能为空");
							  return JSON.toJSONString(rw);  
						} 
					} catch (Exception e) {
						   rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员名称不能为空");
						  return JSON.toJSONString(rw);  
					}
					 
				}
	    	}
	    	bgServiceLog.info("演示中心陪同领导人员信息----->CompanyLeadershipInfo数据的验证结束" );
			return JSON.toJSONString(rw); 	
	    }
	 /**
		 * 演示中心--参观预定的添加--陪同人员信息验证
		 * 
		 * @param
		 * @param
		 * @return
		 */
		 public String  checkCompanyUserInfo(Map<String, Object> paramsMap  ,String ideaId){
		    	bgServiceLog.info("演示中心陪同人员信息----->CompanyUsershipInfo数据的验证开始" );
		    	ResultWarp rw =  null;
		    	List<HashMap> list =  (List<HashMap>) paramsMap.get("companyUserInfo");
		    	if(list.isEmpty()){
		    		for (Map<String, Object> companyUserMap : list) {
		    			String companyUserId=companyUserMap.get("userId")== null ? "" : companyUserMap.get("userId").toString().trim(); //参观职务
		    			try {
							if(companyUserId==""){
								  rw = new ResultWarp(ResultWarp.FAILED ,"陪同人员名称不能为空");
								  return JSON.toJSONString(rw);  
							} 
							List<Map<String, Object>>  userInfoList = yszxMapper.selectForCompanyUserInfo(ideaId,companyUserId);
							if(!userInfoList.isEmpty()){
								for(  Map<String, Object> userInfo:userInfoList){
									String userAlisa= String.valueOf(userInfo.get("userAlisa")) ;
									 rw = new ResultWarp(ResultWarp.FAILED ,"陪同人员名"+userAlisa+"已在陪同人员序列");
									 return JSON.toJSONString(rw);  
								}
							}
						} catch (Exception e) {
							   rw = new ResultWarp(ResultWarp.FAILED ,"陪同人员名称不能为空");
							  return JSON.toJSONString(rw);  
						}
						}
				   }
		    	bgServiceLog.info("演示中心陪同领导人员信息----->CompanyLeadershipInfo数据的验证结束" );
				return JSON.toJSONString(rw); 	
		    }
	/**
	 * 演示中心--参观预定的添加--全部验证代码
	 * 
	 * @param
	 * @param
	 * @return
	 */
	 public   String    checkAllInfo(Map<String, Object> paramsMap ,String ideaId){
		   //参观预定主页数据验证
		   String  ideaforRW=checkIdeaInfo(paramsMap);
		   if(!ideaforRW.equals("null")){
			   return ideaforRW;
		   } 
		   //参观人信息参观预定的添加数据的验证
		   String  visitforRW=checkVisitInfo(paramsMap);
		   if(!ideaforRW.equals("null")){
			   return ideaforRW;
		   } 
		   //陪同领导人员信息添加数据的验证
		   String  CompanyLeadershipforRW=checkCompanyLeaderInfo(paramsMap);
		   if(!CompanyLeadershipforRW.equals("null") ){
			   return CompanyLeadershipforRW;
		   }
		   //陪同部门人员信息添加数据的验证
		   String CompanyDeptforRW =checkCompanyUserInfo(paramsMap , ideaId);
		   if(!CompanyDeptforRW.equals("null")){
			   return CompanyDeptforRW;
		   }
		return "";
		 
		 
	 }  
	@Override
	public String addIdeaInfo(Map<String, Object> paramsMap) {
	   bgServiceLog.info("演示中心参观预定的添加----->开始" );
	   ResultWarp rw =  null;
	   Map<String,String>  userInfoMap=userInfo();
	   String ideaId = paramsMap.get("id") == null ? "" : paramsMap.get("id").toString();  //主ID
	   //演示中心参观预定的添加全部数据的验证
	  String checkRW =checkAllInfo(paramsMap,ideaId);
	  if(!"".equals(checkRW)){
		  return checkRW;
	  } 
	  //演示中心参观预定的添加数据
	   IdeaInfo  ideaInfo=new IdeaInfo();
	   String  IdeaRW=null;
	   if("".equals(ideaId)){//添加
		 ideaId=Rtext.getUUID();
		 IdeaRW= IdeaInfo(paramsMap,ideaInfo,userInfoMap,"add",ideaId); //演示中心参观预定的添加数据实体的返回
		 if(!IdeaRW.equals("null")){
			 return IdeaRW;
		 }
	  }else{//修改
		 IdeaRW =IdeaInfo(paramsMap,ideaInfo,userInfoMap,"updata",ideaId); //演示中心参观预定的添加数据实体的返回
		 if(!IdeaRW.equals("null")){
			 return IdeaRW;
		 }
	  }
	  //演示中心参观人员信息的添加
	  String  visitRW= visitInfo(paramsMap, ideaId,userInfoMap); 
	  if(!visitRW.equals("null")){
			 return IdeaRW;
	  }  
	  //演示中心陪同领导信息的添加
	  String  leaderRW=CompanyLeaderInfo(paramsMap,ideaId,userInfoMap);
	  if(!leaderRW.equals("null")){
			 return IdeaRW;
	  }  
	  //演示中心陪同人员信息的添加
	  String   companyUserRW=CompanyUserInfo(paramsMap,ideaId,userInfoMap);
	  if(!companyUserRW.equals("null")){
			 return IdeaRW;
	  }  
	  rw = new ResultWarp(ResultWarp.SUCCESS ,"添加成功");
	  return JSON.toJSONString(rw);  
	}

	
	/**
	 * 演示中心--参观预定的添加--返回实体数据
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public String IdeaInfo(Map<String, Object> paramsMap, IdeaInfo  ideaInfo,Map<String,String>  userInfoMap,String type,String ideaId){
		 bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo实体返回开始" );
		 ResultWarp rw =  null;
		 String visitLevel = paramsMap.get("visitLevel") == null ? "" : paramsMap.get("visitLevel").toString();  //提交|保存
		 String contactUser = paramsMap.get("contactUser") == null ? "" : paramsMap.get("contactUser").toString(); //联系人名称
		 String contactPhone = paramsMap.get("contactPhone") == null ? "" : paramsMap.get("contactPhone").toString(); //联系人电话
		 String stateDate = paramsMap.get("stateDate") == null ? "" : paramsMap.get("stateDate").toString(); //参观开始时间
		 String endDate = paramsMap.get("endDate") == null ? "" : paramsMap.get("endDate").toString();  //参观结束时间
		 String remark = paramsMap.get("remark") == null ? "" : paramsMap.get("remark").toString();  //参观结束时间
		 String visitUnitType = paramsMap.get("visitUnitType") == null ? "" : paramsMap.get("visitUnitType").toString(); //参观单位性质
		 String visitUnitName = paramsMap.get("visitUnitName") == null ? "" : paramsMap.get("visitUnitName").toString(); //参观单位名称
		 String visitorNumber = paramsMap.get("visitorNumber") == null ? "" : paramsMap.get("visitorNumber").toString(); //参观人数
		 String companyUserNumber = paramsMap.get("companyUserNumber") == null ? "" : paramsMap.get("companyUserNumber").toString(); //陪同人数
		 String approvalUserd = paramsMap.get("approvalUserd") == null ? "" : paramsMap.get("approvalUserd").toString();  //提交|保存
		 
		 String applyDept= userInfoMap.get("deptId");
		ideaInfo.setId(ideaId);
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
		Map<String,String >     applyIdMap=applyId(ideaId);
		String applyNumber= applyIdMap.get("applyNumber");
		int   applyOrder=Integer.valueOf(applyIdMap.get("applyOrder"));
		int   applyYear=Integer.valueOf(applyIdMap.get("applyYear"));
		ideaInfo.setApplyNumber(applyNumber);
		ideaInfo.setApplyOrder(applyOrder);
		ideaInfo.setApplyYear(applyYear);
		  ideaInfo.setApplyId(applyNumber);
		String   bigVisitLevel=checkVisitLevel(paramsMap);
		ideaInfo.setVisitLevel(bigVisitLevel);
		ideaInfo.setValId("1");
		String createUserId= userInfoMap.get("userId");
		ideaInfo.setCreateUser(createUserId);
		ideaInfo.setCreateTime(new Date());
		ideaInfo.setUpdateUser(createUserId);
		ideaInfo.setUpdateTime(new Date());
		try {
		if("add".equals(type)){//添加
		       Map<String, Object>    ideaMap = yszxMapper.selectForId(ideaId);
			   if("null".equals(ideaMap)){
				      rw = new ResultWarp(ResultWarp.FAILED ,"该数据存在");
					  return JSON.toJSONString(rw); 
					  
			   }
			 if(visitLevel.equals("save")){//保存
				   ideaInfo.setStatus("SAVE");
				   yszxMapper.addIdeaInfo(ideaInfo);
			   }else if(visitLevel.equals("submit")){//提交
				   ideaInfo.setStatus("DEPT_HEAD_CHECK");
				   approveService.startApprove("YSZX","DEPT_HEAD_CHECK",ideaId,approvalUserd);
				   yszxMapper.addIdeaInfo(ideaInfo);
			   }
		   }  
		} catch (Exception e) {
			  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
			  return JSON.toJSONString(rw);  
		}
		
		try {
		if("updata".equals(type)){//修改
			  Map<String, Object>    ideaMap = yszxMapper.selectForId(ideaId);
			   if(ideaMap.isEmpty()){
				      rw = new ResultWarp(ResultWarp.FAILED ,"该数据不存在");
					  return JSON.toJSONString(rw);  
			   }
			if(visitLevel.equals("save")){//保存
				  ideaInfo.setStatus("SAVE");
				  yszxMapper.updataIdeaInfo(ideaInfo);
			   }else if(visitLevel.equals("submit")){//提交
				  ideaInfo.setStatus("DEPT_HEAD_CHECK");
				  yszxMapper.updataIdeaInfo(ideaInfo);
				  approveService.startApprove("YSZX","DEPT_HEAD_CHECK",ideaId,approvalUserd);
				  
			   }
			
		}
		} catch (Exception e) {
			  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
			  return JSON.toJSONString(rw);  
		}
		bgServiceLog.info("演示中心参观预定的添加----->IdeaInfo实体返回结束" );
		return   JSON.toJSONString(rw);  
	}
	/**
	 * 演示中心--参观预定的添加--参观领导人信息实体
	 * 
	 * @param
	 * @param
	 * @return
	 */
    public String   visitInfo(Map<String, Object> paramsMap ,String ideaId, Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心参观人员信息----->visitInfo实体开始" );
   	    ResultWarp rw =  null;
    	List<HashMap> list =  (List<HashMap>) paramsMap.get("visitinfo"); 
    	if(!list.isEmpty()){
    		for (Map<String, Object> visitInfoMap : list) {
    			String visitId=visitInfoMap.get("visitId")== null ? "" : visitInfoMap.get("visitId").toString().trim(); //参观主id
    			String userName=visitInfoMap.get("visitUserName")== null ? "" : visitInfoMap.get("visitUserName").toString().trim(); //参观人员名称
    			String position=visitInfoMap.get("visitPosition")== null ? "" : visitInfoMap.get("visitPosition").toString().trim(); //参观职务
    			String userLevel=visitInfoMap.get("userLevel")== null ? "" : visitInfoMap.get("userLevel").toString().trim(); //级别
    			String sortId=visitInfoMap.get("sortId")== null ? "" : visitInfoMap.get("sortId").toString().trim(); //级别
    			//参观人员
    			int sortIds=Integer.parseInt(sortId); //级别
    			VisitInfo  visit=new VisitInfo();
    			visit.setUserName(userName);
    			visit.setPosition(position);
    			visit.setUserLevel(userLevel);
    			visit.setIdeaId(ideaId);
    			visit.setSortId(sortIds);
    			visit.setRemark("");
    			visit.setValId("1");
    			String createUser= userInfoMap.get("userId");
    			visit.setCreateUser(createUser);
    			visit.setCreateTime(new Date());
    			visit.setUpdateUser(createUser);
    			visit.setUpdateTime(new Date());
    			try {
	    			if(visitId==""){//添加
	    				 visitId=Rtext.getUUID();
	    				 visit.setId(visitId);
	    				 yszxMapper.addVisitInfo(visit);
	    			}else{//修改
	    				visit.setId(visitId);
	    				yszxMapper.updataVisitInfo(visit);
	    			}
    			} catch (Exception e) {
    				  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
    				  return JSON.toJSONString(rw);  
    			}
    			
            }
    	}
		return    JSON.toJSONString(rw);  
    }
    /**
	 * 演示中心--参观预定的添加--陪同领导人员实体
	 * @param
	 * @param
	 * @return
	 */
    public  String   CompanyLeaderInfo(Map<String, Object> paramsMap ,String ideaId,Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心参观人员信息----->CompanyLeadershipInfo实体" );
    	 ResultWarp rw =  null;
    	//删除领导人信息
    	String companyLeaderInfo = Rtext.toStringTrim(paramsMap.get("companyLeaderName"),"");
    	if(companyLeaderInfo==""){
    		return   JSON.toJSONString(rw); 
    	}
    	 String[] companyLeaderInfoArr = companyLeaderInfo.split(",");
    	 List<Map<String, Object>>   leaderList=yszxMapper.selectForCompanyLeaderInfo(ideaId,"");
		 if(leaderList.isEmpty()){
			 for (String companyLeaderName : companyLeaderInfoArr) {
		    		CompanyLeaderInfo  companyInfo=new CompanyLeaderInfo();
					String companyLeadershipId=Rtext.getUUID();
					companyInfo.setId(companyLeadershipId);
					companyInfo.setIdeaId(ideaId);
					companyInfo.setRemark("");
					companyInfo.setValid("1");
					companyInfo.setUserId(companyLeaderName);
					String userId= userInfoMap.get("userId");
					companyInfo.setCreateUser(userId);
					companyInfo.setCreateTime(new Date());
					companyInfo.setUpdateUser(userId);
					companyInfo.setUpdateTime(new Date());
					try {
						yszxMapper.addCompanyLeaderInfo(companyInfo); 
					} catch (Exception e) {
						  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
						  return JSON.toJSONString(rw);  
					}
		    	}
			 
			 
		 } else{
			   CompanyLeaderInfo  companyInfo=new CompanyLeaderInfo();
			   companyInfo.setIdeaId(ideaId);
			   companyInfo.setRemark("");
			   companyInfo.setValid("1");
			   String userId= userInfoMap.get("userId");
			   companyInfo.setCreateUser(userId);
			   companyInfo.setCreateTime(new Date());
			   companyInfo.setUpdateUser(userId);
			   companyInfo.setUpdateTime(new Date());
			    //前端的用户信息
			    List<String>  frontlist=new ArrayList<String>();
		    	for(String userid : companyLeaderInfoArr){
		    		frontlist.add(userid);
		    	}
		    	List<String>  afterlist=new ArrayList<String>();
		    	//后端的用户信息
		        for(Map<String, Object>     leaders: leaderList){
		        	String  userid= String.valueOf(leaders.get("userId")) ;
		        	afterlist.add(userid);
		    	}
		         String  frontRW    =  front(frontlist,afterlist,companyInfo);//添加
				  if(!frontRW.equals("null")){
						 return frontRW;
				  }  
				  String  after    =  after(frontlist,afterlist,ideaId, userId);//删除
		        if(!after.equals("null")){
					 return after;
			     }  
		        
		 }
    	
    	
    	
    	
		return   JSON.toJSONString(rw);  
    }
    //取差集
    public  String front(List<String>frontlist,List<String>afterlist,CompanyLeaderInfo companyInfo){
    	ResultWarp rw =  null;
    	List<String> collectionList =getSet.receiveDefectList(frontlist, afterlist);
    	if(!collectionList.isEmpty()){
    		for(String userid:collectionList){//添加
    			String companyLeadershipId=Rtext.getUUID();
				companyInfo.setId(companyLeadershipId);
				companyInfo.setUserId(userid);
				try {
					yszxMapper.addCompanyLeaderInfo(companyInfo); 
				} catch (Exception e) {
					  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
					  return JSON.toJSONString(rw);  
				}
        	}
    	}
    	
    	 return JSON.toJSONString(rw); 
    }
    //取差集
    public  String after(List<String>frontlist,List<String>afterlist, String ideaId,String updateUser ){
    	ResultWarp rw =  null;
    	List<String> collectionList =getSet.receiveDefectList(afterlist, frontlist);
    	if(!collectionList.isEmpty()){
    		for(String userid:collectionList){//删除
    			try {
    			yszxMapper.deleteLeaderInfo(userid, ideaId, "0", updateUser, new  Date());
    			} catch (Exception e) {
					  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
					  return JSON.toJSONString(rw);  
				}
        	}
    	}
    	
    	 return JSON.toJSONString(rw); 
    }
    
    
    
    
    /**
   	 * 演示中心--参观预定的添加--陪同人员信息实体
   	 * @param
   	 * @param
   	 * @return
   	 */
    public  String CompanyUserInfo(Map<String, Object> paramsMap,String  ideaId,Map<String,String>  userInfoMap ){
    	bgServiceLog.info("演示中心陪同部门人员信息----->CompanyDeptInfo数据的验证" );
    	 ResultWarp rw =  null;
    	List<HashMap> list =  (List<HashMap>) paramsMap.get("companyUserInfo");
    	if(!list.isEmpty()){
    		for (Map<String, Object> companyUserMap : list) {
    			String companyId=companyUserMap.get("companyId")== null ? "" : companyUserMap.get("companyId").toString().trim(); //参观人员名称
    			String companyUserId=companyUserMap.get("userId")== null ? "" : companyUserMap.get("userId").toString().trim(); //参观职务
			//查询
			CompanyUserInfo  companyUser=new CompanyUserInfo();
			companyUser.setUserId(companyUserId);
			companyUser.setIdeaId(ideaId);
			String userId= userInfoMap.get("userId");
			companyUser.setCreateUser(userId);
			companyUser.setCreateTime(new Date());
			companyUser.setUpdateUser(userId);
			companyUser.setUpdateTime(new Date());
			companyUser.setValid("1"); 
			try {
			 if(companyId==""){
				  companyId =Rtext.getUUID();
				  companyUser.setId(companyId);
				  yszxMapper.addCompanyUserInfo(companyUser);
			 } 
		    }catch (Exception e) {
				  rw = new ResultWarp(ResultWarp.FAILED ,"添加异常，请重新添加");
				  return JSON.toJSONString(rw);  
			}
    	  }
    	}
		return   JSON.toJSONString(rw); 
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
	@Override
	public String selectForLeader(HttpServletRequest request) {
		Map<String, Object>  map= new  HashMap<String, Object>();
		 List<Map<String, Object>>   list=	yszxMapper.selectForLeader();
		 if(list.isEmpty()){
		    	map.put("leaderData", "");
		 	    map.put("success", "false");
		 	    map.put("msg", "查询失败");
		    }else{
		    	 List<Map<String, Object>>  leaderList=new  ArrayList<Map<String, Object>>();
		    	for(Map<String, Object> leadermaps :list){
		    		String id=String.valueOf( leadermaps.get("USERID")) ;
		    		String text=String.valueOf( leadermaps.get("USERALISA"));
		    		Map<String, Object>  leadermap=new  HashMap<String, Object>();
		    		leadermap.put("id", id);
		    		leadermap.put("text", text);
		    		leaderList.add(leadermap);
		    	}
		    	map.put("leaderData",  leaderList);
		 	    map.put("success", "ture");
		 	    map.put("msg", "查询成功");
		    }
		   
		    String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			return jsonStr;
		 
	}
	/**
	 * 查询演示中心信息
	 * @param pro
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectForIdeaInfo(String  applyId,String createTime) {
		  List<Map<String, Object>>  ideaInfo=yszxMapper.selectForIdeaInfo(applyId, createTime);
		  List<Map<String, Object>>  list=new  ArrayList<Map<String, Object>>();
		  if(!ideaInfo.isEmpty()){
			  for(Map<String, Object>  idea:ideaInfo){
				  String  ideaId=Rtext.toStringTrim(idea.get("id"), "");
				  String  visitName=selectForVisitInfo(ideaId);
				  idea.put("visitName", visitName);
				  String  leaderName=selectForCompanyLeaderInfo(ideaId);
				  idea.put("leaderName", leaderName);
				  String  userName=selectForCompanyUserInfo(ideaId);
				  idea.put("userName", userName);
				  list.add(idea);
			  }
		  } 
		return list;
	}
	/**
	 * 查询演示---参观领导的查询根据ideaId
	 * @param pro
	 * @return
	 */
	  public  String   selectForVisitInfo(String  ideaId){
		  List<Map<String, Object>>  visitInfo = yszxMapper.selectForVisitInfo(ideaId);
		  String visitName = "";
		  if(!visitInfo.isEmpty()){
			  for(Map<String, Object>  visit:visitInfo){
				  String userName=Rtext.toStringTrim(visit.get("userName"), "");
				  if(!"".equals(userName)){
					  visitName +=userName+",";
				  }
				  
			  }
			  String  visitNames =visitName.trim();
			 
			  visitName=visitNames.substring(0, visitNames.length()-1);
			  System.out.print(visitName);
		  } 
		return visitName;
		  
	  }
	  /**
		 * 查询演示---	陪同领导的查询根据ideaId
		 * @param pro
		 * @return
		 */
		  public  String   selectForCompanyLeaderInfo(String  ideaId){
			  List<Map<String, Object>>  leaderInfo = yszxMapper.selectForCompanyLeaderInfo(ideaId,"");
			  String leaderName = "";
			  if(!leaderInfo.isEmpty()){
				  for(Map<String, Object>  visit:leaderInfo){
					  String userName=Rtext.toStringTrim(visit.get("userAlisa"), "");
					//    String userName=Rtext.toStringTrim(visit.get("userId"), "");
					  if(!"".equals(userName)){
						  leaderName +=userName+",";
					  }
				  }
				  String  leaderNames =leaderName.trim();
				  leaderName=leaderNames.substring(0, leaderNames.length()-1);
				  System.out.print(leaderName);
			  } 
			return leaderName;
			  
		  }
		  /**
			 * 查询演示---陪同部门人员信息的查询根据ideaId
			 * @param pro
			 * @return
			 */
		public  String   selectForCompanyUserInfo(String  ideaId){
			List<Map<String, Object>>  userInfo = yszxMapper.selectForCompanyUserInfo(ideaId,"");
			String userName = "";
				  if(!userInfo.isEmpty()){
					  for(Map<String, Object>  visit:userInfo){
						  String useralisa=Rtext.toStringTrim(visit.get("userAlisa"), "");
						  if(!"".equals(useralisa)){
							  userName+=useralisa+",";
						  }
					  }
					String  userNames =userName.trim();
					userName=userNames.substring(0, userNames.length()-1);
					System.out.print(userName);
				  } 
				return userName;
				  
			  }  
	  /**
		* 查询演示---陪同部门人员信息的查询根据ideaId
		* @param pro
		* @return
	    */								  
		@Override
		public Map<String, Object> selectForId(String id) {
			 Map<String, Object>   ideaInfo = 	yszxMapper.selectForId(id);
			 if(!ideaInfo.isEmpty()){
				String ideaId= Rtext.toStringTrim(ideaInfo.get("id"), "");
				List<Map<String, Object>>  visitInfo = yszxMapper.selectForVisitInfo(ideaId);
				if(!visitInfo.isEmpty()){
					ideaInfo.put("visitInfo", visitInfo);
				}
				List<Map<String, Object>>  leaderInfo = yszxMapper.selectForCompanyLeaderInfo(ideaId,"");
				if(!leaderInfo.isEmpty()){
					String leaders="";
					for( Map<String, Object> leader:leaderInfo){
					//	String userAlisa= Rtext.toStringTrim(leader.get("userAlisa"), "");
						String userAlisa= Rtext.toStringTrim(leader.get("userId"), "");
						if(userAlisa!=""){
							leaders+=userAlisa+",";
						} 
					}
					String  userNames =leaders.trim();
					String userName=userNames.substring(0, userNames.length()-1);
					ideaInfo.put("leaderInfo", userName);
				}
				List<Map<String, Object>>  userInfo = yszxMapper.selectForCompanyUserInfo(ideaId,"");
				if(!userInfo.isEmpty()){
					ideaInfo.put("userInfo", userInfo);
				}
			 } 
			return ideaInfo;
		} 
	@Override
	public String selectForuserName(String userId) {	
	    	Map<String, Object>  map= new  HashMap<String, Object>();
		    userId = Rtext.toStringTrim(userId,"");
		    if(userId==""){
		    	map.put("data", "");
		 	    map.put("success", "false");
		 	    map.put("msg", "查询失败");
		    }
		    try{
		    	List<Map<String, String>> UserInfoList=new ArrayList<Map<String, String>>();
				String[] userIdArr = userId.split(",");
				for (String userid : userIdArr) {
							Map<String, String> userInfo=yszxMapper.selectForuserName(userid);
							UserInfoList.add(userInfo);
				}

				map.put("userInfo",  UserInfoList);
		 	    map.put("success", "ture");
		 	    map.put("msg", "查询成功");
		    }catch(Exception e){
		    	map.put("data", "");
		 	    map.put("success", "false");
		 	    map.put("msg", "查询失败");
		    	
		    }
		    String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
			return jsonStr;
	  }
	@Override
	public String deleteVisitInfo(String visitId ) {
		ResultWarp rw =  null;
		try{
			Map<String,String>  userInfoMap=userInfo();
			String updateUser= userInfoMap.get("userId");
			yszxMapper.deleteVisitInfo(visitId, "0", updateUser, new Date());
			rw = new ResultWarp(ResultWarp.SUCCESS ,"陪同人员删除成功");  
		}catch(Exception e){
			rw = new ResultWarp(ResultWarp.FAILED ,"参观领导删除失败");
		}
		String jsonStr=JSON.toJSONStringWithDateFormat(rw,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	@Override
	public String deleteCompanyUserInfo(String companyId ) {
		ResultWarp rw =  null;
		try{
		Map<String,String>  userInfoMap=userInfo();
		String updateUser= userInfoMap.get("userId");
		yszxMapper.deleteCompanyUserInfo(companyId, "0", updateUser, new Date());
		 rw = new ResultWarp(ResultWarp.SUCCESS ,"陪同人员删除成功");  
		}catch(Exception e){
		   rw = new ResultWarp(ResultWarp.FAILED ,"陪同人员删除失败");   
		}
		String jsonStr=JSON.toJSONStringWithDateFormat(rw,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
		 
	}
	@Override
	public String deleteIdeaInfo(String ideaId) {
		bgServiceLog.info("演示中心参观预定主页面删除----->删除开始" );
		ResultWarp rw =  null;
		try{
			Map<String,String>  userInfoMap=userInfo();
			String updateUser= userInfoMap.get("userId");
			yszxMapper.deleteIdeaInfo(ideaId, "0", updateUser, new Date());
			rw = new ResultWarp(ResultWarp.SUCCESS ,"删除成功");  
		}catch(Exception e){
			rw = new ResultWarp(ResultWarp.FAILED ,"删除失败");  
		}
		bgServiceLog.info("演示中心参观预定主页面删除----->删除结束" );
		String jsonStr=JSON.toJSONStringWithDateFormat(rw,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
 	  
		 
				 
}