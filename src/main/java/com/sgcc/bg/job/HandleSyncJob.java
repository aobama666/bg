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
	 * 处理其他系统的接口数据
	 */
	public void handleData() {
		logger.info("开始处转存研系统中间表...");
		handleSyncService.copyFromKY();
		logger.info("科研系统中间表转存完毕！");
		
/*********************************************************************************************/
		logger.info("开始处理科研系统中间表...");
		handleSyncService.validateKY();
		logger.info("科研系统中间表处理完毕！");
		
/*********************************************************************************************/	
		logger.info("开始根据科研系统数据更新报工系统...");
		handleSyncService.updateFromKY();
		logger.info("根据科研系统数据更新报工系统完毕！");
		
/**********************************科研***********************************************************/
/*********************************横向************************************************************/

		logger.info("开始处转存横向系统中间表...");
		handleSyncService.copyFromHX();
		logger.info("横向系统中间表转存完毕！");
		
/*********************************************************************************************/
		logger.info("开始处理横向系统中间表...");
		handleSyncService.validateHX();
		logger.info("横向系统中间表处理完毕！");
		
/*********************************************************************************************/	
		logger.info("开始根据横向系统数据更新报工系统...");
		handleSyncService.updateFromHX();
		logger.info("根据横向系统数据更新报工系统完毕！");	
	}
	
}
