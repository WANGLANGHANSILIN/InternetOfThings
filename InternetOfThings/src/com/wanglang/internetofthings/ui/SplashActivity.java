package com.wanglang.internetofthings.ui;

import java.io.IOException;
import java.util.Properties;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.wanglang.internetofthings.R;
import com.wanglang.internetofthings.utils.FileUtil;

public class SplashActivity extends BaseActivity implements OnClickListener{
	
	public static final int INTO_MAINACTIVITY = 1;
	public static final int ERR_MESSAGE_SHOW = 2;
	
	private String err_msg = "连接服务器错误.请于供应商联系 或访问一下\n网址: http://www.baidu.com \n或发送email:" +
			"123456@qq.com"; 
	
	private int[] randomNum = new int[]{1,2,2,1,2,1,2,1,1,2,1,2,1,2,1};
	
	private View inflate;
	private TextView tv_err_info,viewTitle;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INTO_MAINACTIVITY:
				startActivity(new Intent(SplashActivity.this,  MainActivity.class)); // 显示第2屏
				SplashActivity.this.finish();   // 
				break;

			case ERR_MESSAGE_SHOW:
				ShowPopWindows(viewTitle, inflate);
				break;
			default:
				startActivity(new Intent(SplashActivity.this,  MainActivity.class)); // 显示第2屏
				SplashActivity.this.finish();   // 
					break;
			}
		};
	};
	private AlertDialog dialog;
	private RelativeLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
//		loadConfigFile();
		connectServer();
	}
	
	private void loadConfigFile() {
		try {
			Properties properties = FileUtil.readRawConfig(this);
			ServerSite = properties.getProperty("ServerSite");
			ServerPort = properties.getProperty("ServerPort");
			ID = properties.getProperty("ID");
			AuthenCode = properties.getProperty("AuthenCode");
			Log.i("onCreate", "ServerSite = "+ServerSite+",ServerPort"+ServerPort+
					"ID = "+ID+",AuthenCode = "+AuthenCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	ShowPopWindows(ViewTitle,inflate);
	

	private void connectServer() {
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				int i = (int) (Math.random()*14);
				Log.i("connectServer", "i = "+i);
				handler.sendEmptyMessage(randomNum[i]);
			}
		},2000);
	}

	private void initView() {
		viewTitle = (TextView) findViewById(R.id.app_title_name);
		/****************************************************************/
		inflate = getLayoutInflater().inflate(R.layout.dialog_connect_server, null);
	
		inflate.findViewById(R.id.bt_dialog_exit).setOnClickListener(this);
		inflate.findViewById(R.id.bt_dialog_retry).setOnClickListener(this);
		
		tv_err_info = (TextView) inflate.findViewById(R.id.tv_dialog_err_info);
		tv_err_info.setText(err_msg);
		tv_err_info.setTextSize(25);
		tv_err_info.setLinksClickable(true);
		tv_err_info.setLinkTextColor(getResources().getColor(R.color.darkblue));
	}


	public void ShowPopWindows(View parent,View popupView){
		layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new LayoutParams(100, 100);
		layout.setLayoutParams(params);
		layout.addView(inflate);
		
        AlertDialog.Builder builder = new Builder(this);
        builder.setView(layout);
        dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
	}


	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bt_dialog_exit:
			SplashActivity.this.finish();
			System.exit(0);
			break;
			
		case R.id.bt_dialog_retry:
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			layout.removeView(inflate);
			connectServer();
			break;
		}
	}
}
