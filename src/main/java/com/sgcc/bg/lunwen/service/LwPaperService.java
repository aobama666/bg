package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwPaper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LwPaperService {

    Integer addLwPaper(LwPaper lwPaper);

    Integer updateLwPaper(LwPaper lwPaper);

    Integer updateScoreTableStatus(
            String uuid,
            String scoreTableStatus
    );

    Integer updateScoreStatus(
            String uuid,
            String scoreTableStatus
    );

    Integer updateAllStatus(
            String uuid,
            String allStatus
    );

    Map<String, Object> findPaper(
            String uuid,
            String paperName
    );

    String maxPaperId(String paperType);

    Integer delLwPaper(
            String uuid
    );

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

    public Integer selectLwPaperCount(String paperName,
                                      String paperId,
                                      String year,
                                      String unit,
                                      String author,
                                      String field,
                                      String scoreStatus,
                                      String paperType
    );
}
