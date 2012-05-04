package com.esp.spycatch.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper() {
		super(Const.CONTEXT, Const.DB_NAME, null, 33);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void execute(String Statment) {
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			db.execSQL(Statment);
		} catch (Exception e) {
			Log.error("DataBaseHelper - ExecuteQuery", e);
			Log.print("SQL : ", Statment);
		} finally {
			db.close();
			db = null;
		}
	}

	public Cursor query(String Statment) {
		Cursor cur = null;
		SQLiteDatabase db = this.getWritableDatabase();

		try {
			cur = db.rawQuery(Statment, null);
			cur.moveToPosition(-1);
		} catch (Exception e) {
			Log.error("DataBaseHelper - ExecuteCursor", e);
		} finally {

			db.close();
			db = null;
		}

		return cur;
	}

	public static String getDBStr(String str) {

		str = str != null ? str.replaceAll("\'", "\'\'") : null;
		return str;

	}

	public void upgrade(int level) {
		switch (level) {
		case 0:
			doUpdate0();
		case 1:
			// doUpdate1();
		case 2:
			// doUpdate2();
		case 3:
			// doUpdate3();

		}
	}

	private void doUpdate0() {
		this.execute("CREATE TABLE Image (ImageID INTEGER PRIMARY KEY, FileName TEXT, Thumbnail Text, DoEmail Integer, DoFacebook Integer, DoMMS Integer, CreatedDate INTEGER, Lat TEXT, Lon TEXT)");
		
		
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '1','1',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '2','2',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '3','3',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '4','4',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '5','5',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '6','6',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '7','7',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '8','8',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '9','9',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '10','10',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '11','11',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '12','12',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '13','13',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '14','14',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '15','15',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '16','16',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '17','17',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '18','18',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '19','19',0,0,0,1200)");
//		this.execute("INSERT INTO Image (FileName, Thumbnail, DoEmail, DoMMs, DoFacebook, CreatedDate)Values ( '20','20',0,0,0,1200)");
		
		
	}
	
	public SQLiteDatabase getConnection() {
		SQLiteDatabase dbCon = this.getWritableDatabase();

		return dbCon;
	}	
}
