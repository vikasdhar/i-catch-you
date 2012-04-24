package com.esp.spycatch.uc;

import com.esp.spycatch.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActionItem extends LinearLayout {
	public ImageView actionIcon;
	public TextView actionName;
	
	public ActionItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void init(Context context, String strActionName, int refActionicon){
		
		View view = LayoutInflater.from(context).inflate(R.layout.action_item,this,true);

		this.actionIcon = (ImageView)view.findViewById(R.id.actionIcon);
		this.actionName = (TextView)view.findViewById(R.id.actionName);
		
		this.actionIcon.setImageResource(refActionicon);
		this.actionName.setText(strActionName);	
	}	
}
