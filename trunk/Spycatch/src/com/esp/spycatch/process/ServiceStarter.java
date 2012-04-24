package com.esp.spycatch.process;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Const.CONTEXT = context;
		
		if (Pref.getValue("EMAIL_SESSION_VALID", "No").equals("Yes")){
			
				Intent mintent = new Intent(Const.CONTEXT, EmailReceiver.class);
				Const.Email_objPendingIntent = PendingIntent.getBroadcast(Const.CONTEXT, 0, mintent,
												PendingIntent.FLAG_CANCEL_CURRENT);
				
				Const.objAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				Const.objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
									System.currentTimeMillis(), Const.INTERVAL_TIME,
									Const.Email_objPendingIntent);
				
				Log.print("ServiceStarter | " + Const.SERVICE_EMAIL ,"***** START "+  Const.SERVICE_EMAIL + "ALARM MANAGER *******");
					
			
		}
	
		if (Pref.getValue("MMS_SESSION_VALID", "No").equals("Yes")){
			
			
			Intent mIntent = new Intent(Const.CONTEXT, MMSReceiver.class);
	
			Const.MMS_objPendingIntent = PendingIntent.getBroadcast(Const.CONTEXT, 0, mIntent,
							PendingIntent.FLAG_CANCEL_CURRENT);
			
			Const.objAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Const.objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), Const.INTERVAL_TIME,Const.MMS_objPendingIntent);
			
			Log.print("Setting | " + Const.SERVICE_MMS,"***** START "+  Const.SERVICE_MMS +  "ALARM MANAGER *******");
		}
	
		
		if (Pref.getValue("FB_SESSION_VALID", "No").equals("Yes")) {

			Intent mIntent = new Intent(context, FacebookReceiver.class);
			Const.FB_objPendingIntent = PendingIntent.getBroadcast(
						Const.CONTEXT, 0, mIntent,
						PendingIntent.FLAG_CANCEL_CURRENT);
			
			Const.objAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			Const.objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), Const.INTERVAL_TIME,
						Const.FB_objPendingIntent);
				
			Log.print("ServiceStarter | " + Const.SERVICE_FB,"***** START "+  Const.SERVICE_FB + "ALARM MANAGER *******");

		}
		
	
	}

}