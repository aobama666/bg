package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.yygl.mapper.YyApplyAnnexMapper;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import com.sgcc.bg.yygl.service.YyApplyAnnexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyApplyAnnexServiceImpl implements YyApplyAnnexService {

    @Autowired
    private YyApplyAnnexMapper applyAnnexMapper;

    @Override
    public Map<String, Object> selectApplyAnnex(String applyUuid) {
        //查询对应申请信息的所有用印材料信息
        List<YyApplyAnnexDAO> annexDAOList = applyAnnexMapper.selectApplyAnnex(applyUuid);
        //查询数据封装
        Map<String, Object> listMap = new HashMap<String, Object>();
        listMap.put("data", annexDAOList);
        return listMap;
    }

    @Override
    public String delApplyAnnex(String uuids) {
        String[] uuidStr = uuids.split(",");
        for(String uuid : uuidStr){
            //查询对应材料附件信息
            //删除ftp上的信息
            //删除数据库中bg_lw_file的对应信息
            //删除数据库中bg_ll_apply_annex的对应信息
            applyAnnexMapper.delApplyAnnex(uuid);
        }
        return "成功删除成功"+uuidStr.length+"份材料信息";
    }

    @Override
    public String stuffAdd() {
        //上传用印材料文件
        //上传佐证材料文件
        //保存文件基本信息
        //保存用印材料基本信息
        return null;
    }
}
