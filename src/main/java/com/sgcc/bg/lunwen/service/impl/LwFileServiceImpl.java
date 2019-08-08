package com.sgcc.bg.lunwen.service.impl;

import com.sgcc.bg.lunwen.bean.LwFile;
import com.sgcc.bg.lunwen.constant.LwPaperConstant;
import com.sgcc.bg.lunwen.mapper.LwFileMapper;
import com.sgcc.bg.lunwen.service.LwFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class LwFileServiceImpl implements LwFileService {

    @Autowired
    private LwFileMapper lwFileMapper;

    @Override
    public Integer addLwFile(LwFile lwFile) {
        return lwFileMapper.addLwFile(lwFile);
    }

    @Override
    public Integer delLwFile(String uuid) {
        return lwFileMapper.delLwFile(uuid,LwPaperConstant.VALID_NO);
    }

    @Override
    public Map<String, Object> findFile(String uuid) {
        return lwFileMapper.findLwFile(uuid);
    }

    @Override
    public List<Map<String, Object>> findLwFileForFileName(String fileName, String fileExtName) {
        return lwFileMapper.findLwFileForFileName(fileName,fileExtName,LwPaperConstant.VALID_YES);
    }

    @Override
    public Map<String, Object> findLwFileForPaperId(String paperId, String fileName, String fileExtName) {
        return lwFileMapper.findLwFileForPaperId(paperId,fileName,fileExtName,LwPaperConstant.VALID_YES);
    }

    @Override
    public List<Map<String, Object>> selectLwFile(String bussinessId, String bussinessTable, String valid) {
        return lwFileMapper.selectLwFile(bussinessId,bussinessTable, valid);
    }
}
