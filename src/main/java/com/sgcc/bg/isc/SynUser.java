package com.sgcc.bg.isc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 根据门户推送到中间表的数据，同步到正式表 zjb_isc_ueer1--> isc_user
 * @author hanxifa
 *
 */
public class SynUser {
	private static Logger logger=Logger.getLogger(SynUser.class);

	private String url;
	private String username;
	private String passwd;
	
	public SynUser(String url,String username,String passwd){
		this.url=url;
		this.username=username;
		this.passwd=passwd;
	}
	private Connection getTargetConn() throws Exception{
		Class.forName("oracle.jdbc.OracleDriver");
		return DriverManager.getConnection(url,username,passwd);
	}
	
	private SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd_HHmmss");
	/**
	 * 备份用户表
	 * @throws Exception
	 */
	private void backupTable() throws Exception{
		Connection conn=null;
		try{
			conn=getTargetConn();
			Statement stmt=conn.createStatement();
			String bakTableName="ISC_USER_BAK_"+sf.format(new Date());
			logger.info("begin bakup "+bakTableName);
			stmt.execute("create table "+bakTableName+" as select * from ISC_USER");
			conn.commit();
			stmt.close();
		logger.info("success bakup "+bakTableName);
		}catch (Exception e) {
			logger.info("backup ISC_USER failed ",e);
		}finally{
			conn.close();
		}
		
		
	}
	/**
	 * 获取中间表用户数据
	 * @return
	 */
	private List<ISCZjbUser> getSourceUser() {
		Connection conn = null;
		try {
			conn = getTargetConn();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select id,name,orgid,passwd,rzcode,state from  zjb_isc_ueer1 ");
			List<ISCZjbUser> list = new ArrayList<ISCZjbUser>();
			while (rs.next()) {
				ISCZjbUser izu = new ISCZjbUser();
				izu.setId(rs.getString("id"));
				izu.setName(rs.getString("name"));
				izu.setOrgId(rs.getString("orgid"));
				izu.setPasswd(rs.getString("passwd"));
				izu.setRzCode(rs.getString("rzcode"));
				izu.setState(rs.getString("state"));
				list.add(izu);
			}
			rs.close();
			st.close();
			return list;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取业务表所有组织机构信息
	 * @return
	 */
	private Map<String, String> getBaseOrg(){
		Connection conn=null;
		try {
			conn=getTargetConn();
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select id,name from ISC_BASEORG");
			 Map<String, String> map=new HashMap<String, String>(2);
			while(rs.next()){
				map.put(rs.getString("id"),rs.getString("name"));
			}
			rs.close();
			st.close();
			return map;
		} 
		
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 获取 业务表所有用户信息
	 * @return
	 */
	private Map<String,TbUser> getAllTbUserInfo(){
		Connection conn=null;
		try {
			/**
			 * 这些信息看看能否在tb_config中获取
			 */
			String url="jdbc:oracle:thin:@10.85.60.91:1521/orcl";
			String username="readonly";
			String passwd="readonly";
			Class.forName("oracle.jdbc.OracleDriver");
			conn=DriverManager.getConnection(url,username,passwd);
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select USERID,deptid,username,useralias,mobile,email,postname from tb_user ");
			Map<String,TbUser> map=new HashMap<String, TbUser>();
			while(rs.next()){
				TbUser tbu=new TbUser();
				tbu.setUserId(rs.getString("USERID"));
				tbu.setDeptId(rs.getString("deptid"));
				tbu.setLoginName(rs.getString("username"));
				tbu.setUserName(rs.getString("useralias"));
				tbu.setMobile(rs.getString("mobile"));
				tbu.setEmail(rs.getString("email"));
				tbu.setPostName(rs.getString("postname"));
				map.put(tbu.getLoginName(), tbu);
			}
			rs.close();
			st.close();
			return map;
		} 
		catch (Exception e) {
			logger.error("get tb_user info failed!",e);
			return null;
		}finally{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		String url="jdbc:oracle:thin:@10.85.4.56:1521/erd";
		String username="erdadmin";
		String passwd="ky@2016/04";
		try {
			new SynUser(url,username,passwd).sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sync() throws Exception {
		//backupTable();
		List<ISCZjbUser> list = getSourceUser();
		Map<String, String> allOrg = getBaseOrg();// 获取所有组织机构信息
		Map<String, TbUser> allTbUser = getAllTbUserInfo();// 获取所有tbuser表用户，用于更新email，title等信息
		Connection conn = null;
		try {
			conn = getTargetConn();
			for (ISCZjbUser izu : list) {
				try {
					String sql = "select id,baseorg_id,name,saphrid,login_name,state from isc_user where id=?";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, izu.getId());
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						String id = rs.getString("id");
						String name = rs.getString("name");
						String baseOrgId = rs.getString("baseorg_id");
						String state = rs.getString("state");
						String istate = izu.getState().equals("1") ? "1" : "2";// 中间表0表示无效 1表示有效，isc_user表状态 1：正常2：禁用 3：锁定 4：密码过期
						if("3".equals(state)||"4".equals(state)){//由于中间表没有 3，4两个状态，所有当原值为3，4的时候保持不变
							istate=state;
						}
						if ((izu.getName().equals(name) && izu.getOrgId().equals(baseOrgId) && istate.equals(state)) == false) {
							logger.info("will update id:" + id + ",orbid:" + baseOrgId + "-->" + izu.getOrgId() + ",name:" + name + "-->" + izu.getName() + ",state:" + state + "-->" + istate);
							String sqlu = "update isc_user set baseorg_id=?,name=?,state=? where id=?";
							PreparedStatement psu = conn.prepareStatement(sqlu);
							psu.setString(1, izu.getOrgId());
							psu.setString(2, izu.getName());
							psu.setString(3, istate);
							psu.setString(4, id);
							psu.executeUpdate();
							psu.close();
							conn.commit();
						} else {
							logger.info("same :" + izu);
						}
					} else {
						logger.info("will insert  :" + izu);
						String sqli = "insert into isc_user(id,baseorg_id,name,password,state,start_usefullife2,end_usefullife,saphrid,email,title,org_anisation_name,login_name,lastmodify) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement psi = conn.prepareStatement(sqli);

						int index = 1;
						psi.setString(index++, izu.getId());
						psi.setString(index++, izu.getOrgId());
						psi.setString(index++, izu.getName());
						psi.setString(index++, izu.getPasswd());
						psi.setString(index++, "1");// state

						Calendar cur = Calendar.getInstance();
						Timestamp startDate = new Timestamp(cur.getTimeInMillis());
						psi.setTimestamp(index++, startDate);// 有效开始时间
						cur.add(Calendar.YEAR, 20);
						Timestamp endDate = new Timestamp(cur.getTimeInMillis());
						psi.setTimestamp(index++, endDate);// 结束时间
						psi.setString(index++, izu.getRzCode());
						TbUser tbu = allTbUser.get(izu.getId());
						psi.setString(index++, tbu == null ? null : tbu.getEmail());// email
						psi.setString(index++, tbu == null ? null : tbu.getPostName());
						psi.setString(index++, allOrg.get(izu.getId()));
						psi.setString(index++, izu.getId());
						psi.setTimestamp(index++, startDate);
						psi.executeUpdate();
						psi.close();
						conn.commit();

					}
					rs.close();
					ps.close();
				} catch (Exception e) {
					logger.error("sync iscuser failed!" + izu, e);
				}
			}
			logger.info(" sync user success！");
		} catch (Exception e) {
			logger.error("sycuser failed!", e);
		} finally {
			conn.close();
		}
	}
	
}
