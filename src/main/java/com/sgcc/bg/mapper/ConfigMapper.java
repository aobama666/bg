package com.sgcc.bg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sgcc.bg.model.FileEntity;

@Repository
public interface ConfigMapper {
	
	/**
	 * 获取tb_config表配置
	 * @return
	 */
	public List<Map<String,String>> getConfig();

	/**
	 * 根据文件id获取文件信息
	 * @return 数据列表
	 */
	public FileEntity getFileInfo(@Param("fileid")String fileid);
	
	/**
	 * 修改文件下载次数 在其原有基础上加1
	 * @param id
	 * @return
	 */
	public int updateDownloadNum(@Param("id")String id);
	
	/**
	 * 向附件表中插入数据
	 * @param file FileEntity类实体
	 * @return
	 */
	public int insertFileInfo(FileEntity file);
	
	/**
	 * 获取用户登录日志列表
	 * @param username 用户名
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<Map<String,String>> getUserLoginList(@Param("username")String username,@Param("beginDate")String beginDate,@Param("endDate")String endDate);
	
	/**
	 * 获取用户登录操作日志列表
	 * @param uwlID 操作日志ID
	 * @return
	 */
	public List<Map<String,String>> getUserLoginLogList(@Param("uwlID")String uwlID);
	
	/**
	 * 获取用户登录信息
	 * @param uwlID 操作日志ID
	 * @return
	 */
	public List<Map<String,String>> getCurrentLoginInfo(@Param("uwlID")String uwlID);
	
	/**
	 * 获取下一个用户登录信息
	 * @param uwlID 操作日志ID
	 * @return
	 */
	public List<Map<String,String>> getNextLoginInfo(@Param("username")String username,@Param("datecreated")String datecreated);
	
	/**
	 * 获取待办记录
	 * @param uwlID 操作日志ID
	 * @return
	 */
	public List<Map<String,String>> getAuditInfo(@Param("username")String username,@Param("begindate")String begindate,@Param("enddate")String enddate);
	
	/**
	 * 根据用户名获取用户所在部门/科室
	 * @param username 用户名
	 * @return
	 */
	public Map<String,Object> getDeptByUsername(@Param("username")String username);
	
	/**
	 * 根据用户名获取用户所在部门（电科院下的部门）
	 * @param username 用户名
	 * @return
	 */
	public Map<String,Object> getPDeptByUsername(@Param("username")String username);
	
	
}
