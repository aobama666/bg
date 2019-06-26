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
    Integer addLwFile(LwFile lwFile);

    /**
     * 删除附件信息，逻辑删，修改对应有效状态
     * @param uuid
     * @return
     */
    Integer delLwFile(@Param("uuid") String uuid,@Param("valid") String valid);

    //查询对应附件，根据论文题目，或者论文uuid
    List<Map<String, Object>> selectLwFile(
            @Param("bussinessId") String bussinessId,
            @Param("bussinessTable") String bussinessTable,
            @Param("valid") String valid
    );
}
