package com.sgcc.bg.workinghourinfo.Utils;

 

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.*;
 
 
 
 
public class DataUtil1 {

	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String YYYYMMDD_ZH = "yyyyMMdd";
	public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY; 
	  /** 数字 */
    private static final String V_NUMBER="^\\d*\\.?\\d+$";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   
    
    
    public static void main(String[] args) throws ParseException {
    	 List<DataBean>  DataBeanlist=getWeeks("2018-09-01","2018-09-30");
    	 for(DataBean dataBean:DataBeanlist){
    		 System.out.println( dataBean.getStartData()+"-"+dataBean.getEndData());
    	 }
	}
    
    public static List<DataBean> getWeeks(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);//定义起始日期
        Date end = sdf.parse(endDate);//定义结束日期
        List<DataBean> DataBeanlsit = new ArrayList<DataBean>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        int count = 1;
        if(tempStart.equals(tempEnd)){
        	 SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
      	     String begindata =simpleDateFormat.format(getMondayOfWeek(start));
 		     String enddata  =simpleDateFormat.format(getSundayOfWeek(start ));
 		     DataBean dataBean1=new DataBean();
 		     dataBean1.setStartData(begindata );
 		     dataBean1.setEndData(enddata );
 		     dataBean1.setCount(count);
 		     DataBeanlsit.add(dataBean1);
		     return  DataBeanlsit;
        }
        while (tempStart.before(tempEnd) ) {
        	DataBean dataBean=new DataBean();
        	if(count==1){
        		if(tempStart.get(Calendar.DAY_OF_WEEK)==1){
                  	     SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
                  	     String begindata =simpleDateFormat.format(getMondayOfWeek(start));
             		     String enddata  =simpleDateFormat.format(getSundayOfWeek(start ));
             		     DataBean dataBean1=new DataBean();
             		     dataBean1.setStartData(begindata );
             		     dataBean1.setEndData(enddata );
             		     dataBean1.setCount(count);
             		     DataBeanlsit.add(dataBean1);
               	}
        	}else{
                 int we = tempStart.get(Calendar.DAY_OF_WEEK);
                 if (we == 2) {
                     dataBean.setStartData(sdf.format(tempStart.getTime()));
                 }
                 if (DataBeanlsit.isEmpty()) { //检测map是否为空
                      tempStart.add(Calendar.DAY_OF_YEAR, 1);
                 }else{
                       tempStart.add(Calendar.DAY_OF_YEAR, 6);
                       dataBean.setEndData(sdf.format(tempStart.getTime()));
                       dataBean.setCount(count);
                       DataBeanlsit.add(dataBean);
                 }
        	}
        	 if(tempStart.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
 		         count++;
 		     }
        	 tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return DataBeanlsit;
    }
    
    
    
    public static  String getdoublefromStrings(double num) {
 	   num= num*100;
 	   
 	   DecimalFormat decimalFormat=new DecimalFormat("0.00");
 	   String str=decimalFormat.format(num);
 	   
 	 
 	  return str+"%";
 	  
 }
    private static boolean match(String regex, String str)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    public static boolean Number(String value){
		return match(V_NUMBER,value);
	}
	  
 
   
    public static   List<DataBean> getCustom(String begins, String ends) throws ParseException {
    	List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
    	DataBean  dataBean=new DataBean();
		dataBean.setCount(1);
		dataBean.setStartData(begins);
		dataBean.setEndData(ends);
		DataBeanlist.add(dataBean);
		return DataBeanlist;
    	
    }
    
    public static   List<DataBean> getYears(String begins, String ends) throws ParseException {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = new Date();
		Date end = new Date();
		try {
		begin = sdf.parse(begins);
		end = sdf.parse(ends);
		} catch (ParseException e) {
		System.out.println("日期输入格式不对");
		return null;
		}
		Calendar cal_begin = Calendar.getInstance();
		cal_begin.setTime(begin);
		Calendar cal_end = Calendar.getInstance();
		cal_end.setTime(end);
		List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
		int counts=0;
		while (true) {
			counts++;
	     if (cal_begin.get(Calendar.YEAR) == cal_end.get(Calendar.YEAR)) {
		       System.out.println(sdf.format(cal_begin.getTime())+"~"+sdf.format(cal_end.getTime()));
			   DataBean  dataBean=new DataBean();
			   dataBean.setCount(counts);
			   dataBean.setStartData(sdf.format(cal_begin.getTime()));
			   dataBean.setEndData(sdf.format(cal_end.getTime()));
			   DataBeanlist.add(dataBean);
		       break;
		}else{
			String str_begin = sdf.format(cal_begin.getTime());
			String str_end = getMonthEnd(cal_begin.getTime());
			int years=getYear(str_begin);
			String year=years+"-12"+"-31";
			System.out.println(str_begin+"~"+year);
			DataBean  dataBean=new DataBean();
			dataBean.setCount(counts);
			dataBean.setStartData(str_begin);
			dataBean.setEndData(year);
			DataBeanlist.add(dataBean);
			cal_begin.add(Calendar.YEAR, 1);
			cal_begin.set(Calendar.DAY_OF_YEAR, 1);
		}
		
		}
		List<DataBean>  DataBeanlists=getNewYears(DataBeanlist);
            return DataBeanlists;
     }
     
    
    
    
    public static   List<DataBean> getNewYears(List<DataBean> DataBeanlist) throws ParseException{
      	 List<DataBean> DataBeanlists=new ArrayList<DataBean> ();
    	   for(DataBean dataBean:DataBeanlist){
    			int Count =dataBean.getCount();
    			String begins =dataBean.getStartData();
    		    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
    		    Date date=simpleDateFormat.parse(begins);
    		    String begindata =simpleDateFormat.format(getBeginDayOfYear(date ));
    		    String enddata  =simpleDateFormat.format(getEndDayOfYear(date ));
    		    DataBean DataBean=new DataBean();
    		    DataBean.setCount(Count);
    		    DataBean.setStartData(begindata);
    		    DataBean.setEndData(enddata);
    		    DataBeanlists.add(DataBean);
    	   }
    	return DataBeanlists;
      }
    
    
    
    
    public static   List<DataBean> getQuarters(String begins, String ends) throws ParseException {
      	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   		Date begin = new Date();
   		Date end = new Date();
   		try {
   		begin = sdf.parse(begins);
   		end = sdf.parse(ends);
   		} catch (ParseException e) {
   		System.out.println("日期输入格式不对");
   	 
   		}
   		Calendar cal_begin = Calendar.getInstance();
   		cal_begin.setTime(begin);
   		Calendar cal_end = Calendar.getInstance();
   		cal_end.setTime(end);
   		List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
   		int counts=0;
   		while (true) {
   	
   		String str_begin = sdf.format(cal_begin.getTime());
   		String str_end = getMonthEnd(cal_begin.getTime());
   		Date begin_date = parseDate(str_end);
   		Date end_date = parseDate(str_end);
   		String Quarter_begin=formatDate(getFirstDateOfSeason(begin_date));
   		String Quarter_end=formatDate(getLastDateOfSeason(end_date));
   		Date Quarter_begin_date = parseDate(Quarter_begin);
   		Date Quarter_end_date = parseDate(Quarter_end);
   	 
   		 
   		if(Quarter_end_date.getTime()==end_date.getTime()){
   			counts++;
   			if(Quarter_begin_date.getTime()<=begin.getTime()){
   				Quarter_begin=begins;
   			}
   			if(Quarter_end_date.getTime()>=end.getTime()){
   				Quarter_end=ends;
   			}
   			//System.out.println(Quarter_begin+"~"+Quarter_end);
   		   DataBean  dataBean=new DataBean();
		   dataBean.setCount(counts);
		   dataBean.setStartData(Quarter_begin);
		   dataBean.setEndData(Quarter_end);
		   DataBeanlist.add(dataBean);
   			if (end.getTime() <=end_date.getTime()) {
   			       break;
   			}
   		}else if(Quarter_begin_date.getTime()==begin_date.getTime()){
   			counts++;
   			if(Quarter_begin_date.getTime()<=begin.getTime()){
   				Quarter_begin=begins;
   			}
   			if(Quarter_end_date.getTime()>=end.getTime()){
   				Quarter_end=ends;
   			}
   		//	System.out.println(Quarter_begin+"~"+Quarter_end);
   		   DataBean  dataBean=new DataBean();
		   dataBean.setCount(counts);
		   dataBean.setStartData(Quarter_begin);
		   dataBean.setEndData(Quarter_end);
		   DataBeanlist.add(dataBean);
   		} 
   		
   		cal_begin.add(Calendar.MONTH, 1);
   		cal_begin.set(Calendar.DAY_OF_MONTH, 1);
   		}
   		 
   		List<DataBean> DataBeanlists= getNewQuarters(DataBeanlist);
   		return DataBeanlists;	 
    } 
    
    public static   List<DataBean> getNewQuarters(List<DataBean> DataBeanlist ) throws ParseException{
   	 List<DataBean> DataBeanlists=new ArrayList<DataBean> ();
 	   for(DataBean dataBean:DataBeanlist){
 			int Count =dataBean.getCount();
 			String begins =dataBean.getStartData();
 		    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
 		    Date date=simpleDateFormat.parse(begins);
 		    String begindata =simpleDateFormat.format(getFirstDateOfSeason(date ));
 		    String enddata  =simpleDateFormat.format(getLastDateOfSeason(date ));
 		    DataBean DataBean=new DataBean();
 		    DataBean.setCount(Count);
 		    DataBean.setStartData(begindata);
 		    DataBean.setEndData(enddata);
 		    DataBeanlists.add(DataBean);
 	   }
 	return DataBeanlists;
   }
    
    
    
    
    
    
     public static  List<DataBean> getMonths(String begins, String ends) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		Date begin = new Date();
 		Date end = new Date();
 		try {
 		begin = sdf.parse(begins);
 		end = sdf.parse(ends);
 		} catch (ParseException e) {
 		System.out.println("日期输入格式不对");
 		return null;
 		}
		 
		Calendar cal_begin = Calendar.getInstance();
		cal_begin.setTime(begin);
		Calendar cal_end = Calendar.getInstance();
		cal_end.setTime(end);
		List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
		int counts=0;
		while (true) {
		counts++;
		 
		if (cal_begin.get(Calendar.YEAR) == cal_end.get(Calendar.YEAR)&& cal_begin.get(Calendar.MONTH) == cal_end.get(Calendar.MONTH)) {
		      
		       DataBean  dataBean=new DataBean();
			   dataBean.setCount(counts);
			   dataBean.setStartData(sdf.format(cal_begin.getTime()));
			   dataBean.setEndData(sdf.format(cal_end.getTime()));
			   DataBeanlist.add(dataBean);
		       break;
		}else{
			String str_begin = sdf.format(cal_begin.getTime());
			String str_end = getMonthEnd(cal_begin.getTime());
			 
			DataBean  dataBean=new DataBean();
			dataBean.setCount(counts);
			dataBean.setStartData(str_begin);
			dataBean.setEndData(str_end);
			DataBeanlist.add(dataBean);
			cal_begin.add(Calendar.MONTH, 1);
			cal_begin.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		}
		 for(DataBean dataBean:DataBeanlist){
    		 System.out.println("test3-------"+ dataBean.getStartData()+"-"+dataBean.getEndData());
    	 }
		 List<DataBean>   DataBeanlists=getNewMonths(DataBeanlist);
		return DataBeanlists;
	 
  } 
     
     
     public static List<DataBean>   getNewMonths(List<DataBean> DataBeanlist) throws ParseException{
    	   List<DataBean> DataBeanlists=new ArrayList<DataBean> ();
    	   for(DataBean dataBean:DataBeanlist){
    			int Count =dataBean.getCount();
    			String begins =dataBean.getStartData();
    		    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
    		    Date date=simpleDateFormat.parse(begins);
    		    String begindata =simpleDateFormat.format(getFirstDateOfMonth(date));
    		    String enddata  =simpleDateFormat.format(getLastDateOfMonth(date));
    		    DataBean DataBean=new DataBean();
    		    DataBean.setCount(Count);
    		    DataBean.setStartData(begindata);
    		    DataBean.setEndData(enddata);
    		    DataBeanlists.add(DataBean);
    	   }
    	return DataBeanlists;
       }
     
     
     public static List<DataBean> getWeekss(String begins, String ends) throws ParseException {
         SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");    
         SimpleDateFormat sdw = new SimpleDateFormat("E");    
         String begin_date =begins; 
         String end_date =ends;  
         String begin_date_fm =  begins;
         String end_date_fm = ends;
         Date b = null;
         Date e = null;
         try {    
             b = sd.parse(begin_date_fm); 
             e = sd.parse(end_date_fm);
            
         } catch (ParseException ee) {    
             ee.printStackTrace();    
         }
     	List<DataBean>  DataBeanlist=new ArrayList<DataBean>();
         Calendar rightNow = Calendar.getInstance();
         rightNow.setTime(b);
         Date time = b;
         String year = begin_date_fm.split("-")[0];
         String mon = Integer.parseInt(begin_date_fm.split("-")[1])<10?begin_date_fm.split("-")[1]:begin_date_fm.split("-")[1];
         String day = Integer.parseInt(begin_date_fm.split("-")[2])<10?begin_date_fm.split("-")[2]:begin_date_fm.split("-")[2];
         String timeb = year+mon+day;
         String timee = null;
         int counts=0;
         if(begin_date==end_date){
          
               DataBean  dataBean=new DataBean();
			   dataBean.setCount(1);
			   dataBean.setStartData(begin_date);
			   dataBean.setEndData(end_date);
			   DataBeanlist.add(dataBean);
			    
         }else{
         	 while(time.getTime()<=e.getTime()){
         		
                  rightNow.add(Calendar.DAY_OF_YEAR,1);
                  time = sd.parse(sd.format(rightNow.getTime()));
                
                  if(time.getTime()>e.getTime()){
                	  break;
                  }
                  String timew = sdw.format(time);
                  
                  if(("星期一").equals(timew)){
                      timeb = (sd.format(time)).replaceAll("-", "");
                  }
                  if(("星期日").equals(timew) || ("星期七").equals(timew) || time.getTime() == e.getTime()){
                      timee = (sd.format(time)).replaceAll("-", "");
                      String begindate=fomaToDatas(timeb);
                      String enddate=fomaToDatas(timee);
                      counts++;
                     
                      DataBean  dataBean=new DataBean();
       			      dataBean.setCount(counts);
       			      dataBean.setStartData(begindate);
       			      dataBean.setEndData(enddate);
       			      DataBeanlist.add(dataBean);
                  } 
              }
         	
         }
         
         List<DataBean> DataBeanlists=getNewWeeks(DataBeanlist);
 		return DataBeanlists;
     }
      
     
     public static List<DataBean>   getNewWeeks(List<DataBean> DataBeanlist) throws ParseException{
  	   List<DataBean> DataBeanlists=new ArrayList<DataBean> ();
  	   for(DataBean dataBean:DataBeanlist){
  			int Count =dataBean.getCount();
  			String begins =dataBean.getStartData();
  		    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
  		    Date date=simpleDateFormat.parse(begins);
  		    String begindata =simpleDateFormat.format(getMondayOfWeek(date));
  		    String enddata  =simpleDateFormat.format(getSundayOfWeek(date ));
  		    DataBean DataBean=new DataBean();
  		    DataBean.setCount(Count);
  		    DataBean.setStartData(begindata);
  		    DataBean.setEndData(enddata);
  		    DataBeanlists.add(DataBean);
  	   }
  	return DataBeanlists;
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
				dataBean.setStartData(newday);
				dataBean.setEndData(newday);
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
	
	
	
	
	    public static  String getdoublefromString(double num) {
	    	  num=num*100;
	    	  BigDecimal bd=new BigDecimal(num).setScale(0,BigDecimal.ROUND_HALF_UP);
	    	  int res=Integer.parseInt(bd.toString());
	    	  return res+"%";
	    	  
	    }
	
	  
	   
	
	
	
     
     
     public static String fomaToDatas(String data){
    	 DateFormat fmt=new SimpleDateFormat("yyyyMMdd");
    	 try {
			Date parse=fmt.parse(data);
			DateFormat fmt2=new SimpleDateFormat("yyyy-MM-dd");
			return fmt2.format(parse);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	 
    	 
     }
     
     
 	 
     
     
     
   
  /**
   * 日期解析
   *
   * @param strDate
   * @param pattern
   * @return
   */
  public static Date parseDate(String strDate, String pattern) {
   Date date = null;
   try {
    if (pattern == null) {
     pattern = YYYYMMDD;
    }
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    date = format.parse(strDate);
   } catch (Exception e) {
   
   }
   return date;
  }
  public static int getYear(String date) throws ParseException {
	  Calendar c = Calendar.getInstance();
	  c.setTime(parseDate(date));
	  int year = c.get(Calendar.YEAR);
	  return year;
	 }
     public String getYearMonth (Date date) {
		return formatDateByFormat(date, "yyyy-MM")  ;
		}
		/**
		* 取得指定月份的第一天
		*
		* @param strdate
		* String
		* @return String
		*/
		public String getMonthBegin(Date date) {
		return formatDateByFormat(date, "yyyy-MM") + "-01";
		}
		
		/**
		* 取得指定月份的最后一天
		*
		* @param strdate
		* String
		* @return String
		*/
		public static String getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDateByFormat(calendar.getTime(), "yyyy-MM-dd");
		}
		/**
		* 以指定的格式来格式化日期
		*
		* @param date
		* Date
		* @param format
		* String
		* @return String
		*/
		public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
		try {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		result = sdf.format(date);
		} catch (Exception ex) {
		ex.printStackTrace();
		}
		}
		return result;
		}
		/**
		 * 
		 * @param strDate
		 * @return
		 */
		public static Date parseDate(String strDate) {
			return parseDate(strDate, null);
		}
	 
		 
		/**
		 * format date
		 * 
		 * @param date
		 * @return
		 */
		public static String formatDate(Date date) {
			return formatDate(date, null);
		}
	 
		/**
		 * format date
		 * 
		 * @param date
		 * @param pattern
		 * @return
		 */
		public static String formatDate(Date date, String pattern) {
			String strDate = null;
			try {
				if (pattern == null) {
					pattern = YYYYMMDD;
				}
				SimpleDateFormat format = new SimpleDateFormat(pattern);
				strDate = format.format(date);
			} catch (Exception e) {
				 
			}
			return strDate;
		}
	 
		/**
		 * 取得日期：年
		 * 
		 * @param date
		 * @return
		 */
		public static int getYear(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int year = c.get(Calendar.YEAR);
			return year;
		}
	 
		/**
		 * 取得日期：年
		 * 
		 * @param date
		 * @return
		 */
		public static int getMonth(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			return month + 1;
		}
	 
		/**
		 * 取得日期：年
		 * 
		 * @param date
		 * @return
		 */
		public static int getDay(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int da = c.get(Calendar.DAY_OF_MONTH);
			return da;
		}
	 
		/**
		 * 取得当天日期是周几
		 * 
		 * @param date
		 * @return
		 */
		public static int getWeekDay(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int week_of_year = c.get(Calendar.DAY_OF_WEEK);
			return week_of_year - 1;
		}
	 
		/**
		 * 取得一年的第几周
		 * 
		 * @param date
		 * @return
		 */
		public static int getWeekOfYear(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
			return week_of_year;
		}
	 
		/**
		 * getWeekBeginAndEndDate
		 * 
		 * @param date
		 * @param pattern
		 * @return
		 */
		public static String getWeekBeginAndEndDate(Date date, String pattern) {
			Date monday = getMondayOfWeek(date);
			Date sunday = getSundayOfWeek(date);
			return formatDate(monday, pattern) + " - "
					+ formatDate(sunday, pattern);
		}
	 
		/**
		 * 根据日期取得对应周周一日期
		 * 
		 * @param date
		 * @return
		 */
		public static Date getMondayOfWeek(Date date) {
			Calendar monday = Calendar.getInstance();
			monday.setTime(date);
			monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
			monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return monday.getTime();
		}
	 
		/**
		 * 根据日期取得对应周周日日期
		 * 
		 * @param date
		 * @return
		 */
		public static Date getSundayOfWeek(Date date) {
			Calendar sunday = Calendar.getInstance();
			sunday.setTime(date);
			sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
			sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			return sunday.getTime();
		}
		//获取本年的开始时间
		       public static   Date getBeginDayOfYear(Date date) {
		              Calendar cal = Calendar.getInstance();
		              cal.setTime(date);
		             
		              cal.set(Calendar.MONTH, Calendar.JANUARY);
		              cal.set(Calendar.DATE, 1);
		 
		             return  cal.getTime() ;
		         }
		      


			//获取本年的结束时间
		      public static  Date getEndDayOfYear(Date date) {
		             Calendar cal = Calendar.getInstance();
		             cal.setTime(date);
		             
		             cal.set(Calendar.MONTH, Calendar.DECEMBER);
		             cal.set(Calendar.DATE, 31);
		            return  cal.getTime() ;
		        }
		    
		/**
		 * 取得月的剩余天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getRemainDayOfMonth(Date date) {
			int dayOfMonth = getDayOfMonth(date);
			int day = getPassDayOfMonth(date);
			return dayOfMonth - day;
		}
	 
		/**
		 * 取得月已经过的天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getPassDayOfMonth(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.DAY_OF_MONTH);
		}
	 
		/**
		 * 取得月天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getDayOfMonth(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
	  
		/**
		 * 取得月第一天
		 * 
		 * @param date
		 * @return
		 */
		public static Date getFirstDateOfMonth(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
			return c.getTime();
		}
	 
		/**
		 * 取得月最后一天
		 * 
		 * @param date
		 * @return
		 */
		public static Date getLastDateOfMonth(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			return c.getTime();
		}
	 
		/**
		 * 取得季度第一天
		 * 
		 * @param date
		 * @return
		 */
		public static Date getFirstDateOfSeason(Date date) {
			return getFirstDateOfMonth(getSeasonDate(date)[0]);
		}
	 
		/**
		 * 取得季度最后一天
		 * 
		 * @param date
		 * @return
		 */
		public static Date getLastDateOfSeason(Date date) {
			return getLastDateOfMonth(getSeasonDate(date)[2]);
		}
	 
		/**
		 * 取得季度天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getDayOfSeason(Date date) {
			int day = 0;
			Date[] seasonDates = getSeasonDate(date);
			for (Date date2 : seasonDates) {
				day += getDayOfMonth(date2);
			}
			return day;
		}
	 
		/**
		 * 取得季度剩余天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getRemainDayOfSeason(Date date) {
			return getDayOfSeason(date) - getPassDayOfSeason(date);
		}
	 
		/**
		 * 取得季度已过天数
		 * 
		 * @param date
		 * @return
		 */
		public static int getPassDayOfSeason(Date date) {
			int day = 0;
	 
			Date[] seasonDates = getSeasonDate(date);
	 
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
	 
			if (month == Calendar.JANUARY || month == Calendar.APRIL
					|| month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
				day = getPassDayOfMonth(seasonDates[0]);
			} else if (month == Calendar.FEBRUARY || month == Calendar.MAY
					|| month == Calendar.AUGUST || month == Calendar.NOVEMBER) {// 季度第二个月
				day = getDayOfMonth(seasonDates[0])
						+ getPassDayOfMonth(seasonDates[1]);
			} else if (month == Calendar.MARCH || month == Calendar.JUNE
					|| month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {// 季度第三个月
				day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1])
						+ getPassDayOfMonth(seasonDates[2]);
			}
			return day;
		}
	 
		/**
		 * 取得季度月
		 * 
		 * @param date
		 * @return
		 */
		public static Date[] getSeasonDate(Date date) {
			Date[] season = new Date[3];
	 
			Calendar c = Calendar.getInstance();
			c.setTime(date);
	 
			int nSeason = getSeason(date);
			if (nSeason == 1) {// 第一季度
				c.set(Calendar.MONTH, Calendar.JANUARY);
				season[0] = c.getTime();
				c.set(Calendar.MONTH, Calendar.FEBRUARY);
				season[1] = c.getTime();
				c.set(Calendar.MONTH, Calendar.MARCH);
				season[2] = c.getTime();
			} else if (nSeason == 2) {// 第二季度
				c.set(Calendar.MONTH, Calendar.APRIL);
				season[0] = c.getTime();
				c.set(Calendar.MONTH, Calendar.MAY);
				season[1] = c.getTime();
				c.set(Calendar.MONTH, Calendar.JUNE);
				season[2] = c.getTime();
			} else if (nSeason == 3) {// 第三季度
				c.set(Calendar.MONTH, Calendar.JULY);
				season[0] = c.getTime();
				c.set(Calendar.MONTH, Calendar.AUGUST);
				season[1] = c.getTime();
				c.set(Calendar.MONTH, Calendar.SEPTEMBER);
				season[2] = c.getTime();
			} else if (nSeason == 4) {// 第四季度
				c.set(Calendar.MONTH, Calendar.OCTOBER);
				season[0] = c.getTime();
				c.set(Calendar.MONTH, Calendar.NOVEMBER);
				season[1] = c.getTime();
				c.set(Calendar.MONTH, Calendar.DECEMBER);
				season[2] = c.getTime();
			}
			return season;
		}
	 
		/**
		 * 
		 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
		 * 
		 * @param date
		 * @return
		 */
		public static int getSeason(Date date) {
	 
			int season = 0;
	 
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int month = c.get(Calendar.MONTH);
			switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season = 4;
				break;
			default:
				break;
			}
			return season;
		}
		
}