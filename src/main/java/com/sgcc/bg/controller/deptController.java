package com.sgcc.bg.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;
import org.springframework.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP.Access.Request;
import com.sgcc.bg.service.DeptService;
import com.sgcc.bg.common.ExcelUtil;
/*
 *部门 
 */
@RestController
@RequestMapping(value="/DeptLink")
public class deptController {
	@Autowired 
	DeptService deptService;
	

	/*
	 * 部门、人员查询测试页面
	 */
	@RequestMapping(value="/indexTest")
	public ModelAndView indexTest(){
		ModelAndView model = new ModelAndView("demo/deptTest");
		return model;
	}
	/*
	 * 部门查询接口  查询条件可以是deptId和deptType
	 */
	@RequestMapping(value="/query",method = RequestMethod.GET)
	public String getDataFromDept(HttpServletRequest request){
		int page = request.getParameter("page")==null?0:Integer.parseInt(request.getParameter("page"));
		int size = request.getParameter("size")==null?0:Integer.parseInt(request.getParameter("size"));
		int deptId = request.getParameter("deptId")==null?0:Integer.parseInt(request.getParameter("deptId"));
		String deptType = request.getParameter("deptType")==null?"":request.getParameter("deptType");
		System.out.println(page+"---"+size);
		return deptService.getDataFromDept(page, size, deptId,deptType);
	}
	/*
	 * 部门删除接口 根据deptId
	 * */
	@RequestMapping(value="/delete",method = RequestMethod.GET)
	public String deleteDataFromDept(HttpServletRequest request){
		String deptId = request.getParameter("deptId")==null?"":request.getParameter("deptId");
		
		return deptService.deleteFromDept(deptId);
	}
	
	/*
	 * 部门解析XML，存储接口
	 * */
	@RequestMapping(value="/dealXmlAndSave")
	public String dealXmlAndSave() throws ParserConfigurationException, SAXException, IOException{
		String dealResult = deptService.dealXmlAndSave();
		return dealResult;
	}
	

	/*
	 * 人员解析XML，存储接口
	 * */
	@RequestMapping(value="/dealXmlUser")
	public String dealXmlUser() throws ParserConfigurationException, SAXException, IOException{
		String dealResult = deptService.dealXmlUser();
		return dealResult;
	}
	
	/*
	 * 同步部门表到正式表
	 * */
	@RequestMapping(value="/syncMergeDept")
	public String syncMergeDept(){
		String result = deptService.syncMergeDept();
		return "";
	}
	
	/*
	 * 导出功能测试
	 * */
	@RequestMapping(value="/export")
	public ResponseEntity<byte[]> export(@Param(value = "deptId") String deptId,HttpServletResponse response){
		//构建Excel表头
				LinkedHashMap<String,String> headermap = new LinkedHashMap<>(); 
				headermap.put("DEPTNAME", "部门名称");
				headermap.put("DEPTID", "部门id");
				headermap.put("PARENTID", "父id");
				headermap.put("DEPTTYPE", "部门类别");
				headermap.put("UPDATEDATE", "时间");
				//获取Excel数据信息
				List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
				if(StringUtils.isEmpty(deptId)){
					valueList = deptService.queryOutDelegationExport(null);
				}else{
					valueList = deptService.queryOutDelegationExport(deptId);
				}
				HSSFWorkbook workbook = ExcelUtil.PaddingExcel(headermap,valueList);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.valueOf("application/vnd.ms-excel;charset=UTF-8"));
				headers.set("Content-Disposition", "attachment; filename=export.xls");
				try {
					//设置返回头
					response.setHeader("Content-Disposition","attachment; filename=export.xls");
					response.setContentType("application/vnd.ms-excel;charset=UTF-8");
					workbook.write(response.getOutputStream());
					workbook.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return new ResponseEntity<byte[]>(headers,HttpStatus.OK);
	}
}
