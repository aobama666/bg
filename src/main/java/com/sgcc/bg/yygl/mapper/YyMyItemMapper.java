package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
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


    /**
     * 根据条件查询出对应节点的审批人
     * 部门id
     * 节点类型
     * 用印事项二级id
     */
    List<Map<String,Object>> nextNodeApprove(
            @Param("deptId") String deptId,
            @Param("nodeType") String nodeType,
            @Param("itemSecondId") String itemSecondId);


    /**
     * 事项对应部门
     */
    List<Map<String,Object>> itemBusinessDept(@Param("itemSecondId") String itemSecondId);


    /**
     * 是否需要会签
     */
    String ifSign(@Param("itemSecondId") String itemSecondId);


    /**
     * 是否需要院领导批准
     */
    String ifLeaderApprove(@Param("itemSecondId") String itemSecondId);


    /**
     * 根据登陆用户名获取处室信息
     */
    Map<String,Object> findDeptForUserName(@Param("userName") String userName);

}
