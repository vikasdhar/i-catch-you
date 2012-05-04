package com.esp.spycatch.process;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.bll.ImageBL;
import com.esp.spycatch.fb.AsyncFacebookRunner;
import com.esp.spycatch.fb.AsyncFacebookRunner.RequestListener;
import com.esp.spycatch.fb.Facebook;
import com.esp.spycatch.fb.FacebookError;
import com.esp.spycatch.fb.SessionStore;
import com.esp.spycatch.util.Const;
import com.esp.spycatch.util.Log;
import com.esp.spycatch.util.Pref;
import com.esp.spycatch.util.Utils;

public class FacebookReceiver {

	private String TAG = this.getClass().getSimpleName();
	private Facebook mFacebook;
	private ArrayList<ImageBean> arrayListImage = new ArrayList<ImageBean>();

	public int intIndex;

	public void onFacebookReceive(Context context, Intent intent) {

		Const.CONTEXT = context;

		Log.print(TAG, "|**|**| FACEBOOK SERVICE RUNNING |**|**|");

		if (Pref.getValue("FB_SESSION_VALID", "No").equals("Yes")) {
			try {

				mFacebook = new Facebook(Const.FACEBOOK_APP_ID);
				SessionStore.restore(mFacebook, context);

				new PostOnFBWall().execute();

			} catch (Exception e) {

			}
		}

	}

	private class PostOnFBWall extends AsyncTask<Void, Void, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.print("PostOnFBWall |=> ", "onPreExecute");
		}

		@Override
		protected Integer doInBackground(Void... params) {
			int Result = 0;
			Log.print("PostOnFBWall |=> ", "doInBackground");

			Log.print(
					TAG,
					"GET FACEBOOK_TIMESTMP "
							+ Pref.getValue("FACEBOOK_TIMESTMP", ""));

			ImageBL mImageBL = new ImageBL();
			ImageBean mImageBean = new ImageBean();
			mImageBean.setImageID(0);
			mImageBean.setCreatedDate(Long.parseLong(Pref.getValue(
					"FACEBOOK_TIMESTMP", "")));

			arrayListImage = mImageBL.List(mImageBean, 4);

			if (arrayListImage.size() > 0 && arrayListImage != null) {

				Log.print(TAG,
						" [ arrayListImage size ] " + arrayListImage.size());

				for (int i = 0; i < arrayListImage.size(); i++) {

					Log.print(TAG, " [ DB File Path ] "
							+ arrayListImage.get(i).getFileName());
					postToFacebook("Message From spycatch",
							arrayListImage.get(i).getFileName(), i);
				}

			}

			return Result;

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 0) {

				Log.print(TAG, " SET FACEBOOK_TIMESTMP ");
				Pref.setValue("FACEBOOK_TIMESTMP",
						String.valueOf(Utils.getMilisecond()));
				Log.print("PostOnFBWall |=> ", "onPostExecute");
			}

		}

	}

	private void postToFacebook(final String review, final String Filename,
			final int Index) {

		new Thread(new Runnable() {

			public void run() {

				intIndex = Index;
				Log.print("[IMAGE Position]", "" + Index);
				AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
						mFacebook);

				Bundle params = new Bundle();

				params.putString("message", review);
				params.putString("name", "spycatch");
				params.putString("caption", "spycatch.net");
				params.putString("link", "http://www.spycatch.net");
				params.putString("description", "spycatch catch the face");

				params.putString("method", "photos.upload");

				Log.print("FacebookReceiver |=> postToFacebook ",
						" [ FULL PATH ]" + Const.IMAGE_DIR + "/" + Filename);
				Bitmap temp = BitmapFactory.decodeFile(Const.IMAGE_DIR + "/"
						+ Filename);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				temp.compress(Bitmap.CompressFormat.PNG, 100, baos);
				byte[] imgData = baos.toByteArray();
				params.putByteArray("picture", imgData);

				// mAsyncFbRunner.request("me/feed", params, "POST",null);
				// mAsyncFbRunner.request(null, params, "POST", null);
				mAsyncFbRunner.request(null, params, "POST",
						SampleUploadListener, Index);

			}
		}).start();

	}

	public void UpdateDB(int ImageID) {

		ImageBean mBean = new ImageBean();
		mBean.setImageID(ImageID);
		mBean.setDoEmail(0);
		mBean.setDoMMS(0);
		mBean.setDoFacebook(1);
		new ImageBL().Update(mBean);
	}

	private RequestListener SampleUploadListener = new RequestListener() {

		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Log.error(this.getClass() + " :: onMalformedURLException :: ", e);

		}

		public void onIOException(IOException e, Object state) {
			Log.error(this.getClass() + " :: IOException :: ", e);

		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.error(this.getClass() + " :: FileNotFoundException :: ", e);

		}

		public void onFacebookError(FacebookError e, Object state) {
			Log.error(this.getClass() + " :: onFacebookError() :: ",
					e.toString());

		}

		public void onComplete(String response, Object state) {

			Log.print("", "[ INDEX VALUE ]" + (Integer) state);
			UpdateDB(arrayListImage.get((Integer) state).getImageID());
		}
	};

}
