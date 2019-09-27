package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface YyApplyAnnexMapper {

    /**
     * 新增用印材料
     */
    Integer addApplyAnnex(YyApplyAnnex yyApplyAnnex);

    /**
     * 删除用印材料
     */
    Integer delApplyAnnex(@Param("uuid") String uuid);


    /**
     * 查询用印材料
     */
    List<YyApplyAnnexDAO> selectApplyAnnex(@Param("applyUuid") String applyUuid);


    /**
     * 查询单个用印材料的基本信息
     */
    YyApplyAnnex findApplyAnnex(@Param("uuid") String uuid);
}
