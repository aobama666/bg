package com.sgcc.bg.service.impl;

import com.sgcc.bg.common.*;
import com.sgcc.bg.mapper.ApproverMapper;
import com.sgcc.bg.mapper.BgConfigEfficacyMapper;
import com.sgcc.bg.service.ApproverService;
import com.sgcc.bg.service.BgConfigEfficacyService;
import com.sgcc.bg.service.DataDictionaryService;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BgConfigEfficacyServiceImpl implements BgConfigEfficacyService {

	@Autowired
	private BgConfigEfficacyMapper bgConfigEfficacyMapper;


	private static Logger log = LoggerFactory.getLogger(BgConfigEfficacyServiceImpl.class);

	@Override
	public List<Map<String, Object>> selectForConfigEfficacy(Map<String ,Object> efficacyInfo){
		return bgConfigEfficacyMapper.selectForConfigEfficacy(efficacyInfo);
	}
	@Override
	public List<Map<String, Object>> getDisConfigEfficacy(Map<String ,Object> efficacyInfo){
		return bgConfigEfficacyMapper.getDisConfigEfficacy(efficacyInfo);
	}
	@Override
	public int addConfigEfficacy(Map<String ,Object> efficacyInfo){
		return bgConfigEfficacyMapper.addConfigEfficacy(efficacyInfo);
	}
	@Override
	public int deleteConfigEfficacy(Map<String ,Object> efficacyInfo){
		return bgConfigEfficacyMapper.deleteConfigEfficacy(efficacyInfo);
	}
	@Override
	public int updataConfigEfficacy(  Map<String ,Object> efficacyInfo){
		return bgConfigEfficacyMapper.updataConfigEfficacy(efficacyInfo);
	}
	@Override
	public String exportSelectedItems(Map<String ,Object> efficacyInfo,String index, HttpServletResponse response) {
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> dataList = bgConfigEfficacyMapper.selectForConfigEfficacy(efficacyInfo);
		if(Rtext.isEmpty(index)){
			resultList = dataList;
		}else{
			String[] numArr = index.split(",");
			for (String numStr : numArr) {
				int num = Integer.parseInt(numStr);
				resultList.add(dataList.get(num));
			}
		}
		Object[][] title = {
				{ "年度", "YEAR","nowrap" },
				{ "月度", "MONTH","nowrap" },
				{ "校验考勤工时","EFFICACY_NAME"},
				{ "维护时间","UPDATE_DATE"},
				{ "维护人员","USERALIAS"}
		};
		ExportExcelHelper.getExcel(response, "校验配置-"+DateUtil.getDays(), title, resultList, "normal");
		return "";
	}








}
