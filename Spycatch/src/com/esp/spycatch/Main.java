package com.esp.spycatch;

import com.esp.spycatch.process.UpdateLocation;
import com.esp.spycatch.ui.Dashboard;
import com.esp.spycatch.ui.Login;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Storage;
import com.esp.spycatch.util.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Main extends Activity {
    
	static final int SPALSH_FINISH = 0;
	static final int SPLASH_DELAY_TIME  = 3000;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Const.CONTEXT = this;
        Utils.systemUpgrade();
    	if (!UpdateLocation.STARTED){
        	//Wake up Update Location Service
			AlarmManager amUpdateLocation=(AlarmManager)Const.CONTEXT.getSystemService(Context.ALARM_SERVICE);
			Intent intentUpdateLocation = new Intent(Const.CONTEXT,UpdateLocation.class);
		    PendingIntent piUpdateLocation=PendingIntent.getBroadcast(Const.CONTEXT, 0,intentUpdateLocation, 0);
		    amUpdateLocation.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),300000,piUpdateLocation);
        }        
        try{

            Storage.verifyThumbnailPath();
        	Storage.verifyCatchImagePath();
        }catch (Exception e) {
        	e.printStackTrace();
		}

        
        
        // Copy temp file from asstes to sd card 
        if(!Storage.IsFileAvailable(Const.TEMP_IMAGE_PATH)){
        	
        	int Result = Storage.copyResourceFiles(0);
            Log.print("Main | onCreate "," | *** |copyResourceFiles |****|" + Result);
            
        }
        
        SplashHandler mHandler = new SplashHandler();
        Message mMessage = new Message();
        mMessage.what = SPALSH_FINISH;
        mHandler.sendMessageDelayed(mMessage,SPLASH_DELAY_TIME);        
    }
    
    
    private class SplashHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		
    		switch (msg.what) {
    		
			case SPALSH_FINISH:
				
				Log.print("","[ MESSAGE GETTING ]");
				if(Pref.getValue("PASSWORD_PROTECTION","0").equals("1"))
				startActivity(new Intent(getApplicationContext(),Login.class));
				else
				startActivity(new Intent(getApplicationContext(),Dashboard.class));
				finish();
				break;

			default:
				break;
			}
    	}
    }
}