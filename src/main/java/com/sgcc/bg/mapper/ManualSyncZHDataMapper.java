package com.sgcc.bg.mapper;

import com.sgcc.bg.model.OperationRecordPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ManualSyncZHDataMapper {
    void insertOperationRecord(Map<String,String> recordPo);

    List<Map<String,String>> getAllOperationRecord(@Param("userName") String userName,@Param("dataType") String dataType);
}
