package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LwFileMapper {


    /**
     * 添加附件信息
     * @param lwFile
     * @return
     */
    Integer addLwFile(
            LwFile lwFile
    );

    /**
     * 删除附件信息，逻辑删，修改对应有效状态
     * @param uuid
     * @return
     */
    Integer delLwFile(
            @Param("uuid") String uuid,
            @Param("valid") String valid
    );

    /**
     * 根据主键查询某个附件
     * @param uuid
     * @return
     */
    Map<String,Object> findLwFile(
            @Param("uuid") String uuid
    );

    /**
     * 根据文件名和格式查重
     * @param fileName
     * @param fileExtName
     * @param valid
     * @return
     */
    Map<String,Object> findLwFileForFileName(
            @Param("fileName") String fileName,
            @Param("fileExtName") String fileExtName,
            @Param("valid") String valid
    );


    /**
     * 根据论文信息查询对应的附件信息
     * @param bussinessId
     * @param bussinessTable
     * @param valid
     * @return
     */
    List<Map<String, Object>> selectLwFile(
            @Param("bussinessId") String bussinessId,
            @Param("bussinessTable") String bussinessTable,
            @Param("valid") String valid
    );
}
