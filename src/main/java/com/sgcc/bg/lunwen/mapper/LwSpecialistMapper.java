package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwSpecialistMapper {
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
    List<LwSpecialist> expertList(@Param("name") String name,
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
     * @param uuid
     * @return
     */
    LwSpecialist lwSpecialist(@Param("uuid") String uuid);

    /**
     * 修改
     * @param lwSpecialist
     * @return
     */
    int updateExpert(LwSpecialist lwSpecialist);

    /**
     * 统计
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     * @return
     */
    int count(@Param("name") String name,
              @Param("researchDirection") String researchDirection,
              @Param("unitName") String unitName,
              @Param("field") String field,
              @Param("matchStatus") String matchStatus);

    int deleteExpert(String uuid);

    /**
     * 专家匹配的论文
     * @param uuid
     * @return
     */
    List<PaperVO> paperMap(String uuid);

    List<PaperVO> paperMapPage(@Param("uuid") String uuid, @Param("start") int start, @Param("end") int end);

    int specialistAndPaperCount(String uuid);

    /**
     * 查询全部专家
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     * @return
     */
    List<LwSpecialist> list(@Param("name") String name,
                                  @Param("researchDirection") String researchDirection,
                                  @Param("unitName") String unitName,
                                  @Param("field") String field,
                                  @Param("matchStatus") String matchStatus);

    /**
     * 获取所有的email
     * @return
     */
    List<String> getEmail();

    void addList(@Param("lwSpecialistList") List<LwSpecialist> lwSpecialistList);
}
