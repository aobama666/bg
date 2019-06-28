package com.sgcc.bg.lunwen.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/gradeStatistics")
public class LwGradeStatisticController {



    @RequestMapping("/gradeStatistics")
    public String specialist(HttpServletRequest request) {
        return "lunwen/paperGradeStatistics";
    }
}
