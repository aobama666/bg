package com.sgcc.bg.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mongodb.util.StringParseUtil;
import com.sgcc.bg.mapper.OrganWorkingStaticMapper;
import com.sgcc.bg.service.OrganWorkingStaticService;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;
import com.sgcc.bg.workinghourinfo.Utils.DataUtil1;
@Service
public class OrganWorkingStaticImpl implements OrganWorkingStaticService{
	@Autowired
	OrganWorkingStaticMapper organWorkingStaticMapper;
	
	public String queryDept(HttpServletRequest request){
		String Atype = request.getParameter("Atype")== null?"":request.getParameter("Atype");
		String startTime = request.getParameter("startTime")== null?"":request.getParameter("startTime");
		String endTime = request.getParameter("endTime")== null?"":request.getParameter("endTime");
		String projectName = request.getParameter("projectName")== null?"":request.getParameter("projectName");
		String organ = request.getParameter("organ")== null?"":request.getParameter("organ");
		String userName = request.getParameter("userName")== null?"":request.getParameter("userName");
		String Btype = request.getParameter("Btype")== null?"":request.getParameter("Btype");
		int page = request.getParameter("page")== null?1:Integer.parseInt(request.getParameter("page"));
		int limit = request.getParameter("limit")== null?10:Integer.parseInt(request.getParameter("limit"));
		Map<String, Object> map = new HashMap<>();
		if("".equals(startTime)){
			map.put("status", "201");
			map.put("msg", "开始时间不能为空");
			return JSON.toJSONString(map);
		}
		if("".equals(endTime)){
			map.put("status", "201");
			map.put("msg", "结束时间不能为空");
			return JSON.toJSONString(map);
		}
		List<Map<String,Object>> totalList = getDealMapper(Atype,startTime,endTime,projectName,organ,userName,Btype);
		int total = totalList.size();
		int end = page*limit;
		if(end>total){
			end = total;
		}
		List<Map<String, Object>> list = totalList.subList((page-1)*limit, end);
		map.put("status", "200");
		map.put("items", list);
		map.put("totalCount", total);
		return JSON.toJSONString(map);
	}
	public static List<Map<String, Object>> getDealMapper(String Atype,String startTime,String endTime,String projectName,String organ,String userName,String Btype){
		DataUtil1  DateUtil=new DataUtil1();
		 List<DataBean>  DataBeanlist=null;
		 try {
		   if(Atype.equals("A")){//日
			   DataBeanlist=DateUtil.getDatas(startTime,endTime);
		   }else if(Atype.equals("B")){//周
			   DataBeanlist=DateUtil.getWeeks(startTime,endTime);
		   }else if(Atype.equals("C")){//月
			   DataBeanlist=DateUtil.getMonths(startTime,endTime);
		   }else if(Atype.equals("D")){//季度
			   DataBeanlist=DateUtil.getQuarters(startTime,endTime);
		   }else if(Atype.equals("E")){//年
			   DataBeanlist=DateUtil.getYears(startTime,endTime);
		   }else if(Atype.equals("F")){//自当义
			   DataBeanlist=DateUtil.getCustom(startTime,endTime);
		   }                                                                              
		} catch (ParseException e) {
				e.printStackTrace();
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String ,Object>>();
		for(int i=0;i<DataBeanlist.size();i++){
			Map<String, Object> map = new HashMap<String,Object>();
			String startData = DataBeanlist.get(i).getStartData();
			String endData = DataBeanlist.get(i).getEndData();
			String StartAndEndData = startData + "至" + endData;
			String deptName = "1";
			String workTime = "2";
			String proWorkTime = "3";
			String noProWorkTime = "4";
			String standardWorkTime = "5";
			String workDegree = "6";
			
			map.put("StartAndEndData", StartAndEndData);//统计周期
			map.put("deptName", deptName);//部门
			map.put("workTime", workTime);//投入工时
			map.put("proWorkTime", workTime);//项目投入工时
			map.put("noProWorkTime", noProWorkTime);//非项目投入工时
			map.put("standardWorkTime", standardWorkTime);//标准投入工时
			map.put("workDegree", workDegree);//工作饱和度
			list.add(map);
		}
		return list;
	}
	public static void main(String[] args){
		List<Map<String,Object>> totalList = getDealMapper("B","2018-08-01","2018-08-31","","","","1");
		System.out.println(totalList);
	}
}
