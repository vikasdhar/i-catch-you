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
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Pref;

public class Login extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	private Button btn_login_go;
	private EditText editview_login_Password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Const.CONTEXT = this;
        btn_login_go = (Button)findViewById(R.id.btn_login_go);
        btn_login_go.setOnClickListener(this);
        
        editview_login_Password = (EditText)findViewById(R.id.editview_login_Password);
        
    }
    
    
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_go:
			
			if (!editview_login_Password.getText().toString().equals("")
					&& editview_login_Password != null) {

				if (Pref.getValue("PASSWORD", null).equals(
						editview_login_Password.getText().toString())) {

					startActivity(new Intent(getApplicationContext(),
							Dashboard.class));
					finish();
				}else{
					Toast.makeText(getApplicationContext(),"Please enter valid password !!",Toast.LENGTH_LONG).show();
				}
				
			}else{
				
				Toast.makeText(getApplicationContext(),"Please enter password !!",Toast.LENGTH_LONG).show();
			}
				
				
			break;

		default:
			break;
		}
	}
    
}
