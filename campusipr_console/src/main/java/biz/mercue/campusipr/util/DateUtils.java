package biz.mercue.campusipr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	
	/**
	 * Formate:yyyy年MM月dd日  HH:mm:ss </b>
	 * Example:2017月01日20 12:20:20 </b>
	 * 
	 * @param cal 
	 */
	public static String getChineseFormatDate(Calendar cal){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
		return sdFormat.format(cal.getTime());
	}
	
	/**
	 * Formate:yyyy/MM/dd  HH:mm:ss </b>
	 * Example:2017/01/20 12:20:20 </b>
	 * 
	 * @param cal 
	 */
	public static String getSimpleFormatDate(Calendar cal){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
		return sdFormat.format(cal.getTime());
	}
	
	
	/**
	 * Formate:yyyy/MM/dd  HH:mm:ss </b>
	 * Example:2017/01/20 12:20:20 </b>
	 * 
	 * @param date 
	 */
	public static String getSimpleFormatDate(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
		return sdFormat.format(date.getTime());
	}
	
	
	/**
	 * Formate:yyyy/MM/dd </b>
	 * Example:2017/01/20 </b>
	 * 
	 * @param date 
	 */
	public static String getSimpleSlashFormatDate(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
		return sdFormat.format(date.getTime());
	}
	
	/**
	 * Formate:yyyy/MM </b>
	 * Example:2017/01 </b>
	 * 
	 * @param date 
	 */
	public static String getSimpleSlashFormatDateToMonth(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM");
		return sdFormat.format(date.getTime());
	}
	
	
	/**
	 * Formate:yyyyMMdd </b>
	 * Example:20170120 </b>
	 */
	public static String getSimpleDateFormatDate(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		return sdFormat.format(date.getTime());
	}
	
	/**
	 * Formate:yyyMMdd </b>
	 * Example:1060120 </b>
	 */
	public static String getROCDateFormatDate(String strDate){
		
	
		String strYear = strDate.substring(0, 4);
		String strDay = strDate.substring(4);
		int  year = Integer.parseInt(strYear);
		year = year - 1911;
		strDate = String.valueOf(year) + strDay;
		return strDate;
	}
	
	
	/**
	 * Formate:yyyy-MM-dd
	 * Example:2017-01-20
	 */
	public static String getDashFormatDate(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		return sdFormat.format(date.getTime());
	}
	
	/**
	 * Formate:yyyy-MM-dd HH:mm:ss
	 * Example:2017-01-20 12:00:00
	 */
	public static String getDashFormatDateTime(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdFormat.format(date.getTime());
	}
	
	/**
	 * Parse Formate:yyyy-MM-dd HH:mm:ss
	 * Example:2017-01-20 12:00:00
	 */
	public static Date parserDateTimeString(String strDate) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(strDate);
	}
	
	/**
	 * Formate:yyyy-MM-ddTHH:mm:ssZ
	 * Example:2017-01-20 12:00:00
	 */
	public static String getUTCFormatDateTime(Date date){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return sdFormat.format(date.getTime());
	}
	
	/**
	 * Parse Formate:yyyy-MM-ddTHH:mm:ssZ
	 * Example:2017-01-20 12:00:00
	 */
	public static Date parserDateTimeUTCString(String strDate) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return sdf.parse(strDate);
	}
	

	/**
	 * Parse Formate:yyyyMMdd
	 * Example:20170120
	 */
	public static Date parserSimpleDateFormatDate(String strDate) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		return sdFormat.parse(strDate);
	}
	
	/**
	 * Parse Formate:yyyy-MM-dd
	 * Example:2017-01-20
	 */
	public static Date parserSimpleDateHyphenFormatDate(String strDate) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		return sdFormat.parse(strDate);
	}
	
	/**
	 * Parse Formate:yyyy/MM/dd
	 * Example:2017-01-20
	 */
	public static Date parserSimpleDateSlashFormatDate(String strDate) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
		return sdFormat.parse(strDate);
	}
	
	/**
	 * Parse Formate:yyyy/MM
	 * Example:2017/10
	 */
	public static Date parserSimpleDateSlashFormatDateToMonth(String strDate) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM");
		return sdFormat.parse(strDate);
	}
	
	/**
	 * Parse Formate:yyyMMdd
	 * Example:1060120
	 */
	public static Date parserTaiwanSimpleDateFormatDate(String strDate) throws ParseException{
		String strYear = strDate.substring(0,3);
		strDate = strDate.substring(3);
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
	    String strCurrentTawianYear = String.valueOf(calendar.get(Calendar.YEAR) - 1911);
	    System.out.println("strCurrentTawianYear :"+strCurrentTawianYear);
	    calendar.add(Calendar.YEAR, -1);
	    int lastYear = calendar.get(Calendar.YEAR) - 1911;
	    String strLastTaiwanYear = String.valueOf(lastYear);
	    System.out.println("strLastTaiwanYear :"+strLastTaiwanYear);
	    
	    if(strCurrentTawianYear.equals(strYear)){
	    	strYear = String.valueOf(currentYear);
	    }else if(strLastTaiwanYear.equals(strYear)){
	    	strYear = String.valueOf(lastYear);
	    }else {
	    	throw new ParseException("Error parsing "+ strDate, 0);
	    }
	    strDate =  strYear +strDate;
	    System.out.println("final strDate :"+strDate);
		return parserSimpleDateFormatDate(strDate);
	}
	
	
	
	public static Date getDayStart(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getDayEnd(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static Date getTheMonthFirstDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getTheMonthLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static Date getTheYearFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getTheYearLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	public static Date getTheNextYearLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, 1);
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	
	public static boolean isSameDate(Calendar cal1,Calendar cal2){
		
		if((cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))&&(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) )
		{
			return true;
		}else{
			return false;
		}
	}
	
	public static long  dateDiffereceByDay(Date date1,Date date2){
		
		long diff = date2.getTime() - date1.getTime();
		long diffDay = diff / (24 * 60 * 60 * 1000); 
		return diffDay;
	}
	
	public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

}
