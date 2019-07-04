package com.myfutech.common.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final String DATETYPE1="yyyy-MM-dd";
	public static final String DATETYPE2="yyyy-MM-dd HH:mm:ss";
	public static final String DATETYPE3="yyyy-MM-dd HH:mm";
	public static final String DATETYPE4="yyyy-MM-dd";
	public static final String DATETYPE5="yyyy年MM月dd日";
	public static final String DATETYPE6="yyyyMMdd";
	
	/**
	 * 根据格式将Data转为String
	 * @param date
	 * @return
	 */
	public static String  stringToData(Date date,String dataType){
		SimpleDateFormat format = new SimpleDateFormat(dataType);
		return format.format(date);
	}
	
	
	/**
	 * 截取字符串日期格式的年月日并返回
	 * @Description: TODO
	 * @param @param date
	 * @param @return   
	 * @return String  
	 * @author gzy
	 * @date 2018年2月1日
	 */
	public static String splitDate(String date){
		
		String[] split = date.split(" ");
		
		return split[0];
		
	}
	/**
	 * 获取当前系统时间
	 * <p>Title: currentDate</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	public static String currentDate(){
		
		Date date = new Date();
		
		return stringToData(date,DATETYPE2);
	}
	
	/**
	 * 获取当前系统时间 X年X月X日;
	 * <p>Title: currentDate</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	public static String currentDateYMD(){
		Date date = new Date();
		SimpleDateFormat simdf = new SimpleDateFormat(DATETYPE5);
		return simdf.format(date);
	}
	
	/**
	 * 格式化时间年月日汉字
	 * <p>Title: currentDate</p>  
	 * <p>Description: </p>  
	 * @return
	 */
	public static String dateFormatCNYMD(String date){
		SimpleDateFormat simdf = new SimpleDateFormat(DATETYPE5);
		return (simdf.format(java.sql.Date.valueOf(date)));
	}
	
}
