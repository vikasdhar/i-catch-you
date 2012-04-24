package com.esp.spycatch.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.fb.AsyncFacebookRunner;
import com.esp.spycatch.fb.BaseRequestListener;
import com.esp.spycatch.fb.Facebook;
import com.esp.spycatch.fb.SessionStore;
import com.esp.spycatch.util.Const;


/**
 * This example shows how to post status to Facebook wall.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */
public class TestPost extends Activity{
	private Facebook mFacebook;
	private CheckBox mFacebookCb;
	private ProgressDialog mProgress;
	
	private Handler mRunOnUi = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.post);
		
		final EditText reviewEdit = (EditText) findViewById(R.id.revieew);
		mFacebookCb				  = (CheckBox) findViewById(R.id.cb_facebook);
		
		mProgress	= new ProgressDialog(this);
		
		mFacebook 	= new Facebook(Const.FACEBOOK_APP_ID);
		
		SessionStore.restore(mFacebook, this);

		if (mFacebook.isSessionValid()) {
			mFacebookCb.setChecked(true);
				
			String name = SessionStore.getName(this);
			name		= (name.equals("")) ? "Unknown" : name;
				
			mFacebookCb.setText("  Facebook  (" + name + ")");
		}
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String review = reviewEdit.getText().toString();
				
				if (review.equals("")) return;
			
				if (mFacebookCb.isChecked()) postToFacebook(review);
			}
		});
	}
	
	private void postToFacebook(String review) {	
		mProgress.setMessage("Posting ...");
		mProgress.show();
		
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		Bundle params = new Bundle();
    		
		params.putString("message", review);
		params.putString("name", "Dexter");
		params.putString("caption", "londatiga.net");
		params.putString("link", "http://www.londatiga.net");
		params.putString("description", "Dexter, seven years old dachshund who loves to catch cats, eat carrot and krupuk");
		params.putString("picture", "http://twitpic.com/show/thumb/6hqd44");
		
//		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}
//
//	private final class WallPostListener extends BaseRequestListener {
//        public void onComplete(final String response) {
//        	mRunOnUi.post(new Runnable() {
//        		
//        		public void run() {
//        			mProgress.cancel();
//        			
//        			Toast.makeText(TestPost.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
//        		}
//        	});
//        }
//    }
}