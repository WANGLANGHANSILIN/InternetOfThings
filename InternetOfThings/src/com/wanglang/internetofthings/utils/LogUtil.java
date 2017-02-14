package com.wanglang.internetofthings.utils;

import android.util.Log;

public class LogUtil {
	
	public static final int currentGrade = 5;
	public static final int iGrade = 1;
	public static final int wGrade = 2;
	public static final int eGrade = 3;

	public static void i(String tag ,String msg)
	{
		Log.i(tag, msg);
	}
	
	public static void w(String tag ,String msg)
	{
		Log.w(tag, msg);
	}
	
	public static void e(String tag ,String msg)
	{
		if (currentGrade >= eGrade) {
			Log.e(tag, msg);
		}
	}
}
