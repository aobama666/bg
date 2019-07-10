package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwGradeStatisticService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LwGradeStatisticServiceImpl implements LwGradeStatisticService{

    @Autowired
    private LwSpecialistMapper lwSpecialistMapper;

    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private UserService userService;

    @Override
    public List<Map<String, Object>> statisticsSpecialistName(String year, String paperName, String paperId, String field) {
        List<Map<String, Object>> statisticsSpecialistName = lwSpecialistMapper.statisticsSpecialistName(year,paperName,paperId,field);
        int  num=0;
        for (Map<String, Object> map : statisticsSpecialistName) {
            map.put("scoresName","scores"+num);
            num++;
        }
        return statisticsSpecialistName;
    }

    @Override
    public List<Map<String, Object>> statisticsMap(String year, String paperName, String paperId, String field, int start, int end) {
        List<Map<String, Object>> statisticsMap = lwPaperMapper.statisticsMap(year,paperName,paperId,field,start,end);
        List<Map<String, Object>> statisticsSpecialistName = statisticsSpecialistName(year,paperName,paperId,field);
        List list = new ArrayList();
        /*for(Map s : statisticsSpecialistName){
            list.add(s.get("UUID"));
        }*/
        for(Map<String,Object> map : statisticsMap){
            List<Map<String,Object>> SpeciList = lwSpecialistMapper.SpeciList(String.valueOf(map.get("UUID")));
            for(Map<String,Object> speciMap : SpeciList){
                for(Map<String,Object> li:statisticsSpecialistName){
                    if(speciMap.get("UUID").equals(li.get("UUID"))){
                        map.put(String.valueOf(li.get("scoresName")),speciMap.get("SCORE"));
                    }
                }
            }
        }
        return statisticsMap;
    }

    @Override
    public int statisticsCount(String year, String paperName, String paperId, String field) {
        int count = lwPaperMapper.statisticsCount(year,paperName,paperId,field);
        return count;
    }

    @Override
    public String againReview(String[] uuidStr) {
        //修改论文的评审状态 改为 重新评审
        Date date = new Date();
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        String userId = user.getUserId();
        for(int i=0 ; i<uuidStr.length; i++){
            int j = lwPaperMapper.updateAllStatus(uuidStr[i],"4");
            //int z = lwPaperMatchSpecialistMapper.updateScoreStatus()
        }
        return null;
    }
}
