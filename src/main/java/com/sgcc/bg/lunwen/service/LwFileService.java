package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LwFileService {
    Integer addLwFile(LwFile lwFile);

    Integer delLwFile(String uuid);

    Map<String,Object> findFile(String uuid);

    Map<String,Object> findLwFileForFileName(String fileName, String fileExtName);

    List<Map<String, Object>> selectLwFile(String bussinessId, String bussinessTable, String valid);
}
