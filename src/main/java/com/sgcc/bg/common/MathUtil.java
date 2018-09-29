package com.sgcc.bg.common;

import java.math.BigDecimal;

public class MathUtil {

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 2;

    private static final int DEF_DIV_SCALE6 = 6;

    /**
     * 提供精确的加法运算。
     * @param v1   被加数
     * @param v2   加数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     * @param v1   被加数
     * @param v2   加数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的和
     */
    public static double add(String v1, String v2) {
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * @说明：两个字符串相加
     * @参数：
     * @返回值：String
     * @日期：2014-5-20 下午9:52:39
     * @作者：wanglongjiao
     * @版本：V1.0
     */
    public static String addString(String v1, String v2){
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的加法运算，返回值用于大数转String。
     * @param v1   被加数
     * @param v2   加数
     * @author lxt
     * @date 2012-01-04
     * @return 两个参数的和
     */
    public static BigDecimal add1(String v1, String v2) {
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2  减数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2  减数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的差
     */
    public static double sub(String v1, String v2) {
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2  减数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的差
     */
    public static String strMinus(String v1, String v2) {
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }


    /**
     * 提供精确的乘法运算。
     * @param v1  被乘数
     * @param v2  乘数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * @param v1  被乘数
     * @param v2  乘数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的积,过大会产生科学计数法，改成返回string类型
     */
    public static String mul(String v1, String v2) {
        if(v1.length() == 0){
            v1 = "0";
        }
        if(v2.length() == 0){
            v2 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.multiply(b2));
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     * @param v1  被除数
     * @param v2  除数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后2位，以后的数字四舍五入。
     * @param v1  被除数
     * @param v2  除数
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的商
     */
    public static double div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1  被除数
     * @param v2  除数
     * @param scale  表示表示需要精确到小数点以后几位。
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能是负数！");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1  被除数
     * @param v2  除数
     * @param scale  表示表示需要精确到小数点以后几位。
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的商
     */
    public static double div(String v1, String v2, int scale) {

        if(v2.length() == 0 || "0".equals(v2)){
            throw new IllegalArgumentException("除数不能为空或为0！");
        }

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能是负数！");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     * @param v1  被除数
     * @param v2  除数
     * @param scale  表示表示需要精确到小数点以后几位。
     * @author lxt
     * @date 2011-7-23
     * @return 两个参数的商
     */
    public static String divi(String v1, String v2, int scale) {

        if(v2.length() == 0 || "0".equals(v2)){
            throw new IllegalArgumentException("除数不能为空或为0！");
        }

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能是负数！");
        }
        if(v1 == null || "null".equals(v1) || "".equals(v1)){
            v1 = "0";
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * @description 得到两个数值的比例
     * @param v1
     * @param v2
     * @param scale
     * @return
     * @author 王龙蛟
     */
    public static String getScale(String v1, String v2, int scale){
        if(v2.length() == 0 || "0".equals(v2)){
            throw new IllegalArgumentException("除数不能为空或为0！");
        }

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能是负数！");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf((b1.multiply(new BigDecimal(100)).divide(b2, scale, BigDecimal.ROUND_HALF_UP)))+"%";

    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v   需要四舍五入的数字
     * @param scale  小数点后保留几位
     * @author lxt
     * @date 2011-7-23
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "精度不能是负数！");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的类型转换(Float)
     * @param v  需要被转换的数字
     * @author lxt
     * @date 2011-7-23
     * @return 返回转换结果
     */
    public static float convertsToFloat(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.floatValue();
    }

    /**
     * 提供精确的类型转换(Int)不进行四舍五入
     * @param v 需要被转换的数字
     * @author lxt
     * @date 2011-7-23
     * @return 返回转换结果
     */
    public static int convertsToInt(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.intValue();
    }

    /**
     * 提供精确的类型转换(Long)
     * @param v   需要被转换的数字
     * @author lxt
     * @date 2011-7-23
     * @return  返回转换结果
     */
    public static long convertsToLong(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.longValue();
    }

    /**
     * 返回两个数中大的一个值
     * @param v1  需要被对比的第一个数
     * @param v2  需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 返回两个数中大的一个值
     */
    public static double returnMax(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.max(b2).doubleValue();
    }

    /**
     * 返回两个数中大的一个值
     * @param v1  需要被对比的第一个数
     * @param v2  需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 返回两个数中大的一个值
     */
    public static String max(String v1, String v2) {
        if(v1 == null || v1.length() == 0) v1 = "0";
        if(v2 == null || v2.length() == 0) v2 = "0";
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.max(b2).doubleValue());
    }

    /**
     * 返回两个数中小的一个值
     * @param v1  需要被对比的第一个数
     * @param v2  需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 返回两个数中小的一个值
     */
    public static double returnMin(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.min(b2).doubleValue();
    }

    /**
     * 返回两个数中小的一个值
     * @param v1  需要被对比的第一个数
     * @param v2  需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 返回两个数中小的一个值
     */
    public static String min(String v1, String v2) {
        if(v1 == null || v1.length() == 0) v1 = "0";
        if(v2 == null || v2.length() == 0) v2 = "0";
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.min(b2).doubleValue());
    }

    /**
     * 精确对比两个数字
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * 精确对比两个数字
     * @param v1 需要被对比的第一个数
     * @param v2 需要被对比的第二个数
     * @author lxt
     * @date 2011-7-23
     * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
     */
    public static int compareTo(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * @description 将传入的String转换成double
     * @return
     * @author 王龙蛟
     * @date 2013-5-11
     * @return double
     */
    public static double StringToDouble(String number){
        if("".equals(number) || number == null){
            number = "0";
        }
        double number2 = Double.valueOf(number);

        return number2;
    }

}
