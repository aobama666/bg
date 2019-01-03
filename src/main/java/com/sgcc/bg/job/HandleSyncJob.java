package com.sgcc.bg.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		//handleSyncService.copyFromKY();
		logger.info("科研系统中间表转存完毕！");
		
/*********************************************************************************************/
		logger.info("开始处理科研系统中间表...");
		handleSyncService.validateKY();
		logger.info("科研系统中间表处理完毕！");
		
/*********************************************************************************************/	
		logger.info("开始根据科研系统数据更新报工系统...");
		//handleSyncService.updateFromKY();
		logger.info("根据科研系统数据更新报工系统完毕！");
		
		
		
		
		
		logger.info("开始处理科研系统项目信息中间表...");
		//handleSyncService.handleKY();
//		List<Map<String, String>> proList = handleSyncService.getAllSyncProFromKY();
//		List<Map<String, Object>> proList = new ArrayList<>();
//		Map<String,Object> dataMap = new HashMap<String, Object>();
//		dataMap.put("uuid", "111");
//		dataMap.put("projectName", "test_12");
//		dataMap.put("category", "KY12");
//		dataMap.put("wbsNumber", "WBS12130004");
//		dataMap.put("introduce", "测试4");
//		dataMap.put("startDate","2018-12-04");
//		dataMap.put("endDate", "2018-12-31");
//		
//		Map<String,Object> dataMap_1 = new HashMap<String, Object>();
//		dataMap_1.put("uuid", "222");
//		dataMap_1.put("projectName", "test_2");
//		dataMap_1.put("category", "KY");
//		dataMap_1.put("wbsNumber", "WBS12130002");
//		dataMap_1.put("introduce", "测试2");
//		dataMap_1.put("startDate","2018-12-2");
//		dataMap_1.put("endDate", "2018-12-100");
//		
//		Map<String,Object> dataMap_2 = new HashMap<String, Object>();
//		dataMap_2.put("uuid", "333");
//		dataMap_2.put("projectName", "test_3");
//		dataMap_2.put("category", "KY");
//		dataMap_2.put("wbsNumber", "WBS12130003");
//		dataMap_2.put("introduce", "测3");
//		dataMap_2.put("startDate","2018-12-3");
//		dataMap_2.put("endDate", "2018-12-23");
		
//		proList.add(dataMap);
//		proList.add(dataMap_1);
//		proList.add(dataMap_2);
//		for (Map<String, String> map : proList) {
//			System.out.println("*********************************");
//			handleSyncService.handleKYPro(map);
//		}
		logger.info("科研系统项目信息中间表处理完毕！");
		
		logger.info("开始处理科研系统参与人员中间表...");
		
		List<Map<String, String>> empList = new ArrayList<>();
		Map<String,String> empMap = new HashMap<String, String>();
		
		empMap.put("uuid", "e111");
		empMap.put("hrcode", "41601960");
		empMap.put("empName", "谷凯");
		empMap.put("projectId", "555");//不含此项目
		empMap.put("role", "0");
		empMap.put("startDate","2018-11-12");
		empMap.put("endDate", "2018-12-31");
		
		Map<String,String> empMap_1 = new HashMap<String, String>();
		empMap_1.put("uuid", "e222");
		empMap_1.put("hrcode", "41210014");
		empMap_1.put("empName", "赵");//姓名与编号不匹配
		empMap_1.put("projectId", "111");
		empMap_1.put("role", "0");
		empMap_1.put("startDate","2018-12-2");
		empMap_1.put("endDate", "2018-12-31");
		
		Map<String,String> empMap_2 = new HashMap<String, String>();
		empMap_2.put("uuid", "e333");
		empMap_2.put("hrcode", "41210008");
		empMap_2.put("empName", "孟静");
		empMap_2.put("projectId", "");//为空
		empMap_2.put("role", "1");
		empMap_2.put("startDate","2018--2");
		empMap_2.put("endDate", "2018-12-31");
		
		Map<String,String> empMap_3 = new HashMap<String, String>();
		empMap_3.put("uuid", "e444");
		empMap_3.put("hrcode", "41700090");
		empMap_3.put("empName", "赵紫依");
		empMap_3.put("projectId", "111");
		empMap_3.put("role", "1");
		empMap_3.put("startDate","2018-1212-2");
		empMap_3.put("endDate", "2018-12-31");
		
		empList.add(empMap);
		empList.add(empMap_1);
		empList.add(empMap_2);
		empList.add(empMap_3);
		Set<String> firstAddProIds = new HashSet<>();//记录本次同步中是第一次同步的报工系统项目idroIds
//		for (Map<String, String> map : empList) {
//			System.out.println("*********************************");
//			handleSyncService.handleKYEmp(map,firstAddProIds);
//			System.out.println(firstAddProIds.size()+firstAddProIds.toString());
//		}
		logger.info("科研系统参与人员中间表处理完毕！");
	}
	
	
}
