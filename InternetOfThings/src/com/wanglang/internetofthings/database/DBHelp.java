package com.wanglang.internetofthings.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 创建数据库
 * @author wangl
 *
 */
public class DBHelp extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "deviceDatas.db";
	public static final String TABLE_NAME = "devices";
	public static final int DATABASE_VERSION = 1;
	
	public static final String SEGMENT_DEV_ID = "DevID";
	public static final String SEGMENT_DEV_NAME = "DevName";
	public static final String SEGMENT_DEV_SITE = "DevSite";
	public static final String SEGMENT_DEV_ONLINE = "DevOnline";
	public static final String SEGMENT_DEV_CHECK = "DevCheck";
	

	public DBHelp(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DBHelp(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+SEGMENT_DEV_ID+" INTEGER,"+SEGMENT_DEV_NAME+" TEXT,"
				+SEGMENT_DEV_SITE+" TEXT,"+SEGMENT_DEV_ONLINE+" TEXT,"
				+SEGMENT_DEV_CHECK+" TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN other STRING");
	}

}
