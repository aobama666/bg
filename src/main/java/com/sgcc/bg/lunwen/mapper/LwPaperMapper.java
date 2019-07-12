package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwPaperMapper {

    /**
     * 新增论文信息
     * @param lwPaper
     * @return
     */
    Integer addLwPaper(LwPaper lwPaper);

    /**
     * 修改论文信息
     * @param lwPaper
     * @return
     */
    Integer updateLwPaper(LwPaper lwPaper);

    /**
     * 修改生成打分表状态
     * @param uuid
     * @param scoreTableStatus
     * @return
     */
    Integer updateScoreTableStatus(
            @Param("uuid")String uuid,
            @Param("scoreTableStatus") String scoreTableStatus
    );

    /**
     * 修改打分状态
     * @param uuid
     * @param scoreTableStatus
     * @return
     */
    Integer updateScoreStatus(
            @Param("uuid")String uuid,
            @Param("scoreTableStatus")String scoreTableStatus
    );

    /**
     * 修改论文全生命周期状态
     * @param uuid
     * @param allStatus
     * @return
     */
    Integer updateAllStatus(
            @Param("uuid")String uuid,
            @Param("allStatus") String allStatus
    );

    /**
     * 查找某条论文信息,或根据题目查重
     * @param uuid,paperName
     * @return
     */
    Map<String, Object> findPaper(
            @Param("uuid")String uuid,
            @Param("paperName") String paperName,
            @Param("valid") String valid
    );

    /**
     * 查找当前对应类型的最大编号
     * @param paperType
     * @return
     */
    String maxPaperId(@Param("paperType") String paperType);

    /**
     * 删除论文信息，逻辑删除，单纯修改有效状态
      * @param uuid
     * @return
     */
    Integer delLwPaper(
            @Param("uuid") String uuid,
            @Param("valid") String valid
    );

    /**
     * 按条件查询某批论文信息
     * 条件：年度，论文题目，论文编号，单位，作者，领域
     * @param pageStart
     * @param pageEnd
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @return
     */
    List<Map<String, Object>> selectLwPaper(
            @Param("pageStart") Integer pageStart,
            @Param("pageEnd") Integer pageEnd,
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("scoreStatus") String scoreStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    Integer selectLwPaperCount(
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("scoreStatus") String scoreStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 综合统计论文查询
     * @param year
     * @param paperName
     * @param author
     * @param paperId
     * @param start
     * @param end
     * @return
     */
    List<PaperComprehensiveVO> paperComprehensiveVOList(@Param("year") String year,
                                                        @Param("paperName")String paperName,
                                                        @Param("author")String author,
                                                        @Param("paperId")String paperId,
                                                        @Param("start")int start,
                                                        @Param("end")int end);

    /**
     * 综合统计论文列表count
     * @param paperName
     * @param paperId
     * @param year
     * @param author
     * @return
     */
    int paperComprehensiveCount(@Param("paperName") String paperName,
                                @Param("paperId") String paperId,
                                @Param("year") String year,
                                @Param("author") String author);

    /**
     * 根据选中数据查询对应数据——导出
     * @param uuids
     * @return
     */
    List<LwPaper> selectCheckIdExport(@Param("uuids") String[] uuids);

    /**
     * 按条件查询某批论文信息——导出
     * 条件：年度，论文题目，论文编号，单位，作者，领域
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @return
     */
    List<LwPaper> selectLwPaperExport(
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("scoreStatus") String scoreStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 获取匹配领域的专家信息
     * @param authors
     * @param unit
     * @param field
     * @param valid
     * @return
     */
    List<LwSpecialist> selectSpecialistField(
            @Param("authors") String[] authors,
            @Param("unit") String unit,
            @Param("field") String field,
            @Param("valid") String valid);


    /**
     * 查全部
     * @param year
     * @param paperName
     * @param author
     * @param paperId
     * @return
     */
    List<PaperComprehensiveVO> outPaperComprehensiveVOAll(@Param("year") String year,
                                                          @Param("paperName")String paperName,
                                                          @Param("author")String author,
                                                          @Param("paperId")String paperId);

    /**
     * 根据ids查
     * @param strings
     * @return
     */
    List<PaperComprehensiveVO> outPaperComprehensiveVOIds(@Param("strings") String[] strings);

    /**
     * 取年份
     * @return
     */
    List<Map<String,Object>> year();

    /**
     * 查询论文含有的所有领域
     * @return
     */
    List<Map<String,Object>> fieldList();

    /**
     * 评分统计论文查询
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @param start
     * @param end
     * @return
     */
    List<Map<String,Object>> statisticsMap(@Param("year") String year,
                                           @Param("paperName") String paperName,
                                           @Param("paperId") String paperId,
                                           @Param("field") String field,
                                           @Param("start") int start,
                                           @Param("end") int end);

    /**
     * 评分统计总数
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    int statisticsCount(@Param("year") String year,
                        @Param("paperName") String paperName,
                        @Param("paperId") String paperId,
                        @Param("field") String field);

    /**
     * 评分统计论文查询不带分页（excel导出用）
     * @param year
     * @param paperName
     * @param paperId
     * @param field
     * @return
     */
    List<Map<String,Object>> statisticsMapExcel(@Param("year") String year,
                                                @Param("paperName") String paperName,
                                                @Param("paperId") String paperId,
                                                @Param("field") String field);

    /**
     * 评分统计根据选中的ids查询论文（excel导出用）
     * @param uuids
     * @return
     */
    List<Map<String,Object>> statisticsMapIds(@Param("uuids") String[] uuids);

}
