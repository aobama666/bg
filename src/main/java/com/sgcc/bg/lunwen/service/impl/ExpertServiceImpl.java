package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.mapper.ExpertMapper;
import com.sgcc.bg.lunwen.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExpertServiceImpl implements ExpertService {

    @Autowired
    private ExpertMapper expertMapper;
    @Autowired
    private WebUtils webUtils;


    @Override
    public List<Map<String, Object>> expertList(String name, String researchDirection, String unitName, String field, String matchStatus, int start, int end) {
        List<Map<String, Object>> expertList = expertMapper.expertList(name,researchDirection,unitName,field,matchStatus,start,end);
        return expertList;
    }

    @Override
    public int insertExpert(LwSpecialist lwSpecialist) {
        String uuid = Rtext.getUUID();
        lwSpecialist.setUuid(uuid);
        lwSpecialist.setCreateTime(new Date());
        lwSpecialist.setUpdateTime(new Date());
        lwSpecialist.setCreateUser(webUtils.getUsername());
        lwSpecialist.setUpdateUser(webUtils.getUsername());
        lwSpecialist.setValid("1");
        int i = expertMapper.insertExpert(lwSpecialist);
        return i;
    }

    @Override
    public Map<String, String> lwSpecialist(String id) {
        Map<String, String> lwSpecialist = expertMapper.lwSpecialist(id);
        return lwSpecialist;
    }

    @Override
    public int updateExpert(LwSpecialist lwSpecialist) {
        lwSpecialist.setUpdateUser(webUtils.getUsername());
        lwSpecialist.setUpdateTime(new Date());
        int i = expertMapper.updateExpert(lwSpecialist);
        return i;
    }


}
