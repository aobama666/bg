package com.sgcc.bg.common;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.sgcc.bg.workinghourinfo.Utils.DataBean;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	
	private final static SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final static SimpleDateFormat newsdfTime = new SimpleDateFormat("yyyyMMddHHmmss");

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 获取yyyyMMdd HHmmss
	 * @return
	 */
	public static String getNewsdfTime() {
		return newsdfTime.format(new Date());
	}
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}
	/**
	 * 获取MM格式
	 * @return
	 */
	public static String getMonth() {
		return sdfMonth.format(new Date());
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
	public static Date fomatTime(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
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

	public static Date fomatDateMonth(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	//把date转换为string
	public static String dateToStr(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
		String dateString = simpleDateFormat.format(date);
		return dateString;
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
	public static void main(String[] args) {
		String data="2019-09";
		String[] str= data.split("-");
		String year=	String.valueOf(str[0]);
		String month=	String.valueOf(str[1]);


		System.out.println(year);
		System.out.println(month);
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
	 * 根据日期的字符串转化为Date对象
	 * @param datestr
	 * @param format
	 * @return
	 */
	public static String  getFormatStringToString(String datestr, String format) {
		Date date=getFormatDate(datestr,format);
		String dateTime=getFormatDateString(date,format);
		return dateTime;
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
	 * @param time
	 * @param	 */
    public static boolean isCheckTime(String time){
    	try{
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		df.setLenient(false);
    		Date date = df.parse(time);
    		return true;
    	}catch(Exception e){
    		return false;
    	}
		
    }
  
    
 
       /**
      	 * 校验时间合法
      	 * @param
      	 * @param
      	 * @return
      	 */
          public static String isGetDay(String time){
          	try{
          		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       		df.setLenient(false);
       		Date date = df.parse(time);
          		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
          		return 	sf.format(date);
          	}catch(Exception e){
          		return "0000-00-00";
          	}
      		
          }
         
      
    /**
	 * 校验时间合法
	 * @param
	 * @param
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
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDateYearMonth(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM");
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
	 * @param beginData 日期1
	 * @param endData 日期2
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

	public static boolean judgeDateNOEqu(String beginData,String endData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		if(beginDatas.getTime()<endDatas.getTime()){
			return true;
		}else{
			return false;
		}

	}
	/**
	 * 判断两个日期的大小
	 * @param beginData 日期1
	 * @param endData 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean compareTime(String beginData,String endData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		if(beginDatas.getTime()<endDatas.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
 
	
	
	
	
	
	/**
	 * 判断两个日期的大小
	 * @param beginData 日期1
	 * @param endData 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean compareHms(String beginData,String endData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		if(beginDatas.getTime()<endDatas.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
	 /**
   	 * 校验时间合法
   	 * @param time
   	 * @return
   	 */
       public static String isGetDate(String time){
       	try{
       		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		df.setLenient(false);
    		Date date = df.parse(time);
       		SimpleDateFormat sf= new SimpleDateFormat("HH:mm:ss");
       		return 	sf.format(date);
       	}catch(Exception e){
       		return "00:00:00";
       	}
   		
       }
	/**
	 * 判断一个时间是否在一个时间段中
	 * @param beginTime 日期1
	 * @param endTime 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean toHms(String newtime,String beginTime,String endTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date beginDatas = sdf.parse(beginTime);
		Date endDatas = sdf.parse(endTime);
		String	newData=isGetDate(newtime);
		Date newDatas = sdf.parse(newData);
		if(beginDatas.getTime()<newDatas.getTime()&&newDatas.getTime()<endDatas.getTime()){
			return true;
		}else{
			return false;
		}
	}
	
 
	
	
	
	
	
	/**
	 * 判断两个日期的大小
	 * @param beginData 日期1
	 * @param endData 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean compareDay(String beginData,String endData) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		if(beginDatas.getTime()==endDatas.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
	/**
	 * 判断一个日期是否在一个时间段内
	 * @param beginData 日期1
	 * @param endData 日期2
	 * @return 
	 * @throws ParseException
	 */
	public static boolean compareDay(String beginData,String endData,String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		Date times = sdf.parse(time);
		if(beginDatas.getTime()<=times.getTime()&&times.getTime()<=endDatas.getTime()){
			return true;
		}else{
			return false;
		}
		
	}

	public static boolean compareYear(String beginData,String endData,String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date beginDatas = sdf.parse(beginData);
		Date endDatas = sdf.parse(endData);
		Date times = sdf.parse(time);
		if(beginDatas.getTime()<=times.getTime()&&times.getTime()<=endDatas.getTime()){
			return true;
		}else{
			return false;
		}

	}

	  public static String minutes (String s,int num){
	    	SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String  res = null;
	    	try {
				Date date=f.parse(s);
				Calendar c=Calendar.getInstance();
				 c.setTime(date);
		         c.add(Calendar.MINUTE , num);
		       
		        res = f.format(c.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return res;
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
		 * <li>功能描述：时间相减得到分钟数
		 * @param beginDateStr
		 * @param endDateStr
		 * @param times 间隔 时间
		 * @return long
		 * @author Administrator
		 */
		public static boolean getMinuteSub(String beginDateStr, String endDateStr ,long  times) {
			 
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
			java.util.Date beginDate = null;
			java.util.Date endDate = null;

			try {
				beginDate = format.parse(beginDateStr);
				endDate = format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long    time=endDate.getTime() - beginDate.getTime();
		   // long	day = time / (24 * 60 * 60  * 1000);
		   // long	hour = time / (60 * 60  * 1000);
			long    minute =time/(1000*60);
			if(minute>times){
				return true;
			}else{
				return false;
			}
			 
		}
	  
		 
		/** 
		 * 根据当前日期获得所在周的日期区间（周一和周日日期） 
		 */
		public static  Map<String ,String >  getTimeInterval(Date date){
			  Map<String ,String >  map=new HashMap<String ,String>();
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		      Calendar cal = Calendar.getInstance();
		      cal.setTime(date);
		      // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
		      int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
		      if(1 == dayWeek){
		         cal.add(Calendar.DAY_OF_MONTH,-1);
		      }
		      // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
		      cal.setFirstDayOfWeek(Calendar.MONDAY);
		      // 获得当前日期是一个星期的第几天  
		      int monday = cal.get(Calendar.DAY_OF_WEEK);
		      // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
		      cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - monday);
		      String imptimeBegin = sdf.format(cal.getTime());
		   
		      cal.setFirstDayOfWeek(Calendar.FRIDAY);
		      
		      int friday = cal.get(Calendar.DAY_OF_WEEK);
		      cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - friday);
		      String imptimeEnd = sdf.format(cal.getTime());
		      map.put("weekForStart", imptimeBegin);
		      map.put("weekForEnd", imptimeEnd);
		      return map;
		}
	
		/** 
		 * 根据当前日期获得上周的日期区间（上周周一和周日日期） 
		 */
		public static String getUpTimeInterval(Date date){   
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		      Calendar calendar1 = Calendar.getInstance();
		      Calendar calendar2 = Calendar.getInstance();
		      calendar1.setTime(date);
		      calendar2.setTime(date);
		      int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		      if(dayOfWeek <= 0){
		          dayOfWeek = 7;
		      }
		   
		      int offset1 = 1 - dayOfWeek;
		      int offset2 = 7 - dayOfWeek;
		      calendar1.add(Calendar.DATE, offset1 - 7);
		      calendar2.add(Calendar.DATE, offset2 - 9);
		      // last Monday
		      String lastBeginDate = sdf.format(calendar1.getTime());
		      // last Sunday  
		      String lastEndDate = sdf.format(calendar2.getTime());
		      return lastBeginDate + "," + lastEndDate;
		}
		/** 
		 * 根据当前日期获得上周的日期区间（上周周一和周日日期） 
		 */
		public static String getDownTimeInterval(Date date){   
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		      Calendar calendar1 = Calendar.getInstance();
		      Calendar calendar2 = Calendar.getInstance();
		      calendar1.setTime(date);
		      calendar2.setTime(date);
		      int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		      if(dayOfWeek <= 0){
		          dayOfWeek = 7;
		      }
		      int offset1 = 1 - dayOfWeek;
		      int offset2 = 7 - dayOfWeek;
		      calendar1.add(Calendar.DATE, offset1 + 7);
		      calendar2.add(Calendar.DATE, offset2 + 5);
		      // last Monday
		      String lastBeginDate = sdf.format(calendar1.getTime());
		      // last Sunday  
		      String lastEndDate = sdf.format(calendar2.getTime());
		      return lastBeginDate + "," + lastEndDate;
		}
		 /**
	     * 给定开始和结束时间，遍历之间的所有日期
	     *
	     * @param startAt 开始时间，例：2017-04-04
	     * @param endAt   结束时间，例：2017-04-11
	     * @return 返回日期数组
	     */
	    public static List<DataBean> getDatas(String startAt, String endAt) {
	        List<String> dates = new ArrayList<String>();
	        try {
	            Date startDate = dateFormat.parse(startAt);
	            Date endDate = dateFormat.parse(endAt);
	            dates.addAll(queryData(startDate, endDate));
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    	List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
	    	int counts=0;
	        for(int i=0;i<dates.size();i++){
	        	counts++;
	        	String newday=dates.get(i);
	        	DataBean  dataBean=new DataBean();
				dataBean.setCount(counts);
				dataBean.setStartData(newday+ " 00:00");
				dataBean.setEndData(newday+ " 24:00");
				DataBeanlist.add(dataBean);
	        }
	        return DataBeanlist;
	    }

	    /**
	     * 给定开始和结束时间，遍历之间的所有日期
	     *
	     * @param startAt 开始时间，例：2017-04-04
	     * @param endAt   结束时间，例：2017-04-11
	     * @return 返回日期数组
	     */
	    public static List<String> queryData(Date startAt, Date endAt) {
	        List<String> dates = new ArrayList<String>();
	        Calendar start = Calendar.getInstance();
	        start.setTime(startAt);
	        Calendar end = Calendar.getInstance();
	        end.setTime(endAt);
	        while (start.before(end) || start.equals(end)) {
	            dates.add(dateFormat.format(start.getTime()));
	            start.add(Calendar.DAY_OF_YEAR, 1);
	            
	        } 
	        return dates;
	    }

	//获取本月的开始时间
	public static Date getBeginDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() -1, 1);

		return getDayStartTime(calendar.getTime());
	}
	//获取本月的结束时间
	public static Date getEndDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 1, day);
		return getDayEndTime(calendar.getTime());
	}
	//获取某个日期的开始时间
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}
	//获取某个日期的结束时间
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if(null != d) calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}
	//获取今年是哪一年
	public static Integer getNowYear() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return Integer.valueOf(gc.get(1));
	}
	//获取本月是哪一月
	public static int getNowMonth() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return gc.get(2) + 1;
	}

	/**
	 * 获取指定年月的第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH,firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取指定年月的第一天
	 * @param time 时间
	 * @return
	 */
	public static String getFirstDay(String time) {
		String[] str= time.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH,firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取指定年月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取指定年月的最后一天
	 * @param time 时间
	 * @return
	 */
	public static String getLastDay(String time) {
		String[] str= time.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR, year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	//获取上月的开始时间
	public static Date getBeginDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		return getDayStartTime(calendar.getTime());
	}
	//获取上月的结束时间
	public static Date getEndDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 2, day);
		return getDayEndTime(calendar.getTime());
	}
	public static boolean isFirstDayOfMonth(String FirstDay) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(FirstDay);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(calendar.get(Calendar.DAY_OF_MONTH)==1){
			return  true;
		}
		return  false;
	}

	public static boolean isLastDayOfMonth(String lastDay) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(lastDay);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE,(calendar.get(Calendar.DATE)+1));
		if(calendar.get(Calendar.DAY_OF_MONTH)==1){
			return  true;
		}
		return  false;
	}

}
