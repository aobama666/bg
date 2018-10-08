package com.sgcc.bg.service;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface DeptService {
	public String getDataFromDept(int page,int size,int deptId,String deptType);
	public String deleteFromDept(String deptId);
	
	public String dealXmlAndSave() throws ParserConfigurationException, SAXException, IOException;
	
	public String dealXmlUser() throws ParserConfigurationException, SAXException, IOException;
	
	public String syncMergeDept();
	public List<Map<String,Object>> queryOutDelegationExport(String deptId);
}
