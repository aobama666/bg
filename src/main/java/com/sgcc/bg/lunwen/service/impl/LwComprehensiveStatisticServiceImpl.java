package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.lunwen.bean.PaperComprehensiveVO;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwComprehensiveStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Service
public class LwComprehensiveStatisticServiceImpl implements LwComprehensiveStatisticService {

    @Autowired
    private LwPaperMapper lwPaperMapper;

    @Autowired
    private LwSpecialistMapper lwSpecialistMapper;

    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;


    @Override
    public List<Map<String,Object>> year() {
        List<Map<String,Object>> year = lwPaperMapper.year();
        return year;
    }

    @Override
    public List<PaperComprehensiveVO> paperComprehensiveVOList(String year, String paperName, String author, String paperId,int start,int end) {
        List<PaperComprehensiveVO> paperComprehensiveVOList = lwPaperMapper.paperComprehensiveVOList(year,paperName,author,paperId,start,end);
        if(paperComprehensiveVOList!=null && paperComprehensiveVOList.size()>0){
            for(PaperComprehensiveVO paperComprehensiveVO : paperComprehensiveVOList){
                //取论文评判的专家
                List<Map<String,Object>> paperSpecialist = lwSpecialistMapper.paperSpecialist(paperComprehensiveVO.getUuid());
                if(paperSpecialist!=null && paperSpecialist.size()>0) {
                    StringBuilder sname = new StringBuilder();
                    for (Map<String, Object> map : paperSpecialist) {
                        sname.append(map.get("NAME")).append("，");
                    }
                    sname.deleteCharAt(sname.length() - 1);
                    paperComprehensiveVO.setSpecialistName(sname);
                }
                //判断专家是否全部完成打分
                /*if("6".equals(paperComprehensiveVO.getAllStatus())){
                    Map<String,String> mapScore = mapScore(paperComprehensiveVO.getUuid());
                    paperComprehensiveVO.setWeightingFraction(mapScore.get("weightingFraction"));
                    paperComprehensiveVO.setAverageFraction(mapScore.get("averageFraction"));
                }*/
            }
        }
        return paperComprehensiveVOList;
    }

    @Override
    public List<PaperComprehensiveVO> outPaperComprehensiveVO(HttpServletResponse response, String year, String paperName, String author, String paperId, String ids) {
        List<PaperComprehensiveVO>  outPaperComprehensiveVO = new ArrayList<>();
        if(ids==null || ids == ""){
            outPaperComprehensiveVO = lwPaperMapper.outPaperComprehensiveVOAll(year,paperName,paperId,author);
        }else {
            String [] strings=ids.split(",");
            outPaperComprehensiveVO = lwPaperMapper.outPaperComprehensiveVOIds(strings);
        }
        if(outPaperComprehensiveVO!=null && outPaperComprehensiveVO.size()>0){
            for(PaperComprehensiveVO paperComprehensiveVO : outPaperComprehensiveVO){
                //取论文评判的专家
                List<Map<String,Object>> paperSpecialist = lwSpecialistMapper.paperSpecialist(paperComprehensiveVO.getUuid());
                if(paperSpecialist!=null && paperSpecialist.size()>0) {
                    StringBuilder sname = new StringBuilder();
                    for (Map<String, Object> map : paperSpecialist) {
                        sname.append(map.get("NAME")).append("，");
                    }
                    sname.deleteCharAt(sname.length() - 1);
                    paperComprehensiveVO.setSpecialistName(sname);
                }
                //判断专家是否全部完成打分
                /*if("6".equals(paperComprehensiveVO.getAllStatus())){
                    Map<String,String> mapScore = mapScore(paperComprehensiveVO.getUuid());
                    paperComprehensiveVO.setWeightingFraction(mapScore.get("weightingFraction"));
                    paperComprehensiveVO.setAverageFraction(mapScore.get("averageFraction"));
                }*/
            }
        }
        Object[][] title = {
                { "编号", "paperId","nowrap" },
                { "论文题目", "paperName","nowrap" },
                { "作者", "author","nowrap" },
                { "作者单位","unit","nowrap"},
                { "期刊名称","journal","nowrap"},
                { "领域","field","nowrap"},
                { "推荐单位","recommendUnit","nowrap"},
                { "被引量","quoteCount","nowrap"},
                { "下载量","downloadCount","nowrap"},
                { "专家信息","specialistName","nowrap"},
                { "加权平均分","weightingFraction","nowrap"},
                { "去最高最低得分","averageFraction","nowrap"}
        };
        ExportExcelHelper.getExcel(response, "评分统计"+ DateUtil.getDays(), title, outPaperComprehensiveVO, "normal");
        return outPaperComprehensiveVO;
    }

    /**
     * 计算平均分
     * @param uuid
     * @return
     */
    private Map<String,String> mapScore(String uuid){
        List<Double> scoreList = lwPaperMatchSpecialistMapper.scoreList(uuid);
        String weightingFraction;
        String averageFraction;
        if(scoreList.size()>2){
            //最大
            Double max = Collections.max(scoreList);
            //最小
            Double min = Collections.min(scoreList);
            //和
            Double sum = Double.valueOf(0);
            for(int i = 0 ;i<scoreList.size(); i++) {
                sum += (Double)scoreList.get(i);
            }
            //平均分 String.format("%.2f",(i+j)/2)
            weightingFraction = String.format("%.2f",sum/scoreList.size());
            //去最高最低
            averageFraction = String.format("%.2f",(sum-max-min)/(scoreList.size()-2));
        }else {
            Double sum = Double.valueOf(0);
            for(int i = 0 ;i<scoreList.size(); i++) {
                sum += (Double)scoreList.get(i);
            }
            //平均分 String.format("%.2f",(i+j)/2)
            weightingFraction = String.format("%.2f",sum/scoreList.size());
            //去最高最低
            averageFraction = String.format("%.2f",sum/scoreList.size());
        }
        Map<String,String> mapScore = new HashMap();
        mapScore.put("weightingFraction",weightingFraction);
        mapScore.put("averageFraction",averageFraction);
        return mapScore;
    }
}
