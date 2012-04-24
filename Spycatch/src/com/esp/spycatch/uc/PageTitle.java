package com.esp.spycatch.uc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esp.spycatch.R;
import com.esp.spycatch.util.Const;

public class PageTitle extends RelativeLayout{

	public TextView txtPageTitle;
	
	public PageTitle(Context context) {
		super(context);
	}

	public PageTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PageTitle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void init(){		
		View view = LayoutInflater.from(Const.CONTEXT).inflate(R.layout.page_title,this,true);
		
		this.txtPageTitle = (TextView)view.findViewById(R.id.txtPageTitle);
		
		
		System.out.println("==============================================================");
	}    
}
