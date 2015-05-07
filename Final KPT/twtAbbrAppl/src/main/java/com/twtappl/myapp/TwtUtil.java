package com.twtappl.myapp;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TwtUtil {
	//private static Logger log = Logger.getLogger(RequestController.class);
	
	TwtUtil(String sqmUtil){
		
	}
	
	public static String formatContentForGrid(String str) {
		if (str == null){
			return "";
		}
		try {
			if (str.contains("'")) {
				str = str.replaceAll("'", "&lsquo;");
			}
			if (str.contains("'")) {
				str = str.replaceAll("\"", "&quot;");
			}
			if (str.contains("&")) {
				str = str.replaceAll("&", "&amp;");
			}
			return str;
		} catch (Exception e) {
			// logger.error(e);
			return "";
		}
	}

	private static boolean ifValidField(String input) {

		if ((null != input) && !(input.isEmpty()) && (input.length() != 0)) {
			return true;
		} else {
			return false;
		}

	}

	public static String convertStringToDate(String date) {

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		Date theDate = null;
		try {
			if (ifValidField(date)) {
				theDate = dateFormat.parse(date);
				//log.debug("Date parsed = " + dateFormat.format(theDate));
			} else {
				String dateform = dateFormat.format(new Date());
				theDate = dateFormat.parse(dateform);
				//log.debug("Date parsed = " + dateFormat.format(theDate));
			}

		} catch (Exception e) {
			//log.error("Error parsing date " + e.getMessage());
		}
		return dateFormat.format(theDate);

	}

	public static boolean isNotNullEmpty(String arg) {
		if ((null != arg) && !arg.equalsIgnoreCase("") && arg.length()!= 0) {
			return true;
		}
		return false;
	}

	public static String dateToString(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	public static String decimalPlaces(String value){
		double d = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
	}
	public static Float decimalPlacesFloat(float value){
		double d = value;
        DecimalFormat df = new DecimalFormat("#.00");
        return Float.parseFloat(df.format(d));
	}
	public static double decimalPlacesDouble(double value){
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(value));
	}
	public static Date stringToDate(String dateString, String format) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(dateString);
		} catch (Exception e) {
			//log.error("SQMUtil.stringToDate: " + e.getMessage());
		}
		return null;
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || "".equalsIgnoreCase(str)) {
			return true;
		}
		return false;
	}
	
	public static String dateToString(Date date){
		String strDate = "";
		if(date !=null){
			try{
				DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				strDate = formatter.format(date);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return strDate;
	}
}
