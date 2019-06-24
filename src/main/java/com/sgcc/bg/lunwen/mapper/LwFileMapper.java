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
    public Integer addLwFile(LwFile lwFile);

    /**
     * 删除附件信息，逻辑删，修改对应有效状态
     * @param uuid
     * @return
     */
    public Integer delLwFile(@Param("uuid") String uuid);

    //查询对应附件，根据论文题目，或者论文uuid
    public List<Map<String, Object>> selectLwFile();
}
