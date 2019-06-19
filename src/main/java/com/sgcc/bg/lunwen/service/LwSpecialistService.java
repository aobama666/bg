package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwSpecialist;

import java.util.List;
import java.util.Map;

public interface LwSpecialistService {
    List<Map<String,Object>> expertList(String name, String researchDirection, String unitName, String field, String matchStatus, int start, int end);

    int insertExpert(LwSpecialist lwSpecialist);


    Map<String, String> lwSpecialist(String id);

    int updateExpert(LwSpecialist lwSpecialist);
}
