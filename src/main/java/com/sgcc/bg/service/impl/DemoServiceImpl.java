package com.sgcc.bg.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.common.DataUtils;
import com.sgcc.bg.mapper.DemoMapper;
import com.sgcc.bg.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	
	@Autowired
	private DemoMapper demoMapper;

	@Override
	public String welcome() {
		return "北京欢迎你";
	}

	@Override
	public String getUserAll() {
		return demoMapper.getAll().toString();
	}

	@Override
	public int addUser(String id, String name, String age, String sex) {
		return demoMapper.addUser(id, name, age, sex);
	}

	@Override
	public int delUser(String id) {
		return demoMapper.delUser(id);
	}

	@Override
	public int editUser(HttpServletRequest request) {
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		return demoMapper.editUser(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAudititemListDemp(HttpServletRequest request) {
		//获取 第几页
		int pageNum=Integer.parseInt(request.getParameter("page"));
		//获取每页多少条
		int limit=Integer.parseInt(request.getParameter("limit"));
		//调用分页插件 调用PageHelper.startPage(pageNum,limit) 这个方法时 他会自动对之后执行的第一条SQL进行操作
		Page<?> page=PageHelper.startPage(pageNum,limit);
		String title=request.getParameter("title");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String approveStatus=request.getParameter("approveStatus");
		String orgin=request.getParameter("orgin");
		//正常执行我们写好的查询方法
		demoMapper.getAudititemDemo(title,startTime,endTime,approveStatus,orgin);
		//获取总条数
		long total=page.getTotal();
		//获取数据集合
		List<Map<String,String>> dataList=(List<Map<String, String>>) page.getResult();
		return DataUtils.getPageJsonStr(dataList, total);
	}

	public void test() {
		System.out.println("cccc");
	}

}
