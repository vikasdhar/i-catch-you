package com.esp.spycatch.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Storage;
import com.esp.spycatch.util.Utils;

public class PhotoGallery extends Activity {
	
	//public ImageList imageList;
	public LinearLayout imageList;
	public PageTitle pageTitle; 
	public static boolean doGenerateUI = true;
	
	public int intPageNo;
	public int NumberOfIteam;
	
	public static ArrayList<ImageBean> arrListImageBean = null;
	
	public ArrayList<ImageBean> tempArrayList = null;
	
	public ProgressDialog mProgressDialog;
	
	private Display display = null;
	private int cols = 0; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_gallery);
        Const.CONTEXT = this;
        
        //Page title
        this.pageTitle = (PageTitle)findViewById(R.id.pageTitle);
        this.pageTitle.init();
        this.pageTitle.txtPageTitle.setText("Gallery");

        
        this.imageList = (LinearLayout)findViewById(R.id.imageList);        
       
        mProgressDialog = new ProgressDialog(PhotoGallery.this);
        mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
        
    
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		Const.CONTEXT = this;
    	intPageNo = Integer.parseInt(getIntent().getExtras().getString("PAGENO"));
    	Log.print("","[ PAGE WATCH ] " + intPageNo);
    	
    	// TODO Remove all view
    	this.imageList.removeAllViews();
    	
    	//TODO Get Number of Column
    	display = ((WindowManager)Const.CONTEXT.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//TODO number of images horizontally
		cols = (display.getWidth()-10)/110;
		NumberOfIteam = cols * 3;
		Log.print("Cols : ", cols+ " ,Item PerPage " + NumberOfIteam);
		
    	
    	
    	// TODO If arrayList Not Null Then clear
    	
    	if (arrListImageBean != null) {
			if(!arrListImageBean.isEmpty()){
				arrListImageBean.clear();
				arrListImageBean = null;
			}else{
				arrListImageBean = null;
			}
		}
		
		// Get image
		ImageBL mImageBL = new ImageBL();
		
		if(arrListImageBean == null){
			arrListImageBean = mImageBL.Page_List(intPageNo,NumberOfIteam);
			Log.print("ARRAYLIST : ", arrListImageBean.size()+ "");
		}
		
		//TODO First Time Pass Main ArrayList
		
    	this.generateUI(arrListImageBean);
    	
    	final View Moreview = LayoutInflater.from(Const.CONTEXT).inflate(R.layout.list_image_footer,null,true);
    	Moreview.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//TODO Next Page No 
				AsyncTask<Void, Void, Integer> TaskMore = new AsyncTask<Void, Void, Integer>(){
					
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						mProgressDialog.show();
						
					}
					@Override
					protected Integer doInBackground(Void... params) {
						int Result = 0;
						
						intPageNo ++;
						ImageBL mImageBL = new ImageBL();
						
						// TODO Add To TempArrayList
						tempArrayList = mImageBL.Page_List(intPageNo,NumberOfIteam);
						Log.print("Temp Array ", "Size" + tempArrayList.size());
						if (tempArrayList != null && tempArrayList.size() > 0)
						{
							for(ImageBean mBean : tempArrayList){
								Log.print("TEMP ITEM", mBean.getFileName());
								//TODO Add Item on Main ArrayList
								arrListImageBean.add(mBean);
							}
							runOnUiThread(new Runnable() {
								
								public void run() {
									
									//TODO Remove View If Visible
									imageList.removeView(Moreview);
									generateUI(tempArrayList);
								}
							});
						}
						else
						{
							
							Result = -1;
						}
						
						
						return Result;
					}
					@Override
					protected void onPostExecute(Integer result) {
						super.onPostExecute(result);
						mProgressDialog.dismiss();
						Log.print("More Image", "Result" + result);
						if(result == 0){
							
							// TODO To add More View at End
							ImageBL mImageBL = new ImageBL();
							if(mImageBL.GetNofImages()> arrListImageBean.size())
							{
								Log.print("Main Array ", "Size " + arrListImageBean.size());
								imageList.addView(Moreview);
							}
							
						}
						else if(result == -1){
							imageList.removeView(Moreview);
						}
					}
				};
				
				TaskMore.execute();
			}
		});
    	
    	if(mImageBL.GetNofImages()> arrListImageBean.size())
    	this.imageList.addView(Moreview);
    	
    	
	}

    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
	
    public void generateUI(ArrayList<ImageBean> objArryList){
    	View view = null;
    	LinearLayout linearLayout = null;
    	LayoutParams lparams = null;
    	ImageView image;
    	TextView imagePath;
    	
    	Display display = null;
    	int cols = 0; 
    	//File imageDir = null;
    	String files[] = null;
    	
    	boolean doAddLinearLayout = false;
    	
    	if (PhotoGallery.doGenerateUI){
    		
    		//TODO get width of the screen
    		display = ((WindowManager)Const.CONTEXT.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    		
    		//TODO number of images horizontally
    		cols = (display.getWidth()-10)/110;
    		
    		Log.print("Cols : ", cols+ "");
    		
    		
    		files = Utils.arrListToArray(objArryList);
    		
//    		imageDir = new File(Const.THUMBNAIL_DIR);
//    		files = imageDir.list();
//    	
			
    		if (files.length != 0){
    			lparams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    			
    			//TODO Create new linear layout
    			linearLayout = new LinearLayout(Const.CONTEXT);
    			linearLayout.setLayoutParams(lparams);
    			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    			linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    		}
    		
    		for (int i=0; i<files.length; i++){
    			
    			Log.print("File : ", i + " of " + files.length + " :: " +  files[i]);
    			
    			final int index = i;
    			
    			view = LayoutInflater.from(Const.CONTEXT).inflate(R.layout.image_container,null,true);
    			view.setTag("0");
    			view.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						if(v.getTag().equals("1"))
						{
							v.setTag("0");
							return;
						}
						//Toast.makeText(PhotoGallery.this,arrListImageBean.get(index).getFileName(),//Toast.LENGTH_SHORT).show();
						Intent viewIntent = new Intent(PhotoGallery.this,PhotoViewerActivity.class);
						viewIntent.putExtra("FileName",arrListImageBean.get(index).getFileName());
						viewIntent.putExtra("Index",index+"");
						viewIntent.putExtra("Lat",arrListImageBean.get(index).getLat());
						viewIntent.putExtra("Long",arrListImageBean.get(index).getLon());
						//viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivityForResult(viewIntent,100);
						
					}
				});
    			
    			view.setOnLongClickListener(new OnLongClickListener() {
					
					public boolean onLongClick(final View v) {
						v.setTag("1");
						
						AlertDialog.Builder builder = new AlertDialog.Builder(PhotoGallery.this);
						builder.setMessage("Are you sure you want to Delete?")
						       .setCancelable(false)
						       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						           public void onClick(DialogInterface dialog, int id) {
						              
						        	  // ViewGroup parent = (ViewGroup) v.getParent();
						          	  // parent.removeView(v);
						        	   dialog.dismiss();
						        	   new RemoveView(index).execute();
						        	   
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
    			
    			image = (ImageView)view.findViewById(R.id.image);
    			image.setLayoutParams(lparams);
    			
    			new LoadImage(files[i], image);
    			
    			imagePath = (TextView)view.findViewById(R.id.imagePath);
    			imagePath.setText(files[i]);
    			
    			Log.print("Get image and text view");
    			
    			linearLayout.addView(view);
    			
    			doAddLinearLayout = true;
    			
    			if ( ((i+1)%cols) == 0){
    				Log.print("going to add layout");
    				this.imageList.addView(linearLayout);
    				Log.print("layout added");
    				
    				doAddLinearLayout = false;
    				
    				//Create new linear layout
        			linearLayout = new LinearLayout(Const.CONTEXT);
        			linearLayout.setLayoutParams(lparams);
        			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        			linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        			
        			Log.print("new layout is generated");
    			}
    		}
    		
    		Log.print("Done : : ", "" );
    		
    		if (doAddLinearLayout){
    			this.imageList.addView(linearLayout);
    		}
    	}
    	
    	//release
    	display = null;
    	view= null;
    	//imageDir = null;
    	files = null;
    }
    
    private class LoadImage{
    	
		public LoadImage(final String imageName, final ImageView image){
			
			Log.print("Image Path : ", Const.THUMBNAIL_DIR + "/"+ imageName);
			runOnUiThread(new Runnable() {
				public void run() {
					
					final Bitmap viewBitmap = Utils.decodeFile(Const.THUMBNAIL_DIR + "/"+ imageName,95,95);
					if(viewBitmap != null)
					image.setImageBitmap(viewBitmap);
					
					
				}
			});
		}
	}

	
	class RemoveView extends AsyncTask<Void,Void,Integer>{

		private int Index;
		
		public RemoveView(int Index) {
			this.Index = Index;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		@Override
		protected Integer doInBackground(Void... params) {
			int Result = 0;
			ImageBL mBl = new ImageBL();
			if(mBl.Delete(arrListImageBean.get(this.Index).getFileName()) == 0){
				
				Storage.IsFileAvailableDelete(Const.IMAGE_DIR + "/" + arrListImageBean.get(this.Index).getFileName());
				Storage.IsFileAvailableDelete(Const.TEMP_IMAGE_PATH + "/" + arrListImageBean.get(this.Index).getFileName());
				
			}else{
				
				Result = -1;
			}
			
			return Result;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			
			if(result == 0){
				Intent mItent = new Intent(PhotoGallery.this,PhotoGallery.class);
	        	mItent.putExtra("PAGENO",String.valueOf(intPageNo));
	        	mItent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        	//Toast.makeText(PhotoGallery.this,String.valueOf(intPageNo),//Toast.LENGTH_SHORT).show();
	        	startActivity(mItent);
	        	finish();
			}else{
				Toast.makeText(PhotoGallery.this,"No able to remove!!",Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(" [ requestCode ] " + requestCode + " [ resultCode ]"+ resultCode);
	}
}
