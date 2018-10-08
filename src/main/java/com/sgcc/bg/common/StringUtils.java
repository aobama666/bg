package com.sgcc.bg.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    /**
     * 获得对象的串表示
     *
     * @param obj
     * @return
     */
    public static String getObjectString(Object obj) {
        if (obj == null) {
            return "";
        } else {
            if (obj.getClass() == Timestamp.class) {
                if (((Timestamp) obj).getTime() == 0) {
                    return "";
                } else {
                    return getStringTime(obj);
                }
            } else {
                return obj.toString().trim();
            }
        }
    }

    /**
     * 把Timestamp格式时间转成字符串格式
     *
     * @param objTime
     * @return
     */
    public static String getStringTime(Object objTime) {
        String strTime = "";
        // @@@PTZ
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (objTime != null) {
                if (objTime.getClass() == Timestamp.class) {
                    strTime = dateFormat.format(new Date(((Timestamp) objTime).getTime()));
                } else if (objTime.getClass() == java.util.Date.class) {
                    strTime = dateFormat.format(new Date(((java.util.Date) objTime).getTime()));
                } else {
                    strTime = dateFormat.format(objTime);
                }
            }
        } catch (Exception e) {
        }
        return strTime;
    }

}
