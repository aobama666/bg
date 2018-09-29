package com.sgcc.bg.service.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sgcc.bg.Sync.SyncData;
import com.sgcc.bg.common.DataUtils;
import com.sgcc.bg.mapper.DeptMapper;
import com.sgcc.bg.model.HRDept;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DeptService;

@Service
public class DeptServiceImlp implements DeptService{
	@Autowired
	private DeptMapper deptMapper;
	public Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	public String getDataFromDept(int page,int size,int deptId,String deptType){
		logger.info("-----hahhahah--hahha--------");
		Page<?> page2 = PageHelper.startPage(page,size);
		deptMapper.getDataFromDept(deptId,deptType);
		long total = page2.getTotal();
		List<Map<String, String>> list2 = (List<Map<String, String>>) page2.getResult();
		return DataUtils.getPageJsonStr(list2, total);
	}
	
	public String deleteFromDept(String deptId){
		logger.info("-----开始执行删除操作deleteFromDept--------");
		deptMapper.deleteFromDept(deptId);
		return "删除成功";
	}
	
	/*
	 * 部门解析存储
	 * */
	public String dealXmlAndSave() throws ParserConfigurationException, SAXException, IOException{
		logger.info("-----开始执行保存操作------");
		deptMapper.deleteFromDept("all");
		HashMap<String, String> map = new HashMap<>();
		SyncData syncData = new SyncData();
		String xml = SyncData.RedServiceXml();
		List<HRDept> list = SyncData.analyDeptsXmlDept(xml);
		/*for(int i = 0;i<list.size();i++){
			//String deptid=list.get(i).getDeptId();
			String deptid=""+(i+1);
			String deptName=""+list.get(i).getDeptName();
			String deptType=""+list.get(i).getDeptType();
			String parentId=""+list.get(i).getParentId();
			saveHandle(deptid,deptName,deptType,parentId);
		}*/
		
		for (HRDept hrDept : list) {
			if("".equals(hrDept.getDeptId())||hrDept.getDeptId()==null){
				hrDept.setDeptId("1");
			}
			System.out.println(hrDept.getDeptId());
			saveHandle(hrDept);
		}
		map.put("code", "0000");
		map.put("result", "success");
		System.out.println(list.size());
		return JSON.toJSONString(map);
	}
	
	public String saveHandle(HRDept hrDept){
		Map<String, String> rw =new HashMap<>();
		/*Map<String, String> map =new HashMap<>();
		map.put("deptId", "40001");
		map.put("deptName", "中国电科院");
		map.put("deptType", "1");
		map.put("parentId", "2");*/
		try {
			int a = deptMapper.addAll(hrDept);
			rw.put("success", "yse");
		} catch (Exception e) {
			e.printStackTrace();
			rw.put("faile", "yse");
		}
		return "";
	}
	
	/*
	 * 人员解析存储
	 * */
	public String dealXmlUser() throws ParserConfigurationException, SAXException, IOException{
		logger.info("-----开始执行User解析操作------");
		deptMapper.deleteFromUser("all");
		HashMap<String, String> map = new HashMap<>();
		SyncData syncData = new SyncData();
		String xml = SyncData.RedUserServiceXml();
		List<HRUser> list = SyncData.analyUserXml(xml);
		for (HRUser hrUser : list) {
			saveHandleUser(hrUser);
		}
		map.put("code", "0000");
		map.put("result", "success");
		return JSON.toJSONString(map);
	}
	
	public String saveHandleUser(HRUser hrUser){
		Map<String, String> rw =new HashMap<>();
		try {
			int a = deptMapper.addAllUser(hrUser);
			rw.put("success", "yse");
		} catch (Exception e) {
			e.printStackTrace();
			rw.put("faile", "yse");
			// TODO: handle exception
		}
		
		return "保存成功";
	}
	
	public String syncMergeDept(){
		
		return "";
	}
	
	@Override
	public List<Map<String, Object>> queryOutDelegationExport(String deptId) {
		
		return deptMapper.queryExportPrjs(deptId);
	}
}
