package com.sgcc.bg.mapper;

import com.sgcc.bg.model.Temporary;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BgKqTemporaryMapper {
	/**
	 * 添加考勤临时表数据
	 * @param temporary
	 * @return
	 */
	public int addTemporary(Temporary temporary);
	/**
	 * 添加考勤历史记录表数据
	 * @param temporary
	 * @return
	 */
	public int addHistoryInfo(Temporary temporary);



	/**
	 * 添加考勤业务表数据
	 * @param temporary
	 * @return
	 */
	public int addKqEmpInfo(Temporary temporary);
	/**
	 * 查询该表是否有数据存在
	 * @param
	 * @return
	 */
	public List<Map<String ,String>> selectForAll();
	/**
	 * 修改数据状态
	 * @param status  状态   oncecode  标示
	 * @return
	 */
	public int updataForStatus(@Param("status")String status,
							   @Param("oncecode")String oncecode);
	/**
	 * 删除上批次数据
	 * @param  oncecode  标示
	 * @return
	 */
	public int deleteByOneCode(@Param("oncecode")String oncecode);


	/**
	 * 删除历史表上批次数据
	 * @param  createTime  时间
	 * @return
	 */
	public int deleteHistoryByOneCode(@Param("createTime")Date createTime);


	/**
	 * 修改业务表上次时间是数据
	 * @param  beginDate  开始时间   endDate  结束时间
	 * @return
	 */
	public int updataKqEmpForDate(@Param("status")String status,@Param("valid")String valid,@Param("beginDate")String beginDate,@Param("endDate")String endDate);

	/**
	 * 删除业务表上次时间是数据
	 * @param  beginDate  开始时间   endDate  结束时间
	 * @return
	 */
	public int deleteKqEmpByDate(@Param("valid")String valid,@Param("beginDate")String beginDate,@Param("endDate")String endDate);

	/**
	 * 删除业务表上次时间是数据
	 * @param  beginDate  开始时间   endDate  结束时间
	 * @return
	 */
	public List<Map<String ,Object>> selectForKqInfo(@Param("beginDate")String beginDate,@Param("endDate")String endDate,@Param("empName")String empName,@Param("idslist")List<String> idslist);

}