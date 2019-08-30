package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.bean.LwFile;
import com.sgcc.bg.lunwen.mapper.LwFileMapper;
import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.mapper.YyApplyAnnexMapper;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import com.sgcc.bg.yygl.service.YyApplyAnnexService;
import com.sgcc.bg.yygl.service.YyApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyApplyAnnexServiceImpl implements YyApplyAnnexService {

    @Autowired
    private YyApplyAnnexMapper applyAnnexMapper;

    @Autowired
    private YyApplyService yyApplyService;
    @Autowired
    private LwFileMapper lwFileMapper;

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
        YyApplyAnnex yyApplyAnnex = null;
        Map<String,Object> useSealFile = null;
        Map<String,Object> proofFile = null;
        for(String uuid : uuidStr){

            //查询对应材料附件信息
            yyApplyAnnex = applyAnnexMapper.findApplyAnnex(uuid);
            useSealFile = lwFileMapper.findLwFile(yyApplyAnnex.getUseSealFileId());
            proofFile = lwFileMapper.findLwFile(yyApplyAnnex.getProofFileId());

            //删除ftp上的信息
            

            //删除数据库中bg_lw_file的对应信息
            lwFileMapper.delLwFile(yyApplyAnnex.getUseSealFileId(),"0");
            lwFileMapper.delLwFile(yyApplyAnnex.getProofFileId(),"0");

            //删除数据库中bg_ll_apply_annex的对应信息
            applyAnnexMapper.delApplyAnnex(uuid);
        }
        return "成功删除成功"+uuidStr.length+"份材料信息";
    }

    @Override
    public String stuffAdd(YyApplyAnnex yyApplyAnnex) {
        yyApplyAnnex.setCreateUser(yyApplyService.getLoginUserUUID());
        applyAnnexMapper.addApplyAnnex(yyApplyAnnex);
        return null;
    }

    @Override
    public String fileAdd(String stuffUuid, File file) {
        //初始化主键和ftp文件名称
        String fileUuid = Rtext.getUUID();;
        String ftpFileName = Rtext.getUUID();
        //获取文件名称和后缀
        String fileName = file.getName();
        String fileNameAfter = fileName.substring(fileName.lastIndexOf("."),fileName.length());

        //重命名
        File ftpFile = new File(ftpFileName+fileNameAfter);
        ftpFile.renameTo(file);
        //上传文件至ftp服务器对应文件夹
        FtpUtils.uploadFile(ftpFile,FtpUtils.UseSealStuffPath);

        //给附件实体赋值，保存附件基本信息
        LwFile lwFile = new LwFile();
        lwFile.setUuid(fileUuid);
        lwFile.setFileName(fileName);
        lwFile.setFtpFileName(ftpFileName+fileNameAfter);
        lwFile.setBussinessId(stuffUuid);
        lwFile.setBussinessTable("bg_yy_apply_annex");
        lwFile.setFileExtName(fileNameAfter);
        lwFile.setFtpFilePath(FtpUtils.UseSealStuffPath+ftpFileName+fileNameAfter);
        lwFile.setBussinessModule("yygl");
        lwFile.setFileSize(file.length()+"");
        lwFile.setCreateUser(yyApplyService.getLoginUserUUID());
        lwFile.setCreateTime(new Date());
        lwFile.setValid("1");
        lwFileMapper.addLwFile(lwFile);

        //返回附件主键信息
        return fileUuid;
    }
}
