package com.sgcc.bg.controller.bg2;

 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.PageHelper;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.service.BgNonProjectService;
import com.sgcc.bg.service.DataDictionaryService;

@Controller
@RequestMapping(value="nonProject2")
public class AuditController {
	@Autowired
	private UserUtils userUtils;
	@Autowired
	private WebUtils webUtils;
	@Autowired
	DataDictionaryService dict;
	@Autowired
	private BgNonProjectService bgNonProjectService;
	public Logger log = LoggerFactory.getLogger(AuditController.class);

	@RequestMapping("/proInfo")
	public String  test(HttpServletRequest request){
		Map<String,String> dictMap= dict.getDictDataByPcode("pstatus100001");
		String dictJson=dict.getDictDataJsonStr("pstatus100001");
		request.setAttribute("dictMap",dictMap);
		request.setAttribute("dictJson",dictJson);
		return "bg2/nonproInfo/bg_nonproject_info";
		 
	}
	@RequestMapping("/initPage")
	@ResponseBody
	public String initPage(String proName, String proStatus, Integer page, Integer limit) {
		proStatus=Rtext.toStringTrim(proStatus, "");
		proName=Rtext.toStringTrim(proName, "");
		List<Map<String, String>> content = bgNonProjectService.getAllProjects(proName, proStatus);
		int start = 0;
		int end = 30;
		if(page != null && limit!=null){
			start = (page-1)*limit;
			end = page*limit;
		}
		PageHelper<Map<String, String>>  pageHelper = new PageHelper<>(content, start, end);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("data", pageHelper.getResult());
		jsonMap.put("total", pageHelper.getTotalNum());
		jsonMap.put("msg", "success");
		jsonMap.put("success", "true");
		String jsonStr = JSON.toJSONStringWithDateFormat(jsonMap, "yyyy-MM-dd",
				SerializerFeature.WriteDateUseDateFormat);
		return jsonStr;
	}
	@RequestMapping("/pro_details")
	public ModelAndView projectDetails(String proId, HttpServletRequest request) {
		Map<String, String> proInfo = bgNonProjectService.getProInfoByProId(proId);
		ModelAndView model = new ModelAndView("bg2/nonproInfo/bg_nonproject_details", proInfo);
		return model;
	}
	@RequestMapping("/pro_add")
	public ModelAndView projectAdd() {
		Map<String, String> map = new HashMap<>();
		CommonCurrentUser currentUser=userUtils.getCommonCurrentUserByUsername(webUtils.getUsername());
		map.put("hrcode", currentUser.getHrCode());
		map.put("deptName", currentUser.getDeptName());
		map.put("deptCode", currentUser.getDeptCode());
		ModelAndView model = new ModelAndView("bg2/nonproInfo/bg_nonproject_add",map);
		return model;
	}
	@RequestMapping("/pro_update")
	public ModelAndView projectUpdate(String proId, HttpServletRequest request) {
		Map<String, String> proInfo = bgNonProjectService.getProInfoByProId(proId);
		ModelAndView model = new ModelAndView("bg2/nonproInfo/bg_nonproject_update", proInfo);
		return model;
	}
	
	
	
	
}
