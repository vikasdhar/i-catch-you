package com.esp.spycatch.uc;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.esp.spycatch.R;

public class CustomProgressDialog extends Dialog{

	
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
 
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        System.out.println("Focus changed..");
    }
 
    private static CustomProgressDialog dialog = null;
    
    public static CustomProgressDialog createDialog(Context context,String title,String message){
    	
    	if(dialog == null)
    	{
	        if(title.length()==0||title==null)
	            dialog=new CustomProgressDialog(context,R.style.CustomProgressDialog);
	        else{
	        	
	            dialog=new CustomProgressDialog(context,R.style.CustomProgressDialog);
	            dialog.setTitle(title);
	        }
	 
	        dialog.setContentView(R.layout.loading_dialog);
	        dialog.getWindow().getAttributes().gravity=Gravity.CENTER;
    	}
        return dialog;
    }
    
    @Override
    public void dismiss() {
    	dialog = null;
    	super.dismiss();
    	
    }
}

