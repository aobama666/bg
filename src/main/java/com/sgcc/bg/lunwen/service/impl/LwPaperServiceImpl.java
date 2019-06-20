package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.service.LwPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LwPaperServiceImpl implements LwPaperService {

    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Override
    public Integer addLwPaper(LwPaper lwPaper) {
        return lwPaperMapper.addLwPaper(lwPaper);
    }

    @Override
    public Integer updateLwPaper(LwPaper lwPaper) {
        return lwPaperMapper.updateLwPaper(lwPaper);
    }

    @Override
    public Integer updateScoreTableStatus(String uuid, String scoreTableStatus) {
        return lwPaperMapper.updateScoreTableStatus(uuid,scoreTableStatus);
    }

    @Override
    public Integer updateScoreStatus(String uuid, String scoreTableStatus) {
        return lwPaperMapper.updateScoreStatus(uuid,scoreTableStatus);
    }

    @Override
    public Integer updateAllStatus(String uuid, String allStatus) {
        return lwPaperMapper.updateAllStatus(uuid,allStatus);
    }

    @Override
    public Map<String, Object> findPaper(String uuid, String paperName) {
        return lwPaperMapper.findPaper(uuid,paperName);
    }

    @Override
    public Integer delLwPaper(String uuid) {
        return lwPaperMapper.delLwPaper(uuid);
    }

    @Override
    public List<Map<String, Object>> selectLwPaper(Integer pageStart, Integer pageEnd,
             String paperName, String paperId, String year, String unit,
             String author, String field,String scoreStatus,String paperType) {
        return lwPaperMapper.selectLwPaper(pageStart,pageEnd,paperName,paperId,
                year,unit,author,field,scoreStatus,paperType);
    }

    @Override
    public Integer selectLwPaperCount(String paperName, String paperId, String year, String unit, String author, String field, String scoreStatus, String paperType) {
        return lwPaperMapper.selectLwPaperCount(paperName,paperId,year,unit,author,field,scoreStatus,paperType);
    }
}
