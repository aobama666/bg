package com.sgcc.bg.lunwen.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {
    public static void main(String[] args) {
        System.out.println(isEmail("mingliao@qq.vip.com"));
    }

    /**
     * 验证邮箱格式
     * @param string
     * @return
     */
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-_.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
}
