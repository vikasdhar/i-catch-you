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
import android.widget.TextView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class EmailNotification extends Activity implements OnClickListener,
		OnFocusChangeListener {

	public PageTitle pageTitle;
	private String strTitle;

	private EditText editTextCustom;

	private TextView txtView_Email;
	private EditText editText_Email;
	private EditText editText_Password;
	private EditText editText_SendTo;

	private Button btnDone, btnCancel;
	private LinearLayout view_EmailSupport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emailnotification);

		// Intent Filter
		strTitle = getIntent().getStringExtra("Title");

		if (strTitle.equals("Email support")) {

		}

		// Page title
		this.pageTitle = (PageTitle) findViewById(R.id.pageTitle);
		this.pageTitle.init();
		this.pageTitle.txtPageTitle.setText(strTitle);

		initialization();
	}

	private void initialization() {

		editText_Email = (EditText) findViewById(R.id.editText_Email);
		editText_Email.setText(Pref.getValue("EMAIL_ADDRESS",""));
		editText_Email.setOnFocusChangeListener(this);
		
		editText_Password = (EditText)findViewById(R.id.editText_Password);
		editText_Password.setText(Pref.getValue("EMAIL_PASSWORD",""));
		editText_Password.setOnFocusChangeListener(this);
		
		editText_SendTo = (EditText) findViewById(R.id.editText_SendTo);
		editText_SendTo.setOnFocusChangeListener(this);
		editText_SendTo.setText(Pref.getValue("EMAIL_SEND_TO",""));
		
	
		
		txtView_Email = (TextView) findViewById(R.id.txtView_Email);
		txtView_Email.setText("From Email (gmail only)");

		btnDone = (Button) findViewById(R.id.btn_Done);
		btnDone.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btn_Cancel);
		btnCancel.setOnClickListener(this);

		
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_Done:

			if (Utils.validation(
					(LinearLayout) findViewById(R.id.view_EmailSupport),
					EmailNotification.this)) {

				if (Utils.checkEmail(editText_Email.getText().toString())) {

					if (Utils.checkEmail(editText_SendTo.getText().toString())) {

						Pref.setValue("EMAIL_ADDRESS", editText_Email.getText()
								.toString());
						Pref.setValue("EMAIL_PASSWORD", editText_Password
								.getText().toString());
						Pref.setValue("EMAIL_SEND_TO", editText_SendTo
								.getText().toString());
						setResult(RESULT_OK);
						finish();

					} else {

						Toast.makeText(getApplicationContext(),
								"Please enter valid send to email",
								Toast.LENGTH_LONG).show();
					}
				} else {

					Toast.makeText(getApplicationContext(),
							"Please enter valid from email", Toast.LENGTH_LONG)
							.show();
				}
			} else {

				Toast.makeText(getApplicationContext(), "Please fill details",
						Toast.LENGTH_LONG).show();
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

/*	private boolean[] getSelectDays(String strTrack) {
		
		String[] obj = strTrack.split(",");
		for (int index = 0; index < Const.array_WeekDays.length; index++) {

			for (int i = 0; i < obj.length; i++) {
				if (Const.array_WeekDays[index].equals(obj[i])) {
					boolDays[index] = true;
				}
			}
		}
		return boolDays;

	}*/

	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus)
			editTextCustom = (EditText) v;
	}

	@Override
	protected void onResume() {
		super.onResume();
		view_EmailSupport = (LinearLayout) findViewById(R.id.view_EmailSupport);
		view_EmailSupport.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (v == view_EmailSupport) {
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
