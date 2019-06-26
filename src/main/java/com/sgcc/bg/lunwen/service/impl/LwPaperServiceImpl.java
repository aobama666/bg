
package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class LwPaperServiceImpl implements LwPaperService {

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Override
    public Integer addLwPaper(LwPaper lwPaper) {
        //处理论文编号，根据论文类型和上一个编号id，叠加
        StringBuffer before = new StringBuffer();
        if(LwPaperConstant.LW_TYPE_X.equals(lwPaper.getPaperType())){
            before.append(LwPaperConstant.XUESHU);
        }else if(LwPaperConstant.LW_TYPE_J.equals(lwPaper.getPaperType())){
            before.append(LwPaperConstant.JISHU);
        }else if(LwPaperConstant.LW_TYPE_Z.equals(lwPaper.getPaperType())){
            before.append(LwPaperConstant.ZONGSHU);
        }
        //从数据库中查询当前类型的最大值后加1
        String paperId = lwPaperMapper.maxPaperId(lwPaper.getPaperType());
        if(null == paperId || "".equals(paperId)){
            //这个类型的第一批论文
            paperId = "000";
        }
        paperId = paperId.substring(1,paperId.length());
        Integer maxNum = Integer.valueOf(paperId);
        maxNum = ++maxNum;
        paperId = String.format("%03d",maxNum);
        before.append(paperId);
        lwPaper.setPaperId(before.toString());

        //初始化时间，创建人，各种状态
        lwPaper.setYear(DateUtil.getYear());
        lwPaper.setCreateTime(new Date());
        lwPaper.setScoreTableStatus(LwPaperConstant.SCORE_TABLE_OFF);
        lwPaper.setScoreStatus(LwPaperConstant.SCORE_STATUS_NO);
        lwPaper.setAllStatus(LwPaperConstant.ALL_STATUS_ONE);
        lwPaper.setValid(LwPaperConstant.VALID_YES);
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
        return lwPaperMapper.findPaper(uuid,paperName,LwPaperConstant.VALID_YES);
    }

    @Override
    public String maxPaperId(String paperType) {
        return lwPaperMapper.maxPaperId(paperType);
    }

    @Override
    public Integer delLwPaper(String uuid) {
        return lwPaperMapper.delLwPaper(uuid, LwPaperConstant.VALID_NO);
    }

    @Override
    public List<Map<String, Object>> selectLwPaper(Integer pageStart, Integer pageEnd,
             String paperName, String paperId, String year, String unit,
             String author, String field,String scoreStatus,String paperType) {
        return lwPaperMapper.selectLwPaper(pageStart,pageEnd,paperName,paperId,
                year,unit,author,field,scoreStatus,paperType,LwPaperConstant.VALID_YES);
    }

    @Override
    public Integer selectLwPaperCount(String paperName, String paperId, String year, String unit,
                                      String author, String field, String scoreStatus, String paperType) {
        return lwPaperMapper.selectLwPaperCount(paperName,paperId,year,unit,author,
                field,scoreStatus,paperType,LwPaperConstant.VALID_YES);
    }
}