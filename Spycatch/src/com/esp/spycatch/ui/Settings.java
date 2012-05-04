package com.esp.spycatch.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esp.spycatch.R;
import com.esp.spycatch.bean.CheckBoxItemBean;
import com.esp.spycatch.fb.DialogError;
import com.esp.spycatch.fb.Facebook;
import com.esp.spycatch.fb.Facebook.DialogListener;
import com.esp.spycatch.fb.FacebookError;
import com.esp.spycatch.fb.SessionStore;
import com.esp.spycatch.process.SharePicture;
import com.esp.spycatch.uc.PageTitle;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class Settings extends Activity {

	private ListView listView_setting_Item;
	private AdapterSetting mAdapterSetting;
	private CheckBoxItemBean mBeanCheckBox;
	public ArrayList<CheckBoxItemBean> mData = new ArrayList<CheckBoxItemBean>();

	public PageTitle pageTitle;

	private Facebook mFacebook;
	private ProgressDialog mProgress;
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "read_stream", "offline_access" };

	private ProgressDialog mProgressDialog;

	private CheckBox tempCheckbox = null;
	private String chkTag = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		Const.CONTEXT = this;

		mProgress = new ProgressDialog(this);
		mProgressDialog = new ProgressDialog(Settings.this);
		mProgressDialog.setMessage("Please wait....");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);

		// Page title
		this.pageTitle = (PageTitle) findViewById(R.id.pageTitle);
		this.pageTitle.init();
		this.pageTitle.txtPageTitle.setText("Settings");

		listView_setting_Item = (ListView) findViewById(R.id.listView_setting_Item);
		mAdapterSetting = new AdapterSetting();

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Enable");
		mBeanCheckBox.setStrSubTitle("Turn on/off");
		mBeanCheckBox.setIsCheckBox(true);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Password");
		mBeanCheckBox.setStrSubTitle("Set password");
		mBeanCheckBox.setIsCheckBox(true);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Email support");
		mBeanCheckBox.setStrSubTitle("Set up e-mail notifications");
		mBeanCheckBox.setIsCheckBox(true);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("MMS support");
		mBeanCheckBox.setStrSubTitle("Set up mms notifications");
		mBeanCheckBox.setIsCheckBox(true);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Facebook support");
		mBeanCheckBox.setStrSubTitle("Set up facebook notifications");
		mBeanCheckBox.setIsCheckBox(true);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Advanced");
		mBeanCheckBox
				.setStrSubTitle("Customize more with addition cool features");
		mBeanCheckBox.setIsCheckBox(false);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		mBeanCheckBox = new CheckBoxItemBean();
		mBeanCheckBox.setStrTitle("Setup a screen lock");
		mBeanCheckBox
				.setStrSubTitle("Take picture when it is opened with code/pattern");
		mBeanCheckBox.setIsCheckBox(false);
		mAdapterSetting.addCheckBoxItem(mBeanCheckBox);

		listView_setting_Item.setAdapter(mAdapterSetting);

		listView_setting_Item.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				if (mData.get(position).getStrTitle().equals("Advanced")) {

					Intent mIntent = new Intent(Settings.this,
							AdvanceSettings.class);
					mIntent.putExtra("Title", mData.get(position).getStrTitle());
					startActivityForResult(mIntent,
							Const.DIALOG_ADVANCE_SETTINGS);
				}
				if (mData.get(position).getStrTitle()
						.equals("Setup a screen lock")) {

					Intent settingIntent = new Intent(
							DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
					startActivityForResult(settingIntent, 0);
				}

			}
		});
	}

	private class AdapterSetting extends BaseAdapter {

		private LayoutInflater mInflater;

		public AdapterSetting() {
			mInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void addCheckBoxItem(final CheckBoxItemBean Item) {
			mData.add(Item);
			notifyDataSetChanged();
		}

		public int getCount() {
			return mData.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			final ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.row_setting_checkbox,
						null);

				holder.cBox = (CheckBox) convertView
						.findViewById(R.id.cbox_setting_Checkbox);
				holder.txtView_Title = (TextView) convertView
						.findViewById(R.id.txtview_setting_Title);
				holder.txtView_subTitle = (TextView) convertView
						.findViewById(R.id.txtview_setting_SubTitle);
				holder.view_setting_checkbox = (LinearLayout) convertView
						.findViewById(R.id.view_setting_checkbox);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			if (mData.size() > position) {

				CheckBoxItemBean mBean = (CheckBoxItemBean) mData.get(position);
				if (mBean.isCheckbox()) {

					holder.view_setting_checkbox.setVisibility(View.VISIBLE);
					holder.cBox.setTag(mBean.getStrTitle());
					holder.cBox.setOnCheckedChangeListener(null);

					if (mBean.getStrTitle().equals("Enable")) {

						if (Pref.getValue("STOP_START_SERVICE", "No").equals(
								"Yes"))
							holder.cBox.setChecked(true);
						else
							holder.cBox.setChecked(false);

					}

					if (mBean.getStrTitle().equals("Password")) {

						if (Pref.getValue("PASSWORD_PROTECTION", "0").equals(
								"1"))
							holder.cBox.setChecked(true);
						else
							holder.cBox.setChecked(false);

					}
					if (mBean.getStrTitle().equals("Email support")) {

						if (Pref.getValue("EMAIL_SESSION_VALID", "No").equals(
								"Yes"))
							holder.cBox.setChecked(true);
						else
							holder.cBox.setChecked(false);
					}

					if (mBean.getStrTitle().equals("MMS support")) {

						if (Pref.getValue("MMS_SESSION_VALID", "No").equals(
								"Yes"))
							holder.cBox.setChecked(true);
						else
							holder.cBox.setChecked(false);
					}

					if (mBean.getStrTitle().equals("Facebook support")) {

						// check is Session is valid or not
						if (Pref.getValue("FB_SESSION_VALID", "No").equals(
								"Yes")) {
							// we reach here because he/she is login
							holder.cBox.setChecked(true);
						} else {

							holder.cBox.setChecked(false);
						}
					}

				} else {

					holder.view_setting_checkbox.setVisibility(View.GONE);
					// holder.cBox.setVisibility(View.GONE);

				}

				holder.txtView_Title.setText(mBean.getStrTitle());
				holder.txtView_subTitle.setText(mBean.getStrSubTitle());
				holder.cBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								
								
								if(buttonView.getTag().equals("-1"))
								{
									tempCheckbox.setTag(chkTag);
									return;
								}
								
								if (isChecked) {

									if (buttonView.getTag().equals("Enable")) {

										Log.print("Setting | holder ",
												"Enable " + isChecked);
										
										new StartAllService().execute();
									}

									if (buttonView.getTag().equals("Password")) {
										
										tempCheckbox = holder.cBox;
										chkTag = (String)holder.cBox.getTag();
										
										Intent mIntent = new Intent(
												Settings.this,
												SetPasswordActivity.class);
										mIntent.putExtra("Title",
												mData.get(position)
														.getStrTitle());
										startActivityForResult(mIntent,
												Const.DIALOG_PASSWORD);
									}

									if (buttonView.getTag().equals(
											"Email support")) {

										tempCheckbox = holder.cBox;
										chkTag = (String)holder.cBox.getTag();
										Intent mIntent = new Intent(
												Settings.this,
												EmailNotification.class);
										mIntent.putExtra("Title",
												mData.get(position)
														.getStrTitle());
										startActivityForResult(mIntent,
												Const.DIALOG_EMAIL_SUPPORT);

									}

									if (buttonView.getTag().equals(
											"MMS support")) {

										tempCheckbox = holder.cBox;
										chkTag = (String)holder.cBox.getTag();
										Intent mIntent = new Intent(
												Settings.this,
												MmsNotification.class);
										mIntent.putExtra("Title",
												mData.get(position)
														.getStrTitle());
										startActivityForResult(mIntent,
												Const.DIALOG_MMS_SUPPORT);
									}

									// if check then
									if (buttonView.getTag().equals(
											"Facebook support")) {

										if (Utils.isOnline()) {

											tempCheckbox = holder.cBox;
											chkTag = (String)holder.cBox.getTag();
											mFacebook.authorize(Settings.this,PERMISSIONS,-1,new FbLoginDialogListener());
											
										} else {

											Toast.makeText(
													Settings.this,
													"Please check internet connection !!!",
													Toast.LENGTH_SHORT).show();
										}

									}

								} else {

									if (buttonView.getTag().equals("Enable")) {

										Log.print("Setting | holder ",
												"Enable " + isChecked);
										new StopAllService().execute();

									}

									if (buttonView.getTag().equals("Password")) {

										Pref.setValue("PASSWORD_PROTECTION",
												"0");
									}

									if (buttonView.getTag().equals(
											"Email support")) {
										Pref.setValue("EMAIL_SESSION_VALID",
												"No");
									}

									if (buttonView.getTag().equals(
											"MMS support")) {
										Pref.setValue("MMS_SESSION_VALID", "No");
									}

									if (buttonView.getTag().equals(
											"Facebook support")) {
										if (Pref.getValue("FB_SESSION_VALID",
												"No").equals("Yes"))
											fbLogout();
									}
								}

							}
						});

			} else {

				holder.cBox.setVisibility(View.GONE);
				holder.txtView_Title.setText("");
				holder.txtView_subTitle.setText("");
			}

			return convertView;
		}

	}

	public static class ViewHolder {
		public TextView txtView_Title, txtView_subTitle;
		public CheckBox cBox;
		public LinearLayout view_setting_checkbox;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		System.out.println(" [ requestCode ] " + requestCode
				+ " [ resultCode ]" + resultCode);

		if (requestCode == Const.DIALOG_PASSWORD) {

			if (resultCode == RESULT_OK) {
				
				
				Pref.setValue("PASSWORD", data.getStringExtra("Password"));
				Pref.setValue("PASSWORD_PROTECTION", "1");
				if (tempCheckbox != null) {
					tempCheckbox.setTag("-1");
					tempCheckbox.setChecked(true);


				}
			}else if(resultCode == RESULT_CANCELED){
				
				
				if (tempCheckbox != null) {
					tempCheckbox.setChecked(false);

				}
			}
		}

		if (requestCode == Const.DIALOG_EMAIL_SUPPORT) {

			if (resultCode == RESULT_OK) {

				System.out.println("[ RESUALT  : DIALOG_EMAIL_SUPPORT ");

				// TODO : SET EMAIL_TIMESTAMP
				Pref.setValue("EMAIL_TIMESTAMP",
						String.valueOf(Utils.getMilisecond()));
				Pref.setValue("EMAIL_SESSION_VALID", "Yes");
				if (tempCheckbox != null) {
					tempCheckbox.setTag("-1");
					tempCheckbox.setChecked(true);

				}

			} else if (resultCode == RESULT_CANCELED) {

				System.out.println("[ CANCEL RESUALT  DIALOG_EMAIL_SUPPORT ");
				if (tempCheckbox != null) {
					tempCheckbox.setChecked(false);

				}

			}

		}

		if (requestCode == Const.DIALOG_MMS_SUPPORT) {

			if (resultCode == RESULT_OK) {

				System.out.println("[ RESUALT  : DIALOG_MMS_SUPPORT ");
				// TODO : SET MMS_TIMESTAMP
				Pref.setValue("MMS_TIMESTAMP",
						String.valueOf(Utils.getMilisecond()));
				Pref.setValue("MMS_SESSION_VALID", "Yes");
				if (tempCheckbox != null) {
					tempCheckbox.setTag("-1");
					tempCheckbox.setChecked(true);

				}

			} else if (resultCode == RESULT_CANCELED) {

				System.out.println("[ CANCEL RESUALT  DIALOG_MMS_SUPPORT ");
				if (tempCheckbox != null) {
					tempCheckbox.setChecked(false);
				}

			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		mFacebook = new Facebook(Const.FACEBOOK_APP_ID);
		SessionStore.restore(mFacebook, this);
	}

	private final class FbLoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {

			SessionStore.save(mFacebook, Settings.this);

			// TODO : FACEBOOK_TIMESTMP
			Pref.setValue("FACEBOOK_TIMESTMP",
					String.valueOf(Utils.getMilisecond()));
			Pref.setValue("FB_SESSION_VALID", "Yes");
		}

		public void onFacebookError(FacebookError error) {

			Toast.makeText(Settings.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

			// mFacebookBtn.setChecked(false);
		}

		public void onError(DialogError error) {

			Toast.makeText(Settings.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

			// mFacebookBtn.setChecked(false);
		}

		public void onCancel() {

			tempCheckbox.setChecked(false);
			Toast.makeText(Settings.this, "Facebook connection cancel",
					Toast.LENGTH_SHORT).show();

		}
	}

	private void fbLogout() {

		mProgress.setMessage("Disconnecting from Facebook");
		mProgress.show();
		if (Utils.isOnline()) {

			new Thread() {
				@Override
				public void run() {
					SessionStore.clear(Settings.this);

					int what = 1;

					try {
						mFacebook.logout(Settings.this);

						what = 0;
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					mHandler.sendMessage(mHandler.obtainMessage(what));
				}
			}.start();

		} else {

			Toast.makeText(Settings.this,
					"Please check internet connection !!!", Toast.LENGTH_SHORT)
					.show();
		}

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mProgress.dismiss();
			if (msg.what == 1) {

				Toast.makeText(Settings.this, "Facebook logout failed",
						Toast.LENGTH_SHORT).show();
			} else {

				// TODO : Disconnect from facebook
				Pref.setValue("FB_SESSION_VALID", "No");
				Toast.makeText(Settings.this, "Disconnected from Facebook",
						Toast.LENGTH_SHORT).show();

			}

		}
	};

	public void startService() {
		
	
		Log.print("Setting |=> stratService");
		Intent intent = new Intent(Const.CONTEXT, SharePicture.class);

		Const.objPendingIntent = PendingIntent.getBroadcast(Const.CONTEXT, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		Const.objAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Const.objAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 5000, Const.INTERVAL_TIME,
				Const.objPendingIntent);
	

	}

	public void stopService() {
		
		
		if (Const.objPendingIntent != null) {
			Log.print("Setting |=> stopService");
			Const.objAlarmManager.cancel(Const.objPendingIntent);
			Const.objPendingIntent.cancel();
			Const.objPendingIntent = null;
			SharePicture.STARTED = false;
		}

	}

	class StartAllService extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {

			int Result = 0;
			startService();
			return Result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result == 0) {
				Pref.setValue("STOP_START_SERVICE", "Yes");
				
			}
		}
	}

	class StopAllService extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int Result = 0;
			stopService();
			return Result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			if (result == 0) {
				Pref.setValue("STOP_START_SERVICE", "No");
			}
		}

	}

}
