package com.esp.spycatch.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;

public class Log {

	/*Logging and Console*/
	public static boolean DO_LOGGING = true;
	public static boolean DO_SOP = true;

	public static void debug(String title, String mesg){
		File logFile = null;
		RandomAccessFile raf = null;

		try {

			if (Log.DO_LOGGING) {
				logFile = Storage.verifyLogFile();

				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				raf
						.writeUTF("<TR>"
								+ "<TD>" + new Date() + "</TD>"
								+ "<TD>" + title + "</TD>" 
								+ "<TD>" + mesg + "</TD></TR>");
			}

		} catch (Exception exception) {
			System.out.println("Log :: Debug :: " + exception.getMessage());
			exception.printStackTrace();
			
		} finally {
			if (raf != null) {
				try{
				raf.close();
				}catch(Exception e){}
			}
		}

		raf = null;
		logFile = null;
	}

	public static void error(String title, String mesg) {
		File logFile = null;
		RandomAccessFile raf = null;

		try {
			if (Log.DO_LOGGING) {
				
				logFile = Storage.verifyLogFile();

				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				raf
				.writeUTF("<TR>"
						+ "<TD style=\"color:#FF0000\">" + new Date() + "</TD>"
						+ "<TD style=\"color:#FF0000\">" + title + "</TD>" 
						+ "<TD style=\"color:#FF0000\">" + mesg + "</TD></TR>");
			}
		} catch (Exception exception) {
		} finally {
			if (raf != null) {
				try{
					raf.close();
					}catch(Exception e){}
			}
		}

		raf = null;
		logFile = null;
	}
	
	public static void error(String title, Exception mException) {
		mException.printStackTrace();
		File logFile = null;
		RandomAccessFile raf = null;
		StackTraceElement[] mStackTraceElement;
		String str = new String();
		try {
			if (Log.DO_LOGGING) {
				logFile = Storage.verifyLogFile();
				
				raf = new RandomAccessFile(logFile, "rw");

				// seek to end of file
				raf.seek(logFile.length());

				mStackTraceElement = mException.getStackTrace();
				
				str = "<TR>"
						+ "<TD style=\"color:#FF0000\">" + new Date() + "</TD>"
						+ "<TD style=\"color:#FF0000\">"
						+ title
						+ "</TD><TD style=\"color:#FF0000\"><BR/>"
						+ mException.toString() + "<BR/><BR/>"
						+ mException.getMessage() + "<BR/><BR/>";
				
				for (int ele=0; ele<mStackTraceElement.length; ele++){
					str += mStackTraceElement[ele].getClassName() + "." + mStackTraceElement[ele].getMethodName() + " (" + mStackTraceElement[ele].getFileName() + " : " + mStackTraceElement[ele].getLineNumber() + ") <BR/>";
				}

				str += "</TD></TR>";
				raf
				.writeUTF(str);
			}
		} catch (Exception exception) {
		} finally {
			if (raf != null) {
				try{
					raf.close();
					}catch(Exception e){}
			}
		}

		raf = null;
		logFile = null;
		str = null;

	}

	public static void print(String mesg) {
		if (Log.DO_SOP){
			System.out.println(mesg);
		}
	}

	public static void print(String title, String mesg) {
		if (Log.DO_SOP){
			System.out.println(title + " :: " + mesg);
		}
	}

	public static void print(Exception e) {	
		if (Log.DO_SOP){
			e.printStackTrace();
		}
	}
	
	public static String htmlEncode(String str){
		return str.replaceAll(">", "&lt;").replaceAll("<", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&#039;");
	}
}
