package com.sgcc.bg.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestJob {
	
	private static Logger log =  LoggerFactory.getLogger(TestJob.class);
	
	//测试任务调度
	public void testJob(){
		//System.out.println(ConfigUtils.getConfig("jcgetmenuaddr"));
	}
}
