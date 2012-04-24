package com.esp.spycatch.ui;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.CustomTimePicker;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class MmsNotification extends Activity implements OnClickListener,OnFocusChangeListener{

	public PageTitle pageTitle;
	private String strTitle;
	
	private EditText editTextCustom;
	
	private EditText editText_SendTo;
	private TextView editText_Weekdays;
	private TextView editText_FromTime;
	private TextView editText_ToTime;

	private boolean[] boolDays = new boolean[7];

	private Button btnDone, btnCancel;
	
	private LinearLayout view_MmsSupport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mms_notification);

		// Intent Filter
		strTitle = getIntent().getStringExtra("Title");

		if (strTitle.equals("MMS support")) {

		}

		// Page title
		this.pageTitle = (PageTitle) findViewById(R.id.pageTitle);
		this.pageTitle.init();
		this.pageTitle.txtPageTitle.setText(strTitle);

		initialization();

	}

	private void initialization() {

		editText_SendTo = (EditText) findViewById(R.id.editText_SendTo);
		editText_SendTo.setOnFocusChangeListener(this);
		editText_SendTo.setText(Pref.getValue("MMS_SEND_TO", ""));
		
		editText_Weekdays = (TextView) findViewById(R.id.editText_Weekdays);
		editText_Weekdays.setText(Pref.getValue("MMS_WEEK_DAYS", ""));
		
		editText_FromTime = (TextView) findViewById(R.id.editText_FromTime);
		editText_FromTime.setText(Pref.getValue("MMS_FROM_TIME", ""));
		
		editText_ToTime = (TextView) findViewById(R.id.editText_ToTime);
		editText_ToTime.setText(Pref.getValue("MMS_TO_TIME", ""));
		
		btnDone = (Button) findViewById(R.id.btn_Done);
		btnDone.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		btnCancel.setOnClickListener(this);

		editText_Weekdays.setOnClickListener(this);
		editText_FromTime.setOnClickListener(this);
		editText_ToTime.setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.btn_Done:
			
			if(!editText_Weekdays.getText().equals("") || editText_Weekdays.getText().length()!= 0){
				
				if(!editText_FromTime.getText().equals("") || editText_FromTime.getText().length() != 0){
					
					if(!editText_ToTime.getText().equals("") || editText_ToTime.getText().length() != 0){
						
						
						Pref.setValue("MMS_WEEK_DAYS",editText_Weekdays.getText().toString());
						Pref.setValue("MMS_SEND_TO", editText_SendTo.getText().toString());
						Pref.setValue("MMS_FROM_TIME", editText_FromTime.getText().toString());
						Pref.setValue("MMS_TO_TIME", editText_ToTime.getText().toString());
						
						setResult(RESULT_OK);
						finish();
						
					}else{
						
						//to time 
						Toast.makeText(getApplicationContext(),"Please select to time ", Toast.LENGTH_LONG).show();
					}
					
				}else{
					
					//From Time
					Toast.makeText(getApplicationContext(),"Please select from time ", Toast.LENGTH_LONG).show();
				}
				
			}else{
				// Week Days 
				Toast.makeText(getApplicationContext(),"Please select weekdays", Toast.LENGTH_LONG).show();
			}
			
			break;

		case R.id.btn_Cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.editText_Weekdays:
			
			AlertDialog alertDays = new AlertDialog.Builder(
					MmsNotification.this)
					.setMultiChoiceItems(Const.array_WeekDays,getSelectDays(Pref.getValue("MMS_WEEK_DAYS","")),
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int position, boolean isChecked) {

									boolDays[position] = isChecked;
								}
							})
					.setPositiveButton("Done",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									editText_Weekdays.setText(Utils
											.getStringDays(boolDays));
//									Pref.setValue("MMS_WEEK_DAYS",
//											Utils.getStringDays(boolDays));
									dialog.dismiss();

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							}).create();
			alertDays.setTitle("Select Weekdays");
			alertDays.show();
			
			
			
			
			break;
		case R.id.editText_FromTime:
			
			CustomTimePicker custPickerFromTime = new CustomTimePicker(MmsNotification.this,0,editText_FromTime);
			if(!editText_FromTime.getText().toString().equals("")){
				
				String tempTime[] = Utils.getSplitTime(editText_FromTime.getText().toString());
				if(tempTime != null){
					
					custPickerFromTime.setCurrentHours(Integer.parseInt(tempTime[0]));
					custPickerFromTime.setCurrentMins(Integer.parseInt(tempTime[1]));
				}
					
				
			}else{
				
				
				final Calendar c = Calendar.getInstance();
				custPickerFromTime.setCurrentHours(c.get(Calendar.HOUR_OF_DAY));
				custPickerFromTime.setCurrentMins(c.get(Calendar.MINUTE));

			}
			
			custPickerFromTime.ShowCustomDialog().show();
			
			
			break;
		
		case R.id.editText_ToTime:
			
			
			CustomTimePicker custPickerToTime = new CustomTimePicker(MmsNotification.this,0,editText_ToTime);
			if(!editText_ToTime.getText().toString().equals("")){
				
				String tempTime[] = Utils.getSplitTime(editText_ToTime.getText().toString());
				if(tempTime != null){
					
					custPickerToTime.setCurrentHours(Integer.parseInt(tempTime[0]));
					custPickerToTime.setCurrentMins(Integer.parseInt(tempTime[1]));
				}
					
				
			}else{
				
				
				final Calendar c = Calendar.getInstance();
				custPickerToTime.setCurrentHours(c.get(Calendar.HOUR_OF_DAY));
				custPickerToTime.setCurrentMins(c.get(Calendar.MINUTE));

			}
			
			custPickerToTime.ShowCustomDialog().show();
			
			
			break;
		
		default:
			break;
		}
	}

	private boolean[] getSelectDays(String strTrack) {
		
		String[] obj = strTrack.split(",");
		for (int index = 0; index < Const.array_WeekDays.length; index++) {

			for (int i = 0; i < obj.length; i++) {
				if (Const.array_WeekDays[index].equals(obj[i])) {
					boolDays[index] = true;
				}
			}
		}
		return boolDays;

	}

	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus)
			editTextCustom = (EditText)v;
	}
	@Override
	protected void onResume() {
		super.onResume();
		
		view_MmsSupport = (LinearLayout)findViewById(R.id.view_MmsSupport);
		view_MmsSupport.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				if(v == view_MmsSupport){
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							editTextCustom.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});
		
	}
}
