package com.sgcc.bg.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sgcc.isc.core.orm.complex.DynamicParam;
import com.sgcc.isc.core.orm.identity.Department;
import com.sgcc.isc.core.orm.identity.User;
import com.sgcc.isc.core.orm.role.OrganizationalRole;
import com.sgcc.isc.core.orm.role.Role;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IIdentityService;
import com.sgcc.isc.service.adapter.helper.IRoleService;

/**
 * 获取ISC用户信息
 * 
 * @author epri-xpjt
 *
 */
public class GetISCUserUtils {

	/**
	 * 根据用户名获取ISC用户信息
	 * 
	 * @param username
	 * @return
	 */
	public static User getUserByUsername(String username) {
		IIdentityService identityService = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
		try {
			List<User> list = identityService.getUserByIds(new String[] { username });
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new User();
	}

	/**
	 * 根据部门id获取上级单位id
	 * 
	 * @param orgId
	 * @return
	 */
	public static Department getPOrgByOrgid(String orgId) {
		IIdentityService identityService = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
		try {
			DynamicParam d = new DynamicParam();
			d.setOptr("=");
			d.setType("0");
			DynamicParam d1 = new DynamicParam();
			d1.setValue("id");
			d1.setType("1");
			DynamicParam d2 = new DynamicParam();
			d2.setValue(orgId);
			d2.setType("1");
			d.setLe(d1);
			d.setRe(d2);
			String str = JSONObject.toJSONString(d);
			List<Department> list = identityService.getQuoteDepartmentsByConditionAndOrderBy(str, "");
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Department();
	}
	
	/**
	 * 根据用户名获取用户ISC单位信息（电科院下一级单位）
	 * @param userId
	 * @return
	 * orgId 单位ID
	 * orgName 单位名称
	 */
	public static Map<String, String> getCompanyInfoByUserId(String userId) {
		if(Rtext.isEmpty(userId)){
			return new HashMap<String, String>();
		}
		Map<String, String> deptMap = getDeptInfoByuserId(userId);

		if ("YJS".equals(deptMap.get("orgType")) || "YJY".equals(deptMap.get("orgType"))) {
			return deptMap;
		} else {
			return getBaseOrgInfoById(deptMap.get("orgPid"));
		}
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @description 根据用户id获得业务角色信息
	 * @param userId
	 * @return
	 * @throws Exception
	 * @author wanglongjiao
	 * @date 2014-2-19
	 * @see com.sgcc.erd.common.isc.IIscManageCommon#getRoleByUserId(java.lang.String)
	 */
	public static List<Role> getRoleByUserId(String userId) {
		List<Role> list = new ArrayList<Role>();

		// 获取业务组织角色
		List<OrganizationalRole> orgRoleList = getOrgRolesByUserId(userId, null);

		// 获取对应角色
		if (orgRoleList != null && orgRoleList.size() > 0) {
			List<Role> tmplist = null;
			for (OrganizationalRole curOrgRole : orgRoleList) {
				try {
					tmplist = getIRoleService().getRoleByRoleId(
							String.valueOf(curOrgRole.getRoleId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (tmplist != null && tmplist.size() > 0) {
					// getRoleByRoleId只能获取一个角色对象，不知为啥非要用List，在此只取列表第一个
					if (!list.contains(tmplist.get(0))) {
						list.add(tmplist.get(0));
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * @说明：根据组织ID与类型查其下一级组织
	 * @版本：V1.0
	 */
	//已修改
	public static List<Map<String, String>> getsubBaseOrgByIdAndType(String orgId,String type) {
		IIdentityService orgInfo = AdapterFactory.getIdentityService();
		List<Department> list = new ArrayList<Department>();
		List<Map<String, String>> subOrgList = new ArrayList<Map<String, String>>();//返回结果列表
		String deptId = "1";
		if (orgId != null && orgId.trim().length() > 0 && !"null".equals(orgId)) {
			deptId = orgId;
		} 
		try {
			list = orgInfo.getSubDepartment(deptId, "");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int m=0; m<list.size();m++){
			Department deptInfo = list.get(m);
			//根据funcType字段过滤
			if (null != type && type.trim().length() > 0 && !type.equals(deptInfo.getFuncType())) {
				continue;
			}
			Map<String, String> baseOrgMap = new HashMap<String, String>();
			baseOrgMap.put("orgId", deptInfo.getId());
			baseOrgMap.put("orgName",deptInfo.getName());
			baseOrgMap.put("orgPid",deptInfo.getParentId());
			baseOrgMap.put("orgType",deptInfo.getFuncType());
			subOrgList.add(baseOrgMap);
		}
		return subOrgList;
	}

	/***
	 * 改为通过ISC接口实现该方法功能
	 * 
	 */
	private static Map<String, String> getDeptInfoByuserId(String userId) {
		Map<String, String> deptMap = new HashMap<String, String>();
		deptMap.put("orgId", "");
		deptMap.put("orgName", "");
		deptMap.put("orgPid", "");
		deptMap.put("orgType", "");
		try {
			User userInfo = getUserInfoByIsc("id", userId);
			if (null == userInfo) {
				return deptMap;
			} else {
				Department deptInfo = getOrgInfoByIsc(userInfo.getBaseOrgId());
				if (null == deptInfo) {
					return deptMap;
				} else {
					deptMap.put("orgId", deptInfo.getId());
					deptMap.put("orgName", deptInfo.getName());
					deptMap.put("orgPid", deptInfo.getParentId());
					deptMap.put("orgType", deptInfo.getFuncType());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return deptMap;
	}

	/**
	 * 通过ISC接口实现根据ID查询人员信息
	 * 
	 * @return
	 * @throws Exception
	 */
	private static User getUserInfoByIsc(String paramName, String paramValue) throws Exception {
		User userInfo = null;
		if (null == paramValue || "".equals(paramValue)) {
			return userInfo;
		}
		IIdentityService userService = AdapterFactory.getIdentityService();
		DynamicParam d = new DynamicParam();
		d.setOptr("=");
		d.setType("0");
		DynamicParam d1 = new DynamicParam();
		d1.setValue(paramName);
		d1.setType("1");
		DynamicParam d2 = new DynamicParam();
		d2.setValue(paramValue);
		d2.setType("1");
		d.setLe(d1);
		d.setRe(d2);
		String condition = JSONObject.toJSONString(d);
		List<User> userList = userService.getUsersByConditionAndOrderBy(condition, "");
		if (userList.size() > 0) {
			userInfo = userList.get(0);
		}
		return userInfo;
	}

	/**
	 * 通过ISC接口实现根据ID查询基准组织信息
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Department getOrgInfoByIsc(String orgId) throws Exception {
		if (null == orgId || "".equals(orgId)) {
			return null;
		}
		IIdentityService orgInfo = AdapterFactory.getIdentityService();
		DynamicParam d = new DynamicParam();
		d.setOptr("=");
		d.setType("0");
		DynamicParam d1 = new DynamicParam();
		d1.setValue("id");
		d1.setType("1");
		DynamicParam d2 = new DynamicParam();
		d2.setValue(orgId);
		d2.setType("1");
		d.setLe(d1);
		d.setRe(d2);
		String condition = JSONObject.toJSONString(d);
		List<Department> orgList = orgInfo.getQuoteDepartmentsByConditionAndOrderBy(condition, "");
		Department deptInfo = null;
		if (orgList.size() > 0) {
			deptInfo = orgList.get(0);

		}
		return deptInfo;
	}

	/**
	 * @说明：根据单位Id查询单位信息
	 */
	private static Map<String, String> getBaseOrgInfoById(String orgId) {
		Map<String, String> baseOrgMap = new HashMap<String, String>();
		baseOrgMap.put("orgId", "");
		baseOrgMap.put("orgName", "");
		baseOrgMap.put("orgPid", "");
		baseOrgMap.put("orgType", "");
		Department orgInfo = null;
		try {
			orgInfo = getOrgInfoByIsc(orgId);
			if (null == orgInfo) {
				return baseOrgMap;
			}
			baseOrgMap.put("orgId", orgInfo.getId());
			baseOrgMap.put("orgName", orgInfo.getName());
			baseOrgMap.put("orgPid", orgInfo.getParentId());
			baseOrgMap.put("orgType", orgInfo.getFuncType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseOrgMap;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @description 根据用户ID及组织结构角色信息获取组织结构角色信息。
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 * @author wanglongjiao
	 * @date 2014-2-19
	 * @see com.sgcc.erd.common.isc.IIscManageCommon#getOrgRolesByUserId(java.lang.String,
	 *      java.util.Map)
	 */
	private static List<OrganizationalRole> getOrgRolesByUserId(String userId,
			Map<String, String> param) {
		String ISC_APPID="8ad584b4513ea25b01515707df160004";
		List<OrganizationalRole> list = null;
		try {
			list = getIRoleService().getOrgRolesByUserId(userId, ISC_APPID,
					param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 
	 * @说明：获得角色服务接口
	 * 
	 * @参数：
	 * 
	 * @返回值：IRoleService
	 * @日期：2014-5-6 上午10:41:22
	 * @作者：zhanggk
	 * @版本：V1.0
	 */
	private static IRoleService getIRoleService() {
		return AdapterFactory.getRoleService();
	}
}
