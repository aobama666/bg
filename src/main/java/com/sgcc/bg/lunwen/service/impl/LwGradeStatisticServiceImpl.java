package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.ResultWarp;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwGradeStatisticService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    /*@Transactional*/
    public int againReview(String[] uuidStr) {
        //修改论文的评审状态 改为 重新评审
        Date date = new Date();
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        String userId = user.getUserId();
        int j=0;
        int z=0;
        ResultWarp rw = null;
        for(int i=0 ; i<uuidStr.length; i++){
            j = lwPaperMapper.updateAllStatus(uuidStr[i], LwPaperConstant.P_A_S_REVIEW);
            z = lwPaperMatchSpecialistMapper.updateScoreStatus(uuidStr[i],getLoginUserUUID(),LwPaperConstant.SCORE_STATUS_AGAIN);
        }
        return j;
    }

    @Override
    public List<Map<String, Object>> outStatisticExcel(String year, String paperName, String paperId, String field,String ids,HttpServletResponse response) {
        List<Map<String, Object>> statisticsMap = new ArrayList<>();
        List<Map<String, Object>> statisticsSpecialistName = statisticsSpecialistName(year,paperName,paperId,field);
        if(ids!=null && ids!=""){
            String [] uuids=ids.split(",");
            statisticsMap = lwPaperMapper.statisticsMapIds(uuids);
        }else {
            statisticsMap = lwPaperMapper.statisticsMapExcel(year,paperName,paperId,field);
        }
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
        int size = statisticsSpecialistName.size();
        Object[][] title2 = new Object[size+4][];
        int i=2;
        title2[0]=new Object[]{ "编号", "PAPERID","nowrap" };
        title2[1]=new Object[]{ "论文题目", "PAPERNAME","nowrap" };
        for(Map map:statisticsSpecialistName){
            title2[i]=new Object[]{map.get("NAME"),map.get("scoresName"),"nowrap"};
            i++;
        }
        title2[size+2]=new Object[]{ "加权平均分", "WEIGHTINGFRACTION","nowrap" };
        title2[size+3]=new Object[]{ "去最高最低得分", "AVERAGEFRACTION","nowrap" };

        Object[][] title = title2;
        ExportExcelHelper.getExcel(response, "论文详情"+ DateUtil.getDays(), title, statisticsMap, "normal");
        return statisticsMap;
    }

    /**
     * 获取当前登录用户主键id
     */
    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }
}
