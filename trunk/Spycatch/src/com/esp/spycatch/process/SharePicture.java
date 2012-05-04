package com.esp.spycatch.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class SharePicture extends BroadcastReceiver {
	
	
	public static boolean STARTED = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Const.CONTEXT = context;
		EmailReceiver emailReceiver;
		MMSReceiver   mmsReceiver;
		FacebookReceiver fbReceiver;
		SharePicture.STARTED = true;
		
		Log.print("|************************|");
		Log.print("Running Process SharePicture" + Utils.getMilisecondToDate(System.currentTimeMillis()));
		Log.print("|************************|");
		
		if (Pref.getValue("EMAIL_SESSION_VALID", "No").equals("Yes"))
		{
			emailReceiver = new EmailReceiver();
			emailReceiver.onEmailReceive(context, intent);
		}
		
		if (Pref.getValue("MMS_SESSION_VALID", "No").equals("Yes"))
		{
			mmsReceiver = new MMSReceiver();
			mmsReceiver.onMMSReceive(context, intent);
		}
		
		
		if (Pref.getValue("FB_SESSION_VALID", "No").equals("Yes")) 
		{
			fbReceiver = new FacebookReceiver();
			fbReceiver.onFacebookReceive(context, intent);
		}
		
	}
}
