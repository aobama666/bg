package com.sgcc.bg.ky.irp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.DataUtils;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.ky.irp.mapper.IrpCountMapper;
import com.sgcc.bg.ky.irp.service.IrpCountService;

@Controller
@RequestMapping(value="/kyirp")
public class IrpController {
	
	@Autowired
	private IrpCountService irpCountService;
	@Autowired
	private IrpCountMapper irpCountMapper;
	
	
	/**
	 * 科技部 数据统计  页面
	 * @return
	 */
	@RequestMapping(value = "/dataCountIndexForY", method = RequestMethod.GET)
	public ModelAndView queryDataCountIndexForY(HttpServletRequest request){
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		//默认当前年月
		if(countMonth.length()==0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			countMonth = sdf.format(new Date());
		}
		
		ModelAndView model = new ModelAndView("ky/irp/datacount/dataCountIndexForY");
		return model;
	}
	
	/**
	 * 科技部 数据统计  页面  按单位
	 * @return
	 */
	@RequestMapping(value = "/dataCountIndexForD", method = RequestMethod.GET)
	public ModelAndView dataCountIndexForD(HttpServletRequest request){
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		//默认当前年月
		if(countMonth.length()==0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			countMonth = sdf.format(new Date());
		}
		
		ModelAndView model = new ModelAndView("ky/irp/datacount/dataCountIndexForD");
		return model;
	}
	
		
	/**
	 * 科技部 数据统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDataCountForY", method = RequestMethod.POST)
	public String queryDataCountForY(HttpServletRequest request){
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		//默认当前年月
		if(countMonth.length()==0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			countMonth = sdf.format(new Date());
		}
		
		List<Map<String, Object>> dataList = irpCountService.queryIrpDataCountForAll(countMonth);		
		long total = dataList.size();
		
		return DataUtils.getPageJsonStr(dataList, total);
	}
	
	/**
	 * 科技部 数据统计 各单位
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryDeptDataCountForY", method = RequestMethod.POST)
	public String queryDeptDataCountForY(HttpServletRequest request){
		//统计月份
		String countMonth=request.getParameter("countMonth")==null?"":request.getParameter("countMonth").trim();
		//默认当前年月
		if(countMonth.length()==0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			countMonth = sdf.format(new Date());
		}
		
		List<Map<String, Object>> dataList = irpCountService.queryDeptIrpDataCountForAll(countMonth);		
		long total = dataList.size();
		
		return DataUtils.getPageJsonStr(dataList, total);
	}
	
	/**
	 * 数据统计 单位
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryIrpDataCountForD", method = RequestMethod.POST)
	public String queryIrpDataCountForD(HttpServletRequest request){
		List<Map<String, Object>> dataList = irpCountService.queryIrpDataCountForDept(request);		
		long total = dataList.size();
		
		return DataUtils.getPageJsonStr(dataList, total);
	}
	
	/**
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/exportIrpTotal", method = RequestMethod.GET)
	public void exportIrpTotal(HttpServletRequest request,HttpServletResponse response) {
		irpCountService.exportIrpTotal(request,response);
	};
	
	@RequestMapping(value= "/file/download/{id}",method=RequestMethod.GET)
	public void downloadFile(@PathVariable String id, HttpServletRequest request, HttpServletResponse response, String preview) {
		//String filePath = planService.getFilePathByFileId(id);
		Map<String, String> filePathMap = irpCountMapper.getFileInfoByFileId(id);
		String fileName = filePathMap.get("FTP_FILE_NAME");
		String fileDir = filePathMap.get("FTP_FILE_PATH");
		if(fileDir.startsWith("/")){
			fileDir=fileDir.substring(1);
		}
		File file = new File(FtpUtils.InternationUploadPath + fileDir);
		if (file.exists()) {
			OutputStream outputStream = null;
			try {
				outputStream = new BufferedOutputStream(response.getOutputStream());
				outputStream.write(FileUtils.readFileToByteArray(file));
				outputStream.flush();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}