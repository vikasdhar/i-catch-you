package com.esp.spycatch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {
	
	private static SharedPreferences sharedPreferences = null;
	
	public static void openPref(){
		
		sharedPreferences = Const.CONTEXT.getSharedPreferences(Const.PREF_FILE, Context.MODE_PRIVATE);
		
	}
	
	public static String getValue(String key, String defaultValue)
	{
		Pref.openPref();
		String result = Pref.sharedPreferences.getString(key, defaultValue);
		Pref.sharedPreferences = null;
		return result;
	}
	
	public static void setValue(String key, String value)
	{
		Pref.openPref();
		Editor prefsPrivateEditor = Pref.sharedPreferences.edit();
		prefsPrivateEditor.putString(key, value);
		prefsPrivateEditor.commit();
		prefsPrivateEditor = null;
		Pref.sharedPreferences = null;
	}		
}
