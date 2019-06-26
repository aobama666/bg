package com.sgcc.bg.lunwen.controller;

import com.sgcc.bg.common.Rtext;

public class Uuid {


    public static void main(String[] args) {
        for (int i = 0;i<10;i++){
            System.out.println(Rtext.getUUID());
        }

        String paperId = "X001";
        paperId = paperId.substring(1,paperId.length());
        Integer id = Integer.valueOf(paperId);
        id = ++id;
        paperId = String.format("%03d",id);
        System.out.println(paperId);
    }
}
