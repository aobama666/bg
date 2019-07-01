package com.sgcc.bg.lunwen.mapper;

import com.sgcc.bg.lunwen.bean.LwGrade;
import org.springframework.stereotype.Repository;

@Repository
public interface LwGradeMapper {

    Integer addLwGrade(LwGrade lwGrade);
}
