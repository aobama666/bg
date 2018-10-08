package com.sgcc.bg.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.mapper.ConfigMapper;

import ch.qos.logback.classic.Logger;

/**
 * dddasd
 * @author Administrator
 * 从数据库中读取配置信息
 */
@Service
public class ConfigUtils {

	private static Logger log = (Logger) LoggerFactory.getLogger(ConfigUtils.class);
	private static Map<String, String> configMap = new HashMap<String, String>();

	@Autowired
	private ConfigMapper configMapper;

	/**
	 * 初始化静态变量configMap或重新刷新configMap
	 */
	@PostConstruct
	public void initConfigMap() {
		List<Map<String, String>> list = configMapper.getConfig();
		if (list == null || list.size() <= 0) {
			return;
		}
		configMap.clear();
		for (Map<String, String> map : list) {
			configMap.put(map.get("KEY"), map.get("VALUE"));
		}
		log.info("=========================");
		log.info("redis:"+configMap.get("redis_addr"));
		log.info("=========================");
	}

	/**
	 * 初始化版本号
	 */
	@PostConstruct
	public void initVersionNo() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String versionNo=sdf.format(new Date());
		log.info("=========================");
		log.info("versionNo:"+versionNo);
		VersionUtils.verNo=versionNo;
		log.info("=========================");
	}
	
	/**
	 * 根据传入的key获取配置
	 * 
	 * @param key
	 *            对应数据库中的key列
	 * @return 对应数据库中的value列
	 */
	public static String getConfig(String key) {
		return configMap.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public String getUserLoginList(HttpServletRequest request){
		//获取 第几页
		int pageNum=Integer.parseInt(request.getParameter("page"));
		//获取每页多少条
		int limit=Integer.parseInt(request.getParameter("limit"));
		//调用分页插件 调用PageHelper.startPage(pageNum,limit) 这个方法时 他会自动对之后执行的第一条SQL进行操作
		Page<?> page=PageHelper.startPage(pageNum,limit);
		String username=request.getParameter("username");
		String beginDate=request.getParameter("beginDate");
		String endDate=request.getParameter("endDate");
		//正常执行我们写好的查询方法
		configMapper.getUserLoginList(username, beginDate, endDate);
		//获取总条数
		long total=page.getTotal();
		//获取数据集合
		List<Map<String,String>> dataList=(List<Map<String, String>>) page.getResult();
		return DataUtils.getPageJsonStr(dataList, total);
	}
	
	public String getUserLoginLog(HttpServletRequest request){
		String uwlID=request.getParameter("uwlID");
		
		List<Map<String,String>> data=new ArrayList<Map<String,String>>();
		
		//在这开始写我们的方法
		List<Map<String,String>> data1=configMapper.getCurrentLoginInfo(uwlID);
		Map<String,String> map1=data1.get(0);
		String username=map1.get("USERNAME");
		String currentDate=map1.get("DATECREATED");
		
		Map<String,String> map_1=new HashMap<String, String>();
		map_1.put("R", "-1");
		map_1.put("REMARK", "用户查看了待办列表！");
		map_1.put("DATECREATED", currentDate);
		data.add(map_1);
		
		Map<String,String> map_2=new HashMap<String, String>();
		map_2.put("R", "-1");
		map_2.put("REMARK", "用户查看了通知公告！");
		map_2.put("DATECREATED", currentDate);
		data.add(map_2);
		
		List<Map<String,String>> dataList=configMapper.getUserLoginLogList(uwlID);
		data.addAll(dataList);
		
		List<Map<String,String>> data2=configMapper.getNextLoginInfo(username, currentDate);
		Map<String,String> map2=data2.get(0);
		if(map2!=null&&!map2.isEmpty()){
			String nextDate=map2.get("DATECREATED");
			List<Map<String,String>> data3=configMapper.getAuditInfo(username, currentDate, nextDate);
			for (int i = 0; i < data3.size(); i++) {
				Map<String,String> map_temp=data3.get(i);
				String date=map_temp.get("auditdate");
				Map<String,String> map_new=new HashMap<String, String>();
				map_new.put("R", "");
				map_new.put("REMARK", "用户审批了一条代办！");
				map_new.put("DATECREATED", date);
				data.add(map_new);
			}
		}
		
		List<Map<String,String>> new_data=new ArrayList<Map<String,String>>();
		for (int i = 0; i < data.size(); i++) {
			Map<String,String> temp_map=data.get(i);
			temp_map.put("R", String.valueOf(i+1));
			new_data.add(temp_map);
		}
		
		//获取总条数
		long total=new_data.size();
		//获取数据集合
		return DataUtils.getPageJsonStr(new_data, total);
	}
}
