package com.sgcc.bg.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.sgcc.isc.core.orm.identity.User;
import com.sgcc.isc.core.orm.role.OrganizationalRole;
import com.sgcc.isc.core.orm.role.Role;
import com.sgcc.isc.framework.common.constant.Constants;
import com.sgcc.isc.service.adapter.factory.AdapterFactory;
import com.sgcc.isc.service.adapter.helper.IIdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Component
public class WebUtils {
	
	private  Logger log =  LoggerFactory.getLogger(WebUtils.class);
	
	@SuppressWarnings({"rawtypes" })
	@Autowired
	private RedisTemplate<String,String> stringRedisTemplate;

	/**
	 * 获取当前登录人员信息
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private CommonUser getCurrentUserJson(){
		String cookiename = "loginSessionId";
		String cookievalue = "";
		Cookie[] cookies = getHttpServletRequest().getCookies();
		if(cookies!=null)
		for(Cookie cookie:cookies){
			if(cookiename.equals(cookie.getName())){
				cookievalue=cookie.getValue();
				break;
			}
		}
		log.info("cookievalue:"+cookievalue);
		Map<String,String> resultMap = new HashMap<String,String>();
		CommonUser user = getUserFromRedis(cookievalue);
		return user;
	}
	
	/**
	 * 获取request
	 * @return
	 */
	public  HttpServletRequest getHttpServletRequest(){
		HttpServletRequest request=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	/**
	 * 获取当前登录人用户别名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public  String getUseralias(){
		CommonUser user=getCurrentUserJson();
		return user.getName()==null?"":user.getName().toString();
	}
	
	/**
	 * 获取当前登录人用户名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public  String getUsername(){
		CommonUser user=getCurrentUserJson();
		return user.getUserName()==null?"":user.getUserName().toString();
	}
	
	public CommonUser getCommonUser(){
		CommonUser user=getCurrentUserJson();
		return user;
	}
	
	
	/**
	 * 用户登录系统--写入SessionID
	 * @param request
	 * @return
	 */
	public RedisBeanWarp login(HttpServletRequest request, HttpServletResponse response) { 
		log.info("用户登录系统  --  login");
		String username = request.getParameter("username");//用户姓名	
		String password = request.getParameter("password");//用户密码 
		ValueOperations<String, String> value = stringRedisTemplate.opsForValue();
		Des des = new Des();
		String redisKey = des.strEnc("loginSessionId",username+password);//加密
		String redisData = value.get(redisKey);
		RedisBeanWarp rbw = new RedisBeanWarp();
		User user = null;
		Map rbwMap = new HashMap<>();
		if(redisData!=null&&redisData.trim().length()>0){
			log.info("redis缓存中的对象转换为rbw");
			rbw = JSON.parseObject(redisData, RedisBeanWarp.class);//缓存中的对象转换为rbw
			if(rbw.getValue() == null){
				rbwMap = JSON.parseObject(redisData, Map.class);//缓存中的对象转换为rbw
				String userString = "{\"lastTime\": 1509441746562,value:"+rbwMap.get("user")+"}";
				rbw = JSON.parseObject(userString, RedisBeanWarp.class);
			}
 		}else{
 			log.info("redis缓存中没有对象,从ISC取出数据源并写入缓存");
 			user=getUserFromIsc(username,password);
 			if(user == null||Rtext.isEmpty(user.getId())){
 				rbw.setValue(user.getTitle());
 				return rbw;
 			}
			rbw.setLastTime(System.currentTimeMillis());
			rbw.setValue(user);
			String jsonVal=JSON.toJSONString(rbw);
			log.info("对象为 "+jsonVal);
			value.set(redisKey, jsonVal);
			stringRedisTemplate.expire(redisKey, 15552000, TimeUnit.SECONDS);
 		}
		log.info("添加到cookie");
		addCookie(response,"loginSessionId",redisKey,30*60);//判断cookie中是否含有loginSessionId  没有则添加cookie
		return rbw;
	}
	
	/**
	 * 根据用户姓名和密码从ISC上获取用户信息 
	 * @param userName 用户姓名  
	 * @param passwd 用户密码
	 * @return
	 */
	public  User getUserFromIsc(String userName,String passwd){
		log.info("ISC上获取用户信息");
		IIdentityService service = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
		User user = null;
		try {
			log.info("user "+user);
			user = service.userLoginAuth(userName, passwd);
			log.info("user "+user);
		} catch (Exception e) {
			log.error("get user info failed!"+userName+","+passwd,e);
			String errStr=e.getMessage();
			user=new User();
			user.setTitle("登录失败："+errStr);
		}
		return user;
	}
	
	
	/**
	 * 添加cookie值
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public  void addCookie(HttpServletResponse response, String name,String value, int maxAge) {
		log.info("添加cookie值");
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}
	
	/**
	 * 注销登录
	 * 
	 * @param request
	 */

	public void logOut(HttpServletRequest request,HttpServletResponse response) {
		log.info("注销登录");
		Cookie[] cookies = request.getCookies();
		for (Cookie co : cookies) {
			if (co.getName().equals("loginSessionId")) {
				String key = co.getValue();
				deleteCookie(response,key);
				//删除loginSessionId 和 value
				stringRedisTemplate.delete(key);
				log.info("删除key: "+key);
			}
		}
	}
	
	/**
	 * 删除Cookie
	 * 
	 * @param response
	 * @param name
	 */
	public void deleteCookie(HttpServletResponse response, String name) {
		log.info("删除Cookie");
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	/**
	 * 根据key获取redis数据，转换为User
	 */
	public CommonUser getUserFromRedis(String key){
		ValueOperations<String, String> value = stringRedisTemplate.opsForValue();
		String redisData = value.get(key);
		CommonUser user = new CommonUser();
		if (StringUtils.isEmpty(redisData)){
			return user;
		}
		RedisBeanWarp rbw = new RedisBeanWarp();
		rbw = JSON.parseObject(redisData, RedisBeanWarp.class);
		Map rbwMap = new HashMap<>();
		if(rbw.getValue() == null){
			rbwMap = JSON.parseObject(redisData, Map.class);
			String userString = ""+rbwMap.get("user");
			user = JSON.parseObject(userString, CommonUser.class);
		}else{
			user = JSON.parseObject(""+rbw.getValue(), CommonUser.class);
		}
		return user;
	}
	/**
	 * 清除请求路径 "/" 后缀
	 * @param redirect 请求路径
	 * @return
	 */
	public String redirectclean(String redirect) {
		if(redirect != null && !"null".equals(redirect)){
			if(redirect.lastIndexOf("//") > 0){
				redirect = redirect.substring(0, redirect.indexOf("//"));
			}else if (redirect.length() == redirect.lastIndexOf("/")+1){
				redirect = redirect.substring(0, redirect.length()-1);
			}
		}
		
		return redirect;
	}
	/**
	 * 将汉语转化为汉语全拼
	 * @param data
	 * @return
	 */
	public String converterToSpell(String data){
		String pinyin = "";
		if (data.length() > 0){
			pinyin = Pinyin4jUtil.converterToSpell(data);
		}
		return pinyin;
	}
	
	/**
	 * 将汉语转化为汉语简拼
	 * @param data
	 * @return
	 */
	public String converterToFirstSpell(String data){
		String pinyin = "";
		if (data.length() > 0){
			pinyin = Pinyin4jUtil.converterToFirstSpell(data);
		}
		return pinyin;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 * @description 根据用户id获得业务角色信息
	 * @author wanglongjiao
	 * @date 2014-2-19
	 * @see com.sgcc.erd.common.isc.IIscManageCommon#getRoleByUserId(java.lang.String)
	 */
	public List<Role> getRoleByUserId(String userId) {
		List<Role> list = new ArrayList<Role>();

		// 获取业务组织角色
		List<OrganizationalRole> orgRoleList = getOrgRolesByUserId(userId, null);

		// 获取对应角色
		if (orgRoleList != null && orgRoleList.size() > 0) {
			List<Role> tmplist = null;
			for (OrganizationalRole curOrgRole : orgRoleList) {
				try {
					tmplist = AdapterFactory.getRoleService().getRoleByRoleId(
							com.sgcc.bg.common.StringUtils.getObjectString(curOrgRole.getRoleId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (tmplist != null && tmplist.size() > 0) {
					// getRoleByRoleId只能获取一个角色对象，不知为啥非要用List，在此只取列表第一个
					if (!list.contains(tmplist.get(0))) {
						list.add(tmplist.get(0));
					}
				}
			}
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 * @description 根据用户ID及组织结构角色信息获取组织结构角色信息。
	 * @author wanglongjiao
	 * @date 2014-2-19
	 * @see com.sgcc.erd.common.isc.IIscManageCommon#getOrgRolesByUserId(java.lang.String,
	 * java.util.Map)
	 */
	public List<OrganizationalRole> getOrgRolesByUserId(String userId,
														Map<String, String> param) {
		List<OrganizationalRole> list = null;
		try {
			list = AdapterFactory.getRoleService().getOrgRolesByUserId(userId, "8ad584b4513ea25b01515707df160004",//ISC_APP_ID
					param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 根据业务组织角色标识及用户过滤条件获取业务组织角色下的用户信息
	 */
	public List<User> getUsersByOrgRole(String orgRoleId) throws Exception {   
		IIdentityService identityService = (IIdentityService) AdapterFactory.getInstance(Constants.CLASS_IDENTITY);
		Map<String,String> map = new HashMap<String, String>();
		List<User> list = identityService.getUsersByOrgRole(orgRoleId,map,new String[]{});
		return list;
	}
	
	/**
	 * List数据中依据某字段进行过滤相同数据
	 */
	public List<Map> listRemoveDuplicate(List<Map> list,String indentify){
		List<Map> result = new ArrayList<Map>();
		Set<String> tempSet = new HashSet<String>();//临时集合，放置map中value值，判断重复
		if(list != null && list.size()>0){
			for(int a = 0;a<list.size();a++){
				String identifyValue = "";
				if(list.get(a)!=null){
					identifyValue = list.get(a).get(indentify)==null?"":list.get(a).get(indentify).toString();
					if(tempSet.contains(identifyValue)){//判断同样条件下是否存在相同的值
						continue;
					}
					tempSet.add(identifyValue);
				}else{
					continue;
				}
				result.add(list.get(a));
			}
		}
		return result;
	}
	/**
	 * 设置让浏览器弹出下载对话框的Header. 根据浏览器的不同设置不同的编码格式 防止中文乱码
	 * 
	 * @param fileName
	 *            下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletRequest request,
			HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedfileName = null;
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && -1 != agent.indexOf("MSIE")) {// IE
				encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
				encodedfileName = new String(fileName.getBytes("UTF-8"),
						"iso-8859-1");
			} else {
				encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			}
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
