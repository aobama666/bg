package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwSpecialist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExpertMapper {
    /**
     * 查询
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     * @param start
     * @param end
     * @return
     */
    List<Map<String,Object>> expertList(@Param("name") String name,
                                        @Param("researchDirection") String researchDirection,
                                        @Param("unitName") String unitName,
                                        @Param("field") String field,
                                        @Param("matchStatus") String matchStatus,
                                        @Param("start") int start,
                                        @Param("end") int end);

    /**
     * 增加专家
     * @param lwSpecialist
     * @return
     */
    int insertExpert(LwSpecialist lwSpecialist);

    /**
     * 查询
     * @param id
     * @return
     */
    Map<String, String> lwSpecialist(@Param("id") String id);

    /**
     * 修改
     * @param lwSpecialist
     * @return
     */
    int updateExpert(LwSpecialist lwSpecialist);
}
