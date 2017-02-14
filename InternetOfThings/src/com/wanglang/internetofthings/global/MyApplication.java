package com.wanglang.internetofthings.global;

import android.app.Application;

public class MyApplication extends Application{

	public static final int REQUEST_DEV_DETAILS_STATE = 1;//请求碼---设备详情
	public static final int REQUEST_DEV_ADD_STATE = 2;//请求碼---设备添加
	public static final int REQUEST_DEV_EDID_STATE = 3;//请求碼---设备编辑
	public static final int REQUEST_DEV_MENU_STATE = 4;//请求碼---设备menu
	
	public static final String PARAMETER_TO_DEV_DETAILS = "DeviceEntity";//参数---传输到设备详情
	
	
	public static final String PARAMETER_DEV_ONOFF = "DeviceOnOff";//参数---传输到设备详情
	public static final String PARAMETER_DEV_UPDATE = "UpdateDevice";//更新当前设备
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
}
