package com.sgcc.bg.yygl.service.impl;

import com.sgcc.bg.common.FtpUtils;
import com.sgcc.bg.common.Rtext;
import com.sgcc.bg.lunwen.bean.LwFile;
import com.sgcc.bg.lunwen.mapper.LwFileMapper;
import com.sgcc.bg.yygl.bean.YyApplyAnnex;
import com.sgcc.bg.yygl.constant.YyApplyConstant;
import com.sgcc.bg.yygl.controller.YyApplyStuffController;
import com.sgcc.bg.yygl.mapper.YyApplyAnnexMapper;
import com.sgcc.bg.yygl.pojo.YyApplyAnnexDAO;
import com.sgcc.bg.yygl.service.YyApplyAnnexService;
import com.sgcc.bg.yygl.service.YyApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YyApplyAnnexServiceImpl implements YyApplyAnnexService {

    private static Logger log = LoggerFactory.getLogger(YyApplyAnnexServiceImpl.class);

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

            //删除ftp上的信息
            //删除数据库中bg_lw_file的对应信息
            String useSealFtpFilePath = useSealFile.get("FTPFILEPATH").toString();
            FtpUtils.deleteFile(useSealFtpFilePath);
            lwFileMapper.delLwFile(yyApplyAnnex.getUseSealFileId(),"0");

            //如果有佐证材料,顺带也删除一下
            if(null != yyApplyAnnex.getProofFileId() && !"".equals(yyApplyAnnex.getProofFileId())){
                proofFile = lwFileMapper.findLwFile(yyApplyAnnex.getProofFileId());
                String proofFtpFilePath = proofFile.get("FTPFILEPATH").toString();
                FtpUtils.deleteFile(proofFtpFilePath);
                lwFileMapper.delLwFile(yyApplyAnnex.getProofFileId(),"0");
            }

            //删除数据库中用印材料的对应信息
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
    public String fileAdd(String stuffUuid, String fileName) {
        //初始化主键和ftp文件名称
        String fileUuid = Rtext.getUUID();;
        //获取文件名称和后缀
        String fileNameAfter = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        //崭新的文件名称
        String ftpFileNameUuid = Rtext.getUUID();
        String ftpFileName = ftpFileNameUuid+"."+fileNameAfter;

        String localPath = YyApplyConstant.STUFF_UPLOAD_LOCAL_PATH;
        File file = new File(localPath+fileName);
        File targetFile = new File(localPath+ftpFileName);
        file.renameTo(targetFile);
        targetFile = new File(localPath+ftpFileName);

        log.info("用印管理上传文件,文件名:{},上传路径:{},新文件名:{}",fileName, FtpUtils.UseSealStuffPath,ftpFileName);
        FtpUtils.uploadFile(targetFile,FtpUtils.UseSealStuffPath);

        //删除本地服务对应文件
        targetFile.delete();
        file.delete();

        //给附件实体赋值，保存附件基本信息
        LwFile lwFile = new LwFile();
        lwFile.setUuid(fileUuid);
        lwFile.setFileName(fileName);
        lwFile.setFtpFileName(ftpFileName);
        lwFile.setBussinessId(stuffUuid);
        lwFile.setBussinessTable("bg_yy_apply_annex");
        lwFile.setFileExtName(fileNameAfter);
        lwFile.setFtpFilePath(FtpUtils.UseSealStuffPath+ftpFileName);
        lwFile.setBussinessModule("yygl");
        lwFile.setFileSize(targetFile.length()+"");
        lwFile.setCreateUser(yyApplyService.getLoginUserUUID());
        lwFile.setCreateTime(new Date());
        lwFile.setValid("1");
        lwFileMapper.addLwFile(lwFile);

        //返回附件主键信息
        return fileUuid;
    }
}
