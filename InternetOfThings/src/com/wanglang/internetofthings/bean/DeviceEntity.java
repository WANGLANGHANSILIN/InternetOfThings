package com.wanglang.internetofthings.bean;

import java.io.Serializable;




/**
 * 
 * @author wangl
 *	设备对象描述
 */
public class DeviceEntity implements Serializable{

	private int devId;//设备ID
	private String devName;//设备名称
	private String devLocation;//设备位置
	private boolean isOnline;//设备是否在线
	
	private boolean isCheck;//设备是否选中
	
	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "DeviceEntity [devId=" + devId + ", devName=" + devName
				+ ", isOnline=" + isOnline + "]";
	}

	public DeviceEntity(){
		
	}
	
	public DeviceEntity(int devId,String devName,String devLocation,boolean isOnline){
		setDevId(devId);
		setDevName(devName);
		setDevLocation(devLocation);
		setOnline(isOnline);
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public int getDevId() {
		return devId;
	}
	public void setDevId(int devId) {
		this.devId = devId;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevLocation() {
		return devLocation;
	}
	public void setDevLocation(String devLocation) {
		this.devLocation = devLocation;
	}
	
}
