package organstufftree;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.mapper.OrganStuffTreeMapper;
import com.sgcc.bg.mapper.UserInfoMapper;
import com.sgcc.bg.model.UserPrivilege;

import config.TestConfig;

public class UserPrivilegeTest {
	public static ApplicationContext ctx;
	/**
	 * 这个不能为静态方法
	 */
	public UserInfoMapper bean = ctx.getBean(UserInfoMapper.class);
	
	public UserUtils user = ctx.getBean(UserUtils.class);
	
	public OrganStuffTreeMapper organMapper = ctx.getBean(OrganStuffTreeMapper.class);

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	
	@Test
	public void queryPriv() {
		System.out.println("-----------33333333-----");
		UserPrivilege  rlt = user.getUserOrganPrivilegeByUserName("epri_mengj");
		System.out.println(rlt.getUserRoleCode());
		System.out.println(rlt.getOrganForDeptId());
		System.out.println(rlt.getOrganForLabId());
		System.out.println("----------888888-----");
	}
	
	@Test
	public void queryPrivMapper() {
		System.out.println("-----------33333333-----");
		List<Map<String, Object>> rlt = organMapper.getUserOrganPrivilege("41000001","'7282B7DB4FE0B0BCE0536D3C550A861B'","");
		System.out.println(rlt.size());
		//{id=41000001, parentId=, dataType=ORGAN, level=1, childNum=41, organName=中国电力科学研究院}
		for(Map<String, Object> m :rlt){
			System.out.println(m.get("DEPTNAME").toString()+":"+m.get("CHILD_NUM").toString());
		}
		System.out.println("-----------66666666-----");
	}
		
	@Test
	public void query() {
		System.out.println("-----------33333333-----");
		List<Map<String, Object>>  rlt = bean.getUserRoleByUserName("epri_mengj");
		System.out.println("list="+rlt);
		if(rlt!=null&&rlt.size()>0){
			System.out.println("list.size()="+rlt.size());
			for(Map<String, Object> obj:rlt){
				System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【key:"+obj.get("USERNAME")+"：role:"+obj.get("ROLE_CODE")+"】↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
				for(Map.Entry<String,Object> entry:obj.entrySet()){
					System.out.println(entry.getKey()+":"+entry.getValue());
				}
				System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【key:"+obj.get("USERNAME")+"：role:"+obj.get("ROLE_CODE")+"】↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
			}
		}
		
		rlt = bean.getUserOrganPrivByUserName("epri_mengj","1");
		System.out.println("list="+rlt);
		if(rlt!=null&&rlt.size()>0){
			System.out.println("list.size()="+rlt.size());
			for(Map<String, Object> obj:rlt){
				System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓【key:"+obj.get("USERNAME")+"：role:"+obj.get("DEPTNAME")+"】↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
				for(Map.Entry<String,Object> entry:obj.entrySet()){
					System.out.println(entry.getKey()+":"+entry.getValue());
				}
				System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑【key:"+obj.get("USERNAME")+"：role:"+obj.get("DEPTNAME")+"】↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
			}
		}
		System.out.println("-----------66666666-----");
	}
}