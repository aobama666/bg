package com.sgcc.bg.controller;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.service.KqTemporaryService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "kqInfo")
public class KqController {
	@Autowired
	private KqTemporaryService KqTemporaryService;
	private static Logger Logger = LoggerFactory.getLogger(KqController.class);

	@Autowired
	private RedisTemplate<String,String> stringRedisTemplate;

	/*
	 *工作任务查询页面 index
	 */
	@ResponseBody
	@RequestMapping(value="/index")
	public ModelAndView Index(HttpServletRequest res){

		ModelAndView model = new ModelAndView("bg/kq/kqEmp_info");
		return model;
	}
    /**
     * 查询考勤业务数据
     */
    @ResponseBody
    @RequestMapping(value="/selectForKqInfo" )
    public String selectForKqInfo(HttpServletRequest request){
        Logger.info("############# 考勤数据查询开始 ##############");
        String rw = KqTemporaryService.selectForKqInfo(request);
        Logger.info("############# 考勤数据查询结束 ##############");
        return rw;
    }
	/*
	 *工作任务查询导出
	 */
	@ResponseBody
	@RequestMapping(value="/exportExcel")
	public String exportExcel(HttpServletRequest request,HttpServletResponse response){
		KqTemporaryService.exportExcel(request,response);
		return "";
	}





	/**
	 * 同步考勤数据  1：正在同步  0：同步结束
	 */
	@ResponseBody
	@RequestMapping(value = "/SyncKqData" )
	public String SyncKqData(HttpServletRequest request){
        String beginDate = request.getParameter("startDate" ) == null ? "" : request.getParameter("startDate").toString(); //开始时间
        String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //结束数据
		String kqTime = request.getParameter("kqTime" ) == null ? "" : request.getParameter("kqTime").toString();

		//取月初和月末
		String[] str= kqTime.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		//每月月初
		String dateBegin = DateUtil.getFirstDayOfMonth1(year,month);
		//每月月末
		String dateEnd = DateUtil.getLastDayOfMonth1(year,month);

		ValueOperations<String, String> value = stringRedisTemplate.opsForValue();
		String redisData = value.get("KQ_SyncKqData");
		String res=null;
		if(null != redisData && redisData != "" && redisData.equals("1")){

			Map<String, String> map = new HashMap<>();
			map.put("msg","正在同步考勤数据，请稍后");
			map.put("code","201");
			res =  JSON.toJSONString(map);
		}else {

			Logger.info("############# resid记录当前同步开始状态 ##############");
			value.set("KQ_SyncKqData", "1");
			stringRedisTemplate.expire("KQ_SyncKqData", 60*60, TimeUnit.SECONDS);

			Logger.info("############# 考勤数据同步开始 ##############");
			res = KqTemporaryService.addTemporary(dateBegin, dateEnd);
			Logger.info("############# 考勤数据同步结束  ##############");

			Logger.info("############# resid记录当前同步结束状态 ##############");
			value.set("KQ_SyncKqData", "0");
			stringRedisTemplate.expire("KQ_SyncKqData", 60*60, TimeUnit.SECONDS);
		}

		/*Logger.info("############# 考勤数据同步开始 ##############");
		String res = KqTemporaryService.addTemporary(dateBegin, dateEnd);
		Logger.info("############# 考勤数据同步结束  ##############");*/
		return   res;
	}
}
