package com.esp.spycatch.uc;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.AudioManager;

import com.esp.spycatch.R;
import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
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
		String fileName;
		FileOutputStream outStream=null;
		Bitmap watermark = null;
		Bitmap cameraBitmap = null;
		Bitmap scaledBitmap = null;
		Bitmap drawingCache = null;
		ImageBean imageBean;
		Canvas canvas;
		Paint paint;
		Paint watermarkPaint;
		Paint locationPaint; 
		
		try {
			
			AudioManager mgr = (AudioManager)Const.CONTEXT.getSystemService(Context.AUDIO_SERVICE);
		    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, false);

			if (data != null) {
				//Verify Directory Structure
				Storage.verifyImagePath();
			
				//Capture Camera Image and Store it to File
				cameraBitmap=BitmapFactory.decodeByteArray(data,0,data.length);
				
				int height = cameraBitmap.getHeight();
				int width = cameraBitmap.getWidth();
							
				//Create water mark
				drawingCache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
				canvas = new Canvas(drawingCache);
				paint = new Paint();
				canvas.drawBitmap(cameraBitmap, 0, 0, paint);
			
				//Get Watermark
				watermark = BitmapFactory.decodeResource(Const.CONTEXT.getResources(), R.drawable.watermark);

				
				watermarkPaint = new Paint();
				watermarkPaint.setAlpha(50);
				
				int x = (width/2) - watermark.getWidth()/2; //width of watermark image
				int y = (height/2) - watermark.getHeight()/2; //height of watermark image
				canvas.drawBitmap(watermark,x,y,watermarkPaint);
				
				if (Pref.getValue("IS_LOCATION_AVAILABLE", "0").equals("1")){
					locationPaint = new Paint();
					locationPaint.setColor(Color.WHITE);
					locationPaint.setTextSize(20);
					locationPaint.setTextAlign(Paint.Align.LEFT);
					locationPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	
					canvas.drawText(Const.latitude + "," + Const.longitude, watermarkPaint.measureText(Const.latitude + "," + Const.longitude), 50, watermarkPaint);
				}
				
				fileName = System.currentTimeMillis() + ".sc";
				
				outStream = new FileOutputStream(Const.IMAGE_DIR + "/" + fileName);
				drawingCache.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();
				outStream = null;
				
				scaledBitmap = Bitmap.createScaledBitmap(drawingCache, (int)((100 / (double)height) * (double)width), 100, true);
				
				Storage.verifyThumbnailPath();
				
				outStream = new FileOutputStream(Const.THUMBNAIL_DIR + "/" + fileName);
				scaledBitmap.compress(CompressFormat.JPEG, 100, outStream);				
				
				imageBean = new ImageBean();
				imageBean.fileName = fileName;
				
				new ImageBL().Insert(imageBean);
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
		locationPaint = null;
		drawingCache = null;
		watermarkPaint = null;
		canvas = null;
		paint = null;
		outStream = null;
		cameraBitmap = null;
		scaledBitmap = null;
		fileName = null;
		imageBean = null;
		
	}
}