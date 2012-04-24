package com.esp.spycatch.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;


	
public class PhotoViewerActivity extends Activity implements OnClickListener{
	
	private  ImageView imageView;
	private String strFileName;
	public PageTitle pageTitle;
	
	public ArrayList<ImageBean> arrListImageBean = PhotoGallery.arrListImageBean;
	
	private Button btn_prv,btn_next;
	
	private int Index;
	
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Const.CONTEXT = PhotoViewerActivity.this;
		setContentView(R.layout.photoviewer_activity);
		
		strFileName = getIntent().getExtras().getString("FileName");
		if(!getIntent().getExtras().getString("Index").equals(null)) 
		Index = Integer.parseInt(getIntent().getExtras().getString("Index"));
		Log.print("[ INDEX ] " + Index);
		
		 //Page title
        this.pageTitle = (PageTitle)findViewById(R.id.pageTitle);
        this.pageTitle.init();
        this.pageTitle.txtPageTitle.setText(strFileName);
        
     	mProgressDialog = new ProgressDialog(PhotoViewerActivity.this);
		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
        
        
        btn_prv = (Button)findViewById(R.id.btn_prv);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_prv.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        
        if(Index > 0){
        	btn_prv.setVisibility(View.VISIBLE);
        	btn_next.setVisibility(View.VISIBLE);
        }else if(Index == 0){
        	btn_prv.setVisibility(View.GONE);
        	btn_next.setVisibility(View.VISIBLE);
        }
        
        
        imageView = (ImageView)findViewById(R.id.imageView);
		File imgFile = new  File(Const.IMAGE_DIR + "/" + strFileName);
		
		if(imgFile.exists()){
			Bitmap viewBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			imageView.setImageBitmap(viewBitmap);
		}
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			
			switch (msg.what) {
			case 0:
				mProgressDialog.dismiss();
				if(Index > 0){
					btn_prv.setVisibility(View.VISIBLE);
				}else if(Index == arrListImageBean.size() -1){
					btn_next.setVisibility(View.GONE);
				}
				break;
			case 1:
				mProgressDialog.dismiss();
				break;
			}
		}
	};
	

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_next:
			
			if(arrListImageBean != null && Index < arrListImageBean.size()-1)
			{
				Index++;
				Toast.makeText(PhotoViewerActivity.this,Index+"",Toast.LENGTH_SHORT).show();
				final File imgFile = new File(Const.IMAGE_DIR + "/"+ arrListImageBean.get(Index).getFileName());
				if (imgFile.exists()) 
				{
					Log.print("[ File ] " + Const.IMAGE_DIR + "/"+ arrListImageBean.get(Index).getFileName());
					mProgressDialog.show();
					new Thread(new Runnable() 
					{
							public void run() 
							{
								final Bitmap viewBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
								imageView.post(new Runnable() 
								{
									public void run() 
									{
										imageView.setImageBitmap(viewBitmap);
									}
								});
								
								mHandler.sendMessage(mHandler.obtainMessage(0));
							}
							
					}).start();
					
					
				}
			}
			
			break;
			
		case R.id.btn_prv:
			
			
			if (arrListImageBean != null && Index > 0)
			{
				Index--;
				Toast.makeText(PhotoViewerActivity.this,Index+"",Toast.LENGTH_SHORT).show();
				final File imgFile = new File(Const.IMAGE_DIR + "/"+ arrListImageBean.get(Index).getFileName());
				
				if (imgFile.exists()) 
				{	
					Log.print("[ File ] " + Const.IMAGE_DIR + "/"+ arrListImageBean.get(Index).getFileName());
					mProgressDialog.show();
					new Thread(new Runnable() 
					{
							public void run() 
							{
								final Bitmap viewBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
								imageView.post(new Runnable() 
								{
									public void run() 
									{
										imageView.setImageBitmap(viewBitmap);
									}
								});
								
								if(Index == 0){
									btn_prv.setVisibility(View.GONE);
								}
								
								mHandler.sendMessage(mHandler.obtainMessage(1));
							}
							
					}).start();
				}
			}
			
			break;
	
		default:
			break;
		}
	}
	
}
