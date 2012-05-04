package com.esp.spycatch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esp.spycatch.bean.ImageBean;

public class Utils {

	public static void systemUpgrade() {
		int level = Integer.parseInt(Pref.getValue("LEVEL", "0"));
		switch(level){
			case 0:
				Utils.upgradeLevel1();
				level++;
			case 1:
				//upgradeLevel2();
		}
		
		
		Pref.setValue("LEVEL", level + "");
	}

	public static void upgradeLevel1(){
		Pref.setValue("PRODUCT", "FREE");
		
    	//Set default value		
		new DBHelper().upgrade(0);
	}

	public static void upgradeLevel2(){
		
	}
	
	public static boolean isOnline() {

		try {
			ConnectivityManager cm = (ConnectivityManager) Const.CONTEXT
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (cm != null) {
				return cm.getActiveNetworkInfo().isConnectedOrConnecting();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static String getDeviceID(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public static String doPost(String url, ArrayList<BasicNameValuePair> params)
			throws Exception {

		String strXML = null;
		HttpEntity httpentity = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		httppost.setEntity(new UrlEncodedFormEntity(params));
		HttpResponse response = httpclient.execute(httppost);

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

			httpentity = response.getEntity();
			strXML = new String(EntityUtils.toString(httpentity));
		}

		// release
		httpentity = null;
		response = null;
		httppost = null;
		httpclient = null;

		return strXML;
	}

	public static String doGet(String strURL) throws Exception {
		HttpEntity httpentity = null;
		HttpGet httpGet = null;
		String strResponse = null;
		HttpClient httpclient = new DefaultHttpClient();
		
		Log.print(Utils.class + "::doGet()", "URL : " +  strURL);
		
		httpGet = new HttpGet(strURL);	
		HttpResponse response = httpclient.execute(httpGet);
		
		Log.print("Status Code", response.getStatusLine().getStatusCode()+"");
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			httpentity = response.getEntity();
			strResponse = new String(EntityUtils.toString(httpentity));
	    }
				
		Log.print(Utils.class + "::doGet()", "Response : " + strResponse);
		
		//Release
		response = null;
		httpclient = null;
		httpGet = null;
		httpentity = null;
		
		return strResponse;
	}

	public static String readStream(InputStream is) throws IOException {
		int ch = 0;
		String str = new String();

		while ((ch = is.read()) != -1) {
			str += (char) ch;
		}

		is.close();

		return str;
	}

	public static int indexOfArray(String[] strArray, String strFind) {
		int index;

		for (index = 0; index < strArray.length; index++)
			if (strArray[index].equals(strFind))
				break;

		return index;
	}
	
	public static String[] arrListToArray(ArrayList<ImageBean> arrList) 
	{
		String[] objStr = null;
		try 
		{
			if (arrList != null) 
			{
				objStr = new String[arrList.size()];
				int index = 0;
				for (ImageBean obj : arrList) 
				{
					objStr[index] = obj.getFileName();
					index ++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objStr;

	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public static long dateToMilisec(String parseFormat) {		
		return Utils.convertStringToDate(convertDateToString(new Date(), parseFormat), parseFormat).getTime();		
	}

	public static void showAlert(String title, String message, String btnText) {
		AlertDialog alertDialog = new AlertDialog.Builder(Const.CONTEXT)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(btnText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
	public static String convertDateToString(Date objDate, String parseFormat)
	{
		try {
			return new SimpleDateFormat(parseFormat).format(objDate);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static Date convertStringToDate(String strDate, String parseFormat)
	{
		try {
			return new SimpleDateFormat(parseFormat).parse(strDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String convertDateStringToString(String strDate, String currentFormat, String parseFormat)
	{
		try {
			return convertDateToString(convertStringToDate(strDate, currentFormat), parseFormat);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	

    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(String path, int width, int height){
    	File f = null;;
    	Bitmap bitmap = null;
    	OutputStream os = null;
    	
        try {
        	
        	f = new File(path);
        	
        	if (f.exists())
        		bitmap = BitmapFactory.decodeFile(f.getPath());
        	else
        		Log.error("File not Found:", f.getPath());

        	if (bitmap != null){
	        	bitmap = CropImage(resize(bitmap, width, height), width, height);	
        	}
        	
        } catch (Exception e) {
        	Log.error(Utils.class + " :: decodeFile() :: ", e);
        }finally{
        	try {
				if (os!=null)
					os.close();
			} catch (IOException e) {
			}
        }
        
        //release
		f = null;
		os = null;
        
        return bitmap;
    }
    
	public static Bitmap resize(Bitmap bitMap, int width, int height)
	{
    	int per;
    	int bitWidth = bitMap.getWidth();
    	int bitHeight = bitMap.getHeight();
    	    	
    	if(bitHeight < bitWidth)
    	{
    		per = (height * 100) / bitHeight;
    		bitHeight = height;
    		bitWidth = (bitWidth * per) / 100;
    	}
    	else
    	{
    		per = (width * 100) / bitWidth;
    		bitWidth = width;
    		bitHeight = (bitHeight * per) / 100;
    	}
    	return Bitmap.createScaledBitmap(bitMap , bitWidth, bitHeight, false);
	}
    
	public static Bitmap CropImage(Bitmap bitMap, int Width, int Height)
	{	
    	int bitWidth = bitMap.getWidth();
    	int bitHeight = bitMap.getHeight();

    	int X = 0;
    	int Y = 0;
    	
    	if(bitHeight < bitWidth)
    	{
    		X = (bitWidth / 2) - (Width / 2);
    	}
    	else
    	{
    		Y = (bitHeight / 2) - (Height / 2);
		}
    	
    	if ((X+Width) <= bitWidth && (Y+Height) <= bitHeight)
    		return Bitmap.createBitmap(bitMap, X, Y, Width, Height);
    	else
    		return bitMap;
	}    
	
	public static boolean validation(LinearLayout view,Activity activity)
	{
		
		for (int index = 0; index < view.getChildCount(); index++) 
		{
			try {
				EditText txt = (EditText) view.getChildAt(index);
				String Message = (String) txt.getTag();
				if ((txt.getText() == null || txt.getText().toString().trim().equals("") || txt.getText().toString().length() == 0)&& Message.indexOf("*") == 0 && txt.getVisibility() != View.GONE) 
				{
					Toast.makeText(activity.getApplicationContext(), Message.substring(1), Toast.LENGTH_LONG).show();
					// This is Help to Focus on EditText
					txt.requestFocus();
					return false;
				}
					
			}
			catch (Exception e) {
			}
		}
		
		return true;

	}
	
	
	private static String strPattern  = ".+@.+\\.[a-z]+";
	public static final Pattern EMAIL_ADDRESS_PATTERN=Pattern.compile(strPattern);
	public static boolean checkEmail(String strEmail){
		try{
			if(strEmail!=null){
				
				Log.print("Utils | checkEmail ","checkEmail() ==> EMAIL :"+strEmail);
				return  EMAIL_ADDRESS_PATTERN.matcher(strEmail.trim()).matches();
			}
			
		}catch (Exception e) {
			
			Log.error("Utils | checkEmail ", e);
			e.printStackTrace();
			
		}
		return false;
	}

	public static String getStringDays(boolean[] boolDays){
		
		String pos=new String();
		for(int index=0;index<boolDays.length;index++){
			if(boolDays[index]==true){
				pos+=","+Const.array_WeekDays[index];
			}
		}
		
		if(!pos.equals("")){
			pos=pos.substring(1);	
		}
		
		return pos;
	}
	
	public static String[] getSplitTime(String time){
		
		String sDate[]= null;
		
		Log.print(" [ getSplitTime  ]", time);
		try{
			
			SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
			Date dte = parseFormat.parse(time);
			sDate = displayFormat.format(dte).split(":");
			Log.print(" [Display Time s ]" ,displayFormat.format(dte));
			return sDate;
			
		}catch (Exception e) {
			
			Log.error("Utils, getSplitTime()", e);
			e.printStackTrace();
			
		}
		return sDate;
		
		
	}
	public static String get12hTo24h(String reqFrmt,String strTime,String currentFrmt)
	{
		String newString="";
		try{
			if(!strTime.equals("")){
				
				SimpleDateFormat parseFormat=new SimpleDateFormat(currentFrmt);
				SimpleDateFormat displayFormat=new SimpleDateFormat(reqFrmt);
				Date dte = parseFormat.parse(strTime);
				newString = displayFormat.format(dte);
				return newString;
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return newString;
	}

	
	public static long getTimeToMilisecond(String strTime){
		
		System.out.println("[ Time ] " + strTime);
		Date mDate = new Date();
		try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
				mDate = dateFormat.parse(strTime);
				System.out.println("[ TIME MILISECOND ] " + mDate.getTime());

			} catch (Exception e) {
				e.printStackTrace();
			}
		return mDate.getTime();
	}
	
	public static String getMilisecondToDate(long yourmilliseconds){
		Log.print("Utils |=> getMilisecondToDate()"," yourmilliseconds : "+ yourmilliseconds);
		Date resultdate = new Date(yourmilliseconds);
		Log.print("Utils |=> getMilisecondToDate()"," "+ Utils.convertDateToString(resultdate,"MMM dd,yyyy HH:mm"));
		return Utils.convertDateToString(resultdate,"MMM dd,yyyy HH:mm");
	}
	
	
	public static boolean matchDay(String days) {
		
		Calendar cal = Calendar.getInstance();
		int currentDay = cal.get(Calendar.DAY_OF_WEEK)-1;
		
		System.out.println(" [ CUURET DAY ]" + Const.array_WeekDays[currentDay] );
		System.out.println(" [ YOUR DAY ]" + days );
		
		
		if (days != null && !days.equals("")) {
			
			String result[] = days.split(",");
			for (int index = 0; index < result.length; index++) {

				if (Const.array_WeekDays[currentDay].equals(result[index])) {
					System.out.println(" [ MATCH  DAY ]" + result[index] );
					return true;
					
				}
			}
		} else {

			return false;
		}
		return false;

	}
	
	public static String currentTimeMilisecond(){
		
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int AMPM = cal.get(Calendar.AM_PM); // 0=AM, 1=PM

		System.out.println("[ Day of Hour: ]" + hour);
		System.out.println("[ Day of Min: ] " + min);

		String str = (AMPM == 0) ? "AM" : "PM";
		Log.print("[currentTimeMilisecond ] ", pad(hour) + ":" + pad(min) + " " + str);
		return pad(hour) + ":" + pad(min) + " " + str;
	}
	
	private static String pad(int c) {
		
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

	public static boolean IsBetween(long lngFromTime,long lngCurrent,long lngToTime){
		
		if(lngToTime > lngCurrent && lngFromTime < lngCurrent){
			Log.print("IsBetween | => "," True");
			return true;
		}else{
			Log.print("IsBetween | => "," False");
			return false;
		}
		
	}
	
	public static long getMilisecond(){
		return new Date().getTime();
	}
	
	public static String trimExtension(String filename) {
		
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}
	
	public static int renameFileExtention(String fileName){
		
		int result = 1;
		File originalFile = new File(Const.IMAGE_DIR + "/" + fileName);
	     
		boolean fileExists = originalFile.exists();
	 
        boolean isDirectory = originalFile.isDirectory();
	 
        if (!fileExists) {
 
            Log.print("","File does not exist: " + Const.IMAGE_DIR + "/" + fileName);
            Log.print("","Rename Operation Aborted.");
            return result = -1;
            
        }else if (isDirectory) {
        	
        	Log.print("","The parameter you have provided is a directory: "
                            + Const.IMAGE_DIR + "/" + fileName);
        	Log.print("","Rename Operation Aborted.");
        	return result = -1;
        	
        }else{
        	
        	try {
    			Storage.copyFile(new FileInputStream(Const.IMAGE_DIR + "/" + fileName), new FileOutputStream(Const.CATCH_IMAGE_DIR + "/" + Utils.trimExtension(fileName)+".png"));
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            return result;
        } 
	}
	
	
	
	public static int ShareImage(String fileName){
		
		int result = 1;
		File originalFile = new File(Const.IMAGE_DIR + "/" + fileName);
	     
		boolean fileExists = originalFile.exists();
	 
        boolean isDirectory = originalFile.isDirectory();
	 
        if (!fileExists) {
 
            Log.print("","File does not exist: " + Const.IMAGE_DIR + "/" + fileName);
            Log.print("","Rename Operation Aborted.");
            return result = -1;
            
        }else if (isDirectory) {
        	
        	Log.print("","The parameter you have provided is a directory: "
                            + Const.IMAGE_DIR + "/" + fileName);
        	Log.print("","Rename Operation Aborted.");
        	return result = -1;
        	
        }else{
        	
        	try {
    			Storage.copyFile(new FileInputStream(Const.IMAGE_DIR + "/" + fileName), new FileOutputStream(Const.TEMP_IMAGE_DIR + "/" + Utils.trimExtension(fileName)+".png"));
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            return result;
        } 
	} 
}
