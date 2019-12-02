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
     */
    Integer addLwPaper(LwPaper lwPaper);

    /**
     * 修改论文信息
     */
    Integer updateLwPaper(LwPaper lwPaper);

    /**
     * 修改生成打分表状态
     */
    Integer updateScoreTableStatus(
            @Param("uuid") String uuid,
            @Param("scoreTableStatus") String scoreTableStatus
    );

    /**
     * 修改打分状态
     */
    Integer updateScoreStatus(
            @Param("uuid") String uuid,
            @Param("scoreStatus") String scoreStatus
    );

    /**
     * 修改论文全生命周期状态
     */
    Integer updateAllStatus(
            @Param("uuid") String uuid,
            @Param("allStatus") String allStatus
    );

    /**
     * 批量修改全流程状态，生成、撤回打分表基础sql
     */
    Integer batchUpdateAllStatus(
            @Param("allStatus") String allStatus,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 批量修改打分表状态，生成、撤回打分表基础sql
     */
    Integer batchUpdateScoreTableStatus(
            @Param("scoreTableStatus") String scoreTableStatus,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 查找某条论文信息,或根据题目查重
     */
    Map<String, Object> findPaper(
            @Param("uuid") String uuid,
            @Param("paperName") String paperName,
            @Param("valid") String valid
    );

    /**
     * 查找当前对应类型的最大编号
     */
    String maxPaperId(@Param("paperType") String paperType, @Param("year") String year);

    /**
     * 删除论文信息，逻辑删除，单纯修改有效状态
     */
    Integer delLwPaper(
            @Param("uuid") String uuid,
            @Param("valid") String valid
    );

    /**
     * 论文管理按条件查某批论文-
     * 条件：年度，论文题目，论文编号，单位，作者，领域，打分状态
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
            @Param("allStatus") String allStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 论文管理按条件查某批论文-取分页总数
     */
    Integer selectLwPaperCount(
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("allStatus") String allStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 综合统计论文查询
     */
    List<PaperComprehensiveVO> paperComprehensiveVOList(@Param("year") String year,
                                                        @Param("paperName") String paperName,
                                                        @Param("author") String author,
                                                        @Param("paperId") String paperId,
                                                        @Param("start") int start,
                                                        @Param("end") int end);

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
    List<PaperComprehensiveVO> selectCheckIdExport(@Param("uuids") String[] uuids);

    /**
     * 按条件查询某批论文信息——导出
     * 条件：年度，论文题目，论文编号，单位，作者，领域
     */
    List<PaperComprehensiveVO> selectLwPaperExport(
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("allStatus") String allStatus,
            @Param("paperType") String paperType,
            @Param("valid") String valid
    );

    /**
     * 获取匹配领域的专家信息
     * 如果有likeMatch，代表模糊匹配研究方向，如果没有则是精准匹配研究方向和领域
     */
    List<LwSpecialist> selectSpecialistField(
            @Param("authors") String[] authors,
            @Param("units") String[] units,
            @Param("field") String field,
            @Param("valid") String valid,
            @Param("matchStatus") String matchStatus,
            @Param("likeMatch") String likeMatch
    );


    /**
     * 查全部--统计导出
     */
    List<PaperComprehensiveVO> outPaperComprehensiveVOAll(@Param("year") String year,
                                                          @Param("paperName") String paperName,
                                                          @Param("author") String author,
                                                          @Param("paperId") String paperId);

    /**
     * 根据ids查
     */
    List<PaperComprehensiveVO> outPaperComprehensiveVOIds(@Param("strings") String[] strings);

    /**
     * 取年份
     */
    List<Map<String,Object>> year();

    /**
     * 查询论文含有的所有领域
     */
    List<Map<String,Object>> fieldList(@Param("year") String year);

    /**
     * 评分统计论文查询
     */
    List<Map<String,Object>> statisticsMap(@Param("year") String year,
                                           @Param("paperName") String paperName,
                                           @Param("paperId") String paperId,
                                           @Param("field") String field,
                                           @Param("start") int start,
                                           @Param("end") int end);

    /**
     * 评分统计总数
     */
    int statisticsCount(@Param("year") String year,
                        @Param("paperName") String paperName,
                        @Param("paperId") String paperId,
                        @Param("field") String field);

    /**
     * 评分统计论文查询不带分页（excel导出用）
     */
    List<Map<String,Object>> statisticsMapExcel(@Param("year") String year,
                                                @Param("paperName") String paperName,
                                                @Param("paperId") String paperId,
                                                @Param("field") String field);

    /**
     * 评分统计根据选中的ids查询论文（excel导出用）
     */
    List<Map<String,Object>> statisticsMapIds(@Param("uuids") String[] uuids);


    /**
     * 修改平均分，和最高最低得分
     */
    Integer updateAverageScore(
            @Param("paperId") String paperId,
            @Param("averageScore") Double averageScore,
            @Param("highestLowestAverage") Double highestLowestAverage
    );

    /**
     * 自动匹配，查看当前没有附件信息的论文
     */
    List<Map<String,Object>> notAnnexPaper(
            @Param("bussinessTable") String bussinessTable,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 查看匹配未达标的论文数量，按论文类型划分
     */
    List<Map<String,Object>> matchingPaper(
            @Param("allStatus") String allStatus,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 生成打分表前提条件，判断当年全部论文是否匹配完成
     */
    List<Map<String,Object>> ifAllMatch(
            @Param("matched") String matched,
            @Param("withdraw") String withdraw,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 撤回打分表的前提条件，判断当年全部论文是否未打分
     */
    List<Map<String,Object>> ifAllUnrated(
            @Param("scoreStatus") String scoreStatus,
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 查看当前有效论文全流程状态的最大数，论文评审的最快进展
     * 但是这种排序由于字段类型关系只能在10以内排序，符合当前8种状态
     * 如果有10以上的请及时更换其他方式
     */
    Integer maxAllStatus(
            @Param("year") String year,
            @Param("valid") String valid
    );

    /**
     * 当前年份，能够处理的所有论文主键id,主要提供自动匹配操作
     */
    List<String> allPaperPrimaryKey(
            @Param("year") String year,
            @Param("valid") String valid
    );

}
