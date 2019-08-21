package com.sgcc.bg.yygl.mapper;

import com.sgcc.bg.yygl.bean.YyKind;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YyKindMapper {

    //查询当前申请拥有的种类编码
    List<Integer> getKindCode(@Param("applyUuid") String applyUuid);

    //查询当前申请的其他种类内容
    String  getKindValue(@Param("applyUuid") String applyUuid);

    //根据主键信息删除对应种类信息
    Integer delKind(@Param("uuid") String uuid);

    //修改对应申请的其他种类内容信息
    Integer updateElseKind(@Param("applyUuid") String applyUuid,
                           @Param("useSealValue") String useSealValue);

    //添加对应申请的种类信息
    Integer addKind(YyKind yyKind);
}
