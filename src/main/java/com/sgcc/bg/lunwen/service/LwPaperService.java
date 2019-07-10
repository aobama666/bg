package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface LwPaperService {

    /**
     * 添加论文
     * @param lwPaper
     * @return
     */
    Integer addLwPaper(LwPaper lwPaper);

    /**
     * 修改论文
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
            String uuid,
            String scoreTableStatus
    );

    /**
     * 修改打分状态
     * @param uuid
     * @param scoreTableStatus
     * @return
     */
    Integer updateScoreStatus(
            String uuid,
            String scoreTableStatus
    );

    /**
     * 修改全流程状态
     * @param uuid
     * @param allStatus
     * @return
     */
    Integer updateAllStatus(
            String uuid,
            String allStatus
    );

    /**
     * 查找论文，按照主键，亦或论文题目
     * @param uuid
     * @param paperName
     * @return
     */
    Map<String, Object> findPaper(
            String uuid,
            String paperName
    );

    /**
     * 获取当前编号，按照当前类别最大迭代
     * @param paperType
     * @return
     */
    String maxPaperId(String paperType);

    /**
     * 删除论文
     * @param uuid
     * @return
     */
    Integer delLwPaper(
            String uuid
    );

    /**
     * 论文管理，查找，分页
     * @param pageStart
     * @param pageEnd
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @param scoreStatus
     * @param paperType
     * @return
     */
    List<Map<String, Object>> selectLwPaper(
            Integer pageStart,
            Integer pageEnd,
            String paperName,
            String paperId,
            String year,
            String unit,
            String author,
            String field,
            String scoreStatus,
            String paperType
    );

    /**
     * 查询符合论文领域的专家
     * @param authors
     * @param unit
     * @param field
     * @return
     */
    List<LwSpecialist> selectSpecialistField(
            String[] authors,
            String unit,
            String field);


    /**
     * 提供分页查询总数
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @param scoreStatus
     * @param paperType
     * @return
     */
    public Integer selectLwPaperCount(String paperName,
                                      String paperId,
                                      String year,
                                      String unit,
                                      String author,
                                      String field,
                                      String scoreStatus,
                                      String paperType
    );

    /**
     * 导出查询
     * @param paperName
     * @param paperId
     * @param year
     * @param unit
     * @param author
     * @param field
     * @param scoreStatus
     * @param paperType
     * @param ids
     * @param response
     * @return
     */
    public List<LwPaper> selectLwpaperExport(String paperName,
                                    String paperId,
                                    String year,
                                    String unit,
                                    String author,
                                    String field,
                                    String scoreStatus,
                                    String paperType,
                                    String ids,
                                    HttpServletResponse response);

    /**
     * 首次，亦或重复自动匹配
     * @param lwPaperMap
     * @return
     */
    public Integer autoMaticSecond(Map<String,Object> lwPaperMap,String paperUuid);


    /**
     * 解析上传的批量录入excel表格
     * @param in
     * @return
     */
    String[] joinExcel(InputStream in);

    /**
     * 查询论文含有的所有领域
     * @return
     */
    List<Map<String,Object>> fieldList();
}
