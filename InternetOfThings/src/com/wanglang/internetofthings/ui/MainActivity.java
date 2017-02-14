package com.wanglang.internetofthings.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanglang.internetofthings.R;
import com.wanglang.internetofthings.adpater.DeviceShowAdapter;
import com.wanglang.internetofthings.bean.DeviceEntity;
import com.wanglang.internetofthings.database.DBManger;
import com.wanglang.internetofthings.global.MyApplication;
import com.wanglang.internetofthings.view.GifView;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {

	private ArrayList<DeviceEntity> deviceEntities;
	private ListView decListView;
	private TextView devTitleView;

	private String mLocation[] = { "北京", "广州", "上海", "深圳", "扬州", "天津", "南京" };
	private DeviceShowAdapter adapter;
	private Button bt_dev_del;
	private Button bt_dev_add;
	private Button bt_dev_menu,bt_dev_configNet;
	
	private GifView gifView;

	
	public static int OnClickCount = 0;//用于记录点击del按钮的次数,偶数次显示checkBox
	private ProgressBar updateProgress;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			updateProgress.setProgress(msg.what);
		};
	};
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
		dataHandle();
		startTimer();
	}

	private static int count = 0;
	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(count);
				count++;
				if (count > 100) {
					count = 0;
				}
			}
		}, 0, 1000);
	}

	private void dataHandle() {
		adapter.setDeviceData(deviceEntities);
		decListView.setAdapter(adapter);
	}

	private void initView() {
		decListView = (ListView) findViewById(R.id.lv_show);
		decListView.setOnItemClickListener(this);
		decListView.setOnItemLongClickListener(this);
		
		updateProgress = (ProgressBar) findViewById(R.id.update_progress);
		updateProgress.setProgress(56);

		View includeView = findViewById(R.id.il_title);
		includeView.setBackgroundColor(getResources().getColor(R.color.white));
		devTitleView = (TextView) includeView.findViewById(R.id.tv_title_cent);
		devTitleView.setText("远程设备");
		devTitleView.setTextSize(22);
		devTitleView.setTextColor(getResources().getColor(R.color.red));

		bt_dev_del = (Button) includeView.findViewById(R.id.bt_title_right);
		bt_dev_del.setBackgroundResource(R.drawable.del_background);
		bt_dev_del.setOnClickListener(this);

		bt_dev_add = (Button) includeView.findViewById(R.id.bt_title_dsd);
		bt_dev_add.setBackgroundResource(R.drawable.add_background);
		bt_dev_add.setOnClickListener(this);

		bt_dev_menu = (Button) includeView.findViewById(R.id.bt_title_left);
		bt_dev_menu.setBackgroundResource(R.drawable.menu_background);
		bt_dev_menu.setOnClickListener(this);
		
		bt_dev_configNet = (Button) includeView.findViewById(R.id.bt_title_left1);
		bt_dev_configNet.setTextColor(getResources().getColor(R.color.iconBlueColor));
		bt_dev_configNet.setBackgroundResource(R.drawable.confignet_background);
		bt_dev_configNet.setOnClickListener(this);
		
		adapter = new DeviceShowAdapter(this);
		
//		gifView = (GifView) findViewById(R.id.gifView);
//		gifView.setGifResource(R.raw.gif_jjj);
//		gifView.play();
		
		
	}

	private void initData() {
		deviceEntities = new ArrayList<DeviceEntity>();
		DBManger dbManger = new DBManger(this);
		ArrayList<DeviceEntity> queryAllDevice = dbManger.queryAllDevice();
		if (queryAllDevice == null || queryAllDevice.size() == 0) {
			Log.e("initData", "query date is null");
			for (int i = 0; i < 20; i++) {
				double random = Math.random();
				Log.e("initData", "random:" + random);
				int j = (int) (6 * random);
				int g = (int) (100 * random);
				Log.e("initData", "j = " + j + ", g = " + g + ",  "
						+ mLocation.length);
				deviceEntities.add(new DeviceEntity(i, "Device-" + (i + 1) + "-号",
						mLocation[g % 7], (g % 3 == 0 ? true : false)));
			}
		}else{
			deviceEntities.addAll(queryAllDevice);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		
		case R.id.bt_title_right://del
			Log.i("onClick", "OnClickCount:" + MainActivity.OnClickCount);
			
			if (OnClickCount % 2 == 0) {
				// 通知Adapter显示checkbox
				bt_dev_del.setBackgroundResource(R.drawable.del_background1);
				bt_dev_add.setVisibility(View.INVISIBLE);
				bt_dev_configNet.setVisibility(View.INVISIBLE);
				bt_dev_menu.setVisibility(View.INVISIBLE);
			} else {
				bt_dev_del.setBackgroundResource(R.drawable.del_background);
				if (adapter.getCheckDevices().size() > 0) {
					adapter.removeSelectDevice();
				} else {
					ShowToast("当前沒有选择设备");
				}
				bt_dev_add.setVisibility(View.VISIBLE);
				bt_dev_configNet.setVisibility(View.VISIBLE);
				bt_dev_menu.setVisibility(View.VISIBLE);
			}
			OnClickCount++;
			adapter.notifyDataSetChanged();
			break;

		case R.id.bt_title_dsd://add
			startDeviceActivity(-1,MyApplication.REQUEST_DEV_ADD_STATE,MyApplication.PARAMETER_TO_DEV_DETAILS);
			break;

		case R.id.bt_title_left://menu
			ShowToast("Menu...");
			startDeviceActivity(-1, MyApplication.REQUEST_DEV_MENU_STATE, MyApplication.PARAMETER_TO_DEV_DETAILS);
			break;
		case R.id.bt_title_left1:
			ShowToast("空中配网");
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long arg3) {

		Log.i("onItemClick", "position" + position + "   " + parent.getId());

		if (parent.getId() == R.id.lv_show) {
			//选中操作
			if (OnClickCount % 2 != 0) {
				Log.i("onItemClick", "OnClickCount:"+OnClickCount+", position:"+position);
				if (adapter.getItem(position).isCheck()) {
					adapter.getItem(position).setCheck(false);
				}else {
					adapter.getItem(position).setCheck(true);
				}
				adapter.notifyDataSetChanged();
			}
			else{
				startDeviceActivity(position,MyApplication.REQUEST_DEV_DETAILS_STATE,MyApplication.PARAMETER_TO_DEV_DETAILS);
			}
		}
	}

	//启动activity，查看设备信息
	private void startDeviceActivity(int position,int requestnum,String requestPara) {
		Intent intent = new Intent(this, DeviceInfoActivity.class);
		intent.setFlags(requestnum);
		if (position >= 0) {
			intent.putExtra(requestPara,adapter.getItem(position));
			Log.i("startDeviceActivity", "position = "+position+", "+(adapter.getItem(position)==null));
		}
		startActivityForResult(intent,requestnum);
	}

	@Override//长按操作
	public boolean onItemLongClick(AdapterView<?> parent, View arg1,
			int position, long arg3) {
		Log.i("onItemLongClick", "position" + position + "   " + parent.getId());
		startDeviceActivity(position,MyApplication.REQUEST_DEV_EDID_STATE,MyApplication.PARAMETER_TO_DEV_DETAILS);
		return true;
	}

	//用于接受上个界面返回的信息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			DeviceEntity entity = null;
			Log.i("onActivityResult", "requestCode :"+requestCode+",  resultCode :"+resultCode);
			switch (requestCode) {
			case MyApplication.REQUEST_DEV_DETAILS_STATE:
				entity = (DeviceEntity) data.getSerializableExtra(MyApplication.PARAMETER_DEV_ONOFF);
				//更新当前设备信息
				adapter.upDeviceDate(entity);
				break;

			case MyApplication.REQUEST_DEV_ADD_STATE:
			case MyApplication.REQUEST_DEV_EDID_STATE:
				entity = (DeviceEntity) data.getSerializableExtra(MyApplication.PARAMETER_DEV_UPDATE);
				//更新当前设备信息
				adapter.addDevice(entity);
				break;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

}
