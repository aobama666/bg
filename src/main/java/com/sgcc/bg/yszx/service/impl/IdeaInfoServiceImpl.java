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
import com.sgcc.bg.yszx.bean.CompanyDeptInfo;
import com.sgcc.bg.yszx.bean.CompanyLeadershipInfo;
import com.sgcc.bg.yszx.service.IdeaInfoService;

@Service
public class IdeaInfoServiceImpl implements IdeaInfoService {
	@Autowired
	private YSZXMapper yszxMapper;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	private static Logger bgServiceLog =  LoggerFactory.getLogger(IdeaInfoServiceImpl.class);

	@Override
	public String addIdeaInfo(HttpServletRequest request) {
	   bgServiceLog.info("演示中心参观预定的添加----->开始" );
	   String type = request.getParameter("type" ) == null ? "" : request.getParameter("type").toString(); //提交|保存
	   //演示中心参观预定的添加数据的验证
	   String  ideaforRW=checkIdeaInfo(request);
	  
	   Map<String,String>  userInfoMap=userInfo();
	   if(!ideaforRW.equals("null")){
		   return ideaforRW;
	   } 
	   //参观人信息参观预定的添加数据的验证
	   String  visitforRW=checkVisitInfo(request);
	   if(!ideaforRW.equals("null")&&!ideaforRW.equals("0")){
		   return ideaforRW;
	   } 
	   //陪同领导人员信息添加数据的验证
	   String  CompanyLeadershipforRW=checkCompanyLeadershipInfo(request);
	   if(!CompanyLeadershipforRW.equals("null")&&!ideaforRW.equals("0")){
		   return CompanyLeadershipforRW;
	   }
	   //陪同部门人员信息添加数据的验证
	   String CompanyDeptforRW =checkCompanyDeptInfo(request);
	   if(!CompanyDeptforRW.equals("null")&&!CompanyDeptforRW.equals("0")){
		   return CompanyDeptforRW;
	   }
	   IdeaInfo  ideaInfo=new IdeaInfo();
	   String id=Rtext.getUUID();
	   ideaInfo.setId(id);
	   ideaInfo =IdeaInfo(request,ideaInfo,userInfoMap); //演示中心参观预定的添加数据实体的返回
	   if(type.equals("save")){//保存
		   ideaInfo.setApplyStatus(0);
		   yszxMapper.addIdeaInfo(ideaInfo);
	   }else if(type.equals("submit")){//提交
		   ideaInfo.setApplyStatus(1);
		   yszxMapper.addIdeaInfo(ideaInfo);
	   }
	   if(!"0".equals(visitforRW)){
		   //演示中心参观人员信息的添加
		  visitInfo(request, id,userInfoMap); 
	   }
	   if(!"0".equals(CompanyLeadershipforRW)){
		   //演示中心陪同领导人员信息的添加
		   CompanyLeadershipInfo(request,id,userInfoMap);
	   }  
	   if(!"0".equals(CompanyDeptforRW)){
		 //演示中心陪同部门人员信息的添加
		   CompanyDeptInfo(request,id,userInfoMap);
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
		String contactName = request.getParameter("contactName" ) == null ? "" : request.getParameter("contactName").toString(); //联系人名称
		String contactMoile = request.getParameter("contactMoile" ) == null ? "" : request.getParameter("contactMoile").toString(); //联系人电话
		String stateDate = request.getParameter("stateDate" ) == null ? "" : request.getParameter("stateDate").toString(); //参观开始时间
		String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //参观结束时间
		String remark = request.getParameter("remark" ) == null ? "" : request.getParameter("remark").toString(); //备注
		String visitunitType = request.getParameter("visitunitType" ) == null ? "" : request.getParameter("visitunitType").toString(); //参观单位性质
		String visitunitName = request.getParameter("visitunitName" ) == null ? "" : request.getParameter("visitunitName").toString(); //参观单位名称
		String visitNumber = request.getParameter("visitNumber" ) == null ? "" : request.getParameter("visitNumber").toString(); //参观人数
		String companyNumber = request.getParameter("companyNumber" ) == null ? "" : request.getParameter("companyNumber").toString(); //陪同人数
		ResultWarp rw =  null;
		//联系人名称名称的验证
		if(contactName==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"联系人名称不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			//联系人名称为中文或英文
			boolean flag=StringUtil.checkNotNoOrC(contactName);
			if(!flag){
				  rw = new ResultWarp(ResultWarp.FAILED ,"联系人名称只能为中文或英文");
				  return JSON.toJSONString(rw); 
			}
			
		}
		//联系人电话的验证
		if(contactMoile==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"联系人电话不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			//联系人电话为手机格式和座机格式
			boolean flag=StringUtil.checkPhone(contactMoile);
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
		if(visitunitType==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观单位性质不能为空");
			  return JSON.toJSONString(rw);  
		}
		//参观单位名称
		if(visitunitName==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观单位名称不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			 if(visitunitName.length()>150){
				 rw = new ResultWarp(ResultWarp.FAILED ,"参观单位名称不超过150个字");
				  return JSON.toJSONString(rw); 
			 }
		}
		//参观人数
		if(visitNumber==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"参观人数不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			boolean flag=NumberValidationUtil.isPositiveInteger(visitNumber);
			if(!flag){
				 rw = new ResultWarp(ResultWarp.FAILED ,"参观人数必须为数字");
				 return JSON.toJSONString(rw); 
			}	
		}
		//陪同人数
		if(companyNumber==""){
			  rw = new ResultWarp(ResultWarp.FAILED ,"陪同人数不能为空");
			  return JSON.toJSONString(rw);  
		}else{
			boolean flag=NumberValidationUtil.isPositiveInteger(companyNumber);
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
		String contactName = request.getParameter("contactName" ) == null ? "" : request.getParameter("contactName").toString(); //联系人名称
		String contactMoile = request.getParameter("contactMoile" ) == null ? "" : request.getParameter("contactMoile").toString(); //联系人电话
		String stateDate = request.getParameter("stateDate" ) == null ? "" : request.getParameter("stateDate").toString(); //参观开始时间
		String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //参观结束时间
		String remark = request.getParameter("remark" ) == null ? "" : request.getParameter("remark").toString(); //备注
		String visitunitType = request.getParameter("visitunitType" ) == null ? "" : request.getParameter("visitunitType").toString(); //参观单位性质
		String visitunitName = request.getParameter("visitunitName" ) == null ? "" : request.getParameter("visitunitName").toString(); //参观单位名称
		String visitNumber = request.getParameter("visitNumber" ) == null ? "" : request.getParameter("visitNumber").toString(); //参观人数
		String companyNumber = request.getParameter("companyNumber" ) == null ? "" : request.getParameter("companyNumber").toString(); //陪同人数
		Date startDates = DateUtil.fomatDate(stateDate);
		Date endDates = DateUtil.fomatDate(endDate);
		ideaInfo.setContactName(contactName);
		ideaInfo.setContactMoile(contactMoile);
		ideaInfo.setStateDate(startDates);
		ideaInfo.setEndDate(endDates);
		ideaInfo.setRemark(remark);
		ideaInfo.setVisitunitType(visitunitType);
		ideaInfo.setVisitunitName(visitunitName);
		ideaInfo.setVisitNumber(Integer.parseInt(visitNumber));
		ideaInfo.setCompanyNumber(Integer.parseInt(companyNumber));
		ideaInfo.setType("0");
		String applyDate=DateUtil.getTime();
		Date applyDates = DateUtil.fomatDate(applyDate);
		ideaInfo.setApplyDate(applyDates);
		ideaInfo.setDateCreated(applyDates);
		ideaInfo.setIsFinish(0);
		ideaInfo.setStatus("1");
		String userId= userInfoMap.get("userId");
		String name= userInfoMap.get("name");
		String deptId= userInfoMap.get("deptId");
		String applyId=applyId(name);
		ideaInfo.setApplyId(applyId);
		ideaInfo.setUserId(userId);
		ideaInfo.setDeptId(deptId);
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
		CommonUser user=webUtils.getCommonUser();
		String  name=user.getName();
		map.put("name", name);
		String  userid=user.getId();
		map.put("userId", userid);
		map.put("deptId", "deptId");
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
    public static  String  checkData( String stateDate,String endDate) {
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
			String visitUserName=visitInfoMap.get("visitUserName")== null ? "" : visitInfoMap.get("visitUserName").toString().trim(); //参观人员名称
			String visitPosition=visitInfoMap.get("visitPosition")== null ? "" : visitInfoMap.get("visitPosition").toString().trim(); //参观职务
			String visitLevel=visitInfoMap.get("visitLevel")== null ? "" : visitInfoMap.get("visitLevel").toString().trim(); //级别
			//参观人员名称
			if(visitUserName==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观人员名称不能为空");
				  return JSON.toJSONString(rw);  
			}else{
				//参观人员名称为中文或英文
				boolean flag=StringUtil.checkNotNoOrC(visitUserName);
				if(!flag){
					  rw = new ResultWarp(ResultWarp.FAILED ,"参观人员名称只能为中文或英文");
					  return JSON.toJSONString(rw); 
				}
			}
			//参观人职务
			if(visitPosition==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"参观人职务不能为空");
				  return JSON.toJSONString(rw);  
			}else{
				if(visitPosition.length()>100){
					  rw = new ResultWarp(ResultWarp.FAILED ,"参观人职务不超过200个字");
					  return JSON.toJSONString(rw);  
				}	
			}
			//参观人级别
			if(visitLevel==""){
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
			String visitUserName=visitInfoMap.get("visitUserName")== null ? "" : visitInfoMap.get("visitUserName").toString().trim(); //参观人员名称
			String visitPosition=visitInfoMap.get("visitPosition")== null ? "" : visitInfoMap.get("visitPosition").toString().trim(); //参观职务
			String visitLevel=visitInfoMap.get("visitLevel")== null ? "" : visitInfoMap.get("visitLevel").toString().trim(); //级别
			VisitInfo  visit=new VisitInfo();
			visit.setVisitUserName(visitUserName);
			visit.setVisitPosition(visitPosition);
			visit.setVisitLevel(visitLevel);
			String visitId=Rtext.getUUID();
			visit.setId(visitId);
			visit.setIdeaId(ideaId);
			String userId= userInfoMap.get("userId");
			visit.setCreator(userId);
			String createDate=DateUtil.getTime();
			Date createDates = DateUtil.fomatDate(createDate);
			visit.setDataCreated(createDates);
			visit.setCreateDate(createDates);
			visit.setStatus("1");
			yszxMapper.addVisitInfo(visit);
        }
    }
    public String  checkCompanyLeadershipInfo(HttpServletRequest request){
    	bgServiceLog.info("演示中心陪同领导人员信息----->CompanyLeadershipInfo数据的验证" );
    	ResultWarp rw =  null;
    	String companyLeadershipInfo = Rtext.toStringTrim(request.getParameter("companyLeadershipInfo"),"");
    	if(companyLeadershipInfo==""){
    		return "0";
    	}
		List<HashMap> list = JSON.parseArray(companyLeadershipInfo, HashMap.class);
		for (Map<String, String> CompanyLeadershipInfo : list) {
			String companyUserName=CompanyLeadershipInfo.get("companyUserName")== null ? "" : CompanyLeadershipInfo.get("companyUserName").toString(); //陪同人员名称
			String companyHRcode=CompanyLeadershipInfo.get("companyHRcode")== null ? "" : CompanyLeadershipInfo.get("companyHRcode").toString(); //陪同人员HR
			 //陪同领导人员名称
			if(companyUserName==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员名称不能为空");
				  return JSON.toJSONString(rw);  
			}else{
				//参观人员名称为中文或英文
				boolean flag=StringUtil.checkNotNoOrC(companyUserName);
				if(!flag){
					  rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员名称只能为中文或英文");
					  return JSON.toJSONString(rw); 
				}
			}
			//参观人职务
			if(companyHRcode==""){
				  rw = new ResultWarp(ResultWarp.FAILED ,"陪同领导人员HRcode不能为空");
				  return JSON.toJSONString(rw);  
			} 
		}
		return JSON.toJSONString(rw); 	
    }
    public  void  CompanyLeadershipInfo(HttpServletRequest request ,String ideaId,Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心参观人员信息----->CompanyLeadershipInfo实体" );
    	String companyLeadershipInfo = Rtext.toStringTrim(request.getParameter("companyLeadershipInfo"),"");
		List<HashMap> list = JSON.parseArray(companyLeadershipInfo, HashMap.class);
		for (Map<String, String> CompanyLeadershipInfo : list) {
			String companyUserName=CompanyLeadershipInfo.get("companyUserName")== null ? "" : CompanyLeadershipInfo.get("companyUserName").toString(); //陪同人员名称
			String companyHRcode=CompanyLeadershipInfo.get("companyHRcode")== null ? "" : CompanyLeadershipInfo.get("companyHRcode").toString(); //陪同人员HR
			CompanyLeadershipInfo  companyInfo=new CompanyLeadershipInfo();
			companyInfo.setCompanyHRcode(companyHRcode);
			companyInfo.setCompanyUserName(companyUserName);
			String companyLeadershipId=Rtext.getUUID();
			companyInfo.setId(companyLeadershipId);
			companyInfo.setIdeaId(ideaId);
			String userId= userInfoMap.get("userId");
			companyInfo.setCreator(userId);
			String createDate=DateUtil.getTime();
			Date createDates = DateUtil.fomatDate(createDate);
			companyInfo.setDataCreated(createDates);
		    companyInfo.setCreateDate(createDates);
			companyInfo.setStatus("1");
			yszxMapper.addCompanyLeadershipInfo(companyInfo);
			 
		}
		
    }
    public  String  checkCompanyDeptInfo(HttpServletRequest request){
    	bgServiceLog.info("演示中心陪同部门人员信息----->CompanyDeptInfo数据的验证" );
    	ResultWarp rw =  null;
    	String companyDeptInfo = Rtext.toStringTrim(request.getParameter("companyDeptInfo"),"");
    	if(companyDeptInfo==""){
    		return "0";
    	}
		List<HashMap> list = JSON.parseArray(companyDeptInfo, HashMap.class);
		for (Map<String, String> CompanyDeptInfo : list) {
			String userid=CompanyDeptInfo.get("companyUserId")== null ? "" : CompanyDeptInfo.get("companyUserId").toString(); //陪同人员名称
			//查询
			Map<String ,Object> userInfo=yszxMapper.getUserId(userid);
			if(userInfo==null){
				  rw = new ResultWarp(ResultWarp.FAILED ,"该陪同人信息异常，请联系管理员");
				  return JSON.toJSONString(rw);  
			}
		}
		return JSON.toJSONString(rw); 	
    }
    public  void CompanyDeptInfo(HttpServletRequest request,String  ideaId,Map<String,String>  userInfoMap){
    	bgServiceLog.info("演示中心陪同部门人员信息----->CompanyDeptInfo数据的验证" );
    	String companyDeptInfo = Rtext.toStringTrim(request.getParameter("companyDeptInfo"),"");
		List<HashMap> list = JSON.parseArray(companyDeptInfo, HashMap.class);
		for (Map<String, String> CompanyDeptInfo : list) {
			String userid=CompanyDeptInfo.get("companyUserId")== null ? "" : CompanyDeptInfo.get("companyUserId").toString(); //陪同人员名称
			//查询
			Map<String ,Object> userInfo=yszxMapper.getUserId(userid);
			CompanyDeptInfo  companyDept=new CompanyDeptInfo();
			companyDept.setCompanyUserId(userid);
			String companyUserName =userInfo.get("USERALIAS")== null ? "" : userInfo.get("USERALIAS").toString(); //陪同人员名称;
			companyDept.setCompanyUserName(companyUserName);
			String companyPosition =userInfo.get("POSTNAME")== null ? "" : userInfo.get("POSTNAME").toString(); //陪同人员名称;
			companyDept.setCompanyPosition(companyPosition);
			String companyDepeId=Rtext.getUUID();
			companyDept.setId(companyDepeId);
			companyDept.setIdeaId(ideaId);
			String userId= userInfoMap.get("userId");
			companyDept.setCreator(userId);
			String createDate=DateUtil.getTime();
			Date createDates = DateUtil.fomatDate(createDate);
			companyDept.setDataCreated(createDates);
			companyDept.setCreateDate(createDates);
			companyDept.setStatus("1");
			yszxMapper.addCompanyDeptInfo(companyDept);
		}
		 
    }
    
}