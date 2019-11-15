package com.sgcc.bg.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;

public interface BgConfigEfficacyService {
	/**
	 * 获取校验配置
	 * @param efficacyInfo
	 * @return
	 */
	public List<Map<String, Object>> selectForConfigEfficacy(Map<String ,Object> efficacyInfo);
	/**
	 * 获取校验配置,去除重复
	 * @param efficacyInfo
	 * @return
	 */
	public List<Map<String, Object>> getDisConfigEfficacy(Map<String ,Object> efficacyInfo);

	/**
	 * 新增校验配置
	 * @param efficacyInfo
	 * @return
	 */
	public int addConfigEfficacy(Map<String ,Object> efficacyInfo);

	/**
	 * 删除校验配置
	 * @param efficacyInfo  　
	 * @return
	 */
	public int deleteConfigEfficacy(Map<String ,Object> efficacyInfo);

	/**
	 * 修改校验配置
	 * @param  efficacyInfo  　
	 * @return
	 */
	public int updataConfigEfficacy(  Map<String ,Object> efficacyInfo);
	/**
	 * 导出校验配置
	 * @param efficacyInfo
	 * @return
	 */
	public String exportSelectedItems(Map<String ,Object> efficacyInfo,String index,  HttpServletResponse response);

}
