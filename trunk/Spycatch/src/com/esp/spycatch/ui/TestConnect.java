package com.esp.spycatch.ui;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.fb.DialogError;
import com.esp.spycatch.fb.Facebook;
import com.esp.spycatch.fb.Facebook.DialogListener;
import com.esp.spycatch.fb.FacebookError;
import com.esp.spycatch.fb.SessionStore;
import com.esp.spycatch.util.Const;

/**
 * This example shows how to connect to Facebook, display authorization dialog and save user's access token
 * and username.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */

public class TestConnect extends Activity {
	private Facebook mFacebook;
	private CheckBox mFacebookBtn;
	private ProgressDialog mProgress;
	
	private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.testconnection);
        
        mFacebookBtn	= (CheckBox) findViewById(R.id.cb_facebook);
        
        mProgress		= new ProgressDialog(this);
        mFacebook		= new Facebook(Const.FACEBOOK_APP_ID);
        
        SessionStore.restore(mFacebook, this);
        
        if (mFacebook.isSessionValid()) {
			mFacebookBtn.setChecked(true);
			
			String name = SessionStore.getName(this);
			name		= (name.equals("")) ? "Unknown" : name;
			
			mFacebookBtn.setText("  Facebook (" + name + ")");
			mFacebookBtn.setTextColor(Color.WHITE);
		}
        
        mFacebookBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onFacebookClick();
			}
		});
        
        ((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(TestConnect.this, TestPost.class));
			}
		});
    }
    
    private void onFacebookClick() {
    	
    	
		if (mFacebook.isSessionValid()) {
			
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setMessage("Delete current Facebook connection?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   fbLogout();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                
			                mFacebookBtn.setChecked(true);
			           }
			       });
			
			final AlertDialog alert = builder.create();
			
			alert.show();
		} else {
			
			
			mFacebookBtn.setChecked(false);
			
			mFacebook.authorize(this, PERMISSIONS, -1, new FbLoginDialogListener());
		}
	}
    
    private final class FbLoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionStore.save(mFacebook, TestConnect.this);
           
            mFacebookBtn.setText("  Facebook (No Name)");
            mFacebookBtn.setChecked(true);
			mFacebookBtn.setTextColor(Color.WHITE);
			 
            getFbName();
        }

        public void onFacebookError(FacebookError error) {
           Toast.makeText(TestConnect.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
           
           mFacebookBtn.setChecked(false);
        }
        
        public void onError(DialogError error) {
        	Toast.makeText(TestConnect.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
        	
        	mFacebookBtn.setChecked(false);
        }

        public void onCancel() {
        	mFacebookBtn.setChecked(false);
        }
    }
    
	private void getFbName() {
		mProgress.setMessage("Finalizing ...");
		mProgress.show();
		
		new Thread() {
			@Override
			public void run() {
		        String name = "";
		        int what = 1;
		        
		        try {
		        	String me = mFacebook.request("me");
		        	
		        	JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
		        	name = jsonObj.getString("name");
		        	what = 0;
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        
		        mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
			}
		}.start();
	}
	
	private void fbLogout() {
		mProgress.setMessage("Disconnecting from Facebook");
		mProgress.show();
			
		new Thread() {
			@Override
			public void run() {
				SessionStore.clear(TestConnect.this);
		        	   
				int what = 1;
					
		        try {
		        	mFacebook.logout(TestConnect.this);
		        		 
		        	what = 0;
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        	
		        mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}
	
	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			
			if (msg.what == 0) {
				String username = (String) msg.obj;
		        username = (username.equals("")) ? "No Name" : username;
		            
		        SessionStore.saveName(username, TestConnect.this);
		        
		        mFacebookBtn.setText("  Facebook (" + username + ")");
		         
		        Toast.makeText(TestConnect.this, "Connected to Facebook as " + username, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TestConnect.this, "Connected to Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			
			if (msg.what == 1) {
				Toast.makeText(TestConnect.this, "Facebook logout failed", Toast.LENGTH_SHORT).show();
			} else {
				mFacebookBtn.setChecked(false);
	        	mFacebookBtn.setText("  Facebook (Not connected)");
	        	mFacebookBtn.setTextColor(Color.GRAY);
	        	   
				Toast.makeText(TestConnect.this, "Disconnected from Facebook", Toast.LENGTH_SHORT).show();
			}
		}
	};
}