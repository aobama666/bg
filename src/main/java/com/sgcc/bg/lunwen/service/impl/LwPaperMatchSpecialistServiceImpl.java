package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LwPaperMatchSpecialistServiceImpl implements LwPaperMatchSpecialistService {

    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;

    @Override
    public Integer addPMS(LwPaperMatchSpecialist lwPaperMatchSpecialist) {
        return lwPaperMatchSpecialistMapper.addPMS(lwPaperMatchSpecialist);
    }
}
