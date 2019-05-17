package com.sgcc.bg.yszx.controller;  import java.io.IOException;import java.io.Reader;import java.io.UnsupportedEncodingException;import java.net.URLEncoder;import java.sql.Clob;import java.sql.SQLException;import java.util.ArrayList;import java.util.HashMap;import java.util.LinkedHashMap;import java.util.List;import java.util.Map;import javax.servlet.http.HttpServletRequest;import javax.servlet.http.HttpServletResponse;import org.springframework.http.HttpHeaders;import org.springframework.http.MediaType;import org.apache.poi.hssf.usermodel.HSSFWorkbook;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.ResponseEntity;import org.springframework.stereotype.Controller;import org.springframework.web.bind.annotation.RequestBody;import org.springframework.web.bind.annotation.RequestMapping;import org.springframework.web.bind.annotation.RequestMethod;import org.springframework.web.bind.annotation.ResponseBody;import com.alibaba.fastjson.JSON;import com.alibaba.fastjson.serializer.SerializerFeature;import com.sgcc.bg.common.CommonCurrentUser;import com.sgcc.bg.common.Rtext;import com.sgcc.bg.common.UserUtils;import com.sgcc.bg.common.WebUtils;import com.sgcc.bg.common.newExcelUtil;import com.sgcc.bg.yszx.service.IdeaInfoService;import com.sgcc.bg.yszx.service.PrivilegeService;import org.apache.commons.lang3.StringUtils;import org.springframework.http.HttpStatus;/** * @author cyj * 演示中心---数据字典的查询 */@Controller@RequestMapping(value = "IdeaInfo")public class IdeaInfoController {	@Autowired	private IdeaInfoService ideaInfoService;	@Autowired	private WebUtils webUtils;	@Autowired	UserUtils userUtils;	@Autowired	private PrivilegeService privilegeService;	/**	 *  演示中心---信息的预定	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/addIdeaInfo", method = RequestMethod.POST)	public String addIdeaInfo(@RequestBody Map<String, Object> paramsMap){		String  rw=ideaInfoService.addIdeaInfo(paramsMap);		return rw;	}	/**	 *  演示中心---数据字典的查询	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping("/selectForLeader")	public String  selectForLeader(HttpServletRequest request) {		String  rw=ideaInfoService.selectForLeader(request);		return rw;			}	/**	 *  演示中心---根据用户id查询用户信息	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping("/selectForuserName")	public String  selectForuserName(String userId) {		String  rw=ideaInfoService.selectForuserName(userId);		return rw;			}	/**	 * 演示中心---参观预定的查询	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/selectIdeaInfo")	public String selectIdeaInfo(String applyId, String year,  String month, Integer page, Integer limit){		applyId=Rtext.toStringTrim(applyId, "");		year=Rtext.toStringTrim(year, "");		month=Rtext.toStringTrim(month, "");		int start = 0;		int end = 10;		if(page != null && limit!=null&&page>0&&limit>0){						start = (page-1)*limit;			end = page*limit;		}		List<Map<String, Object>> content =  new ArrayList<Map<String, Object>>();		int  countNum = 0;		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());		//获取当前登录人信息		//一期说明： 一个人有一个角色，一个人可以有可以多个部门		//二期说明：待定		String UserId=  currentUser.getUserId();		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX");		if(privUserList.isEmpty()){			content=new ArrayList<Map<String, Object>>();		}else{			String  roleType=String.valueOf(privUserList.get(0).get("roleType"));			 if("0".equals(roleType)){				String type=currentUser.getType();				String deptId="";				if("2".equals(type)){					deptId=currentUser.getpDeptId() ;				}else{					deptId=currentUser.getDeptId();				} //				List<Map<String, Object>> 	deptList=privilegeService.getPrivMgrByUserId(UserId,"YSZX");//				if(deptList.isEmpty()){//					content=new ArrayList<Map<String, Object>>();//					countNum=0;//				}else{//					List<String> deptLists=new ArrayList<String>();//					for (Map<String, Object> map : deptList) {//						String deptId =Rtext.toStringTrim(map.get("DEPT_ID"), "");//						deptLists.add(deptId);//					}//				}				content = ideaInfoService.selectForIdeaInfo(applyId, year+"-"+month,deptId,start,end);  			    countNum= ideaInfoService.selectForideaNum(applyId,year+"-"+month,deptId);			}else if("1".equals(roleType)){				content = ideaInfoService.selectForIdeaInfo(applyId, year+"-"+month,"",start,end);				countNum= ideaInfoService.selectForideaNum(applyId,year+"-"+month,"");			}else{				content=new ArrayList<Map<String, Object>>();				countNum=0;			}		}		 		Map<String, Object> jsonMap1 = new HashMap<String, Object>();		if(content.isEmpty()){			 content   = new  ArrayList<Map<String, Object>>();			 jsonMap1.put("data", content );			 jsonMap1.put("total", countNum);			}else{			 jsonMap1.put("data", content );			 jsonMap1.put("total", countNum);			}		 		Map<String, Object> jsonMap = new HashMap<String, Object>();				jsonMap.put("data", jsonMap1);		jsonMap.put("msg", "success");		jsonMap.put("success", "true");		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",				SerializerFeature.WriteDateUseDateFormat);		return jsonStr; 	}	/**	 *  修改演示中心信息---参观人信息的删除	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/deleteVisitInfo", method = RequestMethod.POST)	public String deleteVisitInfo(String visitId){		String  rw=ideaInfoService.deleteVisitInfo(visitId);		return rw;	} 	/**	 *  修改演示中心信息---参观人信息的删除	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/deleteCompanyUserInfo", method = RequestMethod.POST)	public String deleteCompanyUserInfo(String companyId){		String  rw=ideaInfoService.deleteCompanyUserInfo(companyId);		return rw;	} 	/**	 *  修改演示中心信息---主页信息的删除	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/deleteIdeaInfo", method = RequestMethod.POST)	public String deleteIdeaInfo(String ideaId){		String  rw=ideaInfoService.deleteIdeaInfo(ideaId);		return rw;	} 	/**	 *  修改演示中心信息---主页信息的提交	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/submitForStatus", method = RequestMethod.POST)	public String submitForStatus(String ideaId,String approvalUserd,String status,String approveId){		String  rw=ideaInfoService.submitForStatus(ideaId,approvalUserd ,status,approveId);		return rw;	} 	/**	 *  修改演示中心信息---主页信息的提交	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/repealForStatus", method = RequestMethod.POST)	public String repealForStatus(String ideaId){		String  rw=ideaInfoService.repealForStatus(ideaId);		return rw;	} 	/**	 *  修改演示中心信息---待办信息的查询	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/selectForDealtInfo")	public String selectForDealtInfo(String appltNumber, String applyDept,  String contactUser, Integer page, Integer limit){		appltNumber=Rtext.toStringTrim(appltNumber, "");		applyDept=Rtext.toStringTrim(applyDept, "");		contactUser=Rtext.toStringTrim(contactUser, "");		int start = 0;		int end = 10;		if(page != null && limit!=null&&page>0&&limit>0){						start = (page-1)*limit;			end = page*limit;		}		List<Map<String, Object>>  content=new ArrayList<Map<String, Object>>();		int  countNum=0;		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());		String approveUserId=  currentUser.getUserId();//		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(approveUserId,"YSZX");//		if(privUserList.isEmpty()){//			content=new ArrayList<Map<String, Object>>();//		}else{//			String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;//			 if("0".equals(roleType)){//				String type=currentUser.getType();//				String applyDeptId="";//				if("2".equals(type)){//					 applyDeptId=currentUser.getpDeptId() ;//				}else{//					 applyDeptId=currentUser.getDeptId();//				} //				  content = ideaInfoService.selectForDealtInfo(applyDeptId,approveUserId,appltNumber, applyDept  ,contactUser,start,end);//				  countNum=ideaInfoService.selectForDealtNum(applyDeptId,approveUserId,contactUser,appltNumber,applyDept);//			}else if("1".equals(roleType)){//				  content = ideaInfoService.selectForDealtInfo("",approveUserId,appltNumber, applyDept  ,contactUser,start,end);//				  countNum=ideaInfoService.selectForDealtNum("",approveUserId,contactUser,appltNumber,applyDept);//				//			} else{//				  content=new ArrayList<Map<String, Object>>();//				  countNum=0;//			}//		}	   content = ideaInfoService.selectForDealtInfo("",approveUserId,appltNumber, applyDept  ,contactUser,start,end);	  	   countNum=ideaInfoService.selectForDealtNum("",approveUserId,contactUser,appltNumber,applyDept);			   Map<String, Object> jsonMap1 = new HashMap<String, Object>();		if(content.isEmpty()){			 content   = new  ArrayList<Map<String, Object>>();			 jsonMap1.put("data", content );			 jsonMap1.put("total", 0);			}else{			jsonMap1.put("data", content );			jsonMap1.put("total", countNum);			}		 		Map<String, Object> jsonMap = new HashMap<String, Object>();				jsonMap.put("data", jsonMap1);		jsonMap.put("msg", "success");		jsonMap.put("success", "true");		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",				SerializerFeature.WriteDateUseDateFormat);		return jsonStr;	}	/**	 *  修改演示中心信息---已办信息的查询	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/selectForAlreadytInfo")	public String selectForAlreadytInfo(String appltNumber, String applyDept,  String contactUser, Integer page, Integer limit){		appltNumber=Rtext.toStringTrim(appltNumber, "");		applyDept=Rtext.toStringTrim(applyDept, "");		contactUser=Rtext.toStringTrim(contactUser, "");		int start = 0;		int end = 10;		if(page != null && limit!=null){			start = (page-1)*limit;			end = page*limit;		}		List<Map<String, Object>>   content=new ArrayList<Map<String, Object>>();		 int  countNum=0;		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());		String approveUserId=  currentUser.getUserId();//		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(approveUserId,"YSZX");//		if(privUserList.isEmpty()){//			content=new ArrayList<Map<String, Object>>();//		}else{//			String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;//			if("0".equals(roleType)){//				String type=currentUser.getType();//				String applyDeptId="";//				if("2".equals(type)){//					 applyDeptId=currentUser.getpDeptId() ;//				}else{//					 applyDeptId=currentUser.getDeptId();//				} //				content = ideaInfoService.selectForAlreadytInfo(applyDeptId,approveUserId,appltNumber, applyDept  ,contactUser,start,end);//				countNum=ideaInfoService.selectForAlreadytNum(applyDeptId,approveUserId,contactUser,appltNumber,applyDept);		//			}else if("1".equals(roleType)){ //				content = ideaInfoService.selectForAlreadytInfo("",approveUserId,appltNumber, applyDept  ,contactUser,start,end);//				countNum=ideaInfoService.selectForAlreadytNum("",approveUserId,contactUser,appltNumber,applyDept);//			} else{//				content=new ArrayList<Map<String, Object>>();//				countNum=0;//			}//		}		content = ideaInfoService.selectForAlreadytInfo("",approveUserId,appltNumber, applyDept  ,contactUser,start,end);		countNum=ideaInfoService.selectForAlreadytNum("",approveUserId,contactUser,appltNumber,applyDept);		Map<String, Object> jsonMap1 = new HashMap<String, Object>();		if(content.isEmpty()){			 content   = new  ArrayList<Map<String, Object>>();			 jsonMap1.put("data", content );			 jsonMap1.put("total", 0);			}else{			jsonMap1.put("data", content );						jsonMap1.put("total", countNum);			}		Map<String, Object> jsonMap = new HashMap<String, Object>();		jsonMap.put("data", jsonMap1);		 		jsonMap.put("msg", "success");		jsonMap.put("success", "true");		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",				SerializerFeature.WriteDateUseDateFormat);		return jsonStr;	}	/**	 *  修改演示中心信息---综合信息的查询	 * @param	 * @param	 * @return	 */	@ResponseBody	@RequestMapping(value = "/selectComprehensiveInfo")	public String selectComprehensiveInfo(String applyNumber, String year,String month,String applyDept,String visitUserName,String visitLevel,Integer page, Integer limit){		applyNumber=Rtext.toStringTrim(applyNumber, "");		year=Rtext.toStringTrim(year, "");		month=Rtext.toStringTrim(month, "");		int start = 0;		int end = 30;		if(page != null && limit!=null){			start = (page-1)*limit;			end = page*limit;		}				CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());		String UserId=  currentUser.getUserId();		List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX");		List<Map<String, Object>>  content;		int  countNum = 0;		if(privUserList.isEmpty()){			content=new ArrayList<Map<String, Object>>();		}else{			String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;			if("0".equals(roleType)){				String type=currentUser.getType();				String applyDeptId="";				if("2".equals(type)){					 applyDeptId=currentUser.getpDeptId() ;				}else{					 applyDeptId=currentUser.getDeptId();				} 				 content = ideaInfoService.selectComprehensiveInfo(applyDeptId,applyNumber, year,month ,applyDept,visitUserName,visitLevel,null,start,end);				 countNum= ideaInfoService.selectForComprehensiveNum(applyDeptId,applyNumber, year,month ,applyDept,visitUserName,visitLevel);			}else if("1".equals(roleType)){				 content = ideaInfoService.selectComprehensiveInfo("",applyNumber, year,month ,applyDept,visitUserName,visitLevel,null,start,end);				 countNum= ideaInfoService.selectForComprehensiveNum("",applyNumber, year,month ,applyDept,visitUserName,visitLevel);			}else{				content=new ArrayList<Map<String, Object>>();				countNum=0;			}		}		Map<String, Object> jsonMap1 = new HashMap<String, Object>();		if(content.isEmpty()){			 content   = new  ArrayList<Map<String, Object>>();			 jsonMap1.put("data", content );			 jsonMap1.put("total", countNum);			}else{			jsonMap1.put("data", content );			jsonMap1.put("total", countNum);			}		Map<String, Object> jsonMap = new HashMap<String, Object>();		jsonMap.put("data", jsonMap1);		jsonMap.put("msg", "success");		jsonMap.put("success", "true");		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",				SerializerFeature.WriteDateUseDateFormat);		return jsonStr;	}	/**	 * 综合查询导出功能	 * @param ids	 * @param response	 * @return	 * @throws IOException 	 */	@RequestMapping("/export")	public ResponseEntity<byte[]> export(HttpServletRequest request,HttpServletResponse response) throws IOException{		//构建Excel表头		LinkedHashMap<String,String> headermap = new LinkedHashMap<>(); 		headermap.put("rn", "序号");		headermap.put("applyNumber", "申请单号");		headermap.put("createTime", "审请时间");		headermap.put("applyDept", "审批部门（单位）");		headermap.put("stateDate", "参观开始时间");		headermap.put("endDate", "参观结束时间");		headermap.put("visitUnitName", "参观单位名称");		headermap.put("visitUnitType", "单位性质");		headermap.put("visitInfodata", "主要参观领导姓名、职务、级别");		headermap.put("visitorNumber", "总参观人数");		headermap.put("leaderInfodata", "院内陪同领导");		headermap.put("userInfodata", "院内陪同人员姓名和职务");		headermap.put("companyUserNumber", "总陪同人数");		headermap.put("contactUser", "联系人");		headermap.put("contactPhone", "联系方式");		//获取Excel数据信息		List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();				String ids = request.getParameter("ids")==null?"":request.getParameter("ids");		if(StringUtils.isEmpty(ids)){			String applyNumber = request.getParameter("applyNumber")==null?"":request.getParameter("applyNumber");			String year = request.getParameter("year")==null?"":request.getParameter("year");			String month = request.getParameter("month")==null?"":request.getParameter("month");			String applyDept = request.getParameter("applyDept")==null?"":request.getParameter("applyDept");			String visitUserName = request.getParameter("visitUserName")==null?"":request.getParameter("visitUserName");			String visitLevel = request.getParameter("visitLevel")==null?"":request.getParameter("visitLevel");			 						CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());			String UserId=  currentUser.getUserId();			List<Map<String, Object>> 	privUserList=ideaInfoService.selectForPrivUserId(UserId,"YSZX");			 		 			if(privUserList.isEmpty()){				valueList=new ArrayList<Map<String, Object>>();			}else{				String   roleType=String.valueOf(privUserList.get(0).get("roleType"))  ;				if("0".equals(roleType)){					String type=currentUser.getType();					String applyDeptId="";					if("2".equals(type)){						 applyDeptId=currentUser.getpDeptId() ;					}else{						 applyDeptId=currentUser.getDeptId();					} 					 valueList = ideaInfoService.selectforEXLComprehensiveInfo(applyDeptId,applyNumber, year,month ,applyDept,visitUserName,visitLevel,null);				}else if("1".equals(roleType)){					 valueList = ideaInfoService.selectforEXLComprehensiveInfo("",applyNumber, year,month ,applyDept,visitUserName,visitLevel,null);  				}else{					valueList=new ArrayList<Map<String, Object>>();				}			 } 											  		   System.out.print(valueList.size());		    for(Map<String, Object>  map:valueList){		    	 		    		    	try {		    		String  visit=Rtext.toStringTrim(map.get("visitInfo"), "");		    		String visitInfodata="";		    		if(visit!=""){		    			Clob  visitInfo=(Clob)map.get("visitInfo");						Reader is=visitInfo.getCharacterStream();						char[] c=new char[(int)visitInfo.length()];						is.read(c);						visitInfodata=new String(c);						is.close();		    		}		    							map.put("visitInfodata", visitInfodata);				} catch (SQLException e) {					e.printStackTrace();				}		    			    			    		    	try {		    		String  leader=Rtext.toStringTrim(map.get("leaderInfo"), "");		    		String leaderInfodata="";		    		if(leader!=""){		    			Clob  leaderInfo=(Clob)map.get("leaderInfo");		    			Reader is=leaderInfo.getCharacterStream();						char[] c=new char[(int)leaderInfo.length()];						is.read(c);						leaderInfodata=new String(c);						is.close();							    		}		    		map.put("leaderInfodata", leaderInfodata);				} catch (SQLException e) {					e.printStackTrace();				}		    	try {		    		String  user=Rtext.toStringTrim(map.get("userInfo"), "");		    		String userInfodata="";		    		if(user!=""){		    			Clob  userInfo=(Clob)map.get("userInfo");						Reader is=userInfo.getCharacterStream();						char[] c=new char[(int)userInfo.length()];						is.read(c);						userInfodata=new String(c);						is.close();		    		}		    		map.put("userInfodata", userInfodata);									} catch (SQLException e) {					e.printStackTrace();				}		    			    	 		    			    }		}else{			//拆分id			 			String[] arryids = ids.split(",");			List<String>  idList=new ArrayList<String>();			for (String id : arryids) {				idList.add(id);			}		   valueList = ideaInfoService.selectforEXLComprehensiveInfo("","","","","","","",idList);		   int num=1;		   for(Map<String, Object>map:valueList){			   map.put("rn", num);			   num++;		   }		}		HSSFWorkbook workbook = newExcelUtil.PaddingExcel(headermap,valueList);		HttpHeaders headers = new HttpHeaders();		headers.setContentType(MediaType.valueOf("application/vnd.ms-excel;charset=UTF-8"));		String fileName = "演示中心-综合查询";		try {			fileName = URLEncoder.encode(fileName, "UTF-8");			headers.set("Content-Disposition", "filename="+fileName+".xls");			//设置返回头			response.setHeader("Content-Disposition","filename="+fileName+".xls");			response.setContentType("application/vnd.ms-excel;charset=UTF-8");			workbook.write(response.getOutputStream());			workbook.close();					} catch (UnsupportedEncodingException e1) {			// TODO Auto-generated catch block			e1.printStackTrace();		} catch (IOException e) {			e.printStackTrace();		}				return new ResponseEntity<byte[]>(headers,HttpStatus.OK);	}}