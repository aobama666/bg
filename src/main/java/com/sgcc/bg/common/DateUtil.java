package com.sgcc.bg.common;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws
	 * @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa = 0;
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
     
	/**
	 * <li>功能描述：时间相减得到天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

 
	
	/**
	 * 根据日期的类型封装为String对象
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getFormatDateString(Date date, String format) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		String temp="";
		temp = dateFormat.format(date);
		return temp;
	}

	/**
	 * 根据日期的字符串转化为Date对象
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static Date getFormatDate(String datestr, String format) {
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		Date date=null;
			try {
				date = dateFormat.parse(datestr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return date;
	}
	
	/**
	 * 校验时间格式
	 * @param s
	 * @param format
	 * @return
	 */
    public static boolean isValidDate(String s, String format){
    	try{
    		SimpleDateFormat df = new SimpleDateFormat(format);
    		Date date = df.parse(s);
    		if(df.format(date).equals(s))
    			return true;
    		else
    			return false;
    	}catch(Exception e){
    		return false;
    	}
    }
    /**
	 * 校验时间合法
	 * @param s
	 * @param format
	 * @return
	 */
    public static boolean isCheckDate(String time){
    	try{
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		df.setLenient(false);
    		Date date = df.parse(time);
    		return true;
    	}catch(Exception e){
    		return false;
    	}
		
    }
    /**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	/**
	 * 判断两个日期的大小
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean judgeDate(String beginData,String endData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		if(beginDatas.getTime()<=endDatas.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
    public static void main(String[] args) {
    	String ksdata="2018-09-31";
    	String jsdata="2018-09-28";
    boolean	flag=isCheckDate(ksdata);
    System.out.println(flag);	
    try {
    		System.out.println(judgeDate(ksdata,jsdata));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
