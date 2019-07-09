
package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.DataDictionaryService;
import com.sgcc.bg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class LwPaperServiceImpl implements LwPaperService {

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private LwPaperMapper lwPaperMapper;
    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;
    @Autowired
    private LwPaperService lwPaperService;
    @Autowired
    private UserService userService;
    @Autowired
    private LwPaperMatchSpecialistService lwPaperMatchSpecialistService;

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
    public List<LwSpecialist> selectSpecialistField(String[] authors, String unit, String field) {
        return lwPaperMapper.selectSpecialistField(authors,unit,field,LwPaperConstant.VALID_YES);
    }

    @Override
    public Integer selectLwPaperCount(String paperName, String paperId, String year, String unit,
                                      String author, String field, String scoreStatus, String paperType) {
        return lwPaperMapper.selectLwPaperCount(paperName,paperId,year,unit,author,
                field,scoreStatus,paperType,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<LwPaper>  selectLwpaperExport(String paperName, String paperId, String year, String unit
            , String author, String field, String scoreStatus, String paperType, String ids, HttpServletResponse response) {
        List<LwPaper> list = new ArrayList<>();
        if(ids == null || ids == "") {
            list = lwPaperMapper.selectLwPaperExport(paperName,paperId,year,unit,author,field,scoreStatus,paperType,LwPaperConstant.VALID_YES);
        }else {
            String [] uuids=ids.split(",");
            list = lwPaperMapper.selectCheckIdExport(uuids);
        }
        Object[][] title = {
                { "论文题目", "paperName","nowrap" },
                { "作者", "author","nowrap" },
                { "单位", "unit","nowrap" },
                { "期刊名称", "journal","nowrap" },
                { "推荐单位", "recommendUnit","nowrap" },
                { "论文类型", "paperType","nowrap" },
                { "被引量", "quoteCount","nowrap" },
                { "下载量", "downloadCount","nowrap" },
                { "领域","field","nowrap"}
        };
        ExportExcelHelper.getExcel(response, "论文详情"+ DateUtil.getDays(), title, list, "normal");
        return list;
    }


    /**
     * 重复匹配————附加首次匹配功能，缩减代码量
     * @param lwPaperMap
     * @param paperUuid
     * @return
     */
    @Override
    public Integer autoMaticSecond(Map<String, Object> lwPaperMap,String paperUuid) {
        String field = lwPaperMap.get("FIELD").toString();
        String unit = lwPaperMap.get("UNIT").toString();
        String author = lwPaperMap.get("AUTHOR").toString();
        String[] authors = null;
        if(author.contains(",")){
            authors = author.split(",");
        }else if(author.contains("，")){
            authors = author.split("，");
        }else{
            authors = new String[]{author};
        }
        //根据论文所属领域，查询能够匹配的专家
        List<LwSpecialist> lwSpList = lwPaperService.selectSpecialistField(authors,unit,field);
        //查询已经匹配上的专家
        List<LwPaperMatchSpecialist> matchSpecialists = lwPaperMatchSpecialistService.selectPMS(paperUuid,null);
        //判断是否有重复，剔除原有匹配专家信息
        for(LwPaperMatchSpecialist lpm : matchSpecialists){
            String specialistId = lpm.getSpecialistId();
            for(int i =0;i<lwSpList.size();i++){
                if(lwSpList.get(i).getUuid().equals(specialistId)){
                    lwSpList.remove(i);
                    break;
                }
            }
        }
        //排序
        List<String> specialistIdList = new ArrayList<>();
        //1.领域，研究方向，皆相同，不同单位，不同作者,统一控制总数，最多15个
        for(LwSpecialist lwSpecialist : lwSpList){
            if(lwSpecialist.getField().contains(field) && lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<15){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //2.领域相同，研究方向不同，不同单位，不同作者
        for(LwSpecialist lwSpecialist : lwSpList){
            if(lwSpecialist.getField().contains(field) && !lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<15){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //3.研究方向相同,领域不同，不同单位，不同作者
        for(LwSpecialist lwSpecialist : lwSpList){
            if(!lwSpecialist.getField().contains(field) && lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<15){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //查询当前论文对应匹配表中最大排序数
        String maxSort = lwPaperMatchSpecialistMapper.findSpecialistSort(paperUuid,LwPaperConstant.VALID_YES);
        Integer specialistSort = 0;
        if(!"".equals(maxSort) && null!=maxSort){
            specialistSort = Integer.valueOf(maxSort);
        }
        //添加新的专家信息
        LwPaperMatchSpecialist lwPaperMatchSpecialist;
        for(int i =0;i<specialistIdList.size();i++){
            lwPaperMatchSpecialist = new LwPaperMatchSpecialist();
            lwPaperMatchSpecialist.setPaperId(paperUuid);
            lwPaperMatchSpecialist.setSpecialistId(specialistIdList.get(i));
            lwPaperMatchSpecialist.setScoreStatus(LwPaperConstant.SCORE_STATUS_NO);
            lwPaperMatchSpecialist.setCreateUser(getLoginUserUUID());
            lwPaperMatchSpecialist.setSpecialistSort((specialistSort+i+1)+"");
            lwPaperMatchSpecialistService.addPMS(lwPaperMatchSpecialist);
        }
        //返回当前匹配总数
        return lwSpList.size()+specialistSort;
    }

    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }

}