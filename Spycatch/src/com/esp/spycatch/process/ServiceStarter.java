package com.esp.spycatch.process;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class ServiceStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{

		Const.CONTEXT = context;
		Log.print("********** Initializing Service  starter	****************");
		
	if (Pref.getValue("STOP_START_SERVICE", "No").equals("Yes")) 
		{
			
			Log.print("|************************|");
			Log.print("CALLED SERVICE STARTER OF SPY CATCH " + Utils.getMilisecondToDate(System.currentTimeMillis()));
			Log.print("|************************|");
			
			Intent mIntent = new Intent(Const.CONTEXT, SharePicture.class);
	
			Const.objPendingIntent = PendingIntent.getBroadcast(
					Const.CONTEXT, 0, mIntent,
					PendingIntent.FLAG_CANCEL_CURRENT);
	
			Const.objAlarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Const.objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis() + 120000,
					Const.INTERVAL_TIME, Const.objPendingIntent);
		}

	}

}