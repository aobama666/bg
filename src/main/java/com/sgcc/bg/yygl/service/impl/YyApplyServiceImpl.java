package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.DateUtil;
import com.sgcc.bg.common.ExportExcelHelper;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.common.WebUtils;
import com.sgcc.bg.model.HRUser;
import com.sgcc.bg.service.UserService;
import com.sgcc.bg.yygl.bean.YyApply;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.mapper.YyApplyMapper;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.service.YyApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyApplyServiceImpl implements YyApplyService {

    @Autowired
    private YyApplyMapper yyApplyMapper;
    @Autowired
    private WebUtils webUtils;
    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> selectApply(
            String applyCode,String startTime,String endTime,String useSealStatus,String itemSecondId,String useSealReason
            ,Integer page, Integer limit,String userId
    ) {
        //查询参数初始化
        applyCode = Rtext.toStringTrim(applyCode,"");
        startTime = Rtext.toStringTrim(startTime,"");
        endTime = Rtext.toStringTrim(endTime,"");
        useSealStatus = Rtext.toStringTrim(useSealStatus,"");
        itemSecondId = Rtext.toStringTrim(itemSecondId,"");
        useSealReason = Rtext.toStringTrim(useSealReason,"");
        if(null != startTime && !"".equals(startTime)){
            startTime = startTime + " 00:00:00";
        }
        if(null != endTime && !"".equals(endTime)){
            endTime = endTime + " 23:59:59";
        }
        //初始化分页
        int pageStart = 0;
        int pageEnd = 10;
        if(page != null && limit!=null && page>0 && limit>0){
            pageStart = (page-1)*limit;
            pageEnd = page*limit;
        }
        //查内容
        List<Map<String,Object>> applyList = yyApplyMapper.selectApply(
                applyCode,startTime,endTime,useSealStatus,itemSecondId,useSealReason,pageStart,pageEnd,userId
        );
        //查数量
        Integer total = yyApplyMapper.selectApplyTotal(applyCode,startTime,endTime,useSealStatus,itemSecondId,useSealReason,userId);
        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", applyList);
        listMap.put("total", total);
        return listMap;
    }

    @Override
    public void applyExport(HttpServletRequest request, HttpServletResponse response) {
        String applyCode = request.getParameter("applyCode").trim();
        String startTime = request.getParameter("startTime").trim();
        String endTime = request.getParameter("endTime").trim();
        String useSealStatus = request.getParameter("useSealStatus").trim();
        String itemSecondId = request.getParameter("itemSecondId").trim();
        String useSealReason = request.getParameter("useSealReason").trim();
        String userId = getLoginUserUUID();
        String ids = request.getParameter("checkList");

        List<YyApplyDAO> list = new ArrayList<>();
        if(ids == null || ids == "") {
            list = yyApplyMapper.selectApplyExport(applyCode,startTime,endTime,useSealStatus,itemSecondId,useSealReason,userId,null);
        }else {
            String [] uuids=ids.split(",");
            list = yyApplyMapper.selectApplyExport(null,null,null,
                    null,null,null,userId,uuids);
        }
        Object[][] title = {
                { "申请编号", "applyCode","nowrap" },
                { "用印事由", "useSealReason","nowrap" },
                { "用印部门", "applyDept","nowrap" },
                { "用印申请人", "applyUser","nowrap" },
                { "用印日期", "useSealDate","nowrap" },
                { "申请日期", "createTime","nowrap" },
                { "用印事项", "useSealItem","nowrap" },
                { "用印种类","useSealKind","nowrap"},
                { "审批状态","useSealStatusValue","nowrap"},
        };
        ExportExcelHelper.getExcel(response,"用印申请详情-"+ DateUtil.getDays(), title, list, "normal");
    }

    @Override
    public String nextApplyCode() {
        String nextApplyCode = "";
        //获取当前库中编号
        List<String> applyCodeList = yyApplyMapper.findDayApplyCode(DateUtil.getDays());
        if(applyCodeList.size() == 0){
            nextApplyCode = DateUtil.getDays()+"-"+"00001";
        }else{
            //转为int数组
            Integer[] applyCodeListI = new Integer[applyCodeList.size()];
            Integer code;
            for(int i = 0; i<applyCodeList.size();i++){
                code = Integer.valueOf(applyCodeList.get(i));
                applyCodeListI[i] = code;
            }
            //排序取得当前最大值
            for(int i = 0; i<applyCodeListI.length; i++){
                for(int j =0; j<applyCodeListI.length-1; j++){
                    if(applyCodeListI[j] < applyCodeListI[j+1]){
                        code = applyCodeListI[j];
                        applyCodeListI[j] = applyCodeListI[j+1];
                        applyCodeListI[j+1] = code;
                    }
                }
            }
            code = applyCodeListI[0];
            nextApplyCode = DateUtil.getDays()+"-"+String.format("%05d",code+1);
        }
        return nextApplyCode;
    }

    @Override
    public List<Map<String, Object>> getItemFirst() {
        return yyApplyMapper.getItemFirst();
    }

    @Override
    public List<Map<String, Object>> getItemSecond(String firstCategoryId) {
        return yyApplyMapper.getItemSecond(firstCategoryId);
    }

    @Override
    public Map<String, Object> findDept(String userId) {
        return yyApplyMapper.findDept(userId);
    }

    @Override
    public String applyAdd(YyApply yyApply) {
        //主键
        String uuid = Rtext.getUUID();
        yyApply.setUuid(uuid);
        //获取申请编号
        String applyCode = nextApplyCode();
        yyApply.setApplyCode(applyCode);
        yyApply.setCreateUser(getLoginUserUUID());
        //初始化状态为待提交
        yyApply.setUseSealStatus(YyApplyConstant.STATUS_DEAL_SUB);
        //入库
        yyApplyMapper.addApply(yyApply);
        //返回主键信息
        return uuid;
    }

    @Override
    public String applyUpdate(YyApply yyApply) {
        return null;
    }

    @Override
    public YyApplyDAO applyDeatil(String applyUuid) {
        return yyApplyMapper.findApply(applyUuid);
    }


    /**
     * 获取当前登录用户主键id
     */
    public String getLoginUserUUID(){
        String userName = webUtils.getUsername();
        HRUser user = userService.getUserByUserName(userName);
        return user.getUserId();
    }

    @Override
    public String applyDel(String checkedContent) {
        //拆分
        String[] checkedIds = checkedContent.split(",");
        Integer successNum = 0;
        Integer failNum = 0;
        for(String checkedId : checkedIds){
            int result = yyApplyMapper.applyDel(checkedId,YyApplyConstant.STATUS_DEAL_SUB);
            //pull

        }
        //循环删除
        //反馈几个能删几个不能删
        return null;
    }
}
