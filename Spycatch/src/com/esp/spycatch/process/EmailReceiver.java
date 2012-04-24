package com.esp.spycatch.process;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
import com.esp.spycatch.email.Mail;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Storage;
import com.esp.spycatch.util.Utils;

public class EmailReceiver extends BroadcastReceiver {

	private String TAG = this.getClass().getSimpleName();
	
	private ArrayList<ImageBean> arrayListImage = new ArrayList<ImageBean>();
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		Const.CONTEXT = context;
		
		Log.print(TAG, "|**|**|  EMAIL SERVICE RUNNING |**|**|");
		
		if(!Pref.getValue("EMAIL_SEND_TO","").equals("")){
			
			if (Utils.matchDay(Pref.getValue("EMAIL_WEEK_DAYS", ""))) {
	
				if (Utils.IsBetween(Utils.getTimeToMilisecond(Pref.getValue(
						"EMAIL_FROM_TIME", "")), Utils.getTimeToMilisecond(Utils
						.currentTimeMilisecond()), Utils.getTimeToMilisecond(Pref
						.getValue("EMAIL_TO_TIME", "")))) {
					
					new SendingEmail().execute();
				}
				
				
			}//match days 
			
		}// send to
		
		
		
	
	}
	
	private class SendingEmail extends AsyncTask<Void,Void, Integer>{
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.print("SendingEmail |=> ","onPreExecute");
			
	
			
		}
		@Override
		protected Integer doInBackground(Void... params) {
			int result = 0;
			Log.print("SendingEmail |=> ","doInBackground");
			
			Log.print(TAG, "GET EMAIL_TIMESTAMP " + Pref.getValue("EMAIL_TIMESTAMP",""));
			
			ImageBL mImageBL = new ImageBL();
			ImageBean mImageBean = new ImageBean();
			mImageBean.setImageID(0);
			mImageBean.setCreatedDate(Long.parseLong(Pref.getValue("EMAIL_TIMESTAMP","")));
			
			arrayListImage = mImageBL.List(mImageBean, 2);
			
			if(arrayListImage.size() > 0 && arrayListImage != null){
				
				Log.print(" [ ListImage ] "," "+ arrayListImage.size());
				
				for (int i = 0; i < arrayListImage.size(); i++) {
					
					Log.print(TAG, " [ DB File Path ]["+ i +"]" + arrayListImage.get(i).getFileName());
					sendEmail(arrayListImage.get(i).getFileName(),i);
					
				}
				
				
				
			}
			
			return result;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if(result == 0){
				
				Log.print(TAG, " SET EMAIL_TIMESTAMP ");
				Pref.setValue("EMAIL_TIMESTAMP",String.valueOf(Utils.getMilisecond()));
				Log.print("SendingEmail |=> ","onPostExecute");
			}
		}
		
	}
	
	public void sendEmail(final String FileName,final int ID){
	
		try {
			
				new Thread(new Runnable() {
					
					public void run() {
						try
						{
							
							Mail m = new Mail(Pref.getValue("EMAIL_ADDRESS","mehulesprit@gmail.com"),Pref.getValue("EMAIL_PASSWORD","9725733677"));
							String[] toArr = {Pref.getValue("EMAIL_SEND_TO","")};
							m.setSendTo(toArr);
							m.setFrom(Pref.getValue("EMAIL_ADDRESS","mehulesprit@gmail.com"));
							m.setSubject("This is an email sent by spycatch application");
							m.setBody("Hey your device locked is open by following persone on image");
							
							if(Utils.renameFileExtention(FileName) == 1){
								
								m.addAttachment(Const.CATCH_IMAGE_DIR +"/" + Utils.trimExtension(FileName)+".png");
							}
							
							Thread.sleep(2000);
							
							if (m.send()) {
								
								if(Storage.IsFileAvailableDelete(Const.CATCH_IMAGE_DIR +"/" + Utils.trimExtension(arrayListImage.get(ID).getFileName())+".png"))
								UpdateDB(arrayListImage.get(ID).getImageID());	
								Thread.sleep(2000);
								messageHandler.sendMessage(messageHandler.obtainMessage(1));
								
							} else {
								
								messageHandler.sendMessage(messageHandler.obtainMessage(-1));	
							}
							
							
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			
	
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Email Notification", e.toString());
		}
		
	}
	
	public void UpdateDB(int ImageID){
		
		ImageBean mBean = new ImageBean();
		mBean.setImageID(ImageID);
		mBean.setDoEmail(1);
		mBean.setDoMMS(0);
		mBean.setDoFacebook(0);
		new ImageBL().Update(mBean);
	}
	
	private Handler messageHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.print("EmailReceiver | onReceive","Email was sent successfully.");
				break;
			case -1 :
				Log.print("EmailReceiver | onReceive","Email was not sent.");
				break;
			default:
				break;
			}
			
		};
	};
	
	

}
