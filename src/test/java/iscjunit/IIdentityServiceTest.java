package iscjunit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sgcc.isc.core.orm.identity.Department;
import com.sgcc.isc.core.orm.identity.User;
import com.sgcc.isc.core.orm.organization.BusinessOrganization;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.framework.common.entity.Paging;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IIdentityService;

public class IIdentityServiceTest {
	IIdentityService identityService = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
	
	@Test
	public void testUserLoginAuth() throws Exception { //1 根据用户名和密码验证用户登录
		User user = identityService.userLoginAuth("epri_chenxi", "000000"); 
		System.out.println("user is -----------------------------------------> "+user);
	}
	
	@Test
	public void testGetUserByIds() throws Exception {//2 根据用户ID集合批量获取用户信息
		List<User> list1 =  identityService.getUserByIds(new String[]{"epri_chenxi"});
		for(User u : list1){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUsersByLoginCode() throws Exception {//3 根据用户登陆名获取用户信息
		List<User> list = identityService.getUsersByLoginCode("epri_weihuafang");
		for(User u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUsersByName() throws Exception {//4 根据用户姓名获取用户信息
		List<User> list = identityService.getUsersByName("陈希");
		for(User u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUserOrgPathByUserId() throws Exception {//5 根据用户Id获取在指定的业务系统下的业务组织路径
		List<BusinessOrganization> list = identityService.getUserOrgPathByUserId("8ad584b4513ea25b01515707df160004", "epri_chenxi");
		for(BusinessOrganization u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUsersByOrgRole() throws Exception {//6 根据业务组织角色标识及用户过滤条件获取业务组织角色下的用户信息
		Map<String,String> map = new HashMap<String, String>();
		List<User> list = identityService.getUsersByOrgRole("8ad584b451628ae9015162b192fd0022",map,new String[]{});
		for(User u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUsersByOrg() throws Exception {//7 根据业务系统ID、业务组织ID及用户过滤条件获取当前业务组织的用户信息
		Map<String,String> map = new HashMap<String, String>();
		List<User> list = identityService.getUsersByOrg("8ad584b4513ea25b01515707df160004","8ad584b4513ea25b015157cabfa30038",map,new String[]{});
		for(User u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetUsers() throws Exception {//8 根据业务应用ID及用户过滤条件分页查询用户信息 
		Map<String,String> map = new HashMap<String, String>();
		Paging paging = identityService.getUsers("8ad584b4513ea25b01515707df160004",map,new String[]{Constants.PARAM_ORDER_USER_NAME+" "+Constants.PARAM_ORDER_ASC},10,1,true);
		List<Map> list = (List<Map>) paging.getData();
		for(Map u : list){
			System.out.println(u);
		}
	}
	
	@Test
	public void testGetSubDepartment() throws Exception {//9 根据基准组织ID、基准组织属性获取当前基准组织下一级基准组织的信息
		Map<String,String> map = new HashMap<String, String>();
		
		List<Department> list = identityService.getSubDepartment("1","");
		for(Department u : list){
			System.out.println(u.getName());
		}
	}
	
	@Test
	public void testGetDepartmentById() throws Exception {//10  取得基准组织所在的单位
		Department department =identityService.getDepartmentById("4200000443");
		System.out.println(department.getName());
		System.out.println(department.getId());
	}
	
	@Test
	public void testgetUsersByConditionAndOrderBy()throws Exception{//15根据条件获取用户信息。此例子为根据用户部门
		//这里是一个Bean自己封装的话可以考虑封装个Bean。
		/*DynamicParam d = new DynamicParam();
		d.setOptr("=");
		d.setType("0");
		DynamicParam d1 = new DynamicParam();
		d1.setValue("");
		d1.setType("1");
		DynamicParam d2 = new DynamicParam();
		d2.setType("1");
		d.setLe(d1);
		d.setRe(d2);*/
		String queryStr="{\"type\" : \"0\",\"optr\" : \"=\",\"le\" : {\"type\" : \"1\",\"value\" : \"baseorgId\"},\"re\" : {\"type\" : \"1\",\"value\" : \"4200000720\"}}";
		List<User> result = identityService.getUsersByConditionAndOrderBy(queryStr, "");
		System.out.println(result);
	}
	

	// 12 13 14 15(getUsersByConditionAndOrderBy) -- IDENTITY 
	
}
