package com.esp.spycatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Pref;

public class SetPasswordActivity extends Activity implements OnClickListener{
	
	public PageTitle pageTitle;
	
	private String strTitle;
	
	private EditText editText_Password;
	
	private Button btnDone,btnCancel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpassword_activity);
		
		// Intent Filter
		strTitle = getIntent().getStringExtra("Title");
		
		  //Page title
        this.pageTitle = (PageTitle)findViewById(R.id.pageTitle);
        this.pageTitle.init();
        this.pageTitle.txtPageTitle.setText(strTitle);
        
        initalization(strTitle);
        
	}
	private void initalization(String strTitle){

		if (strTitle != null && strTitle.equals("Password")
				&& !strTitle.equals("")) {
			editText_Password = (EditText)findViewById(R.id.editText_Password);
			editText_Password.setText(Pref.getValue("PASSWORD", null));
		}
		
		btnDone = (Button)findViewById(R.id.btn_Done);
		btnDone.setOnClickListener(this);
		btnCancel = (Button)findViewById(R.id.btn_Cancel);
		btnCancel.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		
		int viewID = v.getId();
		switch (viewID) {
		
		case R.id.btn_Done:
			
			if (strTitle.equals("Password")) {

				
				if (!editText_Password.getText().toString().equals("")
						&& editText_Password != null) {
					
					Intent mData = new Intent();
					mData.putExtra("Password", editText_Password.getText()
							.toString().trim());
					setResult(RESULT_OK,mData);
					finish();
				}else{
					Toast.makeText(getApplicationContext(),"please enter password",Toast.LENGTH_LONG).show();
				}
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
	
	
	
}
