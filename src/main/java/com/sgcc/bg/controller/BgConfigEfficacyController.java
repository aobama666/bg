package com.sgcc.bg.controller;

import com.alibaba.fastjson.JSON;
import com.sgcc.bg.common.*;
import com.sgcc.bg.service.BgConfigEfficacyService;
import java.util.*;
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
@RequestMapping(value="efficacy")
public class BgConfigEfficacyController {
	@Autowired
	BgConfigEfficacyService bgConfigEfficacyService;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	private UserUtils userUtils;
	private static Logger log = LoggerFactory.getLogger(ApproverController.class);
	/**
	 * 校验配置页面
	 * @return
	 */
	@RequestMapping(value = "/efficacy_index", method = RequestMethod.GET)
	public ModelAndView initPage(){
		log.info("校验配置页面----->接口名称：initPage-----开始");
		Map<String, Object> map = new HashMap<>();
		Map<String ,Object> efficacyInfo=new HashMap<>();
		List<Map<String, Object>>  yearList =bgConfigEfficacyService.getDisConfigEfficacy(efficacyInfo);
		String  year=DateUtil.getYear();
		map.put("yearInfo",yearList);
		map.put("nowYear",year);
		ModelAndView model = new ModelAndView("bg/config/bg_efficacyConfig_info",map);
		log.info("校验配置页面----->返回值："+model);
		return model;
	}
	/**
	 * 校验配置新增页面
	 * @return
	 */
	@RequestMapping(value = "/efficacy_addindex", method = RequestMethod.GET)
	public ModelAndView addPage(){
		log.info("校验配置添加页面----->接口名称：addPage-----开始");
		Map<String, Object> map = new HashMap<>();
		ModelAndView model = new ModelAndView("bg/config/bg_efficacyConfig_add",map);
		log.info("校验配置添加页面----->返回值："+model);
		return model;
	}
	/**
	 * 校验配置修改页面
	 * @return
	 */
	@RequestMapping(value = "/efficacy_updataindex", method = RequestMethod.GET)
	public ModelAndView updataPage(String id){
		log.info("校验配置修改页面----->接口名称：updataPage-----开始");
		Map<String ,Object> efficacyInfo=new HashMap<>();
		log.info("校验配置修改页面----->查询修改数据的信息----->参数：id："+id);
		efficacyInfo.put("id",id);
		List<Map<String,Object>> efficacyList = bgConfigEfficacyService.selectForConfigEfficacy(efficacyInfo);
		Map<String, Object>  map=efficacyList.get(0);
		ModelAndView model = new ModelAndView("bg/config/bg_efficacyConfig_updata",map);
		log.info("校验配置修改页面----->返回值："+model);
		return model;
	}
	/**
	 * 查询校验配置信息
	 * @return
	 */
	@RequestMapping(value = "/selectForConfigEfficacy", method = RequestMethod.POST)
	@ResponseBody
	public String selectForConfigEfficacy(String year, Integer page, Integer limit){
		log.info(" 校验配置查询----->接口名称：selectForConfigEfficacy");
		year = Rtext.toStringTrim(year, "");
		Map<String ,Object> efficacyInfo=new HashMap<>();
		efficacyInfo.put("year",year);
		log.info(" 校验配置查询----->参数：年度（year）："+year );
		List<Map<String,Object>> content = bgConfigEfficacyService.selectForConfigEfficacy(efficacyInfo);
		int start = 0;
		int end = 30;
		if(page != null && limit!=null){
			start = (page-1)*limit;
			end = page*limit;
		}
		PageHelper<Map<String, Object>> pageHelper = new PageHelper<>(content, start, end);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("items", pageHelper.getResult());
		jsonMap.put("totalCount", pageHelper.getTotalNum());
		String jsonStr = JSON.toJSONString(jsonMap);
		log.info(" 校验配置查询--->返回值："+jsonStr);
		return jsonStr;
	}
	/**
	 * 添加校验配置信息
	 * @param year
	 * @param month
	 * @param isEfficacy
	 * @return
	 */
	@RequestMapping(value = "/addConfigEfficacy", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp addConfigEfficacy(String year, String month, String isEfficacy ){
		log.info("添加校验配置信息-----接口名称：addConfigEfficacy----开始");
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(year)){
			rw.setSuccess("false");
			rw.setMsg("年度不能为空");
			return rw;
		}
		if(Rtext.isEmpty(month)){
			rw.setSuccess("false");
			rw.setMsg("月度不能为空");
			return rw;
		}
		if(Rtext.isEmpty(isEfficacy)){
			rw.setSuccess("false");
			rw.setMsg("校验考勤工时不能为空");
			return rw;
		}
		log.info("添加校验配置信息-参数：年度（year）"+year+"-月度(month)"+month
				+"-校验考勤工时(isEfficacy)"+isEfficacy );
		Map<String ,Object> efficacyInfo=new HashMap<>();
		efficacyInfo.put("year",year);
		efficacyInfo.put("month",month);
		efficacyInfo.put("isEfficacy",isEfficacy);
		List<Map<String,Object>> content = bgConfigEfficacyService.selectForConfigEfficacy(efficacyInfo);
		if(!content.isEmpty()){
			rw.setSuccess("false");
			rw.setMsg("该信息已存在");
			return rw;
		}
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		String uuid = Rtext.getUUID();
		efficacyInfo.put("id", uuid);
		efficacyInfo.put("valid", "1");
		efficacyInfo.put("createUser", userId);
		efficacyInfo.put("updateUser", userId);
		efficacyInfo.put("createTime", new Date());
		efficacyInfo.put("updateTime", new Date());
		int result = bgConfigEfficacyService.addConfigEfficacy(efficacyInfo);
		rw.setSuccess("true");
		rw.setMsg(result==1?"添加成功":"请勿重复添加！");
		log.info("添加校验配置信息----结束-----返回值："+rw);
		return rw;
	}
	/**
	 * 修改校验配置信息
	 * @param year
	 * @param month
	 * @param isEfficacy
	 * @return
	 */
	@RequestMapping(value = "/updataConfigEfficacy", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp updataConfigEfficacy(String id,String year, String month, String isEfficacy ){
		log.info("修改校验配置信息-----接口名称：updataConfigEfficacy----开始");
		ResultWarp rw = new ResultWarp();
		if(Rtext.isEmpty(year)){
			rw.setSuccess("false");
			rw.setMsg("年度不能为空");
			return rw;
		}
		if(Rtext.isEmpty(month)){
			rw.setSuccess("false");
			rw.setMsg("月度不能为空");
			return rw;
		}
		if(Rtext.isEmpty(isEfficacy)){
			rw.setSuccess("false");
			rw.setMsg("校验考勤工时不能为空");
			return rw;
		}
		log.info("修改校验配置信息-参数：年度（year）"+year+"-月度(month)"+month
				+"-校验考勤工时(isEfficacy)"+isEfficacy );
		Map<String ,Object> efficacyInfo=new HashMap<>();
		efficacyInfo.put("year",year);
		efficacyInfo.put("month",month);
		efficacyInfo.put("isEfficacy",isEfficacy);
		List<Map<String,Object>> content = bgConfigEfficacyService.selectForConfigEfficacy(efficacyInfo);
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		efficacyInfo.put("id", id);
		efficacyInfo.put("updateUser", userId);
		efficacyInfo.put("updateTime", new Date());
		int result=0;
		if(!content.isEmpty()){
			String  oldisEfficacy=content.get(0).get("IS_EFFICACY").toString();
			if(oldisEfficacy.equals(isEfficacy)){
				rw.setSuccess("true");
				rw.setMsg("修改成功");
				return rw;
			}else{
				result = bgConfigEfficacyService.updataConfigEfficacy(efficacyInfo);
			}
		}else{
			result = bgConfigEfficacyService.updataConfigEfficacy(efficacyInfo);
		}
		rw.setSuccess("true");
		rw.setMsg(result==1?"修改成功":"请勿重复修改！");
		log.info("修改校验配置信息----结束-----返回值："+rw);
		return rw;
	}
	/**
	 * 删除校验配置信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteConfigEfficacy", method = RequestMethod.POST)
	@ResponseBody
	public ResultWarp deleteConfigEfficacy(String id){
		log.info("删除校验配置信息-----接口名称：deleteConfigEfficacy----开始");
		ResultWarp rw = new ResultWarp();
		log.info("删除校验配置信息-参数：主键（id）"+id);
		Map<String ,Object> efficacyInfo=new HashMap<>();
		CommonCurrentUser currentUser = userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		String userId = currentUser.getUserId();
		efficacyInfo.put("id", id);
		efficacyInfo.put("valid", "0");
		efficacyInfo.put("updateUser", userId);
		efficacyInfo.put("updateTime", new Date());
		int result = bgConfigEfficacyService.deleteConfigEfficacy(efficacyInfo);
		rw.setSuccess("true");
		rw.setMsg(result==1?"删除成功":"请勿重复删除！");
		log.info("删除校验配置信息----结束-----返回值："+rw);
		return rw;
	}
	/**
	 * 批量导出
	 * @param response
	 */
	@RequestMapping(value = "/exportSelectedItems", method = RequestMethod.POST)
	public void exportSelectedItems(String year, String index, HttpServletResponse response) {
		Map<String ,Object> efficacyInfo=new HashMap<>();
		year = Rtext.toStringTrim(year, "");
		efficacyInfo.put("year",year);
		bgConfigEfficacyService.exportSelectedItems(efficacyInfo,index,response);
		log.info("将要导出的条目index:"+index);
	}


}
