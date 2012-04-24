package com.esp.spycatch.uc;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;

public class CustomAutoFoucsCallback implements AutoFocusCallback{

	public void onAutoFocus(boolean success, Camera camera) {
		this.takePicture(camera);
	}
	
	public void takePicture(Camera camera) {
		camera.takePicture(
				new CustomShutterCallback(),
				new CustomPictureCallback("raw"),
				new CustomPictureCallback("jpeg"));
	}
}
