package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperVO;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface LwSpecialistService {
    List<LwSpecialist> expertList(String name, String researchDirection, String unitName, String field, String matchStatus, int start, int end);

    int insertExpert(LwSpecialist lwSpecialist);


    LwSpecialist lwSpecialist(String uuid);

    int updateExpert(LwSpecialist lwSpecialist);

    int count(String name, String researchDirection, String unitName, String field, String matchStatus);

    int deleteExpert(String uuid);

    /**
     * 专家匹配的论文
     * @param uuid
     * @return
     */
    List<PaperVO> paperMap(String uuid);

    /**
     * 专家匹配的论文带分页
     * @param uuid
     * @param start
     * @param end
     * @return
     */
    List<PaperVO> paperMapPage(String uuid, int start, int end);

    /**
     * 专家匹配的论文总数
     * @param uuid
     * @return
     */
    int specialistAndPaperCount(String uuid);

    /**
     * 导出专家详情
     * @param name
     * @param researchDirection
     * @param unitName
     * @param field
     * @param matchStatus
     */
    List<LwSpecialist> exportSelectedItems(String name, String researchDirection, String unitName, String field, String matchStatus, String ids,HttpServletResponse response);

    /**
     * 解析上传的批量录入excel表格
     * @param in
     * @return
     */
    String[] joinExcel(InputStream in);
}
