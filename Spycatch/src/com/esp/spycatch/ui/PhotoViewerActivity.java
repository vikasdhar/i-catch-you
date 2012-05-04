package com.esp.spycatch.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class PhotoViewerActivity extends Activity implements OnClickListener {

	private ImageView imageView;
	private String strFileName;
	private String strLat,strLong;
	public PageTitle pageTitle;
	public ArrayList<ImageBean> arrListImageBean = PhotoGallery.arrListImageBean;
	private Button btn_prv, btn_next;
	private Button btn_Delete, btn_Share, btn_location;
	private int Index;
	private ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Const.CONTEXT = PhotoViewerActivity.this;
		setContentView(R.layout.photoviewer_activity);

		strLat = getIntent().getExtras().getString("Lat");
		strLong = getIntent().getExtras().getString("Long");
		
		strFileName = getIntent().getExtras().getString("FileName");
		if (!getIntent().getExtras().getString("Index").equals(null))
			Index = Integer.parseInt(getIntent().getExtras().getString("Index"));
		
		Log.print("[ INDEX ] " + Index);
		Log.print("[ SIZE ] " + arrListImageBean.size());
		Log.print("[ Lat ] " + strLat);
		Log.print("[ Long ] " + strLong);
		
		// Page title
		this.pageTitle = (PageTitle) findViewById(R.id.pageTitle);
		this.pageTitle.init();
		this.pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long
				.parseLong(Utils.trimExtension(strFileName))));

		btn_prv = (Button) findViewById(R.id.btn_prv);
		btn_next = (Button) findViewById(R.id.btn_next);
		
		btn_Delete = (Button)findViewById(R.id.btn_Delete);
		btn_Share = (Button)findViewById(R.id.btn_Share);
		btn_location = (Button)findViewById(R.id.btn_location);
		
		
		btn_prv.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		
		btn_Delete.setOnClickListener(this);
		btn_Share.setOnClickListener(this);
		
		btn_location.setOnClickListener(this);

	}

	@Override
	protected void onResume() {

		super.onResume();
		mProgressDialog = new ProgressDialog(PhotoViewerActivity.this);
		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);

		if (Index == 0) {
			// btn_prv.setVisibility(View.GONE);
			btn_prv.setBackgroundResource(R.drawable.btn_prv_disabled);
			btn_prv.setEnabled(true);
			btn_next.setBackgroundResource(R.drawable.btn_next_selector);
			btn_next.setVisibility(View.VISIBLE);

		} else if (Index > 0 && !(Index == arrListImageBean.size() - 1)) {
			btn_next.setBackgroundResource(R.drawable.btn_next_selector);
			btn_next.setVisibility(View.VISIBLE);
			btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
			btn_prv.setVisibility(View.VISIBLE);

		} else if (Index > 0 && Index == arrListImageBean.size() - 1) {
			// btn_next.setVisibility(View.GONE);
			btn_next.setBackgroundResource(R.drawable.btn_next_disabled);
			btn_next.setEnabled(true);
			btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
			btn_prv.setVisibility(View.VISIBLE);
		}

		imageView = (ImageView) findViewById(R.id.imageView);
		imageView.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				return false;
			}
		});
		File imgFile = new File(Const.IMAGE_DIR + "/" + strFileName);

		if (imgFile.exists()) {
			Bitmap viewBitmap = BitmapFactory.decodeFile(imgFile
					.getAbsolutePath());
			imageView.setImageBitmap(viewBitmap);
		}

		if(strLat == null || strLat.equals("0") || strLat.equals("null")){
			
			btn_location.setBackgroundResource(R.drawable.btn_map_disabled);
			btn_location.setOnClickListener(null);
		}
		
		//registerForContextMenu(imageView);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		menu.setHeaderTitle("Choose an Option");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.Share_photo:
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,"SpyCatch");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "SpyCatch");
				startActivity(Intent.createChooser(shareIntent, "Share SpyCatch"));
			break;
		case R.id.Delete_photo:
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PhotoViewerActivity.this);
			builder.setMessage("Are you sure you want to Delete?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									// ViewGroup parent = (ViewGroup)
									// v.getParent();
									// parent.removeView(v);
									dialog.dismiss();
									new RemoveView(Index).execute();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
			break;
		case R.id.showlocation:
			
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			Log.print("[ INDEX ] " + Index);
			
			
			if(strLat == null || strLat.equals("0") || strLat.equals("null")){
				
				btn_location.setBackgroundResource(R.drawable.btn_map_disabled);
				btn_location.setOnClickListener(null);
				
			}else{
				
				btn_location.setBackgroundResource(R.drawable.btn_map);
				btn_location.setOnClickListener(PhotoViewerActivity.this);
			}
			
			
			if (Index == 0) {
				// btn_prv.setVisibility(View.GONE);
				btn_prv.setBackgroundResource(R.drawable.btn_prv_disabled);
				btn_prv.setEnabled(true);
				btn_next.setBackgroundResource(R.drawable.btn_next_selector);
				btn_next.setVisibility(View.VISIBLE);

			} else if (Index > 0 && !(Index == arrListImageBean.size() - 1)) {
				btn_next.setBackgroundResource(R.drawable.btn_next_selector);
				btn_next.setVisibility(View.VISIBLE);
				btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
				btn_prv.setVisibility(View.VISIBLE);

			} else if (Index > 0 && Index == arrListImageBean.size() - 1) {
				// btn_next.setVisibility(View.GONE);
				btn_next.setBackgroundResource(R.drawable.btn_next_disabled);
				btn_next.setEnabled(true);
				btn_prv.setBackgroundResource(R.drawable.btn_prv_selector);
				btn_prv.setVisibility(View.VISIBLE);
			}

			switch (msg.what) {
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

			if (arrListImageBean != null && Index < arrListImageBean.size() - 1) {
				Index++;
				// Toast.makeText(PhotoViewerActivity.this,Index+"",Toast.LENGTH_SHORT).show();
				final File imgFile = new File(Const.IMAGE_DIR + "/"
						+ arrListImageBean.get(Index).getFileName());
				if (imgFile.exists()) {
					Log.print("[ File ] " + Const.IMAGE_DIR + "/"
							+ arrListImageBean.get(Index).getFileName());
					mProgressDialog.show();
					new Thread(new Runnable() 
					{
						
						public void run() 
						{
							
							final Bitmap viewBitmap = BitmapFactory
									.decodeFile(imgFile.getAbsolutePath());
							imageView.post(new Runnable() {
								public void run() {
									imageView.setImageBitmap(viewBitmap);
								}
							});

							runOnUiThread(new Runnable() {
								public void run() {
									pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long.parseLong(Utils
											.trimExtension(arrListImageBean.get(Index).getFileName()))));
									strLat = arrListImageBean.get(Index).getLat();
									strLat = arrListImageBean.get(Index).getLon();
									Log.print("[ Lat ] " + strLat);
									Log.print("[ Long ] " + strLong);
									
									
									
								}
							});
							
							mHandler.sendMessage(mHandler.obtainMessage(0));
						}

					}).start();

				}
			}

			break;

		case R.id.btn_prv:

			if (arrListImageBean != null && Index > 0) {
				Index--;
				// Toast.makeText(PhotoViewerActivity.this,Index+"",Toast.LENGTH_SHORT).show();
				final File imgFile = new File(Const.IMAGE_DIR + "/"
						+ arrListImageBean.get(Index).getFileName());

				if (imgFile.exists()) {
					Log.print("[ File ] " + Const.IMAGE_DIR + "/"
							+ arrListImageBean.get(Index).getFileName());
					mProgressDialog.show();
					new Thread(new Runnable() {
						public void run() {
							final Bitmap viewBitmap = BitmapFactory
									.decodeFile(imgFile.getAbsolutePath());
							imageView.post(new Runnable() {
								public void run() {
									imageView.setImageBitmap(viewBitmap);
								}
							});

							runOnUiThread(new Runnable() {
								public void run() {
									pageTitle.txtPageTitle.setText(Utils.getMilisecondToDate(Long.parseLong(Utils
											.trimExtension(arrListImageBean.get(Index).getFileName()))));
									strLat = arrListImageBean.get(Index).getLat();
									strLat = arrListImageBean.get(Index).getLon();
									Log.print("[ Lat ] " + strLat);
									Log.print("[ Long ] " + strLong);
									
									
								}
							});

							mHandler.sendMessage(mHandler.obtainMessage(1));
						}

					}).start();
				}
			}

			break;
		case R.id.btn_Delete:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PhotoViewerActivity.this);
			builder.setMessage("Are you sure you want to Delete?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									new RemoveView(Index).execute();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			AlertDialog alert = builder.create();
			alert.show();
			break;
		case R.id.btn_Share:
			
			new Thread(new Runnable() {
				
				public void run() {
					
					Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
					shareIntent.setType("image/*");
					shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					shareIntent.putExtra(Intent.EXTRA_SUBJECT,"SpyCatch");
					shareIntent.putExtra(Intent.EXTRA_TEXT, "SpyCatch");
					if(Utils.ShareImage(arrListImageBean.get(Index).getFileName()) == 1){
						
						Uri uri = Uri.fromFile(new File(Const.TEMP_IMAGE_DIR + "/"+ Utils.trimExtension(arrListImageBean.get(Index).getFileName())+".png"));
						shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
						startActivity(Intent.createChooser(shareIntent, "Share SpyCatch"));
					}
				}
			}).start();
			
			
			break;
		case R.id.btn_location:
			Intent mIntent = new Intent(PhotoViewerActivity.this,ShowLocation.class);
			mIntent.putExtra("Lat",strLat);
			mIntent.putExtra("Long",strLong);
			startActivity(mIntent);
			break;
			
		default:
			break;
		}
	}

	class RemoveView extends AsyncTask<Void, Void, Integer> {

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
			if (mBl.Delete(arrListImageBean.get(TempIndex).getFileName()) == 0) {

				Storage.IsFileAvailableDelete(Const.IMAGE_DIR + "/"
						+ arrListImageBean.get(TempIndex).getFileName());
				Storage.IsFileAvailableDelete(Const.TEMP_IMAGE_PATH + "/"
						+ arrListImageBean.get(TempIndex).getFileName());
				PhotoGallery.arrListImageBean.remove(TempIndex);
				Log.print("[ PhotoGallery ] ",
						PhotoGallery.arrListImageBean.size() + "");
				arrListImageBean = PhotoGallery.arrListImageBean;
				if (arrListImageBean.size() > 0) {
					Index = 0;
					Log.print("[ INDEX ] " + Index);
				} else
					Result = -1;

			} else {
				Result = -1;
			}

			return Result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result == 0) {

				// Toast.makeText(PhotoViewerActivity.this,Index+"",Toast.LENGTH_SHORT).show();
				final File imgFile = new File(Const.IMAGE_DIR + "/"
						+ arrListImageBean.get(Index).getFileName());
				if (imgFile.exists()) {
					Log.print("[ File ] " + Const.IMAGE_DIR + "/"
							+ arrListImageBean.get(Index).getFileName());
					mProgressDialog.show();
					new Thread(new Runnable() {
						public void run() {
							final Bitmap viewBitmap = BitmapFactory
									.decodeFile(imgFile.getAbsolutePath());
							imageView.post(new Runnable() {
								public void run() {
									imageView.setImageBitmap(viewBitmap);
								}
							});
							mHandler.sendMessage(mHandler.obtainMessage(1));
						}

					}).start();
				}

			} else {
				Toast.makeText(PhotoViewerActivity.this, "No able to remove!!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	
}
