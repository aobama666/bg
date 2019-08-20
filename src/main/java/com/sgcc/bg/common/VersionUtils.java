package com.sgcc.bg.common;


/**
 * 提供给JSP页面访问接口和获取JS，CSS时拼接的版本号
 * @author epri-xpjt
 *
 */
public class VersionUtils {
	
	 /**
     * Spring初始化时根据系统当前时间生成版本号
     * 格式：20160101120101
     */
    public static String verNo = "";
    public static String PRODUCTION = "production";
    public static String TEST = "test";
    public static String DEVELOPMENT = "development";
    /**
     * 获取当前系统环境版本、测试、仿真、生产
     *
     * @return development、test、production
     */
    public static String getEnv() {
        String env = ConfigUtils.getConfig("env");
        return env;
    }

    /**
     * 根据当前时间获取long型值5位以后的值
     *
     * @return
     */
    public static String getCurrentimeVersion() {
        String currMillis = System.currentTimeMillis() + "";
        return currMillis.substring(5);
    }

}
