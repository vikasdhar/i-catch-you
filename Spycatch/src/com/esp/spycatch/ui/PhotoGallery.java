package com.esp.spycatch.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
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
import com.esp.spycatch.util.Utils;

public class PhotoGallery extends Activity implements OnClickListener{
	
	//public ImageList imageList;
	public LinearLayout imageList;
	public PageTitle pageTitle; 
	public static boolean doGenerateUI = true;
	
	public int intPageNo;
	public int NumberOfIteam = 50;
	
	public static ArrayList<ImageBean> arrListImageBean = null;
	
	private Button btn_Pre,btn_next;
	
	
    /** Called when the activity is first created. */
	
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
        
        
        btn_Pre = (Button)findViewById(R.id.btn_prv);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_Pre.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        
    	Const.CONTEXT = this;
		intPageNo = Integer.parseInt(getIntent().getExtras().getString("PAGENO"));
		Log.print("","[ PAGE WATCH ] " + intPageNo);
		this.generateUI();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}
    
	public void generateUI(){
    	View view = null;
    	LinearLayout linearLayout = null;
    	LayoutParams lparams = null;
    	ImageView image;
    	TextView imagePath;
    	
    	Display display = null;
    	int cols = 0; 
    	File imageDir = null;
    	String files[] = null;
    	
    	boolean doAddLinearLayout = false;
    	
    	if (PhotoGallery.doGenerateUI){
    		
    		//get width of the screen
    		display = ((WindowManager)Const.CONTEXT.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    		
    		//number of images horizontally
    		cols = (display.getWidth()-10)/65;
    		
    		Log.print("Cols : ", cols+ "");
    		
    		// Get 50 image
    		ImageBL mImageBL = new ImageBL();
    		if(arrListImageBean == null)
    		arrListImageBean = mImageBL.Page_List(intPageNo);
    		Log.print("ARRAYLIST : ", arrListImageBean.size()+ "");
    		
    		
			if (arrListImageBean.size() == NumberOfIteam && intPageNo > 1) {
				// Next Button Visible
				btn_next.setVisibility(View.VISIBLE);
				btn_Pre.setVisibility(View.VISIBLE);
			} else if (arrListImageBean.size() < NumberOfIteam && intPageNo > 1) {
				btn_next.setVisibility(View.GONE);
				btn_Pre.setVisibility(View.VISIBLE);
			} else if (arrListImageBean.size() == NumberOfIteam
					&& intPageNo == 1) {
				btn_next.setVisibility(View.VISIBLE);
				btn_Pre.setVisibility(View.GONE);
			}

    		//Utils.arrListToArray(arrListImageBean);
    		files = Utils.arrListToArray(arrListImageBean);
    		
//    		imageDir = new File(Const.THUMBNAIL_DIR);
//    		files = imageDir.list();
//    	
			
    		if (files.length != 0){
    			lparams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    			
    			//Create new linear layout
    			linearLayout = new LinearLayout(Const.CONTEXT);
    			linearLayout.setLayoutParams(lparams);
    			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    			linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
    		}
    		
    		for (int i=0; i<files.length; i++){
    			
    			Log.print("File : ", i + " of " + files.length + " :: " +  files[i]);
    			
    			final int index = i;
    			
    			view = LayoutInflater.from(Const.CONTEXT).inflate(R.layout.image_container,null,true);
    			view.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						
						Toast.makeText(PhotoGallery.this,arrListImageBean.get(index).getFileName(),Toast.LENGTH_SHORT).show();
						Intent viewIntent = new Intent(PhotoGallery.this,PhotoViewerActivity.class);
						viewIntent.putExtra("FileName",arrListImageBean.get(index).getFileName());
						viewIntent.putExtra("Index",index+"");
						//viewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(viewIntent);
					}
				});
    			image = (ImageView)view.findViewById(R.id.image);
    			image.setLayoutParams(lparams);
    			//image.setImageBitmap(Utils.decodeFile(Const.THUMBNAIL_DIR + "/"+ files[i],75,75));
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
    	imageDir = null;
    	files = null;
    }
    
    private class LoadImage extends Thread {
    	private ImageView image;
    	private String imageName;
		public LoadImage(String imageName, ImageView image){
			this.imageName = imageName;
			this.image = image;
			this.start();
		}
		
		@Override
		public void run() {
			try{
				Log.print("Path : ", Const.THUMBNAIL_DIR + "/"+ imageName);
				this.image.setImageBitmap(Utils.decodeFile(Const.THUMBNAIL_DIR + "/"+ imageName,50,50));							
			}catch(Exception e){
				Log.print("Exception Path : ", Const.THUMBNAIL_DIR + "/"+ imageName);
			}
		}
    }

	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_prv:
			
			
			if(intPageNo > 1){
				arrListImageBean.clear();
				intPageNo--;
				Intent mItent = new Intent(PhotoGallery.this,PhotoGallery.class);
				mItent.putExtra("PAGENO",String.valueOf(intPageNo));
				mItent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Toast.makeText(PhotoGallery.this,String.valueOf(intPageNo),Toast.LENGTH_SHORT).show();
				startActivity(mItent);
				finish();
			}
			break;

		case R.id.btn_next:
			
			if(arrListImageBean.size() == NumberOfIteam){
				arrListImageBean.clear();
				intPageNo++;
				Intent mItent = new Intent(PhotoGallery.this,PhotoGallery.class);
				mItent.putExtra("PAGENO",String.valueOf(intPageNo));
				mItent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Toast.makeText(PhotoGallery.this,String.valueOf(intPageNo),Toast.LENGTH_SHORT).show();
				startActivity(mItent);
				finish();
			}
			
			break;

		default:
			break;
		};
	}
}
