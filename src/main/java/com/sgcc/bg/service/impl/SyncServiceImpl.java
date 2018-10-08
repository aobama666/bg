package com.sgcc.bg.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.Sync.SyncData;
import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.mapper.SyncDataMapper;
import com.sgcc.bg.model.HRDept;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.SyncService;

@Service
public class SyncServiceImpl implements SyncService {
	@Autowired
	private SyncDataMapper syncDataMapper;
	
	private final static Log logger = LogFactory.getLog(SyncServiceImpl.class);
	//所有方法受声明式事务控制，如发生异常，则该方法整体回滚
	public void syncErpSyncData() {
		logger.info("[QuartzJob]:1、开始同步HR临时机构数据");
		syncTempHrOrganData();		
		logger.info("[QuartzJob]:2、开始更新HR机构");			
		syncHrOrganData();
		logger.info("[QuartzJob]:3、开始同步HR临时员工数据");	
		syncTempHrUserData();
		logger.info("[QuartzJob]:4、开始更新HR临时员工数据的门户信息");
		syncTempHrUserDataByMh();
		logger.info("[QuartzJob]:5、开始更新HR员工");	
		syncHrUserData();
		logger.info("[QuartzJob]:6-1、ERP部门、人员数据同步完成");
	}
	
	public void syncUserOrganRelationData(){
		logger.info("[QuartzJob]:1、开始同步HR临时员工的组织关系数据");	
		syncTempHrUserOrganRelationData();
		logger.info("[QuartzJob]:2、开始更新HR员工的组织关系");
		syncHrUserOrganRelationData();
		logger.info("[QuartzJob]:2-1、ERP人员组织关系数据同步完成");
	}

	public void syncZhglSyncData() {
		logger.info("[QuartzJob]:1、开始同步ZHGL临时特殊机构数据");
		syncTempZhglOrganData();
		logger.info("[QuartzJob]:2、开始更新ZHGL特殊机构");			
		syncZhglOrganData();
		logger.info("[QuartzJob]:3、开始同步ZHGL临时特殊员工数据");			
		syncTempZhglUserData();
		logger.info("[QuartzJob]:4、开始更新ZHGL特殊员工");	
		syncZhglUserData(); 
		logger.info("[QuartzJob]:5、开始同步ZHGL临时部门排序数据");			
		syncTempZhglDeptOrderData();
		logger.info("[QuartzJob]:6、开始更新ZHGL部门排序");	
		syncZhglDeptOrderData();
		logger.info("[QuartzJob]:7、开始同步ZHGL临时单位排序数据");			
		syncTempZhglOfficeOrderData();
		logger.info("[QuartzJob]:8、开始更新ZHGL单位排序");
		syncZhglOfficeOrderData();;
		logger.info("[QuartzJob]:9、开始同步ZHGL临时员工排序数据");			
		syncTempZhglUserOrderData();
		logger.info("[QuartzJob]:10、开始更新ZHGL员工排序");
		syncZhglUserOrderData();
		logger.info("[QuartzJob]:10-1、综合人资数据同步完成");
	}
	
	public void syncTempHrOrganData(){
		syncDataMapper.deleteErpHrOrgan();
		List<HRDept> deptList = SyncData.getWebServiceDeptList();//如果获取失败则返回的集合为null，抛出异常回滚事务
		for (HRDept dept : deptList) {
			syncDataMapper.addErpHrDept(dept);
		}
	}
	
	public void syncHrOrganData() {
		syncDataMapper.invalidHrOrganData();//先将组织状态置为失效0
		syncDataMapper.syncHrOrganData();
		syncDataMapper.syncHrFatherData();
	}

	
	public void syncTempHrUserData() {
		syncDataMapper.deleteErpHrUser();
		List<HRUser> userList = SyncData.getWebServiceUserList();//如果获取失败则返回的集合为null，抛出异常回滚事务
		for (HRUser user : userList) {
			syncDataMapper.addErpHrUser(user);
		}
	}

	public void syncTempHrUserDataByMh() {
		syncDataMapper.deleteMhUser();
		syncMhHrUser();//如果调用失败则该方法抛出异常，回滚事务
		syncDataMapper.syncErpHrUserDataByMh();
	}
	
	public void syncHrUserData() {
		syncDataMapper.syncHrUserData();
	}

	
	public void syncTempHrUserOrganRelationData() {
		syncDataMapper.deleteUserOrganRelation();
		List<Map<String,Object>> UserOrganRelationList = SyncData.getWebServiceUserDeptRelationList();//如果获取失败则返回的集合为null，抛出异常回滚事务
		for (Map<String, Object> map : UserOrganRelationList) {
			syncDataMapper.addUserOrganRelation(map);
		}
	}

	public void syncHrUserOrganRelationData() {
		syncDataMapper.invalidHrUserOrganRelationData();//先将erp同步人员组织关系状态置为失效0
		syncDataMapper.syncHrUserOrganRelationData();
	}
/*#######################################################################################################*/
	
	public boolean syncTempZhglOrganData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncZhglOrganData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncTempZhglUserData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncZhglUserData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncTempZhglDeptOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncZhglDeptOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncTempZhglOfficeOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncZhglOfficeOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncTempZhglUserOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean syncZhglUserOrderData() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 获取仿真104库的数据源
	 * @return
	 * @throws Exception
	 */
	private Connection getTargetConn() throws Exception{
		String url=ConfigUtils.getConfig("jdbc_syncUser_url");
		String username=ConfigUtils.getConfig("jdbc_syncUser_username");;
		String passwd=ConfigUtils.getConfig("jdbc_syncUser_password");;
		Class.forName("oracle.jdbc.OracleDriver");
		return DriverManager.getConnection(url,username,passwd);
	}
	
	/**
	 * 
	 * jdbc获取104库的门户人员信息并添到门户用户表
	 */
	public void syncMhHrUser(){
		//删除逻辑
		Connection conn=null;
		try{
			conn=getTargetConn();
			Statement stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from TB_TEMP_USER");
			while(rs.next()) {
				String cn=Rtext.toStringTrim(rs.getString("cn"), "");
				String fullname= Rtext.toStringTrim(rs.getString("fullname"),"");
				String sgccdeptname= Rtext.toStringTrim(rs.getString("sgccdeptname"),"");
				String sgcchrcode= Rtext.toStringTrim(rs.getString("sgcchrcode"),"");
				String sgcchrusercode= Rtext.toStringTrim(rs.getString("sgcchrusercode"),"");
				String login_pwd= Rtext.toStringTrim(rs.getString("login_pwd"),"");
				//保存逻辑
				syncDataMapper.addMhHrUser(cn,fullname,sgccdeptname,sgcchrcode,sgcchrusercode,login_pwd);
			}
			logger.info("get TB_TEMP_USER end！");
			conn.commit();
			rs.close();
			stmt.close();
		}catch (Exception e) {
			logger.info("get  TB_TEMP_USER  failed ",e);
			throw new RuntimeException("jdbc方式获取门户用户数据失败！");
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException("关闭jdbc连接出错！");
			}
		}
	}

	@Override
	public String addDuty(String empCode, String deptCode, String roleCode) {
		int affectedRows_1=syncDataMapper.addUserRole(empCode,roleCode);
		int affectedRows_2=syncDataMapper.addUserOrgan(empCode,deptCode);
		if(affectedRows_1==1 && affectedRows_2==1){
			return "授权成功!";
		}else{
			return "授权失败!";
		}
	}
	
	/*	public static void main(String[] args) {
		syncErpHrUserDataByMh();
	}
	*/

}
