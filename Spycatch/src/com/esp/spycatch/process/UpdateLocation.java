package com.esp.spycatch.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
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
		this.mGPSLocationManager = (LocationManager) Const.CONTEXT
				.getSystemService(Context.LOCATION_SERVICE);
				
		LocationListener mGPSLocationListener = new GPSLocationListener();
		
		if (this.mGPSLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			this.mGPSLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 1, mGPSLocationListener);
		}else{
			Pref.setValue("IS_LOCATION_AVAILABLE", "0");
			Toast
			.makeText(
					Const.CONTEXT,
					"GPS system is not available.",Toast.LENGTH_LONG).show();
		}
	}
	
	private class GPSLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			try {
				if (location != null) {

					Const.latitude = String.valueOf(location.getLatitude());
					Const.longitude = String.valueOf(location.getLongitude());
					
					Log.debug("GPS Reading :: ", "Longitude : " + Const.longitude + " <br/>Laitude : " + Const.latitude);
					
					Log.print("GPS Reading :: ", "============================================");
					Log.print("GPS Reading :: ", "Longitude : " + Const.longitude );
					Log.print("GPS Reading :: ", "Laitude : " + Const.latitude );
					Log.print("GPS Reading :: ", "============================================");
					
					Pref.setValue("IS_LOCATION_AVAILABLE", "1");
					
					mGPSLocationManager.removeUpdates(this);
				}

			} catch (Exception e) {
				Pref.setValue("IS_LOCATION_AVAILABLE", "0");
				Log.error("Main :: onLocationChanged() :: ", e);
			}
		}

		public void onProviderDisabled(String provider) {

			Pref.setValue("IS_LOCATION_AVAILABLE", "0");
			
			if (provider == LocationManager.GPS_PROVIDER) {

				Toast
						.makeText(
								Const.CONTEXT,
								"Wireless networks is disabled.\n\nGo to \"Location and Security Settings\" to enable it.",
								Toast.LENGTH_LONG).show();
				Log.error(this.getClass()+"", "Wireless networks is disabled.");
			}

		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

			if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
				Pref.setValue("IS_LOCATION_AVAILABLE", "0");
				Log.error(this.getClass()+"", "Wireless networks is TEMPORARILY_UNAVAILABLE.");
			} else if (status == LocationProvider.OUT_OF_SERVICE) {
				Pref.setValue("IS_LOCATION_AVAILABLE", "0");
				Log.error(this.getClass()+"", "Wireless networks is OUT_OF_SERVICE.");
			} else if (status == LocationProvider.AVAILABLE) {
				Log.error(this.getClass()+"", "Wireless networks is AVAILABLE.");
			}
		}

	}/* End of Class GPSLocationListener */
}
