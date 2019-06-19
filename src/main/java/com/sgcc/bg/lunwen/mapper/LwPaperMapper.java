package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwPaper;
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
    public Integer addLwPaper(LwPaper lwPaper);

    /**
     * 修改论文信息
     * @param lwPaper
     * @return
     */
    public Integer updateLwPaper(LwPaper lwPaper);

    /**
     * 修改生成打分表状态
     * @param uuid
     * @param scoreTableStatus
     * @return
     */
    public Integer updateScoreTableStatus(
            @Param("uuid")String uuid,
            @Param("scoreTableStatus") String scoreTableStatus
    );

    /**
     * 修改打分状态
     * @param uuid
     * @param scoreTableStatus
     * @return
     */
    public Integer updateScoreStatus(
            @Param("uuid")String uuid,
            @Param("scoreTableStatus")String scoreTableStatus
    );

    /**
     * 修改论文全生命周期状态
     * @param uuid
     * @param allStatus
     * @return
     */
    public Integer updateAllStatus(
            @Param("uuid")String uuid,
            @Param("allStatus") String allStatus
    );

    /**
     * 查找某条论文信息,或根据题目查重
     * @param uuid,paperName
     * @return
     */
    public LwPaper findPaper(
            @Param("uuid")String uuid,
            @Param("paperName") String paperName
    );

    /**
     * 删除论文信息，逻辑删除，单纯修改有效状态
      * @param uuid
     * @return
     */
    public Integer delLwPaper(
            @Param("uuid") String uuid
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
    public List<Map<String, Object>> selectLwPaper(
            @Param("pageStart") String pageStart,
            @Param("pageEnd") String pageEnd,
            @Param("paperName") String paperName,
            @Param("paperId") String paperId,
            @Param("year") String year,
            @Param("unit") String unit,
            @Param("author") String author,
            @Param("field") String field,
            @Param("scoreStatus") String scoreStatus
    );


}
