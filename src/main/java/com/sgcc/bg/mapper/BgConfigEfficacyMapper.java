package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BgConfigEfficacyMapper {

	/**
	 * 获取工作日
	 * @param dateTime
	 * @return
	 */
	public  Map<String, Object>  selectForBcxx(@Param("dateTime") String  dateTime);
	/**
	 * 获取校验配置
	 * @param efficacyInfo
	 * @return
	 */
	public List<Map<String, Object>> selectForConfigEfficacy(@Param("efficacyInfo") Map<String ,Object> efficacyInfo);
	/**
	 * 获取校验配置,去除重复
	 * @param efficacyInfo
	 * @return
	 */
	public List<Map<String, Object>> getDisConfigEfficacy(@Param("efficacyInfo") Map<String ,Object> efficacyInfo);
	/**
	 * 新增校验配置
	 * @param efficacyInfo
	 * @return
	 */
	public int addConfigEfficacy(@Param("efficacyInfo") Map<String ,Object> efficacyInfo);

	/**
	 * 删除校验配置
	 * @param efficacyInfo  　
	 * @return
	 */
	public int deleteConfigEfficacy(@Param("efficacyInfo") Map<String ,Object> efficacyInfo);
	/**
	 * 修改校验配置
	 * @param  efficacyInfo  　
	 * @return
	 */
	public int updataConfigEfficacy(@Param("efficacyInfo") Map<String ,Object> efficacyInfo);






}