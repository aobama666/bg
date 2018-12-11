package com.sgcc.bg.job;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.service.HandleSyncService;

import ch.qos.logback.classic.Logger;


@Service
public class HandleSyncJob {
	private Logger logger = (Logger) LoggerFactory.getLogger(HandleSyncJob.class);
	@Autowired
	private HandleSyncService handleSyncService;
	
	/**
	 * 
	 */
	public void handleData() {
		logger.info("开始处转存研系统中间表...");
		handleSyncService.copyKY();
		logger.info("科研系统中间表转存完毕！");
		
		logger.info("开始处理科研系统中间表...");
		handleSyncService.handleKY();
		logger.info("科研系统中间表处理完毕！");
	}
	
	
}
