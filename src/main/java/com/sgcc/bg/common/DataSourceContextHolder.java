package com.sgcc.bg.common;

/**
 * 数据源切换操作类
 * @author epri-xpjt
 *
 */
public class DataSourceContextHolder {
	
	private static final ThreadLocal<String> contextHolder=new ThreadLocal<String>();
	
	/**
	 * 设置要切换的数据源
	 * @param dbType
	 */
	public static void setDbType(String dbType){
		contextHolder.set(dbType);
	}
	
	/**
	 * 获取当前应该连接的数据源
	 * @return
	 */
	public static String getDbType(){
		return contextHolder.get();
	}
	
	/**
	 * 清除当前配置的数据源
	 */
	public static void clearDbType(){
		contextHolder.remove();
	}

}
