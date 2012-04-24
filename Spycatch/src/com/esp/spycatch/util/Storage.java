package com.esp.spycatch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.res.AssetManager;

public class Storage {
	
	public static void verifyImagePath() throws IOException {
		File dir = new File(Const.IMAGE_DIR);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
		
	}
	
	public static void verifyThumbnailPath() throws IOException {
		File dir = new File(Const.THUMBNAIL_DIR);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
	}

	public static void veirfyLogPath() throws IOException {
		File dir = new File(Const.LOG_DIR);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
	}

	public static File verifyLogFile() throws IOException {
		File logFile = new File(Const.LOG_DIR + "/Log_"
				+ new SimpleDateFormat("yyyy_MM_dd").format(new Date())
				+ ".html");
		FileOutputStream fos = null;

		Storage.veirfyLogPath();

		if (!logFile.exists()) {
			logFile.createNewFile();
			
			fos = new FileOutputStream(logFile);
			
			String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
				+ "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
				+ "<TD style=\"width:20%\"><B>Title</B></TD>" 
				+ "<TD style=\"width:50%\"><B>Description</B></TD></TR>";
			
			fos.write(str.getBytes());
		}

		if (fos != null){
			fos.close();
		}
		
		fos = null;
		
		return logFile;
	}
	
	public static void verifyCatchImagePath()throws IOException{
		
		File dir = new File(Const.CATCH_IMAGE_DIR);

		if (!dir.exists()) {
			dir.mkdirs();
		}

		dir = null;
		
	}
	
	public static int copyResourceFiles(int level) {
		int i = 0;
		switch (level) {
		case 0:
			i = Storage.CopyAssets();
			break;
		}
		return i;
	}
	
	private static int CopyAssets() {
		
		int errorNo = 0 ;
	    AssetManager assetManager = Const.CONTEXT.getAssets();
	    InputStream in = null;
		OutputStream out = null;
		try {
			
			  in = assetManager.open("watermark.png");
			  out = new FileOutputStream(Const.TEMP_IMAGE_PATH);
			  copyFile(in, out);
			  in.close();
			  in = null;
			  out.flush();
			  out.close();
			  out = null;
		
		} catch(Exception e) {
			errorNo = -2;
			e.printStackTrace();
		} 
	
	   return errorNo;
	}
	
	public static void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}

	public static boolean IsFileAvailableDelete(String strFileName){
		
		File dir = new File(strFileName);

		if (!dir.exists()) {
			return false;
		}
		return dir.delete();
	}
	
	public static boolean IsFileAvailable(String strFileName){
		File dir = new File(strFileName);

		if (!dir.exists()) {
			return false;
		}
		return true;
	}
	
	
}

