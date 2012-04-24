package com.esp.spycatch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.esp.spycatch.R;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;

public class About extends Activity{
    
	public PageTitle pageTitle;
	private TextView txtview_link_BuyPro,txtview_link_Rateus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Const.CONTEXT = this;
        
        //Page title
        this.pageTitle = (PageTitle)findViewById(R.id.pageTitle);
        this.pageTitle.init();
        this.pageTitle.txtPageTitle.setText("About");
        
        txtview_link_BuyPro = (TextView)findViewById(R.id.txtview_link_BuyPro);
        txtview_link_BuyPro.setText(Html.fromHtml("<a href=\"" + "http://www.google.com" + "\">" + getResources().getString(R.string.BuyPro) + "</a>"));
        txtview_link_BuyPro.setMovementMethod(LinkMovementMethod.getInstance());

       
        txtview_link_Rateus = (TextView)findViewById(R.id.txtview_link_Rateus);
        txtview_link_Rateus.setText(Html.fromHtml("<a href=\"" + "http://www.google.com" + "\">" + getResources().getString(R.string.RateUs) + "</a>"));
        txtview_link_Rateus.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    
}
