package com.esp.spycatch.process;

import android.content.Context;
import android.content.Intent;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;

public class MMSReceiver {

	private String TAG = this.getClass().getSimpleName();

	public void onMMSReceive(Context context, Intent intent) {

		Const.CONTEXT = context;
		Log.print(TAG, "|**|**|  MMS SERVICE  RUNNING |**|**|");
		if (!Pref.getValue("MMS_SEND_TO", "").equals("")) {

		}
	}

}
