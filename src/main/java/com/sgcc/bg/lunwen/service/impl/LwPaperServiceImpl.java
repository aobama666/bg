
package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.*;
import com.sgcc.bg.lunwen.bean.LwPaper;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwPaperMapper;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwPaperMatchSpecialistService;
import com.sgcc.bg.lunwen.service.LwPaperService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class LwPaperServiceImpl implements LwPaperService {

    private static Logger log = LoggerFactory.getLogger(LwPaperServiceImpl.class);

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
        lwPaper.setAllStatus(LwPaperConstant.P_A_S_SAVED);
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
    public Integer updateScoreStatus(String uuid, String scoreStatus) {
        return lwPaperMapper.updateScoreStatus(uuid,scoreStatus);
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
        return lwPaperMapper.selectSpecialistField(authors,unit,field,
                LwPaperConstant.VALID_YES,LwPaperConstant.SPECIALIST_MATCH_SHIELD);
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
                { "被引量", "quoteCount","nowrap" },
                { "下载量", "downloadCount","nowrap" },
                { "领域","field","nowrap"}
        };
        paperType = list.get(0).getPaperType();
        ExportExcelHelper.getExcel(response, paperType+"-论文详情"+ DateUtil.getDays(), title, list, "normal");
        return list;
    }

    @Override
    public List<Map<String, Object>> fieldList() {
        List<Map<String, Object>> fieldLsit = lwPaperMapper.fieldList();
        return fieldLsit;
    }

    @Override
    public List<Map<String,Object>> getTableYear() {
        List<Map<String,Object>> yearList = lwPaperMapper.year();
        return yearList;
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
        //成功匹配数量
        int successMatchNums = matchSpecialists.size();
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
        int limitNum = 15 - successMatchNums;
        //排序
        List<String> specialistIdList = new ArrayList<>();
        //1.领域，研究方向，皆相同，不同单位，不同作者,统一控制总数，最多15个
        for(LwSpecialist lwSpecialist : lwSpList){
            if(lwSpecialist.getField().contains(field) && lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<limitNum){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //2.领域相同，研究方向不同，不同单位，不同作者
        for(LwSpecialist lwSpecialist : lwSpList){
            if(lwSpecialist.getField().contains(field) && !lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<limitNum){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //3.研究方向相同,领域不同，不同单位，不同作者
        for(LwSpecialist lwSpecialist : lwSpList){
            if(!lwSpecialist.getField().contains(field) && lwSpecialist.getResearchDirection().contains(field)
                    && specialistIdList.size()<limitNum){
                specialistIdList.add(lwSpecialist.getUuid());
            }
        }
        //查询当前论文对应匹配表中最大排序数
        String maxSort = lwPaperMatchSpecialistMapper.findSpecialistSort(paperUuid);
        Integer specialistSort = 0;
        if(!"".equals(maxSort) && null!=maxSort){
            specialistSort = Integer.valueOf(maxSort);
        }

        //控制匹配专家数量不能为双数
        int readyMatchNums = matchSpecialists.size()+specialistIdList.size();
        if(readyMatchNums>7 && readyMatchNums%2 == 0){
            specialistIdList.remove(specialistIdList.size()-1);
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
            successMatchNums = successMatchNums+1;
        }
        //返回当前匹配总数
        return successMatchNums;
    }

    @Override
    public String[] joinExcel(InputStream in,String paperType) {
        HSSFWorkbook wb = null;
        //获取通过验证的非项目信息
        List<LwPaper> lwPaperList=new ArrayList<>();
        //出错非项目信息
        List<Map<String,Object>> errorList = new ArrayList<>();
        //存储在ftp的错误文件名称
        String errorUUID = "";
        try{
            POIFSFileSystem fs = null;
            HSSFSheet sheet;
            HSSFRow row;
            HSSFCell cell;
            // 获取POI操作对象
            fs = new POIFSFileSystem(in);
            wb = new HSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);
            // 得到sheet内总行数
            int rows = sheet.getLastRowNum();
            //从数据字典中获取项目类型
            //Map<String,String> dictMap=dict.getDictDataByPcode("category100002");
            //获取所有项目编号存入一个集合
//            List<String> list=lwSpecialistMapper.getEmail();
            String regex = "^([1-9]+0*|[1-9]*\\.[05]|0\\.5)$";
            log.info("项目信息excel表格最后一行： " + rows);
            /* 保存有效的Excel模版列数 */
            String[] cellValue = new String[9];

            for (int i = 1; i <=rows; i++) {
                // 获取正式数据并封装进cellValue数组中
                StringBuffer checkStr = new StringBuffer();
                log.info("第" + (i + 1) + "行");
                row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                for (int c = 0; c < cellValue.length; c++) {
                    cell= row.getCell(c);// StringUtils.trimToEmpty(sheet.getCell(c,
                    if (cell != null) {
                        cellValue[c]= ExcelUtil.getStringCellValueForOnLine(cell);
                    }else{
                        cellValue[c]="";
                        log.info("cell is null");
                    }
                    checkStr.append(cellValue[c]);
                    log.info("cellValue is " + cellValue[c]);
                }
                //校验此行是否为空
                if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {
                    StringBuffer errorInfo = new StringBuffer();
                    Set<Integer> errorNum = new HashSet<Integer>();
                    if(cellValue[1] == null || cellValue[1] == ""){
                        errorInfo.append("论文题目不能为空！ ");
                        errorNum.add(1);
                    }else if (cellValue[1].length() > 50){
                        errorInfo.append("论文题目不能超过50个字！ ");
                        errorNum.add(1);
                    }else{
                        Map<String,Object> lwMap = lwPaperService.findPaper(null,cellValue[1]);
                        if(lwMap != null){
                            errorInfo.append("论文题目已存在！ ");
                            errorNum.add(1);
                        }
                    }

                    if (cellValue[2] == null || cellValue[2] == ""){
                        errorInfo.append("作者不能为空！ ");
                        errorNum.add(2);
                    }else if (cellValue[2].length() > 50){
                        errorInfo.append("作者不能超过50个字！ ");
                        errorNum.add(2);
                    }

                    if(cellValue[3] == null || cellValue[3] ==""){
                        errorInfo.append("单位不能为空！ ");
                        errorNum.add(3);
                    }else if (cellValue[3].length()>50){
                        errorInfo.append("单位不能超过50个字！ ");
                        errorNum.add(3);
                    }

                    if(cellValue[4] == null || cellValue[4] ==""){
                        errorInfo.append("期刊名称不能为空！ ");
                        errorNum.add(4);
                    }else if (cellValue[4].length()>50){
                        errorInfo.append("期刊名称不能超过50个字！ ");
                        errorNum.add(4);
                    }

                    if(cellValue[5] == null || cellValue[5] ==""){
                        errorInfo.append("推荐单位不能为空！ ");
                        errorNum.add(5);
                    }else if (cellValue[5].length()>200){
                        errorInfo.append("推荐单位不能超过200个字！ ");
                        errorNum.add(5);
                    }

                    if(cellValue[6] == null || cellValue[6] ==""){
                        errorInfo.append("被引量不能为空！ ");
                        errorNum.add(6);
                    }else if (cellValue[6].length()>8){
                        errorInfo.append("被引量不能超过8个字！ ");
                        errorNum.add(6);
                    }

                    if(cellValue[7] == null || cellValue[7] ==""){
                        errorInfo.append("下载量不能为空！ ");
                        errorNum.add(7);
                    }else if (cellValue[7].length()>8){
                        errorInfo.append("下载量不能超过8个字！ ");
                        errorNum.add(7);
                    }

                    if(cellValue[8] == null || cellValue[8] ==""){
                        errorInfo.append("领域不能为空！ ");
                        errorNum.add(8);
                    }else if (cellValue[8].length()>20){
                        errorInfo.append("领域不能超过20位！ ");
                        errorNum.add(8);
                    }

                    // 校验结束，分流数据，成功入库，反之入错误文件
                    LwPaper lwPaper = null;
                    String userName = webUtils.getUsername();
                    HRUser user = userService.getUserByUserName(userName);
                    if ("".equals(errorInfo.toString())) {
                        lwPaper = new LwPaper();
                        lwPaper.setUuid(Rtext.getUUID());
                        lwPaper.setPaperName(cellValue[1]);
                        lwPaper.setAuthor(cellValue[2]);
                        lwPaper.setUnit(cellValue[3]);
                        lwPaper.setJournal(cellValue[4]);
                        lwPaper.setRecommendUnit(cellValue[5]);
                        lwPaper.setQuoteCount(cellValue[6]);
                        lwPaper.setDownloadCount(cellValue[7]);
                        lwPaper.setField(cellValue[8]);
                        lwPaper.setPaperType(paperType);
                        lwPaper.setCreateUser(user.getUserId());
                        lwPaperList.add(lwPaper);
                        lwPaperService.addLwPaper(lwPaper);
                    }else {
                        Map<String,Object> map=new HashMap<>();
                        map.put("id",cellValue[0]);
                        map.put("paperName",cellValue[1]);
                        map.put("author",cellValue[2]);
                        map.put("unit",cellValue[3]);
                        map.put("journal",cellValue[4]);
                        map.put("recommendUnit",cellValue[5]);
                        map.put("quoteCount",cellValue[6]);
                        map.put("downloadCount",cellValue[7]);
                        map.put("field",cellValue[8]);
                        map.put("errInfo",errorInfo.toString());
                        map.put("errSet",errorNum);
                        errorList.add(map);
                    }
                }
            }

            // 返回错误数据
            if (errorList.size() > 0) {
                log.info("出错的项目： " + errorList);
                Object[][] title = {
                        {"序号","id","nowrap"},
                        { "论文题目", "paperName","nowrap" },
                        { "作者", "author","nowrap" },
                        { "单位", "unit","nowrap" },
                        { "期刊名称","journal","nowrap"},
                        { "推荐单位","recommendUnit","nowrap"},
                        { "被引量","quoteCount","nowrap"},
                        { "下载量","downloadCount","nowrap"},
                        { "领域","field","nowrap"},
                        { "错误数量","errSet","nowrap"},
                        { "错误说明","errInfo"}
                };
                errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
                log.info("errorUUID: " + errorUUID);
            }
        }catch (Exception e){
            e.printStackTrace();
            String[] object = {"Error:项目信息导入失败，请重新导入！",""};
            return object;
        }finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] object = {"成功导入项目信息"+lwPaperList.size()+"条，失败"+errorList.size()+"条",errorUUID};
        return object;
    }

    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }

}