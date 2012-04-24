package com.esp.spycatch.process;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MMSReceiver extends BroadcastReceiver{

	private String TAG = this.getClass().getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Const.CONTEXT = context;
		Log.print(TAG,"|**|**|  MMS SERVICE  RUNNING |**|**|");
		if(!Pref.getValue("MMS_SEND_TO", "").equals(""))
		{
			
		}
	}

}
