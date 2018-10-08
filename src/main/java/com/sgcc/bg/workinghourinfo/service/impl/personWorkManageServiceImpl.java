package com.sgcc.bg.workinghourinfo.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.mapper.BgWorkinghourInfoMapper;
import com.sgcc.bg.service.IStaffWorkingHourManageService;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
import com.sgcc.bg.workinghourinfo.service.personWorkManageService;
 
 
 @Service 
 public class personWorkManageServiceImpl implements personWorkManageService {
	 public Logger log = LoggerFactory.getLogger(personWorkManageServiceImpl.class); 
	 ResultWarp rw; 
		 @Autowired 
		 private BgWorkinghourInfoMapper bgworkinghourinfoMapper; 
		 @Autowired 
		 private  WebUtils webUtils;
	     @Autowired
		 UserUtils userUtils;
	     @Autowired
	 	 private IStaffWorkingHourManageService smService;
		   /** 
			* 个人工时管理-----查询
	 		* @param 
	 		* @param 
	 		* @return 
			* */ 
			 @Override 
			 public String selectForbgWorkinghourInfo(HttpServletRequest request){
			 log.info("[bgWorkinghourInfo]: 个人工时管理" );
			 CommonUser userInfo = webUtils.getCommonUser();
			 String userName = userInfo.getUserName();
			 int pageNum=Integer.parseInt(request.getParameter("page")); 
			 int limit=Integer.parseInt(request.getParameter("limit")); 
			 Page<?> page=PageHelper.startPage(pageNum,limit); 
			 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
			 String StartData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); 
			 String EndData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); 
			 String worker = userName;
			 String category = request.getParameter("category" ) == null ? "" : request.getParameter("category").toString(); 
			 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); 
			 String status = request.getParameter("status" ) == null ? "" : request.getParameter("status").toString(); 
			 bgworkinghourinfoMapper.selectForbgWorkinghourInfo(id,StartData,EndData,worker,category,projectName,status);   
			 long total=page.getTotal(); 
			 @SuppressWarnings("unchecked")
			    List<Map<String,String>> dataList=(List<Map<String, String>>) page.getResult(); 
				Map<String, Object> map = new HashMap<String, Object>();	
			    map.put("items", dataList);
				map.put("totalCount", total);
				String jsonStr=JSON.toJSONStringWithDateFormat(map,"yyyy-MM-dd",SerializerFeature.WriteDateUseDateFormat);
				return jsonStr;
			  }
			 /** 
			   *  个人工时管理修改页面-----保存
		 	   * @param 
		 	   * @param 
		 	   * @return 
			   * */ 
			@Override 
			public String updatabgWorkinghourInfo(HttpServletRequest request){
				 log.info("[bgWorkinghourInfo]: 保存" );
				 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
				 String projectName = request.getParameter("proName" ) == null ? "" : request.getParameter("proName").toString(); 
				 String jobContent = request.getParameter("workContent" ) == null ? "" : request.getParameter("workContent").toString(); 
				 String workingHour = request.getParameter("hours" ) == null ? "" : request.getParameter("hours").toString(); 
				 /**
				  * 验证
				  * */
				 if(jobContent.length()>200){
					rw = new ResultWarp(ResultWarp.FAILED ,"工作内容超出最大200长度限制！"); 
					return JSON.toJSONString(rw);  
				   }
				 
				 
				 boolean flag= DataUtil1.Number(workingHour);
				    if(!flag){
						 rw = new ResultWarp(ResultWarp.FAILED ,"投入工时必须为数字"); 
						 return JSON.toJSONString(rw);  
					}
				 if(projectName.length()>50){
						rw = new ResultWarp(ResultWarp.FAILED ,"项目名称超出最大50长度限制！"); 
						return JSON.toJSONString(rw);  
				 }
				 int res=bgworkinghourinfoMapper.updatabgWorkinghourInfo(id,projectName,jobContent,workingHour);   
				 if(res>0){ 
				  rw = new ResultWarp(ResultWarp.SUCCESS ,"保存成功"); 
				  }else{
				  rw = new ResultWarp(ResultWarp.FAILED ,"保存失败");
				  }
				   return JSON.toJSONString(rw);  
			}
			 /** 
			   *  个人工时管理修改页面-----提交
		 	   * @param 
		 	   * @param 
		 	   * @return 
			   * */  
				 @Override 
				 public String commitbgWorkinghourInfo(HttpServletRequest request){
				 log.info("[bgWorkinghourInfo]: 提交 " );
				 CommonUser userInfo = webUtils.getCommonUser();
				 String userName = userInfo.getUserName();
				 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
				 String projectName = request.getParameter("proName" ) == null ? "" : request.getParameter("proName").toString(); 
				 String jobContent = request.getParameter("workContent" ) == null ? "" : request.getParameter("workContent").toString(); 
				 String workingHour = request.getParameter("hours" ) == null ? "" : request.getParameter("hours").toString(); 
				 String date = request.getParameter("time" ) == null ? "" : request.getParameter("time").toString(); 
				 /**
				  * 验证
				  * */
				 if(jobContent.length()>200){
					rw = new ResultWarp(ResultWarp.FAILED ,"工作内容超出最大200长度限制！"); 
					return JSON.toJSONString(rw);  
				   }
				 
				 double todayHours;
				 boolean flag= DataUtil1.Number(workingHour);
				    if(!flag){
						 rw = new ResultWarp(ResultWarp.FAILED ,"投入工时必须为数字"); 
						 return JSON.toJSONString(rw);  
					}
				 try {
					todayHours=Double.parseDouble(workingHour);
				 } catch (Exception e) {
				     rw = new ResultWarp(ResultWarp.FAILED ,"投入工时转换异常"); 
					 return JSON.toJSONString(rw);  	 
				 }	
				 String checkResult="";
				 //校验当天工时是否超标
				 checkResult=smService.checkWorkHour(userName,date,todayHours);
				 if (!"".equals(checkResult)) {
					  rw = new ResultWarp(ResultWarp.FAILED ,checkResult); 
					  return JSON.toJSONString(rw);  	 
				 } 
				 if(projectName.length()>50){
						rw = new ResultWarp(ResultWarp.FAILED ,"项目名称超出最大50长度限制！"); 
						return JSON.toJSONString(rw);  
				 }
				 int res=bgworkinghourinfoMapper.commitbgWorkinghourInfo(id,projectName,jobContent,workingHour);   
				 if(res>0){ 
				  rw = new ResultWarp(ResultWarp.SUCCESS ,"提交成功"); 
				  }else{
				  rw = new ResultWarp(ResultWarp.FAILED ,"提交失败");
				  }
				   return JSON.toJSONString(rw);  
				}
			 /** 
				*  个人工时管理--删除
		 		* @param 
		 		* @param 
		 		* @return 
				* */ 
			@Override 
			public String deletebgWorkinghourInfo(HttpServletRequest request){
				 log.info("[bgWorkinghourInfo]: 删除" );
				 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
				 /**
				  * 验证
				  * */
				 if(id==""){
					  rw = new ResultWarp(ResultWarp.FAILED ,"ID不能为空"); 
					  return JSON.toJSONString(rw);  
				 }
				 String valid="0";
				 int res=bgworkinghourinfoMapper.deletebgWorkinghourInfo(id,valid);   
				 if(res>0){ 
				  rw = new ResultWarp(ResultWarp.SUCCESS ,"删除成功"); 
				  }else{
				  rw = new ResultWarp(ResultWarp.FAILED ,"删除失败");
				  }
				  return JSON.toJSONString(rw);  
			  }
			  /** 
			   * 个人工时管理--撤回
			   * @param 
			   * @param 
			   * @return 
		       * */ 
			   @Override 
			   public String backbgWorkinghourInfo(HttpServletRequest request){
					 log.info("[bgWorkinghourInfo]: 撤回" );
					 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
					 /**
					  * 验证
					  * */
					 if(id==""){
						  rw = new ResultWarp(ResultWarp.FAILED ,"ID不能为空"); 
						  return JSON.toJSONString(rw);  
					 }
					 String status="0";
					 int res=bgworkinghourinfoMapper.backbgWorkinghourInfo(id,status);   
					 if(res>0){ 
					  rw = new ResultWarp(ResultWarp.SUCCESS ,"撤回成功"); 
					  }else{
					  rw = new ResultWarp(ResultWarp.FAILED ,"撤回失败");
					  }
					 return JSON.toJSONString(rw);  
				}
					
			   /** 
				* 提交
		 		* @param 
		 		* @param 
		 		* @return 
				* */ 
				 @Override 
				 public String savebgWorkinghourInfo(HttpServletRequest request){
				 log.info("[bgWorkinghourInfo]: 提交" );
				 CommonUser userInfo = webUtils.getCommonUser();
				 String userName = userInfo.getUserName();	 
				 String id = request.getParameter("id") == null ? "" : request.getParameter("id").toString(); 
				 String xh = request.getParameter("xh") == null ? "" : request.getParameter("xh").toString(); 
				 List<String > idlist=new ArrayList<>();
					if(id!=""){
						String[]  ids=id.split(",");
						for(int i=0;i<ids.length;i++){
							idlist.add(ids[i]);
						}
					} 	
			 List<Map<String, Object>> CheckHouslist= bgworkinghourinfoMapper.selectCheckHous(idlist);
				 if(!CheckHouslist.isEmpty()){
					 for(Map<String, Object> map:CheckHouslist){
							String worktime= (String) map.get("WORK_TIME");
							String workhour= (String) map.get("WORKING_HOUR").toString();
							double workhours=Double.valueOf(workhour);
							if(workhours>11){
								 rw = new ResultWarp(ResultWarp.FAILED ,worktime+"工时超过11个小时"); 
						    	 return JSON.toJSONString(rw);  
							}
							 List<Map<String, Object>> Houslist=bgworkinghourinfoMapper.selectCheckWorker(userName,worktime);
							 if(!Houslist.isEmpty()){
								 double workHours=0;
								 for(Map<String, Object> maps:Houslist){
										String workHour = (String) maps.get("WORKING_HOUR").toString();
										  workHours +=Double.valueOf(workHour);	
						         }
									double zone=workHours+workhours;
									if(zone>11){
										 rw = new ResultWarp(ResultWarp.FAILED ,worktime+"工时超过11个小时"); 
								    	 return JSON.toJSONString(rw);  
									}
								 
				         }
			         }
				 }	 
				 String status="1";
				 String[] idnum=id.split(",");
				 String[] xhnum=xh.split(",");
				 int res = 0;
				 int count=0;
				 int nocount=0;
					for(int i=0;i<idnum.length;i++){
						String ids=idnum[i];
						String xhs=xhnum[i];
						List<Map<String , Object>> map=bgworkinghourinfoMapper.selectCheckTime(ids,"","","","");
						if(!map.isEmpty()){
							String  worktime=(String) map.get(0).get("WORK_TIME");
							String  workhour=(String) map.get(0).get("WORKING_HOUR").toString();
							double workhours=Double.valueOf(workhour);
							//校验当天工时是否超标
							 String checkResult="";
							 //校验当天工时是否超标
							 checkResult=smService.checkWorkHour(userName,worktime,workhours);
								
							 if (!"".equals(checkResult)) {
								  rw = new ResultWarp(ResultWarp.FAILED ,checkResult); 
								  return JSON.toJSONString(rw);  	 
							 } 
						}
						@SuppressWarnings("rawtypes")
						List<Map> list=bgworkinghourinfoMapper.selectForKilebgWorkinghourInfo(ids);   
					     if(list.size()==0){
					    	 rw = new ResultWarp(ResultWarp.FAILED ,"该数据不存在"); 
					    	 return JSON.toJSONString(rw);  
					     }
					     String statuss=(String) list.get(0).get("STATUS");
					     if(!statuss.equals("0")||!statuss.equals("2")){
					    	 rw = new ResultWarp(ResultWarp.FAILED ,"序号为 "+xhs+"行 已提交或已通过,请勿重复提交！"); 
					     }
						 res=bgworkinghourinfoMapper.backbgWorkinghourInfo(ids,status);
						 if(res==1){
							 count++;
						 }else{
							 nocount++;
						 }
					}
				 if(count>0){ 
				      rw = new ResultWarp(ResultWarp.SUCCESS ,"提交成功"+count+"条,失败"+nocount+"条 。"); 
				  }else{
					  rw = new ResultWarp(ResultWarp.SUCCESS ,"提交成功"+count+"条,失败"+nocount+"条 。"); 
				  }
				 return JSON.toJSONString(rw);  
			}
			 

				 
										 
		 /** 
		   * 个人工时管理-----导出 
		   * @param 
		   * @param 
		   * @return 
		   * */ 
		 @Override 
		 public void  exportExcelForpersonWorkManage(HttpServletRequest request,HttpServletResponse response){
				 log.info("[bgWorkinghourInfo]: 个人工时管理" );
				 CommonUser userInfo = webUtils.getCommonUser();
				 String userName = userInfo.getUserName();	 
				 String id = request.getParameter("id" ) == null ? "" : request.getParameter("id").toString(); 
				 String StartData = request.getParameter("startTime" ) == null ? "" : request.getParameter("startTime").toString(); 
				 String EndData = request.getParameter("endTime" ) == null ? "" : request.getParameter("endTime").toString(); 
				 String worker = userName;
				 String category = request.getParameter("category" ) == null ? "" : request.getParameter("category").toString(); 
				 String projectName = request.getParameter("projectName" ) == null ? "" : request.getParameter("projectName").toString(); 
				 String status = request.getParameter("status" ) == null ? "" : request.getParameter("status").toString(); 
				 createExcelForpersonWorkManage(id,StartData,EndData,worker,category,projectName,status, response);			 
			 
		}
		/**
		 * 个人工时管理的导出 
		 * */	 
		 public String createExcelForpersonWorkManage(String id,String StartData,String EndData,
				 String worker,String category,String projectName,String status, HttpServletResponse response){
				//构建Excel表头
						 Object[][] title = { 
								 { "日期", "WORK_TIME" }, 
								 { "类型","CATEGORY"},
								 { "项目名称","PROJECT_NAME"},
								 { "工作内容","JOB_CONTENT"},
								 { "投入工时(h)","WORKING_HOUR"},
								 { "审核人","USERALIAS"},
								 { "审核结果","STATUS"},
								 { "审核备注","PROCESS_NOTE"},
								};
						List<String > idlist=new ArrayList<>();
						if(id!=""){
							String[]  ids=id.split(",");
							for(int i=0;i<ids.length;i++){
								idlist.add(ids[i]);
							}
						} 
						List<Map<String, Object>>  datalist ;
						if(idlist.isEmpty()){
						  datalist= bgworkinghourinfoMapper.selectForbgWorkinghourInfos(null,StartData,EndData,worker,category,projectName,status);				  
						}else{
						  datalist= bgworkinghourinfoMapper.selectForbgWorkinghourInfos(idlist,"","","","","","");				  
						}
						String excelName="个人工时管理-"+DateUtil.getDays();
						ExportExcelHelper.getExcel(response, excelName, title, datalist, "normal"); 
				return "";
		}
		 
		 
		 
		 
		 
	}
