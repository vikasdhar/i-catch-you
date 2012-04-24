package com.esp.spycatch.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;

public class UpdateLocation extends BroadcastReceiver{
	public static boolean STARTED=false;
	public LocationManager mGPSLocationManager=null;
	public LocationManager mNetLocationManager=null;	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		UpdateLocation.STARTED=true;
		
		Log.print("update Location Service is Started");
		
		this.start();
	}

	public void start(){
		Location location;
		Criteria criteria;
		
		int count=0;
		
		this.mGPSLocationManager = (LocationManager) Const.CONTEXT
				.getSystemService(Context.LOCATION_SERVICE);
		
		if (this.mGPSLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			count++;
		}
		
		this.mNetLocationManager = (LocationManager) Const.CONTEXT
				.getSystemService(Context.LOCATION_SERVICE);
		
		if (this.mNetLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			count++;			
		}
		
		if (count == 0){
			Pref.setValue("IS_LOCATION_AVAILABLE", "0");
			Toast
			.makeText(
					Const.CONTEXT,
					"GPS system is not available.",Toast.LENGTH_LONG).show();
			Log.debug(this.getClass() + "", "GPS and Netwok provider are not working");
		}else{
			
			criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			
			location = this.mGPSLocationManager.getLastKnownLocation(this.mGPSLocationManager.getBestProvider(criteria, true));
			
			if (location != null){
				Const.latitude = location.getLatitude()+"";
				Const.longitude = location.getLongitude()+"";
				Pref.setValue("IS_LOCATION_AVAILABLE", "1");
				
				Log.print("GPS Reading :: ", "============================================");
				Log.print("GPS Reading :: ", "Longitude : " + Const.longitude );
				Log.print("GPS Reading :: ", "Laitude : " + Const.latitude );
				Log.print("GPS Reading :: ", "============================================");
				
			}else{
				location = this.mNetLocationManager.getLastKnownLocation(this.mGPSLocationManager.getBestProvider(criteria, true));
				
				if (location != null){
					Const.latitude = location.getLatitude()+"";
					Const.longitude = location.getLongitude()+"";
					Pref.setValue("IS_LOCATION_AVAILABLE", "1");
					
					Log.print("NET Reading :: ", "============================================");
					Log.print("NET Reading :: ", "Longitude : " + Const.longitude );
					Log.print("NET Reading :: ", "Laitude : " + Const.latitude );
					Log.print("NET Reading :: ", "============================================");
					
				}else{
					Pref.setValue("IS_LOCATION_AVAILABLE", "0");
					Toast
					.makeText(
							Const.CONTEXT,
							"GPS system is not available.",Toast.LENGTH_LONG).show();
					Log.debug(this.getClass() + "", "GPS and Netwok provider are not working");					
				}
			}
		}
	}
}
