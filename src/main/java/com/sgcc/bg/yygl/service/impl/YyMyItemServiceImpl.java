package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.yygl.mapper.YyMyItemMapper;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import com.sgcc.bg.yygl.pojo.YyApplyDAO;
import com.sgcc.bg.yygl.service.YyMyItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyMyItemServiceImpl implements YyMyItemService{


    @Autowired
    private YyMyItemMapper myItemMapper;

    @Override
    public Map<String,Object> selectMyItem(String applyCode,
                                           String deptId,
                                           String useSealUser,
                                           String ifComingSoon,
                                           Integer page,
                                           Integer limit) {
        applyCode = Rtext.toStringTrim(applyCode,"");
        useSealUser = Rtext.toStringTrim(useSealUser,"");
        //初始化分页
        int pageStart = 0;
        int pageEnd = 10;
        if(page != null && limit!=null && page>0 && limit>0){
            pageStart = (page-1)*limit;
            pageEnd = page*limit;
        }
        //查内容
        List<YyApplyDAO> applyList = myItemMapper.selectMyItem(applyCode,deptId,useSealUser,null,null,pageStart,pageEnd);
        //查数量
        Integer total = myItemMapper.getMyItemNums(applyCode,deptId,useSealUser,null,null);
        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", applyList);
        listMap.put("total", total);
        return listMap;
    }

    @Override
    public String addSign() {
        return null;
    }

    @Override
    public String agree() {
        return null;
    }

    @Override
    public String sendBack() {
        return null;
    }
}
