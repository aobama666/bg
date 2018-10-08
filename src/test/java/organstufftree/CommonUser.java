package organstufftree;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.UserUtils;

import config.TestConfig;


public class CommonUser {
	public static ApplicationContext ctx;
	/**
	 * 这个不能为静态方法
	 */
	public UserUtils userUtils = ctx.getBean(UserUtils.class);

	@BeforeClass
	public static void initConfig(){
		ctx=TestConfig.getApplicationContext("src/main/resources/applicationContext.xml");
	}
	@Test
	public void query() {
		System.out.println("-----------33333333-----");
//		CommonCurrentUser rlt = userUtils.getCommonCurrentUserByUsername("epri_xukaijin");
		CommonCurrentUser rlt = userUtils.getCommonCurrentUserByHrCode("41590634","2018-08-31");
		System.out.println(rlt);
		System.out.println("-----------66666666-----");
	}
}