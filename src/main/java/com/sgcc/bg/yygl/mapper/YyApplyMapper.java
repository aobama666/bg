package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface YyApplyMapper {

    /**
     * 按照条件查询当前所有申请
     */
    List<Map<String,Object>> selectApply(
            @Param("applyCode") String applyCode,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("useSealStatus") String useSealStatus,
            @Param("itemSecondId") String itemSecondId,
            @Param("useSealReason") String useSealReason,
            @Param("pageStart") Integer pageStart,
            @Param("pageEnd") Integer pageEnd,
            @Param("userId") String userId);



    /**
     * 按照条件查询申请总数
     */
    Integer selectApplyTotal(
            @Param("applyCode") String applyCode,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("useSealStatus") String useSealStatus,
            @Param("itemSecondId") String itemSecondId,
            @Param("useSealReason") String useSealReason,
            @Param("userId") String userId
    );


    /**
     * 按照条件导出
     */
    List<YyApplyDAO> selectApplyExport(
            @Param("applyCode") String applyCode,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("useSealStatus") String useSealStatus,
            @Param("itemSecondId") String itemSecondId,
            @Param("useSealReason") String useSealReason,
            @Param("userId") String userId,
            @Param("uuids") String[] uuids
    );



    /**
     * 一级用印事项
     */
    List<Map<String,Object>> getItemFirst();



    /**
     * 二级用印事项
     */
    List<Map<String,Object>> getItemSecond(@Param("firstCategoryId") String firstCategoryId);



    /**
     * 根据编号或者主键查询对应申请详细信息
     */
    YyApplyDAO findApply(@Param("uuid") String uuid, @Param("applyCode") String applyCode);



    /**
     * 新增申请
     */
    Integer addApply(YyApply apply);



    /**
     * 修改申请
     */
    Integer updateApply(YyApply apply);



    /**
     * 修改申请状态
     */
    Integer updateApplyStatus(@Param("uuid") String uuid, @Param("useSealStatus") String useSealStatus);



    /**
     * 删除申请
     */
    Integer updateValid(@Param("uuid") String uuid, @Param("valid") String valid);



    /**
     * 查询当前日期所有申请编号
     */
    List<String> findDayApplyCode(@Param("date") String date);



    /**
     * 获取用户对应部门和处室信息
     */
    Map<String,Object> findDept(@Param("userId") String userId);
}
