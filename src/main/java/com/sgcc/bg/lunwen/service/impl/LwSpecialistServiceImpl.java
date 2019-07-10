package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.common.*;
import com.sgcc.bg.lunwen.bean.LwPaperMatchSpecialist;
import com.sgcc.bg.lunwen.bean.LwSpecialist;
import com.sgcc.bg.lunwen.bean.PaperVO;
import com.sgcc.bg.lunwen.mapper.LwPaperMatchSpecialistMapper;
import com.sgcc.bg.lunwen.mapper.LwSpecialistMapper;
import com.sgcc.bg.lunwen.service.LwSpecialistService;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.service.impl.BgNonProjectServiceImpl;
import com.sgcc.bg.service.impl.StaffWorkbenchServiceImpl;
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
public class LwSpecialistServiceImpl implements LwSpecialistService {

    @Autowired
    private LwSpecialistMapper lwSpecialistMapper;

    @Autowired
    private LwPaperMatchSpecialistMapper lwPaperMatchSpecialistMapper;

    @Autowired
    private WebUtils webUtils;

    @Autowired
    private UserService userService;

    private static Logger lwServiceLog =  LoggerFactory.getLogger(LwSpecialistServiceImpl.class);



    @Override
    public List<LwSpecialist> expertList(String name, String researchDirection, String unitName, String field, String matchStatus, int start, int end) {
        List<LwSpecialist> expertList = lwSpecialistMapper.expertList(name,researchDirection,unitName,field,matchStatus,start,end);
        return expertList;
    }

    @Override
    public int insertExpert(LwSpecialist lwSpecialist) {
        String uuid = Rtext.getUUID();
        lwSpecialist.setUuid(uuid);
        lwSpecialist.setCreateTime(new Date());
        lwSpecialist.setUpdateTime(new Date());
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        lwSpecialist.setCreateUser(user.getUserId());
        lwSpecialist.setUpdateUser(user.getUserId());
        lwSpecialist.setValid("1");
        lwSpecialist.setMatchStatus("0");
        int i = lwSpecialistMapper.insertExpert(lwSpecialist);
        return i;
    }

    @Override
    public LwSpecialist lwSpecialist(String uuid) {
        LwSpecialist lwSpecialist = lwSpecialistMapper.lwSpecialist(uuid);
        return lwSpecialist;
    }

    @Override
    public int updateExpert(LwSpecialist lwSpecialist) {
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        lwSpecialist.setUpdateUser(user.getUserId());
        lwSpecialist.setUpdateTime(new Date());
        int i = lwSpecialistMapper.updateExpert(lwSpecialist);
        return i;
    }

    @Override
    public int count(String name, String researchDirection, String unitName, String field, String matchStatus) {
        int count = lwSpecialistMapper.count(name,researchDirection,unitName,field,matchStatus);
        return count;
    }

    @Override
    public String deleteSpecialist(String ids) {
        String [] strings=ids.split(",");
        int i = lwSpecialistMapper.deleteSpecialist(strings);
        String str = "删除成功"+i+"条数据"+"，"+"删除失败"+(strings.length-i)+"条数据";
        return str;
    }

    /**
     * 专家匹配的论文
     * @param uuid
     * @return
     */
    @Override
    public List<PaperVO> paperMap(String uuid) {
        List<PaperVO> paperMap = lwSpecialistMapper.paperMap(uuid);
        return paperMap;
    }

    @Override
    public List<PaperVO> paperMapPage(String uuid, int start, int end) {
        List<PaperVO> paperMapPage = lwSpecialistMapper.paperMapPage(uuid,start,end);
        return paperMapPage;
    }

    @Override
    public int specialistAndPaperCount(String uuid) {
        int specialistAndPaperCount = lwSpecialistMapper.specialistAndPaperCount(uuid);
        return specialistAndPaperCount;
    }

    @Override
    public List<LwSpecialist> exportSelectedItems(String name, String researchDirection, String unitName,
                                                  String field, String matchStatus,String ids,HttpServletResponse response) {
        List<LwSpecialist> list = new ArrayList<>();
        if(ids == null || ids == "") {
           list = lwSpecialistMapper.list(name, researchDirection, unitName, field, matchStatus);
        }else {
            String [] strings=ids.split(",");
            /*for(int i=0;i<strings.length;i++){
                String uuid=strings[i];
                LwSpecialist lwSpecialist = lwSpecialistMapper.lwSpecialist(uuid);
                list.add(lwSpecialist);
            }*/
            list = lwSpecialistMapper.listIds(strings);
        }
        Object[][] title = {
                { "专家姓名", "name","nowrap" },
                { "详细地址", "address","nowrap" },
                { "单位名称", "unitName","nowrap" },
                { "单位性质","unitNature","nowrap"},
                { "职务/职称","position","nowrap"},
                { "研究方向","researchDirection","nowrap"},
                { "领域","field","nowrap"},
                { "联系电话","phone","nowrap"},
                { "电子邮件","email","nowrap"},
                { "匹配状态","matchStatus","nowrap"}
        };
        ExportExcelHelper.getExcel(response, "专家详情"+ DateUtil.getDays(), title, list, "normal");
        return list;
    }

    @Override
    public String[] joinExcel(InputStream in) {
        HSSFWorkbook wb = null;
        //获取通过验证的非项目信息
        List<LwSpecialist> lwSpecialistList=new ArrayList<>();
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
            List<String> list=lwSpecialistMapper.getEmail();
            String regex = "^([1-9]+0*|[1-9]*\\.[05]|0\\.5)$";
            lwServiceLog.info("项目信息excel表格最后一行： " + rows);
            /* 保存有效的Excel模版列数 */
            String[] cellValue = new String[10];

            for (int i = 1; i <=rows; i++) {
                // 获取正式数据并封装进cellValue数组中
                StringBuffer checkStr = new StringBuffer();
                lwServiceLog.info("第" + (i + 1) + "行");
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
                        lwServiceLog.info("cell is null");
                    }
                    checkStr.append(cellValue[c]);
                    lwServiceLog.info("cellValue is " + cellValue[c]);
                }
                //校验此行是否为空
                if (!"#N/A!#N/A!".equals(checkStr.toString()) && !"".equals(checkStr.toString())) {
                    StringBuffer errorInfo = new StringBuffer();
                    Set<Integer> errorNum = new HashSet<Integer>();

                    if(cellValue[1] == null || cellValue[1] == ""){
                        errorInfo.append("专家姓名不能为空！ ");
                        errorNum.add(1);
                    }else if (cellValue[1].length() > 20){
                        errorInfo.append("专家姓名不能超过20个字！ ");
                        errorNum.add(1);
                    }

                    if (cellValue[2] == null || cellValue[2] == ""){
                        errorInfo.append("详细地址不能为空！ ");
                        errorNum.add(2);
                    }else if (cellValue[2].length() > 30){
                        errorInfo.append("详细地址不能超过30个字！ ");
                        errorNum.add(2);
                    }

                    if(cellValue[3] == null || cellValue[3] ==""){
                        errorInfo.append("单位名称不能为空！ ");
                        errorNum.add(3);
                    }else if (cellValue[3].length()>20){
                        errorInfo.append("单位名称不能超过20个字！ ");
                        errorNum.add(3);
                    }

                    if(cellValue[4] == null || cellValue[4] ==""){
                        errorInfo.append("单位性质不能为空！ ");
                        errorNum.add(4);
                    }else if (cellValue[4].length()>20){
                        errorInfo.append("单位性质不能超过20个字！ ");
                        errorNum.add(4);
                    }

                    if(cellValue[5] == null || cellValue[5] ==""){
                        errorInfo.append("职务/职称不能为空！ ");
                        errorNum.add(5);
                    }else if (cellValue[5].length()>20){
                        errorInfo.append("职务/职称不能超过20个字！ ");
                        errorNum.add(5);
                    }

                    if(cellValue[6] == null || cellValue[6] ==""){
                        errorInfo.append("研究方向不能为空！ ");
                        errorNum.add(6);
                    }else if (cellValue[6].length()>20){
                        errorInfo.append("研究方向不能超过20个字！ ");
                        errorNum.add(6);
                    }

                    if(cellValue[7] == null || cellValue[7] ==""){
                        errorInfo.append("领域不能为空！ ");
                        errorNum.add(7);
                    }else if (cellValue[7].length()>20){
                        errorInfo.append("领域不能超过20个字！ ");
                        errorNum.add(7);
                    }

                    if(cellValue[8] == null || cellValue[8] ==""){
                        errorInfo.append("手机号码不能为空！ ");
                        errorNum.add(8);
                    }else if (cellValue[8].length()>20){
                        errorInfo.append("手机号码不能超过20位！ ");
                        errorNum.add(8);
                    }

                    if(cellValue[9] == null || cellValue[9] ==""){
                        errorInfo.append("邮箱不能为空！ ");
                        errorNum.add(9);
                    }else if (ParamValidationUtil.eimail(cellValue[9])){
                        errorInfo.append("邮箱格式不对！ ");
                        errorNum.add(9);
                    }else {
                        for (String obj : list) {
                            if (obj.equals(cellValue[9])) {
                                errorInfo.append("该电子邮箱已存在！ ");
                                errorNum.add(9);
                            }
                        }
                    }
                    // 校验结束，分流数据
                    if ("".equals(errorInfo.toString())) {
                        LwSpecialist lwSpecialist = new LwSpecialist();
                        lwSpecialist.setUuid(Rtext.getUUID());
                        lwSpecialist.setName(cellValue[1]);
                        lwSpecialist.setAddress(cellValue[2]);
                        lwSpecialist.setUnitName(cellValue[3]);
                        lwSpecialist.setUnitNature(cellValue[4]);
                        lwSpecialist.setPosition(cellValue[5]);
                        lwSpecialist.setResearchDirection(cellValue[6]);
                        lwSpecialist.setField(cellValue[7]);
                        lwSpecialist.setPhone(cellValue[8]);
                        lwSpecialist.setEmail(cellValue[9]);
                        lwSpecialist.setMatchStatus("0");
                        String userName = webUtils.getUsername();
                        HRUser user = userService.getUserByUserName(userName);
                        lwSpecialist.setCreateUser(user.getUserId());
                        lwSpecialist.setUpdateUser(user.getUserId());
                        lwSpecialist.setCreateTime(new Date());
                        lwSpecialist.setUpdateTime(new Date());
                        lwSpecialist.setValid("1");
                        lwSpecialistList.add(lwSpecialist);
                    }else {
                        Map<String,Object> map=new HashMap<>();
                        map.put("id",cellValue[0]);
                        map.put("name",cellValue[1]);
                        map.put("address",cellValue[2]);
                        map.put("unitName",cellValue[3]);
                        map.put("unitNature",cellValue[4]);
                        map.put("position",cellValue[5]);
                        map.put("researchDirection",cellValue[6]);
                        map.put("field",cellValue[7]);
                        map.put("phone",cellValue[8]);
                        map.put("email",cellValue[9]);
                        map.put("errInfo",errorInfo.toString());
                        map.put("errSet",errorNum);
                        errorList.add(map);
                    }
                }
            }

            // 返回错误数据
            if (errorList.size() > 0) {
                lwServiceLog.info("出错的项目： " + errorList);
                Object[][] title = {
                        {"序号","id","nowrap"},
                        { "专家姓名", "name","nowrap" },
                        { "详细地址", "address","nowrap" },
                        { "单位名称", "unitName","nowrap" },
                        { "单位性质","unitNature","nowrap"},
                        { "职务/职称","position","nowrap"},
                        { "研究方向","researchDirection","nowrap"},
                        { "领域","field","nowrap"},
                        { "联系电话","phone","nowrap"},
                        { "电子邮件","email","nowrap"},
                        { "错误说明","errInfo"}
                };
                errorUUID = ExportExcelHelper.createErrorExcel(FtpUtils.BgTempUploadPath, title, errorList);
                lwServiceLog.info("errorUUID: " + errorUUID);
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
        if(!lwSpecialistList.isEmpty()) {
            lwSpecialistMapper.addList(lwSpecialistList);
        }
        String[] object = {"成功导入项目信息"+lwSpecialistList.size()+"条，失败"+errorList.size()+"条",errorUUID};
        return object;
    }

    //论文领域同专家领域（精准）、论文领域同专家研究方向（精准）、回避本人、回避本单位，回避已匹配的专家
    @Override
    public Map<String, Object> renewalMap(String uuid) {
        //专家已匹配的所有论文
        List<PaperVO> paperMap = lwSpecialistMapper.paperMap(uuid);
        //存本人及已匹配的专家
        Set spUuid = new HashSet();
        //存论文涉及到的本单位
        Set unitSet = new HashSet();
        //储存涉及到的领域
        Set fieIdSet = new HashSet();
        //符合条件的专家list
        List<LwSpecialist> matchingSpecialistList = new ArrayList<>();
        for(PaperVO paperVO : paperMap){
            //取出论文已经匹配的专家
            List<Map<String,Object>> paperSpecialist = lwSpecialistMapper.paperSpecialist(paperVO.getUuid());
            for (Map<String, Object> map : paperSpecialist) {
               spUuid.add(map.get("UUID"));
            }
            //根据论文的作者姓名和单位查询出作者id（专家id）
            LwSpecialist specialistMatching = lwSpecialistMapper.specialistMatching(paperVO.getAuthor(),paperVO.getUnit());
            //回避本人（取出该论文专家本人的id 放入到spuuid中）
            if(specialistMatching!=null){
                spUuid.add(specialistMatching.getUuid());
            }
            unitSet.add(paperVO.getUnit());
            fieIdSet.add(paperVO.getField());
        }
        if(fieIdSet.size()==1) {
            //String fieId = fieIdSet.toString();
            String fieId = fieIdSet.toArray()[0].toString();
            matchingSpecialistList = lwSpecialistMapper.matchingSpecialistList(spUuid, unitSet, fieId);
        }
        Map map = new HashMap();
        map.put("paperMap",paperMap);
        map.put("matchingSpecialistList",matchingSpecialistList);
        map.put("beforeUuid",uuid);
        return map;
    }

    @Override
    public int renewal(String beforeUuid, String nowUuid) {
        List<PaperVO> paperMap = lwSpecialistMapper.paperMap(beforeUuid);
        List<LwPaperMatchSpecialist> paperMatchSpecialist = new ArrayList<>();
        for(PaperVO paperVO : paperMap){
            LwPaperMatchSpecialist lwPaperMatchSpecialist = new LwPaperMatchSpecialist();
            lwPaperMatchSpecialist.setUuid(Rtext.getUUID());
            lwPaperMatchSpecialist.setPaperId(paperVO.getUuid());
            lwPaperMatchSpecialist.setSpecialistId(nowUuid);
            lwPaperMatchSpecialist.setScoreStatus("0");
            lwPaperMatchSpecialist.setCreateTime(new Date());
            lwPaperMatchSpecialist.setUpdateTime(new Date());
            String userName = webUtils.getUsername();
            HRUser user = userService.getUserByUserName(userName);
            lwPaperMatchSpecialist.setCreateUser(user.getUserId());
            lwPaperMatchSpecialist.setUpdateUser(user.getUserId());
            lwPaperMatchSpecialist.setValid("1");
            paperMatchSpecialist.add(lwPaperMatchSpecialist);
        }
        //删除原来专家匹配的信息
        int i = lwPaperMatchSpecialistMapper.updateValid(beforeUuid);
        //修改原来专家的匹配状态
        lwSpecialistMapper.updateMatchStatus(beforeUuid,"0");
        //增加新的
        int j = lwPaperMatchSpecialistMapper.insertMatch(paperMatchSpecialist);
        //修改新匹配专家的匹配状态
        lwSpecialistMapper.updateMatchStatus(nowUuid,"1");
        return j;
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
