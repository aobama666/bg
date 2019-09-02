package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    int deleteSpecialist(@Param("strings") String[] strings);

    /**
     * 专家匹配的论文
     * @param uuid
     * @return
     */
    List<PaperVO> paperMap(String uuid);

    /**
     * 专家已经匹配的论文信息-更换专家使用
     */
    List<PaperVO> exportMatchPaperForYear(
            @Param("specialistId") String specialistId,
            @Param("year") String year);

    /**
     * 专家匹配的论文分页
     * @param uuid
     * @param start
     * @param end
     * @return
     */
    List<PaperVO> paperMapPage(@Param("uuid") String uuid, @Param("start") int start, @Param("end") int end);

    /**
     * 专家匹配的论文总数
     * @param uuid
     * @return
     */
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

    /**
     * 判断email是否存在，存在返回对应uuid
     * @param email
     * @return
     */
    String ifEmail(@Param("email") String email);

    /**
     * 获取论文匹配的专家
     * @param uuid
     * @return
     */
    List<Map<String,Object>> paperSpecialist(String uuid);

    /**
     * 根据ids查
     * @param strings
     * @return
     */
    List<LwSpecialist> listIds(@Param("strings") String[] strings);

    /**
     * //根据论文的作者姓名和单位查询出作者id（专家id）
     * @param author
     * @param unit
     * @return
     */
    LwSpecialist specialistMatching(@Param("author") String author, @Param("unit") String unit);


    //论文领域同专家领域（精准）、论文领域同专家研究方向（精准）、回避本人、回避本单位，回避已匹配的专家
    List<LwSpecialist> matchingSpecialistList(@Param("spUuid") Set spUuid,
                                              @Param("unitSet") Set unitSet,
                                              @Param("fieldSet") Set fieldSet);

    //修改专家的匹配状态
    int updateMatchStatus(@Param("beforeUuid") String beforeUuid, @Param("matchStatus") String matchStatus);

    /**
     * 评分统计专家查询
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    List<Map<String,Object>> statisticsSpecialistName(@Param("year") String year,
                                                      @Param("paperName") String paperName,
                                                      @Param("paperId") String paperId,
                                                      @Param("field") String field);

    /**
     * 根据论文id获取专家
     * @param uuid
     * @return
     */
    List<Map<String,Object>> SpeciList(@Param("uuid") String uuid);
}
