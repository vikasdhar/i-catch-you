package com.esp.spycatch.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

public class CustomTimePicker {

	
	private Context contxt$obj;
	private int int$ID;
	private TextView edittxt$Time;
	private int int$Hours,int$Mins;
	public CustomTimePicker(Context contxt$obj,int int$ID,TextView edittxt$Time){
		
		this.contxt$obj = contxt$obj;
		this.int$ID = int$ID;
		this.edittxt$Time = edittxt$Time;
		
		
	}
	public Dialog ShowCustomDialog(){
		
		return onCreateDialog(int$ID);
		
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =new TimePickerDialog.OnTimeSetListener() {
		
			public void onTimeSet(TimePicker view, int hour, int minute) {
				setTime(hour,minute);
			}
			
			
		};
		
	protected void setTime(int hour,int minute){
		
//		edittxt$Time.setText(
//	            new StringBuilder()
//	                    .append(pad(hour)).append(":")
//	                    .append(pad(minute)));
		edittxt$Time.setText(Utils.get12hTo24h("hh:mm a",new StringBuilder().append(pad(hour)).append(":").append(pad(minute)).toString(),"HH:mm"));

	}
	
	public void setCurrentHours(int int$Hours){
		this.int$Hours = int$Hours;
	}
	public void setCurrentMins(int int$Mins){
		this.int$Mins = int$Mins;
	}

	protected Dialog onCreateDialog(int id) {
		
	switch (id) {
	
	case 0:
		return new TimePickerDialog(contxt$obj,mTimeSetListener,this.int$Hours,this.int$Mins,false);
	}
	return null;
	}
	
	private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	
}
