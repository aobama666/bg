package com.sgcc.bg.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgcc.bg.mapper.ConfigMapper;
import com.sgcc.bg.mapper.OrganStuffTreeMapper;
import com.sgcc.bg.mapper.UserInfoMapper;
import com.sgcc.bg.model.Dept;
import com.sgcc.bg.model.UserPrivilege;
import com.sgcc.bg.model.UserRole;

@Service
public class UserUtils {

	@Autowired
	private ConfigMapper configMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private OrganStuffTreeMapper organStuffTreeMapper;
	
	/**
	 * 根据用户名获取用户所在部门/科室 ID
	 * @param username 用户名
	 * @return
	 */
	public String getDeptIdByUsername(String username){
		String deptid="";
		Map<String,Object> map=configMapper.getDeptByUsername(username);
		if(map!=null&&!map.isEmpty()){
			deptid=map.get("DEPTID").toString();
		}
		return deptid;
	}
	
	/**
	 * 根据用户名获取用户所在部门/科室 名称
	 * @param username 用户名
	 * @return
	 */
	public String getDeptNameByUsername(String username){
		String deptName="";
		Map<String,Object> map=configMapper.getDeptByUsername(username);
		if(map!=null&&!map.isEmpty()){
			deptName=map.get("DEPTNAME").toString();
		}
		return deptName;
	}
	
	/**
	 * 根据用户名获取用户所在部门（电科院下的部门）ID
	 * @param username 用户名
	 * @return
	 */
	public String getPDeptIdByUsername(String username){
		String pDeptid="";
		Map<String,Object> map=configMapper.getPDeptByUsername(username);
		if(map!=null&&!map.isEmpty()){
			pDeptid=map.get("DEPTID").toString();
		}
		return pDeptid;
	}
	
	/**
	 * 根据用户名获取用户所在部门（电科院下的部门）名称
	 * @param username 用户名
	 * @return
	 */
	public String getPDeptNameByUsername(String username){
		String pDeptName="";
		Map<String,Object> map=configMapper.getPDeptByUsername(username);
		if(map!=null&&!map.isEmpty()){
			pDeptName=map.get("DEPTNAME").toString();
		}
		return pDeptName;
	}
	
	/**
	 * 根据用户名获取用户最新信息  
	 * @param username 用户名
	 * @return
	 */
	public CommonCurrentUser getCommonCurrentUserByUsername(String username){
		CommonCurrentUser user = getCommonCurrentUserByUsernameOrHrCode(username, null, "userName",null);
		return user;
	}
	
	/**
	 * 根据用户名获取用户最新信息  
	 * @param username 用户名
	 * @param curDate  为空时，查询当天信息  yyyy-MM-dd
	 * @return
	 */
	public CommonCurrentUser getCommonCurrentUserByUsername(String username,String curDate){
		CommonCurrentUser user = getCommonCurrentUserByUsernameOrHrCode(username, null, "userName",curDate);
		return user;
	}

	/**
	 * 根据用户名获取用户最新信息
	 * @param username 用户名
	 * @param  beginDate 开始时间
	 * @return
	 */
	public CommonCurrentUser getCommonCurrentUserByUsernameScope(String username,String beginDate,String endDate){
		CommonCurrentUser user = getCommonCurrentUserByUsernameOrHrCodeScope(username, null, "userName",beginDate,endDate);
		return user;
	}

	/**
	 * 根据用户名或人资编号获取用户特定时间（时间段）信息
	 * @param userName 用户名
	 * @param hrCode  人资编号
	 * @param type  userName  hrCode
	 * @param
	 * @return
	 */
	private CommonCurrentUser getCommonCurrentUserByUsernameOrHrCodeScope(String userName,String hrCode,String type,String beginDate,String endDate){
		CommonCurrentUser user = null;
		try{
			if(beginDate==null||beginDate.length()==0 || endDate==null || endDate.length()==0){
				return null;
			}

			if(!DateUtil.isValidDate(beginDate, "yyyy-MM-dd")){
				return null;
			}
			if(!DateUtil.isValidDate(endDate, "yyyy-MM-dd")){
				return null;
			}

			Map<String,Object> userMap = null;
			if(type.equals("userName")){
				userMap = userInfoMapper.getCommonCurrentUserByUsernameOrHrCodeScope(userName, null, "userName",beginDate,endDate);
			}else if(type.equals("hrCode")){
				userMap = userInfoMapper.getCommonCurrentUserByUsernameOrHrCodeScope(null, hrCode, "hrCode",beginDate,endDate);
			}
			user = formatCurrentUser(userMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * 根据用户名或人资编号获取用户特定时间信息
	 * @param userName 用户名
	 * @param hrCode  人资编号
	 * @param type  userName  hrCode
	 * @param curDate  为空时，查询当天信息 yyyy-MM-dd
	 * @return
	 */
	private CommonCurrentUser getCommonCurrentUserByUsernameOrHrCode(String userName,String hrCode,String type,String curDate){
		CommonCurrentUser user = null;
		try{
			if(curDate==null||curDate.length()==0){
				curDate = DateUtil.getDay();
			}
			
			if(!DateUtil.isValidDate(curDate, "yyyy-MM-dd")){
				return null;
			}
			
			Map<String,Object> userMap = null;
			if(type.equals("userName")){
				userMap = userInfoMapper.getCommonCurrentUserByUsernameOrHrCode(userName, null, "userName",curDate);
			}else if(type.equals("hrCode")){
				userMap = userInfoMapper.getCommonCurrentUserByUsernameOrHrCode(null, hrCode, "hrCode",curDate);
			}			
			user = formatCurrentUser(userMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * 根据人资编号获取用户最新信息
	 * @param hrCode 人资编号
	 * @return
	 */
	public CommonCurrentUser getCommonCurrentUserByHrCode(String hrCode){
		CommonCurrentUser user = getCommonCurrentUserByUsernameOrHrCode(null, hrCode, "hrCode",null);
		return user;
	}
	
	/**
	 * 根据人资编号获取用户最新信息
	 * @param hrCode 人资编号
	 * @param curDate  为空时，查询当天信息 yyyy-MM-dd
	 * @return
	 */
	public CommonCurrentUser getCommonCurrentUserByHrCode(String hrCode,String curDate){
		CommonCurrentUser user = getCommonCurrentUserByUsernameOrHrCode(null, hrCode, "hrCode",curDate);
		return user;
	}
	
	private CommonCurrentUser formatCurrentUser(Map<String,Object> userMap) {
		CommonCurrentUser user = null;
		try{
			if(userMap!=null){
				user = new CommonCurrentUser();
						
				String userId = userMap.get("USERID")==null?"":userMap.get("USERID").toString();
				String userName = userMap.get("USERNAME")==null?"":userMap.get("USERNAME").toString();
				String hrCode = userMap.get("HRCODE")==null?"":userMap.get("HRCODE").toString();
				String userAlias = userMap.get("USERALIAS")==null?"":userMap.get("USERALIAS").toString();
				String pDeptId = userMap.get("PDEPTID")==null?"":userMap.get("PDEPTID").toString();
				String pDeptName = userMap.get("PDEPTNAME")==null?"":userMap.get("PDEPTNAME").toString();
				String deptId = userMap.get("DEPTID")==null?"":userMap.get("DEPTID").toString();
				String deptName = userMap.get("DEPTNAME")==null?"":userMap.get("DEPTNAME").toString();
				String deptCode = userMap.get("DEPTCODE")==null?"":userMap.get("DEPTCODE").toString();
				String pDeptCode = userMap.get("PDEPTCODE")==null?"":userMap.get("PDEPTCODE").toString();
				String state = userMap.get("STATE")==null?"":userMap.get("STATE").toString();
				String mobile = userMap.get("MOBILE")==null?"":userMap.get("MOBILE").toString();
				String email = userMap.get("EMAIL")==null?"":userMap.get("EMAIL").toString();
				String type = userMap.get("TYPE")==null?"":userMap.get("TYPE").toString();
				
				user = new CommonCurrentUser();
				
				user.setUserId(userId);
				user.setUserName(userName);
				user.setHrCode(hrCode);
				user.setUserAlias(userAlias);
				user.setpDeptId(pDeptId);
				user.setpDeptName(pDeptName);
				user.setDeptId(deptId);
				user.setDeptName(deptName);
				user.setpDeptCode(pDeptCode);
				user.setDeptCode(deptCode);
				user.setState(state);
				user.setMobile(mobile);
				user.setEmail(email);
				user.setType(type);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 根据登陆账号 获取用户管理角色列表  获取用户组织权限列表
	 * @param
	 * @return
	 */
	public UserPrivilege getUserOrganPrivilegeByUserName(String userName){
		UserPrivilege userPriv = null;
		try{
			List<Map<String,Object>> roleList = userInfoMapper.getUserRoleByUserName(userName);
			if(roleList!=null&&roleList.size()>0){
				userPriv = new UserPrivilege();
				
				List<UserRole> userRole = new ArrayList<UserRole>();				
				List<Dept> organForDept = new ArrayList<Dept>();				
				List<Dept> organForLab = new ArrayList<Dept>();	
				
				userPriv.setUserRole(userRole);
				userPriv.setOrganForDept(organForDept);
				userPriv.setOrganForLab(organForLab);
				
				for(Map<String,Object> map:roleList){					
					String roleCode = map.get("ROLE_CODE")==null?"":map.get("ROLE_CODE").toString();
					String roleName = map.get("ROLE_NAME")==null?"":map.get("ROLE_NAME").toString();
					
					UserRole role = new UserRole();
					role.setRoleCode(roleCode);
					role.setRoleName(roleName);
					userRole.add(role);
					/*  
					 	用户角色列表   MANAGER_UNIT,MANAGER_DEPT,MANAGER_LAB
						 院专责	MANAGER_UNIT
						 部门专责	MANAGER_DEPT
						 处室专责	MANAGER_LAB
					 */
					if(roleCode.equals("MANAGER_DEPT")){
						String roleType = "1";
						List<Map<String,Object>> organList = userInfoMapper.getUserOrganPrivByUserName(userName, roleType);
						if(organList!=null&&organList.size()>0){
							for(Map<String,Object> o:organList){
								String deptId = o.get("DEPTID")==null?"":o.get("DEPTID").toString();
								String deptName = o.get("DEPTNAME")==null?"":o.get("DEPTNAME").toString();
								String deptCode = o.get("DEPTCODE")==null?"":o.get("DEPTCODE").toString();
								String pDeptId = o.get("PDEPTID")==null?"":o.get("PDEPTID").toString();
								String pDeptName = o.get("PDEPTNAME")==null?"":o.get("PDEPTNAME").toString();
								String pDeptCode = o.get("PDEPTCODE")==null?"":o.get("PDEPTCODE").toString();
								
								Dept dept = new Dept();
								dept.setDeptid(deptId);
								dept.setDeptname(deptName);
								dept.setOrganid(deptCode);
								dept.setPdeptid(pDeptId);
								dept.setPdeptname(pDeptName);
								dept.setPorganid(pDeptCode);
								
								organForDept.add(dept);
							}
						}
					}
					else if(roleCode.equals("MANAGER_LAB")){
						String roleType = "2";
						List<Map<String,Object>> organList = userInfoMapper.getUserOrganPrivByUserName(userName, roleType);
						if(organList!=null&&organList.size()>0){
							for(Map<String,Object> o:organList){
								String deptId = o.get("DEPTID")==null?"":o.get("DEPTID").toString();
								String deptName = o.get("DEPTNAME")==null?"":o.get("DEPTNAME").toString();
								String deptCode = o.get("DEPTCODE")==null?"":o.get("DEPTCODE").toString();
 								String pDeptId = o.get("PDEPTID")==null?"":o.get("PDEPTID").toString();
								String pDeptName = o.get("PDEPTNAME")==null?"":o.get("PDEPTNAME").toString();
								String pDeptCode = o.get("PDEPTCODE")==null?"":o.get("PDEPTCODE").toString();
 
								
								Dept dept = new Dept();
								dept.setDeptid(deptId);
								dept.setDeptname(deptName);
								dept.setOrganid(deptCode);
								dept.setPdeptid(pDeptId);
								dept.setPdeptname(pDeptName);
								dept.setPorganid(pDeptCode);
								
								organForLab.add(dept);
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return userPriv;
	}
	/**
	 * 根据登陆账号 获取用户管理角色列表  获取用户组织权限列表  新 2019-08-21
	 * @param userName 用户账号
	 * @param funcName 功能名称
	 * @param funcType 功能类型  0 管理类  1 审批类
	 * @return
	 */
	public UserPrivilege getNewUserOrganPrivilegeByUserName(String userName,String funcName,String funcType){
		UserPrivilege userPriv = null;
		try{
			List<Map<String,Object>> roleList = userInfoMapper.getNewUserRoleByUserName(userName,funcName,funcType);
			if(roleList!=null&&roleList.size()>0){
				userPriv = new UserPrivilege();
				
				List<UserRole> userRole = new ArrayList<UserRole>();				
				
				userPriv.setUserRole(userRole);
				
				for(Map<String,Object> map:roleList){	
					String roleCode = map.get("ROLE_CODE")==null?"":map.get("ROLE_CODE").toString();
					String roleName = map.get("ROLE_NAME")==null?"":map.get("ROLE_NAME").toString();
					String roleType = map.get("ROLE_TYPE")==null?"":map.get("ROLE_TYPE").toString();
					String roleStatus = map.get("ROLE_STATUS")==null?"":map.get("ROLE_STATUS").toString();
					String functionType = map.get("FUNCTION_TYPE")==null?"":map.get("FUNCTION_TYPE").toString();
					
					UserRole role = new UserRole();
					role.setRoleCode(roleCode);
					role.setRoleName(roleName);
					role.setRoleType(roleType);
					role.setRoleStatus(roleStatus);
					role.setFunctionType(functionType);
					
					userRole.add(role);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return userPriv;
	}
	/**
	 * 根据登陆账号 获取用户管理角色列表  获取用户组织权限列表
	 * @param
	 * @return
	 */
	public UserPrivilege getUserOrganPrivilegeByUserNames(String userName){
		UserPrivilege userPriv = null;
		try{
			String rootId="41000001";
			UserPrivilege  userPrivilege=getUserOrganPrivilegeByUserName(userName);
			List<UserRole>  UserRoleList=userPrivilege.getUserRole();
			 
			 /**
			  * 部门 
			  * **/
			 List<Dept> Deptlist=userPrivilege.getOrganForDept();
			
			 /**
			  * 处室 
			  * */
			 List<Dept> Lablist=userPrivilege.getOrganForLab();
			  
			 /**
			  * 纬度---》部门  
			  * */
			 List<Dept> organForDept = new ArrayList<Dept>();
			 if(!Deptlist.isEmpty()){
				 for(Dept dept:Deptlist){
					 String deptPriv= dept.getDeptid();
					 List<Map<String, Object>>  list = organStuffTreeMapper.getUserOrganPrivilege(rootId, deptPriv, "");
					 if(!list.isEmpty()){
						 for(int j=0;j<list.size();j++){
							    Dept depts = new Dept();
							    String Deptid=(String) list.get(j).get("DEPTID");
							    String PDeptid=(String) list.get(j).get("PDEPTID");
							    String DeptName=(String) list.get(j).get("DEPTNAME");
							    String PDeptName=(String) list.get(j).get("PDEPTNAME");
								depts.setDeptid(Deptid);
								depts.setDeptname(DeptName);
								depts.setPdeptid(PDeptid);
								depts.setPdeptname(PDeptName);
								organForDept.add(depts);
								 
						 }
					 }
				 }
			 } 
			 if(!Lablist.isEmpty()){
				 for(Dept lab:Lablist){
					 String labPriv= lab.getDeptid();
					 List<Map<String, Object>>  list = organStuffTreeMapper.getUserOrganPrivilege(rootId, "", labPriv);
                     if(!list.isEmpty()){
                         for(int j=0;j<list.size();j++){
        							    Dept depts = new Dept();
        							    String Deptid=(String) list.get(j).get("DEPTID");
        							    String PDeptid=(String) list.get(j).get("PDEPTID");
        							    String DeptName=(String) list.get(j).get("DEPTNAME");
        							    String PDeptName=(String) list.get(j).get("PDEPTNAME");
        								depts.setDeptid(Deptid);
        								depts.setDeptname(DeptName);
        								depts.setPdeptid(PDeptid);
        								depts.setPdeptname(PDeptName);  
        								organForDept.add(depts);
        					 }
					 }
				 }
			 } 
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		return userPriv;
	}	
}
