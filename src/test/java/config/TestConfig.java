package config;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class TestConfig {

	private static Logger log = Logger.getLogger(TestConfig.class);
	
	/**
	 * 获取springContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext(String contextPath){
		ApplicationContext ctx = null;
		try {
			if(!StringUtils.isBlank(contextPath)){
				ctx = new FileSystemXmlApplicationContext(contextPath);
				log.info("Context URL is : "+contextPath);
			}
		} catch (BeansException e) {
			log.error("Load "+contextPath+" failed!!!");
		}
		if(ctx==null){
			ctx = new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
			log.info("Context URL is : "+"src/main/resources/applicationContext.xml");
		}
		return ctx;
	}	
	
}
