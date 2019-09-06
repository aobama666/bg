package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface YyMyItemMapper {



    /**
     * 查询待办列表信息
     */
    List<YyApplyDAO> selectMyItem(
            @Param("applyCode") String applyCode,
            @Param("deptId") String deptId,
            @Param("useSealUser") String useSealUser,
            @Param("ifComingSoon") String ifComingSoon,
            @Param("loginUserId") String loginUserId,
            @Param("pageStart") Integer pageStart,
            @Param("pageEnd") Integer pageEnd
    );


    /**
     * 待办列表信息总数
     */
    Integer getMyItemNums(
            @Param("applyCode") String applyCode,
            @Param("deptId") String deptId,
            @Param("useSealUser") String useSealUser,
            @Param("ifComingSoon") String ifComingSoon,
            @Param("loginUserId") String loginUserId
    );


    /**
     * 申请部门信息，列表查询下拉框使用
     */
    List<Map<String,Object>> getDept();


}
