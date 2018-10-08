package com.sgcc.bg.common;
import java.io.BufferedReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
public class BaseController {
	 public Logger log = LoggerFactory.getLogger(BaseController.class); 
	/**
	 * 解析request请求
	 * 
	 * @param req
	 * @return
	 */
	@SuppressWarnings("null")
	public RequestInfo parseFromRequest(HttpServletRequest req) {
		RequestInfo requestInfo = new RequestInfo();
		String data = null;
		try {
		    data = req.getParameter("data");
		    
		    	if (data == null || data.trim().length() == 0) {
					BufferedReader reader = req.getReader();
					String line = null;
					StringBuffer jsonIn = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						jsonIn.append(line);
					}
					data = jsonIn.toString();
				}
				log.info("request str is:" + data);
				if (StringUtils.isNotBlank(data)) {
					 JSONObject jsonObj=JSON.parseObject(data);
					 requestInfo.setParams(jsonObj);
				}
		    	
		    	
		    
		} catch (Exception e) {
			e.printStackTrace();
			log.info("parse req failed!", e);
		}
		return requestInfo;

	}

	/**
	 * 验证参数合法性
	 * 
	 * @param req
	 * @param response
	 * @param validateKeys
	 *            * 用法举例{"name|R|L50"} 参数名称name提示语可以追加在‘@’后面，比如
	 *            name@姓名，提示为'姓名不能够为空‘，否则提示'name不能为空'
	 *            R必填选项，L是长度效验，F敏感词效验，N数值效验，S特殊字符效验, Z工程之类名称校验 , M人员姓名，P电话号码,
	 *            O数字或字母, C只能中文
	 * @return
	 */
	@SuppressWarnings("null")
	public RequestInfo parseFromRequest(HttpServletRequest req,String validateKeys[]) {
		String data = req.getParameter("data");
		String vrs = "";
		RequestInfo rinfo = new RequestInfo();
		if(data==null){
			vrs = validateParams(req, validateKeys);
			 
		}else{
			rinfo = parseFromRequest(req);
			if (StringUtils.isNotBlank(rinfo.getResStr())) {
				return rinfo;
			}
			vrs = validateParam(rinfo.getParams(), validateKeys);
		}
		if (StringUtils.isNotBlank(vrs)) {
			rinfo.setResStr(vrs);
		}
		return rinfo;
	}

	/*
	 * 功能描述：参数验证判断 用法举例{"name|R|L50|F|N|S"}
	 * 参数名称name，R必填选项，L是长度效验，F敏感词效验，N数值效验，S特殊字符效验, Z工程之类名称校验，M人员姓名，P电话号码,
	 * O数字或字母, C只能中文,D匹配中文英文数字 G 编码
	 * 
	 * @param paramsKey
	 * 
	 * @return
	 */
	public String validateParams(HttpServletRequest request, String paramsKey[]) {
		StringBuilder sb = new StringBuilder();
		for (String key : paramsKey) {
			int index = key.indexOf("|");
			if (index < 0) {
				continue;
			}
			String[] array = key.split("\\|");
			String rkey = null;// 数据字段的键值
			String rlable = null;// 验证失败情况下，提示给用户的信息，为空则使用键值提示
			index = array[0].indexOf('@');

			if (index > 0) {
				rkey = array[0].substring(0, index);
				rlable = array[0].substring(index + 1);
			} else {
				rkey = array[0];
				rlable = array[0];
			}
			String params = request.getParameter(rkey) == null ? "" : request.getParameter(rkey).toString(); 
			for (int i = 1; i < array.length; i++) {
				String rn = array[i].toUpperCase();
				String str = null;
				if (rn.startsWith("R")) {
					str = validateRequires(params, rkey, rn, rlable);
				} else if (rn.startsWith("L")) {// 验证长度
					str = validateLens(params, rkey, rn, rlable);
				} else if (rn.startsWith("N")) {// 数值效验
					str = validateNumbers(params, rkey, rlable);
				} else if (rn.startsWith("Z")) {// 工程之类名称校验
					str = validatePNames(params, rkey, rlable);
				} else if (rn.startsWith("P")) {// 电话号码
					str = validatePhones(params, rkey, rlable);
				} else if (rn.startsWith("O")) {// 数字或字母
					str = validateNoOrLetters(params, rkey, rlable);
				} else if (rn.startsWith("C")) {// 只能中文
					str = validateChineses(params, rkey, rlable);
				} else if (rn.startsWith("D")) {// 只能中文英文数字
					str = validatecs(params, rkey, rlable);
				}
				if (str != null) {
					sb.append(str).append(",");
					break;
				}
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/*
	 * 功能描述：参数验证判断 用法举例{"name|R|L50|F|N|S"}
	 * 参数名称name，R必填选项，L是长度效验，F敏感词效验，N数值效验，S特殊字符效验, Z工程之类名称校验，M人员姓名，P电话号码,
	 * O数字或字母, C只能中文,D匹配中文英文数字 G 编码
	 * 
	 * @param paramsKey
	 * 
	 * @return
	 */
	public String validateParam(JSONObject params, String paramsKey[]) {
		StringBuilder sb = new StringBuilder();
		for (String key : paramsKey) {
			int index = key.indexOf("|");
			if (index < 0) {
				continue;
			}
			String[] array = key.split("\\|");
			String rkey = null;// 数据字段的键值
			String rlable = null;// 验证失败情况下，提示给用户的信息，为空则使用键值提示
			index = array[0].indexOf('@');

			if (index > 0) {
				rkey = array[0].substring(0, index);
				rlable = array[0].substring(index + 1);
			} else {
				rkey = array[0];
				rlable = array[0];
			}
			for (int i = 1; i < array.length; i++) {
				String rn = array[i].toUpperCase();
				String str = null;
				if (rn.startsWith("R")) {
					str = validateRequire(params, rkey, rn, rlable);
				} else if (rn.startsWith("L")) {// 验证长度
					str = validateLen(params, rkey, rn, rlable);
				} else if (rn.startsWith("N")) {// 数值效验
					str = validateNumber(params, rkey, rlable);
				} else if (rn.startsWith("Z")) {// 工程之类名称校验
					str = validatePName(params, rkey, rlable);
				} else if (rn.startsWith("M")) {// 人员姓名
					str = validateNoOrC(params, rkey, rlable);
				} else if (rn.startsWith("P")) {// 电话号码
					str = validatePhone(params, rkey, rlable);
				} else if (rn.startsWith("O")) {// 数字或字母
					str = validateNoOrLetter(params, rkey, rlable);
				} else if (rn.startsWith("C")) {// 只能中文
					str = validateChinese(params, rkey, rlable);
				} else if (rn.startsWith("D")) {// 只能中文英文数字
					str = validatec(params, rkey, rlable);
				}
				if (str != null) {
					sb.append(str).append(",");
					break;
				}
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 验证空值
	 * 
	 * @param params
	 * @param key
	 * @param rn
	 * @param rlable
	 * @return
	 */
	private String validateRequire(JSONObject params, String key, String rn,
			String rlable) {
		boolean isRequre = false;
		if (rn.equalsIgnoreCase("R")) {
			isRequre = true;
		}
		if (isRequre) {
			if (params == null || StringUtils.isBlank(params.getString(key))) {
				StringBuilder sb = new StringBuilder();
				sb.append(rlable).append("不能为空");
				return sb.toString();
			}
		}
		return null;
	}
	/**
	 * 验证空值
	 * 
	 * @param params
	 * @param key
	 * @param rn
	 * @param rlable
	 * @return
	 */
	private String validateRequires(String params, String key, String rn,
			String rlable) {
		boolean isRequre = false;
		if (rn.equalsIgnoreCase("R")) {
			isRequre = true;
		}
		if (isRequre) {
			if (params == "" || StringUtils.isBlank(params)) {
				StringBuilder sb = new StringBuilder();
				sb.append(rlable).append("不能为空");
				return sb.toString();
			}
		}
		return null;
	} 
	/**
	 * 验证长度
	 * 
	 * @param params
	 * @param key
	 * @param rn
	 * @return
	 */
	private String validateLen(JSONObject params, String key, String rn,
			String rlable) {
		int len = Integer.parseInt(rn.substring(1).trim());
		if (params.getString(key) != null
				&& params.getString(key).length() > len) {
			return rlable + " 的长度不能大于" + len;
		}
		return null;
	}
	/**
	 * 验证长度
	 * 
	 * @param params
	 * @param key
	 * @param rn
	 * @return
	 */
	private String validateLens(String params, String key, String rn,
			String rlable) {
		int len = Integer.parseInt(rn.substring(1).trim());
		if (params != ""&&params.length() > len) {
			return rlable + " 的长度不能大于" + len;
		}
		return null;
	}

	/**
	 * 数值校验
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateNumber(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.checkNumber(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 的格式必须为纯数字";
		}
		return null;
	}
	/**
	 * 数值校验
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateNumbers(String params, String key, String rlable) {

		boolean check = StringUtil.checkNumber(params.trim());
		if (params == "" || check != true) {
			return rlable + " 的格式必须为纯数字";
		}
		return null;
	}
	/**
	 * 只能含有汉字中划线和下划线，不能以中划线或下划线开头或结尾
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validatePName(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.checkPName(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 只能含有中文、中划线、下划线、数字、字母且不能以中划线或下划线开头或结尾 ";
		}
		return null;
	}
	/**
	 * 只能含有汉字中划线和下划线，不能以中划线或下划线开头或结尾
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validatePNames(String  params, String key, String rlable) {

		boolean check = StringUtil.checkPName(params.trim());
		if (params == "" || check != true) {
			return rlable + " 只能含有中文、中划线、下划线、数字、字母且不能以中划线或下划线开头或结尾 ";
		}
		return null;
	}

	/**
	 * 键盘上可见的非字母的字符
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateNoOrC(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.checkNotNoOrC(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 只能含有中文和英文";
		}
		return null;
	}

	/**
	 * 电话号码
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validatePhone(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.checkPhone(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 不是电话号码";
		}
		return null;
	}
	/**
	 * 电话号码
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validatePhones(String params, String key, String rlable) {

		boolean check = StringUtil.checkPhone(params.trim());
		if (params == "" || check != true) {
			return rlable + " 不是电话号码";
		}
		return null;
	}

	/**
	 * 数字或字母
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateNoOrLetter(JSONObject params, String key,
			String rlable) {

		boolean check = StringUtil
				.checkNoOrLetter(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 只能是数字或字母";
		}
		return null;
	}
	/**
	 * 数字或字母
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateNoOrLetters(String params, String key,
			String rlable) {

		boolean check = StringUtil.checkNoOrLetter(params.trim());
		if (params == null || check != true) {
			return rlable + " 只能是数字或字母";
		}
		return null;
	}

	/**
	 * 只能中文
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validateChinese(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.checkChinese(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 的格式不正确";
		}
		return null;
	}
	private String validateChineses(String params, String key, String rlable) {

		boolean check = StringUtil.checkChinese(params.trim());
		if (params == "" || check != true) {
			return rlable + " 的格式不正确";
		}
		return null;
	}

	/**
	 * 只能中文英文数字
	 * 
	 * @param params
	 * @param rn
	 * @return
	 */
	private String validatec(JSONObject params, String key, String rlable) {

		boolean check = StringUtil.check(params.getString(key).trim());
		if (params == null || check != true) {
			return rlable + " 的格式不正确";
		}
		return null;
	}
	private String validatecs(String params, String key, String rlable) {

		boolean check = StringUtil.check(params.trim());
		if (params == "" || check != true) {
			return rlable + " 的格式不正确";
		}
		return null;
	}
	 

	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		return request;
	}

	 

	 

	public static void logBefore(Logger logger, String interfaceName) {
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}

	public static void logAfter(Logger logger) {
		logger.info("end");
		logger.info("");
	}

	 

}
