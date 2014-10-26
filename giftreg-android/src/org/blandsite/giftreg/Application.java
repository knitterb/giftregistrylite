package org.blandsite.giftreg;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Application extends android.app.Application {
	
	public static String username = "";
	public static String token = "";
	
	public Application() {
		super();
	}
	
	public void toast (String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public SharedPreferences getPrefs() {
		 return getSharedPreferences("org.blandsite.giftreg", 0);
	}
	
}
