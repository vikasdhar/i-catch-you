package com.esp.spycatch.util;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class ImageFactory {
	public Bitmap bitmap;
	
	public ImageFactory(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public Bitmap addWatermark(){
		Bitmap resultBitmap = null;
		Bitmap watermark = null;
		Canvas canvas = null;
		
		try{
			watermark = this.getBitmapFromAsset("watermark.png");
			
			int width = this.bitmap.getWidth();
			int height = this.bitmap.getHeight();
			
			resultBitmap = Bitmap.createBitmap(width, height, this.bitmap.getConfig());
			canvas = new Canvas(resultBitmap);
			//canvas.drawBitmap(this.bitmap, new Matrix(), null);
			canvas.drawBitmap(watermark, 0, 0, null);
			
		}catch(Exception e){
			resultBitmap = null;
		}
		
		//release
		watermark = null;
		canvas = null;
		
		return resultBitmap;
	}
	
	public Bitmap getBitmapFromAsset(String strName) {
	    AssetManager assetManager = Const.CONTEXT.getAssets();

	    InputStream istr;
	    Bitmap bitmap = null;
	    try {
	        istr = assetManager.open(strName);
	        bitmap = BitmapFactory.decodeStream(istr);
	    } catch (IOException e) {
	        return null;
	    }

	    return bitmap;
	}	
}
