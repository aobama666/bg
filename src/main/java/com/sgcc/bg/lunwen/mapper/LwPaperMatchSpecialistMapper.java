package com.sgcc.bg.lunwen.mapper;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LwPaperMatchSpecialistMapper {


    /**
     * 查论文得分
     * @param uuid
     * @return
     */
    List<Double> scoreList(String uuid);


}
