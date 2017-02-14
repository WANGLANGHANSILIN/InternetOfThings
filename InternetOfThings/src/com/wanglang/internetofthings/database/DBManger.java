package com.wanglang.internetofthings.database;

import java.util.ArrayList;

import com.wanglang.internetofthings.bean.DeviceEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库操作---增删改查
 * @param deviceEntity
 */
public class DBManger {

	private DBHelp dbHelp;
	private SQLiteDatabase db;

	public DBManger(Context context){
		dbHelp = new DBHelp(context);
		db = dbHelp.getWritableDatabase();
	}
	
	/**
	 * 添加一个新设备到数据库
	 * @param deviceEntity
	 */
	public void addDevice(DeviceEntity deviceEntity){
		db.beginTransaction();
		try {
//			db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?)", new Object[]{person.name, person.age, person.info});  
			db.execSQL("INSERT INTO "+DBHelp.TABLE_NAME+" VALUES(null, ?, ?, ?, ?, ?)", 
					new Object[]{deviceEntity.getDevId(),deviceEntity.getDevName(),
					deviceEntity.getDevLocation(),String.valueOf(deviceEntity.isOnline()),
					String.valueOf(deviceEntity.isCheck())});
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}
	
	/**
	 * 从数据库更新一个设备
	 * @param deviceEntity
	 */
	public void updateDevice(DeviceEntity deviceEntity){
		
		ContentValues values = new ContentValues();
		values.put(DBHelp.SEGMENT_DEV_NAME, deviceEntity.getDevName());
		values.put(DBHelp.SEGMENT_DEV_SITE, deviceEntity.getDevLocation());
		values.put(DBHelp.SEGMENT_DEV_ONLINE, String.valueOf(deviceEntity.isOnline()));
		values.put(DBHelp.SEGMENT_DEV_CHECK, String.valueOf(deviceEntity.isCheck()));
		db.update(DBHelp.TABLE_NAME, values, DBHelp.SEGMENT_DEV_ID+" = ?", new String[]{String.valueOf(deviceEntity.getDevId())});
	}
	
	/**
	 * 从数据库删除一个设备
	 * @param deviceEntity
	 */
	public void delDevice(DeviceEntity deviceEntity){
		db.delete(DBHelp.TABLE_NAME, DBHelp.SEGMENT_DEV_ID+" = ?", new String[]{String.valueOf(deviceEntity.getDevId())});
	}
	
	
	/**
	 * 从数据库查询全部设备
	 * @param deviceEntity
	 */
	public ArrayList<DeviceEntity> queryAllDevice(){
		ArrayList<DeviceEntity> deviceEntities = new ArrayList<DeviceEntity>();
		Cursor cursor = db.rawQuery("SELECT * FROM "+DBHelp.TABLE_NAME, null);
		while (cursor.moveToNext()) {
			DeviceEntity entity = new DeviceEntity();
			entity.setDevId(cursor.getInt(cursor.getColumnIndex(DBHelp.SEGMENT_DEV_ID)));
			entity.setDevName(cursor.getString(cursor.getColumnIndex(DBHelp.SEGMENT_DEV_NAME)));
			entity.setDevLocation(cursor.getString(cursor.getColumnIndex(DBHelp.SEGMENT_DEV_SITE)));
			entity.setOnline(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBHelp.SEGMENT_DEV_ONLINE))));
			entity.setCheck(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(DBHelp.SEGMENT_DEV_CHECK))));
			deviceEntities.add(entity);
		}
		cursor.close();
		return deviceEntities;
	}
	
}
