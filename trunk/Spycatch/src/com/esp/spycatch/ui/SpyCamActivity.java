package com.esp.spycatch.ui;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.GLPreview;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SpyCamActivity extends Activity {
	
	public GLPreview glPreview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.spycam);
        
        Const.CONTEXT = this;
        
		this.glPreview = (GLPreview)findViewById(R.id.glPreview);
			
		this.glPreview.init();
        
		new TakeSnap(this).start();
    }
    
    private class TakeSnap extends Thread {
    	Activity activity;
    	public TakeSnap(Activity activity){
    		this.activity = activity;
    	}
    	
		@Override
		public void run() {
			try{
				Thread.sleep(1000);
				glPreview.startTakePicture();
				Thread.sleep(2000);
			}catch(Exception e){
				Log.print("Exception Path : ", e.getMessage());
			}
			
			this.activity.finish();
		}
    }    
}
