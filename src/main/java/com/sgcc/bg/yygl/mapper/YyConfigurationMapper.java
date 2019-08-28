package com.sgcc.bg.yygl.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface YyConfigurationMapper {

   /**
     * 用印事项的查询
     */
    List<Map<String,Object>> selectForMatters(@Param("mattersMap") Map<String, Object> mattersMap);
    /**
     *  用印事项的查询--总数
     */
    String selectForMattersNum(@Param("mattersMap") Map<String ,Object> mattersMap );

}
