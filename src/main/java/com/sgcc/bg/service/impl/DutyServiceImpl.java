package com.sgcc.bg.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.CommonCurrentUser;
import com.sgcc.bg.common.ExcelUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.UserUtils;
import com.sgcc.bg.mapper.DutyMapper;
import com.sgcc.bg.service.DutyService;

@Service
public class DutyServiceImpl implements DutyService {
	@Autowired
	private DutyMapper dutyMapper;
	
	@Autowired
	private UserUtils userUtils;
	
	private static Logger log = LoggerFactory.getLogger(DutyServiceImpl.class);

	@Override
	public int addDuty(String empCode, String deptCode, String roleCode) {
		//如果新增组织在权限表中存在父级组织，则不再添加
		if(existsFatherOrgan(empCode,deptCode)) return 0;
		
		//新增组织下如果有组织，则将其全部删除；如果有上级组织，则不允许添加
		if("MANAGER_UNIT".equals(roleCode) || "MANAGER_KJB".equals(roleCode)){
			dutyMapper.deleteOrganByPDeptCode(empCode,null);
		}else if("MANAGER_DEPT".equals(roleCode)){
			dutyMapper.deleteOrganByPDeptCode(empCode,deptCode);
		}else if("MANAGER_LAB".equals(roleCode)){
			
		}
		dutyMapper.deleteRole(empCode);
		
		dutyMapper.addUserRole(empCode,roleCode);//在sql中已做了判断：如果该人员已存在该类型角色，则不再重复添加
		dutyMapper.addUserOrgan(empCode,deptCode);
		return 1;
	}
	
	/**
	 * 验证某人是否存在该组织对应的上级部门的权限
	 * @param empCode
	 * @param deptCode
	 * @return
	 */
	private boolean existsFatherOrgan(String empCode,String deptCode){
		Map<String,Object> fatherOrgan = dutyMapper.getFatherOrgan(empCode,deptCode);
		return fatherOrgan==null?false:true;
	}

	@Override
	public List<Map<String, Object>> getAllDuties(String username,String deptCode,String roleCode) {
		return dutyMapper.getAllDuties(username, deptCode, roleCode);
	}

	@Override
	public String deleteDuty(String hrCode, String deptCode, String roleCode) {
		//顺序不能颠倒，删除组织后再删除角色（如果该用户已不含该角色下的部门，则删除角色）
		dutyMapper.deleteOrgan(hrCode,deptCode);
		dutyMapper.deleteRole(hrCode);
		return "删除成功！";
	}

	private boolean validateHrCode(String hrCode){
		CommonCurrentUser user = userUtils.getCommonCurrentUserByHrCode(hrCode);
		return user==null?false:true;
	}
	
	private boolean validateDeptCode(String deptCode){
		Map<String,Object> dept = dutyMapper.getDeptByDeptCode(deptCode);
		return dept==null?false:true;
	}
	
	@Override
	public String[] parseDutyExcel(InputStream in) {

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
			//
			String roleType = "院专责,科技部专责,部门专责,处室专责"; 
			log.info("项目信息excel表格最后一行： " + rows);
			/* 保存有效的Excel模版列数 */
			String[] cellValue = new String[6];
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
					String role = cellValue[3];
					String deptCode = cellValue[5];
					
					// 校验人资编号
					if (Rtext.isEmpty(empCode)) {
						errorInfo.append("人员编号不能为空！");
						errorNum.add(2);
					} else if(!validateHrCode(empCode)){
						errorInfo.append("人员编号错误！");
						errorNum.add(2);
					}
					
					// 校验角色
					if (Rtext.isEmpty(role)) {
						errorInfo.append("角色不能为空！");
						errorNum.add(3);
					} else if(!roleType.contains(role)){
						errorInfo.append("无此角色类型！");
						errorNum.add(3);
					}
					
					// 校验部门编号
					if (Rtext.isEmpty(deptCode)) {
						errorInfo.append("部门编号不能为空！");
						errorNum.add(5);
					} else {
						Map<String,Object> dept = dutyMapper.getDeptByDeptCode(deptCode);
						if (dept==null) {
							errorInfo.append("部门编号错误！");
							errorNum.add(5);
						}else if(!errorNum.contains(3)) {
							String type = Rtext.toString(dept.get("TYPE"));
							if(
								("0".equals(type) && !"院专责".equals(role) && !"科技部专责".equals(role)) ||
								("1".equals(type) && !"部门专责".equals(role)) ||
								("2".equals(type) && !"处室专责".equals(role))){
								
								errorInfo.append("组织类型和角色不匹配！ ");
								errorNum.add(3);
								errorNum.add(5);
							}
						}
					}
					
					//如果新增组织在权限表中存在父级组织，则不再添加
					if(errorNum.size()==0 && existsFatherOrgan(empCode,deptCode)){
						errorNum.add(5);
						errorInfo.append("已存在上级组织权限！ ");
					}
					
					//校验重复
					String str = empCode+role+deptCode;
					if(errorNum.size()==0 && !repeatChecker.add(str)){
						errorNum.add(2);
						errorNum.add(5);
						errorInfo.append("重复添加！ ");
					}
					
					// 校验结束，分流数据
					if (errorNum.size()==0) {// 通过校验
						//保存正确数据
						String roleCode = null;
						if("院专责".equals(role)){
							roleCode = "MANAGER_UNIT";
						}else if("科技部专责".equals(role)){
							roleCode = "MANAGER_KJB";
						}else if("部门专责".equals(role)){
							roleCode = "MANAGER_DEPT";
						}else if("处室专责".equals(role)){
							roleCode = "MANAGER_LAB";
						}
						int result = addDuty(empCode,deptCode,roleCode);
						
						if(result==1) count++;
						
					} else {// 未通过校验
						Map<String,Object> errortMap = new HashMap<String, Object>();
						errortMap.put("sqnum", cellValue[0]);
						errortMap.put("empName", cellValue[1]);
						errortMap.put("empCode", cellValue[2]);
						errortMap.put("role", cellValue[3]);
						errortMap.put("deptName", cellValue[4]);
						errortMap.put("deptCode", cellValue[5]);
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
						{ "人员姓名\r\n（选填）", "empName","nowrap" }, 
						{ "人员编号\r\n（必填）", "empCode" ,"nowrap"},
						{ "角色\r\n（必填）","role","nowrap"}, 
						{ "组织名称\r\n（选填）","deptName","nowrap"},
						{ "组织编号\r\n（必填）","deptCode","nowrap"}, 
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
	
	/*	public static void main(String[] args) {
		syncErpHrUserDataByMh();
	}
	*/

}
