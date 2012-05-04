package com.esp.spycatch.uc;

import java.io.IOException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;

public class GLPreview extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder surfaceHolder;
	public Camera camera;
	
	public GLPreview(Context context) {
		super(context);
	}

	public GLPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void init(){	
		this.surfaceHolder = this.getHolder();
		this.surfaceHolder.setFormat(PixelFormat.JPEG);
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);		
	}
    
	public void onResume(){
		
	}
	
	public void onPause(){
		
	}
	
	// SurfaceHolder.Callback
	public void surfaceCreated(SurfaceHolder holder) {	
		
		Log.print(this.getClass().toString(), "surfaceCreated()");
		
		camera = Camera.open();
		//camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);

		try {
			camera.setPreviewDisplay(this.surfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// SurfaceHolder.Callback
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.print(this.getClass().toString(), "surfaceChanged()");
		
		Parameters params = null;
		if (camera != null) {
			params = camera.getParameters();
			
			params.setPictureFormat(PixelFormat.JPEG);

			camera.setParameters(params);

			camera.startPreview();
		}
	}

	// SurfaceHolder.Callback
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();

		camera = null;
	}	
	
	public void startTakePicture() {
		
		Log.print(this.getClass().toString(), "startTakePictre()");
		
		//Log.debug(this.getClass().toString(), camera.toString());
		
		Parameters params = null;
		
		AudioManager mgr = (AudioManager)Const.CONTEXT.getSystemService(Context.AUDIO_SERVICE);
	    mgr.setStreamMute(AudioManager.STREAM_SYSTEM, true);
		
		if (camera != null){
			
			params = camera.getParameters();
			
			params.setPictureFormat(PixelFormat.JPEG);
			
			camera.setParameters(params);
			
			camera.autoFocus(new CustomAutoFoucsCallback());
		}
	}
}
