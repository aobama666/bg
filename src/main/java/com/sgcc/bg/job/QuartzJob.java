package com.sgcc.bg.job;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sgcc.bg.common.ConfigUtils;
import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.service.SyncService;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


@Service
public class QuartzJob {
	private Logger logger = (Logger) LoggerFactory.getLogger(QuartzJob.class);
	@Autowired
	private SyncService syncService;



	
	/**
	 * applicationContext.xml配置的定时任务。每天4点进行调用
	 * 同步用户、部门信息，
	 * 包含：ERP 1、部门  2、员工  3、员工的组织关系记录
	 *     门户    人员（获取门户账号）
	 *     综合人资   1、特殊部门 2、特殊员工 3、部门排序 4、单位（科室）排序 5、员工排序
	 */
	public void syncDeptAndUserData() {
		/*
		 * 同步ERP数据，共3部分数据，1、部门  2、员工  3、员工的组织关系记录
		 * 当配置表BG_SYS_CONFIG:DataSync01=true;DataSync01_IP=服务器IP，执行定时任务
		 */
		String localhost_ip = getLocalIP()==null?"xxx":getLocalIP();
		logger.info("[QuartzJob]:ERP数据同步配置：DataSync01="+ConfigUtils.getConfig("DataSync01")+";"
		                +"DataSync01_IP="+ConfigUtils.getConfig("DataSync01_IP")+";"
						+"localhost_ip="+localhost_ip);
		if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSync01"))
				&&ConfigUtils.getConfig("DataSync01_IP").equals(getLocalIP())) {
			syncService.syncErpSyncData();
			//同步人员组织短关系时需要用到新同步的人员表和组织表，所以须待syncErpSyncData()事务提交后再执行此方法
			syncService.syncUserOrganRelationData();
		}else{
			logger.error("[QuartzJob]:ERP数据同步已关闭！请检查");
		}
		/*
		 * 同步综合人资数据，共5部分数据，1、特殊部门 2、特殊员工 3、部门排序 4、单位（科室）排序 5、员工排序
		 * 当配置表BG_SYS_CONFIG:DataSync02=true;DataSync01_IP=服务器IP，执行定时任务
		 */
		logger.info("[QuartzJob]:ERP数据同步配置：DataSync02="+ConfigUtils.getConfig("DataSync02")+";"
        							+"DataSync02_IP="+ConfigUtils.getConfig("DataSync02_IP")+";"
        							+"localhost_ip="+localhost_ip);
		if (Rtext.ToBoolean(ConfigUtils.getConfig("DataSync02"))
				&&ConfigUtils.getConfig("DataSync02_IP").equals(getLocalIP())) {
			syncService.syncZhglSyncData();
		}else{
			logger.error("[QuartzJob]:综合人资数据同步已关闭！请检查");
		}
	}
	
	/**
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统
	 * 
	 * @return String
	 */
	 public static String getLocalIP() {
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}
	
	/**
	 * 获得主机IP
	 * 
	 * @return String
	 */
	private static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
	
	/**
	 * 每天凌晨4点从ftp删除报工系统批量导入出错信息文件
	 */
	public void deleteErrorInfoFilesFromFtp() {
		logger.info("开始删除报工系统批量导入出错信息文件...");
		FtpUtils.deleteAll(FtpUtils.BgTempUploadPath, false);
		logger.info("报工系统批量导入出错信息文件删除完毕！");
	}
}
