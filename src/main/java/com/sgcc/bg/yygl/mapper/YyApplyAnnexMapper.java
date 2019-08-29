package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
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


}
