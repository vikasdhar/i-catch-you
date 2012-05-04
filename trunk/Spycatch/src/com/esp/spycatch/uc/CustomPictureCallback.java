package com.esp.spycatch.uc;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;

import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
import com.esp.spycatch.process.ShareService;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Storage;

public class CustomPictureCallback  implements PictureCallback {

	private String imageFormat;
	
	public CustomPictureCallback(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	public void onPictureTaken(byte[] data, Camera camera) {
		
		Log.debug(this.getClass().toString(), "onPictureTaken()");
		
		if (this.imageFormat.equals("raw")) {
			this.onRawPictureTaken(data, camera);
		} else if (this.imageFormat.equals("jpeg")) {
			this.onJpegPictureTaken(data, camera);
		}
	}
	
	public void onRawPictureTaken(byte[] data, Camera camera) {
	}

	public void onJpegPictureTaken(byte[] data, Camera camera) {
		
		Log.debug(this.getClass().toString(), "onJpegPictureTaken()");
		Intent intent;
		String fileName;
		FileOutputStream outStream=null;
		Bitmap cameraBitmap = null;
		ImageBean imageBean;
		
		try {
			
			AudioManager mgr = (AudioManager)Const.CONTEXT.getSystemService(Context.AUDIO_SERVICE);
		    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);

			if (data != null) {
				//Verify Directory Structure
				Storage.verifyImagePath();
			
				//Capture Camera Image and Store it to File
				cameraBitmap=BitmapFactory.decodeByteArray(data,0,data.length);
				
				fileName = System.currentTimeMillis() + ".sc";
				
				outStream = new FileOutputStream(Const.IMAGE_DIR + "/" + fileName);
				cameraBitmap.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();				
				
				imageBean = new ImageBean();
				imageBean.fileName = fileName;
				
				if (Const.latitude != null && Const.longitude != null){
					imageBean.lat = Const.latitude;
					imageBean.lon = Const.longitude;
				}else{
					imageBean.lat = "0";
					imageBean.lon = "0";					
				}
				
				new ImageBL().Insert(imageBean);
				
				intent = new Intent(Const.CONTEXT, ShareService.class);
				intent.putExtra("FileName", fileName);
				intent.putExtra("Lat",imageBean.lat);
				intent.putExtra("Lon",imageBean.lon);
				Const.CONTEXT.startService(intent);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(this.getClass().toString() + " :: onJpegPictureTaken() :: ", e);
		}finally{
			if (outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//wind-up process
		outStream = null;
		cameraBitmap = null;
		fileName = null;
		imageBean = null;
	}
}