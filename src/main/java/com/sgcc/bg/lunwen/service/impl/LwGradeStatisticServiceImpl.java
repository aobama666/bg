package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwGradeStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LwGradeStatisticServiceImpl implements LwGradeStatisticService{

    @Autowired
    private LwSpecialistMapper lwSpecialistMapper;

    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Override
    public List<Map<String, Object>> statisticsSpecialistName(String year, String paperName, String paperId, String field) {
        List<Map<String, Object>> statisticsSpecialistName = lwSpecialistMapper.statisticsSpecialistName(year,paperName,paperId,field);
        int  num=0;
        for (Map<String, Object> map : statisticsSpecialistName) {
            num++;
            map.put("scoresName","scores"+num);
        }
        return statisticsSpecialistName;
    }

    @Override
    public List<PaperComprehensiveVO> comprehensiveVOList(String year, String paperName, String paperId, String field, int start, int end) {
        List<PaperComprehensiveVO> comprehensiveVOList = lwPaperMapper.comprehensiveVOList(year,paperName,paperId,field,start,end);
        List<Map<String, Object>> statisticsSpecialistName = lwSpecialistMapper.statisticsSpecialistName(year,paperName,paperId,field);
        List list = new ArrayList();
        for(Map s : statisticsSpecialistName){
            list.add(s.get("UUID"));
        }
        for (PaperComprehensiveVO paperComprehensiveVO : comprehensiveVOList){
           /* //取专家打的分值
            List<Map<String,Object>> SpeciList = lwSpecialistMapper.SpeciList(paperComprehensiveVO.getUuid(),list);
            paperComprehensiveVO.setList(SpeciList);*/

            List<Map<String, Object>> paperSpecialist = lwSpecialistMapper.paperSpecialist(paperComprehensiveVO.getUuid());
            if (paperSpecialist != null && paperSpecialist.size() > 0) {
                List<LwPaperMatchSpecialist> lwPaperMatchSpecialistList = new ArrayList<>();
                List<Map<String, Object>> lwPaperMatchSpecialistMap = new ArrayList<Map<String, Object>>();
                int  num=0;
                for (Map<String, Object> map : paperSpecialist) {
                    num++;
                    LwPaperMatchSpecialist lwPaperMatchSpecialist = new LwPaperMatchSpecialist();
                    lwPaperMatchSpecialist.setSpecialistId(String.valueOf(map.get("UUID")));
                    if(map.get("SCORE")!= null && map.get("SCORE")!= "" ) {
                        lwPaperMatchSpecialist.setScore(String.valueOf(map.get("SCORE")));
                        map.put("scores"+num,map.get("SCORE"));
                    }else {
                        lwPaperMatchSpecialist.setScore("0");
                        map.put("scores"+num,"0");
                    }
                    map.put("scoresName","scores"+num);
                    lwPaperMatchSpecialistMap.add(map);
                    lwPaperMatchSpecialistList.add(lwPaperMatchSpecialist);
                }
                paperComprehensiveVO.setLwPaperMatchSpecialistList(lwPaperMatchSpecialistList);
                paperComprehensiveVO.setMaps(lwPaperMatchSpecialistMap);
            }
        }
        return comprehensiveVOList;
    }
}
