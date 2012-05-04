package com.esp.spycatch.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Environment;

public class Const {
	
	public static String DB_NAME = "spycatch";
	public static String PREF_FILE = "spycatch";
	
	
	public static String APP_HOME = Environment.getExternalStorageDirectory().getPath() + "/Spycatch";
	public static String LOG_DIR = APP_HOME + "/log";
	public static String IMAGE_DIR = APP_HOME + "/images";
	public static String CATCH_IMAGE_DIR = APP_HOME + "/catch";
	public static String TEMP_IMAGE_DIR = APP_HOME + "/temp";
	public static String THUMBNAIL_DIR = APP_HOME + "/thumbnails";
	public static String TEMP_IMAGE_PATH = APP_HOME + "/temp.png";
	
	public static Context CONTEXT = null;
	
	public static String longitude;
	public static String latitude;
	
	public static AlarmManager spyCatchAlarmManager = null;
	public static PendingIntent spyCatchPendingIntent = null;
	
	public static final int DIALOG_PASSWORD = 1;
	public static final int DIALOG_EMAIL_SUPPORT = 2;
	public static final int DIALOG_MMS_SUPPORT = 3;
	public static final int DIALOG_FACEBOOK_SUPPORT = 4;
	public static final int DIALOG_ADVANCE_SETTINGS = 5;
	
	
	public static final int DIALOG_WEEKDAYS = 5;
	
	public static String array_WeekDays[] = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thusday", "Friday", "Saturday" };
	
	public static String ANDROID_MARKET_LINK = "market://details?id=com.esp.spycatch";
	
	
	// FACEBOOK APPLICATION ID 
	public static final String FACEBOOK_APP_ID = "137918546333299"; 
	
	
	public static AlarmManager objAlarmManager = null;
	public static PendingIntent objPendingIntent = null;
	
	
	
	public static boolean IS_STRATUP = true;
	
	/**
	 * 1 Min = 60000 Milisecond
	 * 
	 */
	public static long INTERVAL_TIME = (60000 * 5);
	
}
