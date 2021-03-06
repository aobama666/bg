package com.sgcc.bg.lunwen.service;

import com.sgcc.bg.lunwen.bean.LwFile;

import java.util.List;
import java.util.Map;

public interface LwFileService {
    Integer addLwFile(LwFile lwFile);

    Integer delLwFile(String uuid);

    Map<String,Object> findFile(String uuid);

    List<Map<String,Object>> findLwFileForFileName(String fileName, String fileExtName);

    Map<String,Object> findLwFileForPaperId(String paperId, String fileName, String fileExtName);

    List<Map<String, Object>> selectLwFile(String bussinessId, String bussinessTable, String valid);
}
