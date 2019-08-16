package com.sgcc.bg.controller;

import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.CommonUser;
import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.service.KqTemporaryService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	 * 同步考勤数据
	 */
	@ResponseBody
	@RequestMapping(value = "/SyncKqData" )
	public String SyncKqData(HttpServletRequest request){
        String beginDate = request.getParameter("startDate" ) == null ? "" : request.getParameter("startDate").toString(); //开始时间
        String endDate = request.getParameter("endDate" ) == null ? "" : request.getParameter("endDate").toString(); //结束数据
        Logger.info("############# 考勤数据同步开始 ##############");
	    String 	res=KqTemporaryService.addTemporary(beginDate,endDate);
		Logger.info("############# 考勤数据同步结束  ##############");
		return   res;
	}
}
