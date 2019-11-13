package com.sgcc.bg.service.impl;

import com.sgcc.bg.common.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.sgcc.bg.mapper.ApproverMapper;
import com.sgcc.bg.service.ApproverService;
import com.sgcc.bg.service.DataDictionaryService;

@Service
public class ApproverServiceImpl implements ApproverService {


	@Autowired
	private ApproverMapper approverMapper;
	
	@Autowired
	private UserUtils userUtils;
	 
	
	
	@Autowired
	private DataDictionaryService dict;
	
	private static Logger log = LoggerFactory.getLogger(ApproverServiceImpl.class);

	@Override
	public List<Map<String, Object>>  selectForDatadicttionary(String  pcode){
		return approverMapper.selectForDatadicttionary(pcode);
	}
	@Override
	public List<Map<String, Object>>  selectForUserInfo(String  empCode){
		return approverMapper.selectForUserInfo(empCode);
	}
	@Override
	public  Map<String, Object> selectForDeptInfo(String  deptCode){
		return approverMapper.getDeptByDeptCode(deptCode);
	}
	@Override
	public List<Map<String, Object>> getInfoForShow(String target) {
		List<Map<String, Object>> result = new ArrayList<>();
		if("organType".equals(target)){
			result = approverMapper.getOrganType();
		}else if("approveRule".equals(target)){
			result = approverMapper.getApproveRule();
		}
		return result;
	}

	@Override
	public int addApprover(String empCode, String deptCode, String subType,String priority) {
		//如果新增组织在权限表中存在父级组织，则不再添加
		if(existsApprover(empCode,deptCode,subType)){
			return 0;
		}else{
			approverMapper.addApprover(empCode,deptCode,subType ,priority);//在sql中已做了判断：如果该人员已存在该类型角色，则不再重复添加
			return 1;
		}
	}

	@Override
	public int addNewApprover( Map<String ,Object> approverInfo) {
		approverMapper.addNewApprover(approverInfo);
		return 1;
	}
	@Override
	public int updataApprover(Map<String ,Object> approverInfo){
		approverMapper.updataApprover(approverInfo);
		return 1;
	}

	/**
	 * 验证某人是否权限重复
	 * @param empCode
	 * @param deptCode
	 * @return
	 */
	private boolean existsApprover(String empCode,String deptCode, String subType){
		Map<String ,Object> approverList=new HashMap<>();
		approverList.put("empCode",empCode);
		approverList.put("deptCode",deptCode);
		Map<String,String> dictMap=dict.getDictDataByPcode("submitUserType");
		for (Map.Entry<String,String> entry : dictMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(subType.equals(value)){
				subType=key;
			}
		}
		approverList.put("subType",subType);
		List<Map<String, Object>> apprpverList = approverMapper.getAllApprovers(approverList);
		if(apprpverList.isEmpty()){
			return false;
		} else {
			return true;
		}

	}

	@Override
	public List<Map<String, Object>> getAllApprovers(Map<String ,Object> approverList) {
		return approverMapper.getAllApprovers(approverList);
	}
	@Override
	public List<Map<String, Object>> isforApprovers(Map<String ,Object> approverList) {
		return approverMapper.getAllApprovers(approverList);
	}

	@Override
	public String deleteApprover(String id) {
		//顺序不能颠倒，删除组织后再删除角色（如果该用户已不含该角色下的部门，则删除角色）
		approverMapper.deleteApprover(id);
		return "删除成功！";
	}

	private boolean validateHrCode(String hrCode){
		CommonCurrentUser user = userUtils.getCommonCurrentUserByHrCode(hrCode);
		return user==null?false:true;
	}
	
	
	@Override
	public String[] parseApproverFile(InputStream in) {
		HSSFWorkbook wb = null;
		//获取通过验证的专责信息
		//List<Map<String,Object>> correctList = new ArrayList<>();
		int count = 0;
		//出错项目信息
		List<Map<String,Object>> errorList = new ArrayList<>();
		//存储在ftp的错误文件名称
		String errorUUID = "";
		try{
			POIFSFileSystem fs = null;
			HSSFSheet sheet;
			HSSFRow row;
			HSSFCell cell;
			// 获取POI操作对象
			fs = new POIFSFileSystem(in);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
			// 得到sheet内总行数
			int rows = sheet.getLastRowNum();
			//检查重复
			Set<String> repeatChecker = new HashSet<String>();
			
			//从数据字典中获取项目类型
			Map<String,String> dictMap=dict.getDictDataByPcode("submitUserType");
			log.info("项目信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[7];
			for (int i = 1; i <=rows; i++) {// 从第2行开始是正式数据
				// 获取正式数据并封装进cellValue数组中
				StringBuffer checkStr = new StringBuffer();
				log.info("第" + (i + 1) + "行");
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				for (int c = 0; c < cellValue.length; c++) {
					cell= row.getCell(c);// StringUtils.trimToEmpty(sheet.getCell(c,
					if (cell != null) {
						cellValue[c]=ExcelUtil.getStringCellValueForOnLine(cell);
					}else{
						cellValue[c]="";
						log.info("第"+(i + 1)+"行 第"+(c+1)+"列 is null");
					}
					checkStr.append(cellValue[c]);
					log.info("cellValue is " + cellValue[c]);
				}
				if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {// 校验此行是否为空
					StringBuffer errorInfo = new StringBuffer();
					Set<Integer> errorNum = new HashSet<Integer>();
					String empCode = cellValue[2];
					String subType = cellValue[3];
					String deptCode = cellValue[5];
					String priority = cellValue[6];
					
					// 校验人资编号
					if (Rtext.isEmpty(empCode)) {
						errorInfo.append("人员编号不能为空！ ");
						errorNum.add(2);
					} else if(!validateHrCode(empCode)){
						errorInfo.append("人员编号错误！ ");
						errorNum.add(2);
					}
					
					// 校验角色
					if (Rtext.isEmpty(subType)) {
						errorInfo.append("审核人类型不能为空！ ");
						errorNum.add(3);
					} else if(!dictMap.containsValue(subType)){
						errorInfo.append("无此审核人类型！ ");
						errorNum.add(3);
					}
					
					// 校验部门编号
					if (Rtext.isEmpty(deptCode)) {
						errorInfo.append("部门编号不能为空！ ");
						errorNum.add(5);
					} else {
						Map<String,Object> dept = approverMapper.getDeptByDeptCode(deptCode);
						if (dept==null) {
							errorInfo.append("部门编号错误！ ");
							errorNum.add(5);
						}
						String type=String.valueOf(dept.get("TYPE"));
						if(!"1".equals(type)){
							errorInfo.append("部门编号错误！必须为部门级别 ");
							errorNum.add(5);
						}
					}
					boolean  flag=  StringUtil.PositiveNumber(priority);
					if(!flag){
						errorInfo.append("优先级错误！必须为大于0的整数 ");
						errorNum.add(6);
					}

					//如果新增组织在权限表中存在父级组织，则不再添加
					/*if(errorNum.size()==0 && existsApprover(empCode,deptCode)){
						errorNum.add(5);
						errorInfo.append("已存在上级组织权限！ ");
					}*/
					
					//校验重复
					String str = empCode+subType+deptCode;
					if(errorNum.size()==0 && (!repeatChecker.add(str) || existsApprover(empCode,deptCode,subType))){
						errorNum.add(2);
						errorNum.add(3);
						errorNum.add(5);
						errorInfo.append("重复添加！  ");
					}

					// 校验结束，分流数据
					if (errorNum.size()==0) {// 通过校验
						//保存正确数据
						for (Map.Entry<String,String> entry : dictMap.entrySet()) {
							String key = entry.getKey();
							String value = entry.getValue();
							if(subType.equals(value)){
								subType=key;
							}
						}
						int result = addApprover(empCode,deptCode,subType,priority);
						if(result==1){
							count++;
						}
					} else {// 未通过校验
						Map<String,Object> errortMap = new HashMap<String, Object>();
						errortMap.put("sqnum", cellValue[0]);
						errortMap.put("empName", cellValue[1]);
						errortMap.put("empCode", cellValue[2]);
						errortMap.put("subType", cellValue[3]);
						errortMap.put("deptName", cellValue[4]);
						errortMap.put("deptCode", cellValue[5]);
						errortMap.put("priority", cellValue[6]);
						errortMap.put("errInfo",errorInfo.toString());
						errortMap.put("errSet",errorNum);
						errorList.add(errortMap);
					}
				}
			}

			// 返回错误数据
			if (errorList.size() > 0) {
				log.info("出错的项目： " + errorList);
				// 生成错误信息文件
				Object[][] title = { 
						{ "序号\r\n（选填）", "sqnum","nowrap" }, 
						{ "姓名\r\n（选填）", "empName","nowrap" },
						{ "人员编号\r\n（必填）", "empCode" ,"nowrap"},
						{ "角色\r\n（必填）","subType","nowrap"}, 
						{ "组织名称\r\n（选填）","deptName","nowrap"},
						{ "组织编号\r\n（必填）","deptCode","nowrap"},
						{ "审核优先级\r\n（必填）","priority","nowrap"},
						{ "错误说明","errInfo"}
				};
				errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
				log.info("errorUUID: " + errorUUID);
			}
		}catch (Exception e) {
			e.printStackTrace();
			String[] object = {"Error:项目信息导入失败，请重新导入！",""};
			return object;
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		String[] object = {"成功导入项目信息"+ count +"条，失败"+errorList.size()+"条",errorUUID};
		return object;
		//
	}

	@Override
	public String exportSelectedItems(String username, String deptCode, String roleCode, String index,
			HttpServletResponse response) {
		username = Rtext.toStringTrim(username, "");
		deptCode = Rtext.toStringTrim(deptCode, "");
		roleCode = Rtext.toStringTrim(roleCode, "");
		index = Rtext.toStringTrim(index, "");
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
		//复用页面查询方法
		Map<String ,Object> approverList=new HashMap<>();
		approverList.put("username",username);
		approverList.put("deptCode",deptCode);
		approverList.put("subType",roleCode);
		List<Map<String,Object>> dataList = getAllApprovers(approverList);
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
							 { "姓名", "USERALIAS","nowrap" },
							 { "审核人类别", "SUBNAME","nowrap" },
							 { "组织名称","DEPTNAME"},
				             { "组织等级","TYPE"},
				             { "审核优先级","PRIORITY"}
							// { "组织编号","DEPTCODE","nowrap"}
							};
		ExportExcelHelper.getExcel(response, "报工管理-审批权限授权-"+DateUtil.getDays(), title, resultList, "normal");
		return "";
	}

	@Override
	public List<Map<String, Object>> selectForApproveRule(Map<String, Object> roleList) {
		return approverMapper.selectForApproveRule(roleList);
	}
	@Override
	public List<Map<String, Object>> selectForApproverOrgant(Map<String, Object> organList) {
		return approverMapper.selectForApproverOrgant(organList);
	}

}
