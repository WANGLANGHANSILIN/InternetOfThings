package com.wanglang.internetofthings.utils;

import java.util.ArrayList;

import android.util.Log;

import com.wanglang.internetofthings.bean.DeviceEntity;

public class CompareUtil {

	public static boolean IsDeviceExits(ArrayList<DeviceEntity> entities,DeviceEntity entity){
		for (DeviceEntity deviceEntity : entities) {
			if (deviceEntity.getDevId() == entity.getDevId()) {
				Log.e("IsDeviceExits", ""+deviceEntity.toString()+" , "+entity.toString());
				return true;
			}
		}
		return false;
	}
	
	public static int getDeviceExitsIndex(ArrayList<DeviceEntity> entities,DeviceEntity entity){
		if (IsDeviceExits(entities, entity)) {
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i).getDevId() == entity.getDevId()) {
					return i;
				}
			}
		}
		return -1;
	}
}
