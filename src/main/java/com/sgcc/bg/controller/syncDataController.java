package com.sgcc.bg.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sgcc.bg.service.SyncService;

@Controller
@RequestMapping(value = "sync")
public class syncDataController {
	@Autowired
	private SyncService syncService;
	
	private static Logger Logger = LoggerFactory.getLogger(syncDataController.class);
	
	/**
	 * 同步erp数据
	 */
	@RequestMapping(value = "/syncErpData", method = RequestMethod.GET)
	public void testSyncErpData(HttpServletResponse res){
		Logger.info("############# 同步erp数据开始 ##############");
		res.setContentType("application/json;charset=UTF-8");
		PrintWriter out=null;
		try {
			out = res.getWriter();
			out.write("开始同步erp数据，请耐心等待......");
			out.flush();
			Long start = System.currentTimeMillis();
			syncService.syncErpSyncData();
			syncService.syncUserOrganRelationData();
			Long end = System.currentTimeMillis();
			out.write("同步erp数据成功,耗时"+(end-start)/60000.0+"(min)");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		Logger.info("############# 同步erp数据结束  ##############");
	}
}
