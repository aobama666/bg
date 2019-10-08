package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.bean.YyKind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YyKindMapper {

    //查询当前申请拥有的种类编码
    List<Integer> getKindCode(@Param("applyUuid") String applyUuid);

    //查询当前申请的其他种类内容
    String  getKindValue(@Param("applyUuid") String applyUuid);

    //根据申请id和种类编码删除对应信息
    Integer delKind(@Param("applyUuid") String applyUuid,@Param("useSealKindCode") String useSealKindCode);

    //修改对应申请的其他种类内容信息
    Integer updateElseKind(@Param("applyUuid") String applyUuid,
                           @Param("useSealValue") String useSealValue);

    //添加对应申请的种类信息
    Integer addKind(YyKind yyKind);
}
