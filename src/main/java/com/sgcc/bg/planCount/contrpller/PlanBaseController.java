package com.sgcc.bg.planCount.contrpller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.*;
import com.sgcc.bg.planCount.service.PlanBaseService;
import com.sgcc.bg.planCount.service.PlanExecutionService;
import com.sgcc.bg.planCount.service.PlanInputService;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "planBase")
public class PlanBaseController {

	private static Logger Logger = LoggerFactory.getLogger(PlanBaseController.class);
	@Autowired
	private WebUtils webUtils;
	@Autowired
	UserUtils userUtils;
	@Autowired
	private PlanBaseService planBaseService;

	/**
	 * 计划统计--根据专项类别获取资金来源,所谓联动
	 */
	@ResponseBody
	@RequestMapping(value = "/selectForFundsSource")
	public String selectForFundsSource(String specalType){
		specalType=Rtext.toStringTrim(specalType, ""); //专项类别
		Map<String, Object> fundsSourceMap = new HashMap<String, Object>();
		fundsSourceMap.put("specalType",specalType);
		List<Map<String,Object>>	fundsSourceList = planBaseService.selectForFundsSourceInfo(fundsSourceMap);
		ResultWarp rw = new ResultWarp(ResultWarp.SUCCESS,"success");
		rw.addData("fundsSourceList",fundsSourceList);
		return JSON.toJSONString(rw);
	}


}
