package com.esp.spycatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.esp.spycatch.R;
import com.esp.spycatch.util.Pref;

public class LocationAlert extends Activity implements OnClickListener{
	
	private Button btnIgnor,btnsetLocation;
	private CheckBox cbox_showagain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationalert);
		btnIgnor = (Button)findViewById(R.id.btn_Ignore);
		btnsetLocation = (Button)findViewById(R.id.btn_setLocation);
		
		btnIgnor.setOnClickListener(this);
		btnsetLocation.setOnClickListener(this);
		
		cbox_showagain =(CheckBox)findViewById(R.id.cbox_showdialog);
		cbox_showagain.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Pref.setValue("IS_DIALOG_SHOW", "0");
					finish();
				}else{
					Pref.setValue("IS_DIALOG_SHOW", "1");
					finish();
				}
			}
		});
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_Ignore:
			finish();
			break;
		case R.id.btn_setLocation:
			Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
