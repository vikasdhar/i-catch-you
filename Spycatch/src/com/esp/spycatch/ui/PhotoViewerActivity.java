package com.esp.spycatch.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Storage;
import com.esp.spycatch.util.Utils;


	
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
		Log.print("[ SIZE ] " + arrListImageBean.size());
		 //Page title
        this.pageTitle = (PageTitle)findViewById(R.id.pageTitle);
        this.pageTitle.init();
        this.pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long.parseLong(Utils.trimExtension(strFileName))));
        
     	
        btn_prv = (Button)findViewById(R.id.btn_prv);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_prv.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        
        
		
		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		mProgressDialog = new ProgressDialog(PhotoViewerActivity.this);
		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		
		
		if(Index == 0){
			
//        	btn_prv.setVisibility(View.GONE);
			btn_prv.setBackgroundResource(R.drawable.btn_prv_disabled);
			btn_prv.setEnabled(true);
        	btn_next.setVisibility(View.VISIBLE);
        	
        }else if(Index > 0 && !(Index == arrListImageBean.size() -1)){
        	btn_next.setBackgroundResource(R.drawable.btn_next_selector);
        	btn_next.setVisibility(View.VISIBLE);
        	btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
        	btn_prv.setVisibility(View.VISIBLE);
        	
        }else if(Index > 0 && Index == arrListImageBean.size() -1){
//        	btn_next.setVisibility(View.GONE);
        	btn_next.setBackgroundResource(R.drawable.btn_next_disabled);
        	btn_next.setEnabled(true);
        	btn_prv.setVisibility(View.VISIBLE);
        }
        
        
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(PhotoViewerActivity.this);
				builder.setMessage("Are you sure you want to Delete?")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				              
				        	  // ViewGroup parent = (ViewGroup) v.getParent();
				          	  // parent.removeView(v);
				        	   dialog.dismiss();
				        	   new RemoveView(Index).execute();
				        	   
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				
				AlertDialog alert = builder.create();
				alert.show();
				
				return false;
			}
		});
		File imgFile = new  File(Const.IMAGE_DIR + "/" + strFileName);
		
		if(imgFile.exists()){
			Bitmap viewBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			imageView.setImageBitmap(viewBitmap);
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			Log.print("[ INDEX ] " + Index);
			if(Index == 0){
//		        	btn_prv.setVisibility(View.GONE);
					btn_prv.setBackgroundResource(R.drawable.btn_prv_disabled);
		        	btn_prv.setEnabled(true);
		        	btn_next.setVisibility(View.VISIBLE);
	        	
	        }else if(Index > 0 && !(Index == arrListImageBean.size() -1)){
	        	btn_next.setBackgroundResource(R.drawable.btn_next_selector);
	        	btn_next.setVisibility(View.VISIBLE);
	        	btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
	        	btn_prv.setVisibility(View.VISIBLE);
	        	
	        }else if(Index > 0 && Index == arrListImageBean.size() -1){
//	        	btn_next.setVisibility(View.GONE);
	        	btn_next.setBackgroundResource(R.drawable.btn_next_disabled);
	        	btn_next.setEnabled(true);
	        	btn_prv.setVisibility(View.VISIBLE);
	        }
			 
			switch (msg.what) 
			{
				case 0:
					mProgressDialog.dismiss();
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
								
								runOnUiThread(new Runnable() 
								{
									public void run() 
									{
										pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long.parseLong(Utils.trimExtension(strFileName))));
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
								
								runOnUiThread(new Runnable() 
								{
									public void run() 
									{
										pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long.parseLong(Utils.trimExtension(strFileName))));
									}
								});

								
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
	
	class RemoveView extends AsyncTask<Void,Void,Integer>{

		private int TempIndex;
		
		public RemoveView(int Index) {
			this.TempIndex = Index;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
			
		}
		@Override
		protected Integer doInBackground(Void... params) {
			int Result = 0;
			ImageBL mBl = new ImageBL();
			if(mBl.Delete(arrListImageBean.get(TempIndex).getFileName()) == 0){
				
				Storage.IsFileAvailableDelete(Const.IMAGE_DIR + "/" + arrListImageBean.get(TempIndex).getFileName());
				Storage.IsFileAvailableDelete(Const.TEMP_IMAGE_PATH + "/" + arrListImageBean.get(TempIndex).getFileName());
				PhotoGallery.arrListImageBean.remove(TempIndex);
				Log.print("[ PhotoGallery ] ", PhotoGallery.arrListImageBean.size()+"");
				arrListImageBean = PhotoGallery.arrListImageBean;
				if(arrListImageBean.size()> 0){
					Index = 0;
					Log.print("[ INDEX ] " + Index);
				}
				else
				Result = -1;	
				
			}else{
				Result = -1;
			}
			
			return Result;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if(result == 0){
					
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
									mHandler.sendMessage(mHandler.obtainMessage(1));
								}
								
						}).start();
					}
					
			
				
			}else{
				Toast.makeText(PhotoViewerActivity.this,"No able to remove!!",Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
