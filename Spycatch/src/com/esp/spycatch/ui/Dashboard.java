package com.esp.spycatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.ActionItem;
import com.esp.spycatch.util.Const;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class Dashboard extends Activity implements OnClickListener {

	private ActionItem actionGallery, actionAbout, actionSettings;
	private Button btn_dashboard_exit,btn_dashboard_rating;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		Const.CONTEXT = this;

		this.actionGallery = (ActionItem)findViewById(R.id.actionGallery);
		this.actionGallery.init(this,"Gallery", R.drawable.gallery);
		this.actionGallery.setOnClickListener(this);
		
		this.actionSettings = (ActionItem)findViewById(R.id.actionSettings);
		this.actionSettings.init(this,"Settings", R.drawable.settings);
		this.actionSettings.setOnClickListener(this);
		
		this.actionAbout = (ActionItem)findViewById(R.id.actionAbout);
		this.actionAbout.init(this,"About", R.drawable.about);
		this.actionAbout.setOnClickListener(this);
				
		// Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());    
		
	    btn_dashboard_exit = (Button)findViewById(R.id.btn_dashboard_exit);
	    btn_dashboard_rating = (Button)findViewById(R.id.btn_dashboard_rating);
	    btn_dashboard_exit.setOnClickListener(this);
	    btn_dashboard_rating.setOnClickListener(this);
	    
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionGallery:
			Intent mItent = new Intent(getApplicationContext(),PhotoGallery.class);
			mItent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			mItent.putExtra("PAGENO","1");
			startActivity(mItent);
			break;
		case R.id.actionSettings:
			startActivity(new Intent(getApplicationContext(),Settings.class));
			break;
		case R.id.actionAbout:
			startActivity(new Intent(getApplicationContext(),About.class));
			break;
		case R.id.btn_dashboard_exit:
			finish();
			break;
		case R.id.btn_dashboard_rating:
			
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(Const.ANDROID_MARKET_LINK));
			// if activity already in stack then no need to reload again
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}

}
