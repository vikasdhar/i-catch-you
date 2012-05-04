package com.esp.spycatch.ui;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Pref;

public class MmsNotification extends Activity implements OnClickListener,OnFocusChangeListener{

	public PageTitle pageTitle;
	private String strTitle;
	private EditText editTextCustom;
	private EditText editText_SendTo;


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
		
		
		btnDone = (Button) findViewById(R.id.btn_Done);
		btnDone.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		btnCancel.setOnClickListener(this);

		
	}

	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.btn_Done:
			
			if(!editText_SendTo.getText().toString().equals("") || editText_SendTo.getText().toString().length()!= 0){
				
				Pref.setValue("MMS_SEND_TO", editText_SendTo.getText().toString());
				setResult(RESULT_OK);
				finish();
				
			}else{
				Toast.makeText(getApplicationContext(),"Please enter send details", Toast.LENGTH_LONG).show();
			}
			
			break;

		case R.id.btn_Cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		
		default:
			break;
		}
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
