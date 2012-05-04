package com.esp.spycatch.process;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.IBinder;

import com.esp.spycatch.R;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Storage;

public class ShareService extends Service {
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		Log.print("","| ********** Share service ****************** |");
		String fileName;
		String lat;
		String lon;
		FileOutputStream outStream=null;
		Bitmap watermark = null;
		Bitmap cameraBitmap = null;
		Bitmap scaledBitmap = null;
		Bitmap drawingCache = null;
		Canvas canvas;
		Paint paint;
		Paint watermarkPaint;
		Paint locationPaint;
		
		super.onStart(intent, startId);
		
		fileName = intent.getExtras().getString("FileName");
		lat = intent.getExtras().getString("Lat");
		lon = intent.getExtras().getString("Lon");
		
		try{
			
			cameraBitmap = BitmapFactory.decodeFile(Const.IMAGE_DIR + "/" + fileName);
			
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
			
			if ( !(lat.equals("0") && lon.equals("0")) ){
				locationPaint = new Paint();
				locationPaint.setColor(Color.WHITE);
				locationPaint.setTextSize(20);
				locationPaint.setTextAlign(Paint.Align.LEFT);
				locationPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

				canvas.drawText(Const.latitude + "," + Const.longitude, watermarkPaint.measureText(Const.latitude + "," + Const.longitude), 50, watermarkPaint);
			}
			
			outStream = new FileOutputStream(Const.IMAGE_DIR + "/" + fileName);
			drawingCache.compress(CompressFormat.JPEG, 100, outStream);
			outStream.close();
			outStream = null;
			
			//Save Thumbnail
			scaledBitmap = Bitmap.createScaledBitmap(drawingCache, (int)((100 / (double)height) * (double)width), 100, true);
			Storage.verifyThumbnailPath();
			Log.print("Share service","Image: "+Const.THUMBNAIL_DIR + "/" + fileName);
			outStream = new FileOutputStream(Const.THUMBNAIL_DIR + "/" + fileName);
			scaledBitmap.compress(CompressFormat.JPEG, 100, outStream);
			
		}catch(Exception e){
			e.printStackTrace();
			Log.error(this.getClass().toString() + " :: start() :: ", e);
		}finally{
			if (outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
