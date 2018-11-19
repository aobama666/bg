package sync;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.service.SyncService;

import config.TestConfig;


public class syncTest {
	public static ApplicationContext ctx;
	public SyncService sync = ctx.getBean(SyncService.class);

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	@Test
	public void sync() {
		System.out.println("------start-----");
		sync.syncErpSyncData();
		sync.syncUserOrganRelationData();
		System.out.println("------end-----");
		/*System.out.println("------start-----");
		StringBuilder sb=new StringBuilder();
		sb.append("1").append("2");
		System.out.println(sb.length());
		System.out.println(sb.deleteCharAt(1).toString());*/
	}
}
