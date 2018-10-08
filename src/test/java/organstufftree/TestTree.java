package organstufftree;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.service.OrganStuffTreeService;

import config.TestConfig;


public class TestTree {
	public static ApplicationContext ctx;
	/**
	 * 这个不能为静态方法
	 */
	public OrganStuffTreeService organStuffTreeService = ctx.getBean(OrganStuffTreeService.class);

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	@Test
	public void query() {
		System.out.println("-----------33333333-----");
//		List<Map<String, Object>> rlt = organStuffTreeService.queryAllOrganTree("41000001","0","");
//		System.out.println(rlt.size());
//		//{id=41000001, parentId=, dataType=ORGAN, level=1, childNum=41, organName=中国电力科学研究院}
//		for(Map<String, Object> m :rlt){
//			System.out.println(m.get("organName").toString());
//		}
		
		List<Map<String, Object>> rlt = organStuffTreeService.queryDeptByCurrentUserPriv("41000001","2","epri_gukai");
		for(Map<String, Object> m :rlt){
			System.out.println(m.get("organName").toString());
		}
		System.out.println("-----------66666666-----");
	}
}