package com.sgcc.bg.common;
/**
 * 字符串相关方法
 */
public class StringUtil {

	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr) {
		int i = 0;
		String TempStr = valStr;
		String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
		valStr = valStr + ",";
		while (valStr.indexOf(',') > 0) {
			returnStr[i] = valStr.substring(0, valStr.indexOf(','));
			valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

			i++;
		}
		return returnStr;
	}
	public static boolean checkNumber(double value){  
        String str = String.valueOf(value);  
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";  
        return str.matches(regex);  
    }  
      
    public static boolean checkNumber(int value){  
        String str = String.valueOf(value);  
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";  
        return str.matches(regex);  
    }  
      
    public static boolean checkNumber(String value){  
        String regex = "^(-?[0-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";  
       return value.matches(regex);  
    } 
    
    public static boolean checkNotChienes(String value){
    	String regex = "^[A-Za-z0-9]+$";
    	return value.matches(regex);
    }
    /** 数字 */
    private static final String V_NUMBER="^\\d*\\.?\\d+$";
    /*
     * 只能含有中文、中划线、下划线、数字、字母且不能以中划线或下划线开头或结尾 
     */
    public static boolean checkPName(String value){
    	String regex = "^(?!_)(?!.*?_$)[\\u4e00-\\u9fa5\\-_a-zA-Z0-9]+$";
    	return value.matches(regex);
    }
    
    /*
     * 只能含有中文、英文
     */
    public static boolean checkNotNoOrC(String value){
    	String regex = "^[a-zA-Z\\u4e00-\\u9fa5]+$";
    	return value.matches(regex);
    }
    
    /*
     * 电话号码
     */
    public static boolean checkPhone(String value){
    	String regex = "^1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}$";
    	return value.matches(regex);
    }
    
    /*
     * 数字或字母
     */
    public static boolean checkNoOrLetter(String value){
    	String regex = "^[a-zA-Z0-9]+$";
    	return value.matches(regex);
    }
    
    /*
     * 只中文
     */
    public static boolean checkChinese(String value){
    	String regex = "^[\\u4e00-\\u9fa5]+$";
    	return value.matches(regex);
    }
    /*
     * 匹配中英数字
     */
    public static boolean check(String value){
    	String regex = "^[\\u4e00-\\u9fa5\\w]+$";
    	return value.matches(regex);
    }
   
    
    
    public static void main(String[] args) {  
        System.out.println(checkNumber(0));// true  
       System.out.println(checkNumber(-0));// true  
       System.out.println(checkNumber(23));// true  
        System.out.println(checkNumber(-23));// true  
        System.out.println(checkNumber(0.0));// true  
        System.out.println(checkNumber(-0.0));// true  
        System.out.println(checkNumber(-23.01));// true  
        System.out.println(checkNumber("10.5"));// false  
        System.out.println(checkNumber("02132132132132121110"));// false 
   }  
}
