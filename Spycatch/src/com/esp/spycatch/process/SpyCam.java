package com.esp.spycatch.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esp.spycatch.ui.SpyCamActivity;
import com.esp.spycatch.util.Log;

public class SpyCam extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {	
		Log.debug(this.getClass().toString(),"Launched");
		context.startActivity(new Intent(context,SpyCamActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}
}
