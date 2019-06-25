
package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.service.LwPaperService;
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
        if("1".equals(lwPaper.getPaperType())){
            before.append("X");
        }else if("2".equals(lwPaper.getPaperType())){
            before.append("J");
        }else if("3".equals(lwPaper.getPaperType())){
            before.append("Z");
        }
        //先做个随机数，后期改成从数据库中查询当前类型的最大值后加1
        Random random = new Random();
        before.append(random.nextInt(1000));
        lwPaper.setPaperId(before.toString());

        String userName = webUtils.getUsername();
        //初始化时间，创建人，各种状态
        lwPaper.setYear(DateUtil.getYear());
        lwPaper.setUuid(Rtext.getUUID());
        lwPaper.setCreateUser(userName);
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