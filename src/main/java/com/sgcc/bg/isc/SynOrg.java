package com.sgcc.bg.isc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 门户目录服务推送数据到组织机构中间表，同步到isc组织机构表
 * @author hanxifa
 *
 */
public class SynOrg {
	Logger logger=Logger.getLogger(SynOrg.class);
	private String url;
	private String username;
	private String passwd;
	
	public SynOrg(String url,String username,String passwd){
		this.url=url;
		this.username=username;
		this.passwd=passwd;
	}
	private Connection getConn() throws Exception{
		Class.forName("oracle.jdbc.OracleDriver");
		return DriverManager.getConnection(url,username,passwd);
	}
	private SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd_HHmmss");
	private void backupTable() throws Exception{
		Connection conn=getConn();
		Statement stmt=conn.createStatement();
		String bakTableName="ISC_BASEORG_"+sf.format(new Date());
		logger.info("begin bakup "+bakTableName);
		stmt.execute("create table "+bakTableName+" as select * from ISC_BASEORG");
		conn.commit();
		conn.close();
		logger.info("success bakup "+bakTableName);
	}
	/**
	 * 获取中间表的 组织机构信息
	 * @return
	 */
	private List<String[]> getBaseOrg(){
		Connection conn=null;
		try {
			conn=getConn();
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select id,name,pid from ZJB_ISC_BASEORG");
			List<String[]> list=new ArrayList<String[]>();
			while(rs.next()){
				String[] sa=new String[3];
				sa[0]=rs.getString("id");
				sa[1]=rs.getString("pid");
				sa[2]=rs.getString("name");
				list.add(sa);
			}
			rs.close();
			st.close();
			logger.info("query ZJB_ISC_BASEORG success！"+list.size());
			return list;
		} 
		
		catch (Exception e) {
			logger.error("query ZJB_ISC_BASEORG failed！ ",e);
			return null;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 以中间表ZJB_ISC_BASEORG 为基准， 同步组织机构信息到ISC_BASEORG表，不进行删除操作，更新名称和pid，新机构会进行插入操作。
	 */
	public void sync() {
		try {
			//生产环境不进行备份
			//backupTable();
		} catch (Exception e) {
			logger.error("backup table failed!", e);
		}
		Connection conn = null;
		try {
			conn = getConn();
			List<String[]> list = getBaseOrg();
			for (String[] ar : list) {
				String line = ar[0] + "," + ar[1] + "," + ar[2];
				try {
					String id=ar[0];
					if(id.equals("4200000001")){
						logger.info("root node(中国电力科学研究院)不进行更新！");
						continue;
					}
					String sql = "select id,name,parent_id from ISC_BASEORG where id=? ";
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setString(1, id);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						String baseOrgName = rs.getString("name");
						String pid = rs.getString("parent_id");
						// 组织机构名称和父id任何一个不一致，则更新
						if (baseOrgName.equals(ar[2]) == false || pid.equals(ar[1]) == false) {
							logger.info("update:" + baseOrgName + "-->" + ar[2]);
							logger.info("update:" + pid + "-->" + ar[1]);
							PreparedStatement ps2 = conn.prepareStatement("update ISC_BASEORG set name=?,parent_id=? where id=? ");
							ps2.setString(1, ar[2]);
							ps2.setString(2, ar[1]);
							ps2.setString(3, ar[0]);
							ps2.executeUpdate();
							ps2.close();
						} else {
							logger.info("orginfo is same,not require update ：" + line);
						}
					} else {
						logger.info("will insert org --> " + line);
						PreparedStatement ps3 = conn.prepareStatement("insert into ISC_BASEORG(id,name,attr,Parent_Id,state) values(?,?,'DEPT',?,'Y')");
						ps3.setString(1, ar[0]);
						ps3.setString(2, ar[2]);
						ps3.setString(3, ar[1]);
						ps3.executeUpdate();
						ps3.close();
					}
					rs.close();
					ps.close();
					conn.commit();
				} catch (Exception e) {
					logger.error("syn org failed! " + line, e);
				}
			}
		} catch (Exception e) {
			logger.error("syn failed", e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		String url="jdbc:oracle:thin:@10.85.157.41:1521/fzerd";
		String username="erdadmin";
		String passwd="tygl#20161222";
		new SynOrg(url,username,passwd).sync();
	}
	
}
