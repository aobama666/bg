package com.sgcc.bg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.sgcc.bg.service.DemoService;

@Controller
@RequestMapping(value="demo")
public class DemoController {
	
	@Autowired
	private DemoService demoService;
	
	@ResponseBody
	@RequestMapping("/welcome")
	public String welcome(){
		System.out.println("asdasd");
		return demoService.welcome();
	}
	
	@ResponseBody
	@RequestMapping("/getUserAll")
	public String getUserAll(){
		return demoService.getUserAll();
	}
	
	@ResponseBody
	@RequestMapping("/addUser")
	public int addUser(String id,String name,String age,String sex){
		System.out.println(id);
		return demoService.addUser(id, name, age, sex);
	}
	
	@ResponseBody
	@RequestMapping("/delUser")
	public int delUser(HttpServletRequest request){
		String id=request.getParameter("id");
		System.out.println(id);
		return demoService.delUser(id);
	}
	
	@ResponseBody
	@RequestMapping("/editUser")
	public int editUser(HttpServletRequest request){
		return demoService.editUser(request);
	}
	
	/**
	 * demo-index mmGrid数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/indexPage")
	public String indexPage(){
		List<Object> jsonarry =new ArrayList<Object>();   //Map 是{}  List 是[]

		Map<String,String> obj =new HashMap<String,String>();
		obj.put("hex2", "1");
		obj.put("label", "Yshubumen");
		obj.put("rgb", "(205, 149, 117)");
		obj.put("phone", "17742784856");
		
		Map<String,String> obj2 = new HashMap<String,String>();
		obj2.put("hex2", "2");
		obj2.put("label", "Mmbumen");
		obj2.put("rgb", "(205, 149, 117)");
		obj2.put("phone", "18542784856");
		
		Map<String,String> obj3 = new HashMap<String,String>();
		obj3.put("hex2", "3");
		obj3.put("label", "Ywwubumen");
		obj3.put("rgb", "(253, 217, 181)");
		obj3.put("phone", "14442784856");
		
		Map<String,String> obj4= new HashMap<String,String>();
		obj4.put("hex2", "4");
		obj4.put("label", "Xintongsuo");
		obj4.put("rgb", "(120, 219, 226)");
		obj4.put("phone", "15542784856");
		
		Map<String,String> obj5= new HashMap<String,String>();
		obj5.put("hex2", "5");
		obj5.put("label", "Anzhibu");
		obj5.put("rgb", "(102, 153, 204)");
		obj5.put("phone", "18512457845");
		
		Map<String,String> obj6= new HashMap<String,String>();
		obj6.put("hex2", "6");
		obj6.put("label", "Yuanban");
		obj6.put("rgb", "(115, 102, 189)");
		obj6.put("phone", "14258745863");
		
		Map<String,String> obj7= new HashMap<String,String>();
		obj7.put("hex2", "7");
		obj7.put("label", "Bangongshixinwenxinxi");
		obj7.put("rgb", "(28, 211, 162)");
		obj7.put("phone", "17584235996");

		
		jsonarry.add(obj);
		jsonarry.add(obj2);
		jsonarry.add(obj3);
		jsonarry.add(obj4);
		jsonarry.add(obj5);
		jsonarry.add(obj6);
		jsonarry.add(obj7);
		
		Map<String,Object> jsonMap =new HashMap<String,Object>();
		jsonMap.put("items",jsonarry);
		jsonMap.put("totalCount", 7);
		
		return JSONObject.toJSONString(jsonMap);
	}
	
	@RequestMapping("/authentication_add")
	public ModelAndView authentication_add(){
		ModelAndView model = new ModelAndView("demo/authentication_add");
		return model;
	}
	
	@RequestMapping("/authentication_import_excel_page")
	public ModelAndView authentication_import_excel_page(){
		ModelAndView model = new ModelAndView("demo/authentication_import_excel_page");
		return model;
	}
	
	@RequestMapping("/authentication_index_yg")
	public ModelAndView authentication_index_yg(){
		ModelAndView model = new ModelAndView("demo/authentication_index_yg");
		return model;
	}
	
	@RequestMapping("/authentication_index")
	public ModelAndView authentication_index(){
		ModelAndView model = new ModelAndView("demo/authentication_index");
		return model;
	}
	
	@RequestMapping("/authentication_update")
	public ModelAndView authentication_update(){
		ModelAndView model = new ModelAndView("demo/authentication_update");
		return model;
	}
}
