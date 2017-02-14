package com.wanglang.internetofthings.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends Activity{

	private static Toast toast;
	
	public static String ServerSite;
	public static String ServerPort;
	public static String ID;
	public static String AuthenCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public void ShowToast(String msg){
		ShowToast(this, msg);
	}
	
	public static void ShowToast(Context context,String msg){
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		toast.setText(msg);
		toast.show();
	}
	
	 private long clickTime=0;
	 @Override  
	        public boolean onKeyDown(int keyCode, KeyEvent event) {  
	            if (keyCode == KeyEvent.KEYCODE_BACK) {  
	                exit();  
	                return true;  
	            }  
	            return super.onKeyDown(keyCode, event);  
	        }  
	      
	        private void exit() {  
	            if ((System.currentTimeMillis() - clickTime) > 2000) {  
	                ShowToast(getApplicationContext(), "再次点击退出");
	                clickTime = System.currentTimeMillis();  
	            } else {  
	                this.finish();  
	                System.exit(0);  
	            }  
	        }  
}
