package com.esp.spycatch.bll;

import java.util.ArrayList;

import android.database.Cursor;

import com.esp.spycatch.bean.ImageBean;
import com.esp.spycatch.util.DBHelper;
import com.esp.spycatch.util.Log;

public class ImageBL {
	
	public static int RESULT;
	
	public ArrayList<ImageBean> List(ImageBean objBean, int Mode)
	{
		DBHelper objDBHelper = new DBHelper();
		StringBuilder Query = new StringBuilder();
		ArrayList<ImageBean> objList = new ArrayList<ImageBean>();
		Cursor objCursor = null;
				
		try {
			
			if(objBean.getImageID() == 0)
			{
				if(Mode == 1)
					Query.append("SELECT ")
					 	 .append("ImageID, FileName, Thumbnail, DoEmail, DoMMS, DoFacebook, CreatedDate, Lat, Lon ")
					 	 .append("FROM Image ORDER BY IMAGEID");
				
				else if(Mode == 2)
					Query.append("SELECT ")
					 	 .append("ImageID, FileName, Thumbnail, DoEmail, DoMMS, DoFacebook, CreatedDate, Lat, Lon ")
					 	 .append("FROM Image ")
					 	 .append("WHERE DoEmail = 0 AND CreatedDate <= ")
					 	 .append(objBean.getCreatedDate())
					 	 .append(" ORDER BY IMAGEID");
				
				else if(Mode == 3)
					Query.append("SELECT ")
					 	 .append("ImageID, FileName, Thumbnail, DoEmail, DoMMS, DoFacebook, CreatedDate, Lat, Lon ")
					 	 .append("FROM Image ")
					 	 .append("WHERE DoMMS = 0 AND CreatedDate <= ")
					 	 .append(objBean.getCreatedDate())
					 	 .append(" ORDER BY IMAGEID");
				
				else if(Mode == 4)
					Query.append("SELECT ")
					 	 .append("ImageID, FileName, Thumbnail, DoEmail, DoMMS, DoFacebook, CreatedDate, Lat, Lon ")
					 	 .append("FROM Image ")
					 	 .append("WHERE DoFacebook = 0 AND CreatedDate <= ")
					 	 .append(objBean.getCreatedDate())
					 	 .append(" ORDER BY IMAGEID");
				
			}
			
			Log.print("","ImageBL |-> List");
			System.out.println(Query.toString());
			Log.print("","ImageBL |-> List");
			
			objCursor = objDBHelper.query(Query.toString());
			objCursor.moveToPosition(-1);
			
			while (objCursor.moveToNext()) {
				
				ImageBean objImageList = new ImageBean();
				objImageList.setImageID(objCursor.getInt(0));
				objImageList.setFileName(objCursor.getString(1));
				objImageList.setThumbnail(objCursor.getString(2));
				objImageList.setDoEmail(objCursor.getInt(3));
				objImageList.setDoMMS(objCursor.getInt(4));
				objImageList.setDoFacebook(objCursor.getInt(5));
				objImageList.setLat(objCursor.getString(7));
				objImageList.setLon(objCursor.getString(8));
				objList.add(objImageList);
			}
			
			Log.print("ImageBL |-> List ","LIST SIZE " + objList.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Image List", e.getMessage());
		} finally {
			objDBHelper = null;
			Query = null;
			objCursor.close();
			objCursor = null;
		}
		
		return objList;
	}
	
	public ArrayList<ImageBean> Page_List(int PageNo,int point)
	{
		DBHelper objDBHelper = new DBHelper();
		StringBuilder Query = new StringBuilder();
		ArrayList<ImageBean> objList = new ArrayList<ImageBean>();
		Cursor objCursor = null;
		
//		int start = (PageNo - 1) * 50 + 1;
//		int end = start + 49;
		
		int start = (PageNo - 1) * point;
		int end = point;
		
		try {

			if (PageNo != 0) {

				Query.append("SELECT ")
						.append("ImageID, FileName, Thumbnail, DoEmail, DoMMS, DoFacebook, CreatedDate, Lat, Lon ")
						.append("FROM Image ")
						.append("ORDER BY CreatedDate Desc Limit ")
						.append(start)
						.append(" , ")
						.append(end);
						

			}

			Log.print("", "ImageBL |-> List");
			System.out.println(Query.toString());

			objCursor = objDBHelper.query(Query.toString());
			objCursor.moveToPosition(-1);

			while (objCursor.moveToNext()) {

				ImageBean objImageList = new ImageBean();
				objImageList.setImageID(objCursor.getInt(0));
				objImageList.setFileName(objCursor.getString(1));
				objImageList.setThumbnail(objCursor.getString(2));
				objImageList.setDoEmail(objCursor.getInt(3));
				objImageList.setDoMMS(objCursor.getInt(4));
				objImageList.setDoFacebook(objCursor.getInt(5));
				objImageList.setLat(objCursor.getString(7));
				objImageList.setLon(objCursor.getString(8));
				objList.add(objImageList);
			}

			Log.print("ImageBL |-> List ", "PAGE LIST SIZE " + objList.size());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("PAGE LIST SIZE ", e.getMessage());
		} finally {
			objDBHelper = null;
			Query = null;
			objCursor.close();
			objCursor = null;
		}

		return objList;
	}
	
	public void Insert(ImageBean objBean)
	{
		DBHelper objDBHelper = new DBHelper();	
		StringBuilder Query = new StringBuilder();
		try {
			
			Query.append("INSERT INTO ")
			 .append("Image ( FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate, Lat, Lon) ")
			 .append("Values ( '")
			 .append(objBean.fileName)
			 .append("','")
			 .append(objBean.fileName)
			 .append("', 0,0,0,")
			 .append(System.currentTimeMillis())
			 .append(",'")
			 .append(objBean.lat)
			 .append("','")
			 .append(objBean.lon)
			 .append("'")
			 .append(")");
			
			Log.print("ImageBL |-> List " + Query.toString());
			objDBHelper.execute(Query.toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(this.getClass()+" :: Insert() ", e.getMessage());
		} finally {
			objDBHelper = null;
			Query = null;
		}
		
	}
	
	public void Update(ImageBean objBean)
	{
		DBHelper objDBHelper = new DBHelper();
		StringBuilder Query = new StringBuilder();
		try {
			
			Query.append("UPDATE Image ")
				 .append("SET ");
				 if(objBean.getDoEmail() != 0)
				 {
					 Query.append("DoEmail = ")
					 	  .append(objBean.getDoEmail());
				 }
				 if(objBean.getDoMMS() != 0)
				 {
					 Query.append("DoMMs =")
					 	  .append(objBean.getDoMMS());
				 }
				 if(objBean.getDoFacebook() != 0)
				 {
					 Query.append("DoFacebook = ")
					 	  .append(objBean.getDoFacebook());
				 }
				 Query.append(" WHERE ImageID = ")
				 	  .append(objBean.getImageID());
			 
			Log.print("ImageBL |-> List " + Query.toString());
			objDBHelper.execute(Query.toString());		
			
		} catch (Exception e) {
			Log.error(this.getClass() + " :: Update() ", e);
			e.printStackTrace();
		} finally {
			objDBHelper = null;
			Query = null;
		}
		
		return;
	}
	
	public int Delete(String strFileName){
		
		int Result = 0;
		DBHelper objDBHelper = new DBHelper();	
		StringBuilder Query = new StringBuilder();
		try {
			
			Query.append("Delete From ")
			.append("Image WHERE FileName='")
			 .append(strFileName)
			 .append("'");
			
			Log.print("ImageBL |-> List | Delete " + Query.toString());
			objDBHelper.execute(Query.toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(this.getClass()+" :: Delete() ", e.getMessage());
			Result = -1;
			
			
		} finally {
			objDBHelper = null;
			Query = null;
		}
		
		return Result;
		
	}
	
	public int GetNofImages(){
		
		DBHelper objDBHelper = new DBHelper();	
		StringBuilder Query = new StringBuilder();
		Cursor objCursor = null;
		int TotalImage = 0;
		try {
			
			Query.append("SELECT COUNT(ImageID) FROM Image");
			
			Log.print("ImageBL |-> MAX IMAGES " + Query.toString());
			objCursor = objDBHelper.query(Query.toString());
			objCursor.moveToPosition(-1);
			
			if(objCursor.getCount() > 0)
			{
				objCursor.moveToNext();
				TotalImage = objCursor.getInt(0);
			}
			
		} catch (Exception e) {
			Log.error("Image counter ", e.getMessage());
		} finally {
			objDBHelper = null;
			Query = null;
			objCursor.close();
			objCursor = null;
		}
		
		Log.print("ImageBL |-> Totals IMAGES " + TotalImage);
		return TotalImage;
	}
	
}
