package com.wanglang.internetofthings.ui;

import java.io.IOException;
import java.util.Properties;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanglang.internetofthings.R;
import com.wanglang.internetofthings.bean.DeviceEntity;
import com.wanglang.internetofthings.global.MyApplication;
import com.wanglang.internetofthings.utils.FileUtil;
/**
 * 设备Dialog处理操作，包括查看、编辑、添加、菜单设置等
 * @author wangl
 *
 */
public class DeviceInfoActivity extends BaseActivity implements OnClickListener{
	
	private View devEditView,devStateView,devSettingView;
	
	/***********设备显示***************/
	private Button bt_OnOff;
	private TextView tv_Online_State,tv_OnOff_State,tv_devName,tv_Location,tv_devID;
	private ImageView iv_Online_Icon,iv_OnOff_Icon,iv_devName_Icon,iv_Location_Icon,iv_devID_Icon;
	
	/***********设备编辑、添加***************/
	private TextView tv_edit_title;
	private EditText et_devId,et_devName,et_devLocal;
	private Button bt_Confirm,bt_Cancle;
	private EditText mDevIP,mDevPort,mDevID,mDevAuth;
	private Button bt_menuConfirm;
	
	/***********Menu设置***************/
	private String ServerSite;
	private String ServerPort;
	private String ID;
	private String AuthenCode;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_devinfo);
		
		initView();
		
		initData();
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		View view = getWindow().getDecorView();
		WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view
				.getLayoutParams();
		lp.gravity = Gravity.CENTER;
		lp.width = (int) (dm.widthPixels * 4/5);
		lp.height = (int) (dm.widthPixels * 1);
		getWindowManager().updateViewLayout(view, lp);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		view.setBackgroundResource(R.drawable.dialog_activity_bg);
	}

	private void initView() {
		devEditView = findViewById(R.id.il_dialog_dev_edit);
		devStateView = findViewById(R.id.il_dialog_dev_state);
		devSettingView = findViewById(R.id.il_dialog_dev_setting);
		
		devEditView.setVisibility(View.VISIBLE);
		devStateView.setVisibility(View.VISIBLE);
		devSettingView.setVisibility(View.VISIBLE);
		initDialogView();
	}

	private void initData() {
		DeviceEntity deviceEntity = null;
		Intent intent = getIntent();
		if (intent == null) {
			Log.i("initData", "intent = "+(intent == null));
			return;
		}
		if (MyApplication.REQUEST_DEV_DETAILS_STATE == intent.getFlags()
				|| MyApplication.REQUEST_DEV_EDID_STATE == intent.getFlags()) {
			deviceEntity = (DeviceEntity) intent.getSerializableExtra(MyApplication.PARAMETER_TO_DEV_DETAILS);
			if (deviceEntity == null) {
				Log.i("initData", "deviceEntity = "+(deviceEntity == null));
				return;
			}
		}
		Log.i("initData", ""+intent.getFlags());
		switch (intent.getFlags()) {
		case MyApplication.REQUEST_DEV_DETAILS_STATE://设备状态查看
			showDeviceInfo(deviceEntity);
			break;
			
		case MyApplication.REQUEST_DEV_EDID_STATE://设备编辑
			bt_Confirm.setTag(R.id.tag_first, MyApplication.REQUEST_DEV_EDID_STATE);
			EditDeviceInfo(deviceEntity);
			break;
			
		case MyApplication.REQUEST_DEV_ADD_STATE://设备添加
			devStateView.setVisibility(View.GONE);
			devSettingView.setVisibility(View.GONE);
			devEditView.setVisibility(View.VISIBLE);
			tv_edit_title.setText("添加设备信息");
			bt_Confirm.setTag(R.id.tag_first, MyApplication.REQUEST_DEV_ADD_STATE);
			break;
			
		case MyApplication.REQUEST_DEV_MENU_STATE://系统设置
			showMenuSetting();
			break;

		default:
			break;
		}
	}

	private void showMenuSetting() {
		devStateView.setVisibility(View.GONE);
		devEditView.setVisibility(View.GONE);
		devSettingView.setVisibility(View.VISIBLE);
		
		SharedPreferences preferences = getSharedPreferences("config", MODE_APPEND);
		 ServerSite = preferences.getString("ServerSite", "");
		 ServerPort = preferences.getString("ServerPort", "");
		 ID = preferences.getString("ID", "");
		 AuthenCode = preferences.getString("AuthenCode", "");
		
		if (ServerSite.equals("") && ServerSite.equals("") && ID.equals("") && AuthenCode.equals("")) {
			try {
				Properties readProperties = FileUtil.readRawConfig(this);
				 ServerSite = readProperties.getProperty("ServerSite");
				 ServerPort = readProperties.getProperty("ServerPort");
				 ID = readProperties.getProperty("ID");
				 AuthenCode = readProperties.getProperty("AuthenCode");
				
				Editor edit = preferences.edit();
				edit.putString("ServerSite", ServerSite);
				edit.putString("ServerPort", ServerPort);
				edit.putString("ID", ID);
				edit.putString("AuthenCode", AuthenCode);
				edit.commit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			mDevIP.setText(ServerSite);
			mDevPort.setText(ServerPort);
			mDevID.setText(ID);
			mDevAuth.setText(AuthenCode);
		}
	}
	

	private void initDialogView() {
		/*****************************设备状态显示的View**********************************/
		tv_devID = (TextView) devStateView.findViewById(R.id.tv_dialog_dev_id);
		iv_devID_Icon = (ImageView) devStateView.findViewById(R.id.iv_dialog_dev_id);
		
		tv_devName = (TextView) devStateView.findViewById(R.id.tv_dialog_dev_name);
		iv_devName_Icon = (ImageView) devStateView.findViewById(R.id.iv_dialog_dev_name);
		
		tv_Online_State = (TextView) devStateView.findViewById(R.id.tv_dialog_online_state);
		iv_Online_Icon = (ImageView) devStateView.findViewById(R.id.iv_dialog_online);
		
		tv_OnOff_State = (TextView) devStateView.findViewById(R.id.tv_dialog_on_off_state);
		iv_OnOff_Icon = (ImageView) devStateView.findViewById(R.id.iv_dialog_on_off);
		
		tv_Location = (TextView) devStateView.findViewById(R.id.tv_dialog_dev_locat);
		iv_Location_Icon = (ImageView) devStateView.findViewById(R.id.iv_dialog_dev_locat);
		
		bt_OnOff = (Button) devStateView.findViewById(R.id.bt_dialog_on_off);
		bt_OnOff.setOnClickListener(this);
		
		devStateView.findViewById(R.id.bt_dialog_on_off_cancle).setOnClickListener(this);
		
		/******************************添加新设备显示的View*********************************/
		tv_edit_title = (TextView) devEditView.findViewById(R.id.tv_dialog_edit_title);
		et_devId = (EditText) devEditView.findViewById(R.id.et_dev_id);
		et_devName = (EditText) devEditView.findViewById(R.id.et_dev_name);
		et_devLocal = (EditText) devEditView.findViewById(R.id.et_dev_loca);
		bt_Confirm = (Button) devEditView.findViewById(R.id.bt_dev_add_confirm);
		bt_Cancle = (Button) devEditView.findViewById(R.id.bt_dev_add_cancle);
		bt_Confirm.setOnClickListener(this);
		bt_Cancle.setOnClickListener(this);
		
		/******************************设备设置IP和端口*********************************/
		mDevIP = (EditText) devSettingView.findViewById(R.id.et_menu_ip);
		mDevPort = (EditText) devSettingView.findViewById(R.id.et_menu_port);
		mDevID = (EditText) devSettingView.findViewById(R.id.et_menu_id);
		mDevAuth = (EditText) devSettingView.findViewById(R.id.et_menu_authen);
		
		bt_menuConfirm = (Button) devSettingView.findViewById(R.id.bt_menu_confirm);
		bt_menuConfirm.setOnClickListener(this);
		devSettingView.findViewById(R.id.bt_menu_cancle).setOnClickListener(this);
	}
	
	private void AddDeviceInfo() {
		
		DeviceEntity deviceEntity = new DeviceEntity();
		deviceEntity.setDevLocation(et_devLocal.getText().toString());
		
		if (!TextUtils.isEmpty(et_devId.getText().toString())) {
			deviceEntity.setDevId(Integer.valueOf(et_devId.getText().toString()));
		}else {
			ShowToast("设备ID不能为空!");
			return;
		}
		
		if (!TextUtils.isEmpty(et_devName.getText().toString())) {
			deviceEntity.setDevName(et_devName.getText().toString());
		}else {
			ShowToast("设备名称不能为空!");
			return;
		}
		deviceEntity.setOnline(true);
		deviceEntity.setCheck(false);
		bt_Confirm.setTag(R.id.tag_second,deviceEntity);
	}
	
	
	private void EditDeviceInfo(DeviceEntity deviceEntity) {
		devStateView.setVisibility(View.GONE);
		devSettingView.setVisibility(View.GONE);
		devEditView.setVisibility(View.VISIBLE);
		
		tv_edit_title.setText("编辑设备信息");
		
		et_devId.setText(deviceEntity.getDevId()+"");
		et_devId.setFocusable(false);
		et_devName.setText(deviceEntity.getDevName());
		et_devLocal.setText(deviceEntity.getDevLocation());
		
		bt_Confirm.setTag(R.id.tag_second,deviceEntity);
	}
	
	private void showDeviceInfo(DeviceEntity deviceEntity) {
		devStateView.setVisibility(View.VISIBLE);
		devSettingView.setVisibility(View.GONE);
		devEditView.setVisibility(View.GONE);
		
		boolean online = deviceEntity.isOnline();
		
		tv_devID.setText(deviceEntity.getDevId()+"");
		iv_devID_Icon.setImageResource(R.drawable.ic_menu_id);
		
		tv_devName.setText(deviceEntity.getDevName());
		iv_devName_Icon.setImageResource(R.drawable.ic_dev_icon_name);
		
		tv_Location.setText(deviceEntity.getDevLocation());
		iv_Location_Icon.setImageResource(R.drawable.ic_dev_icon_locat);
		
		bt_OnOff.setText(online?"关机":"开机");
		bt_OnOff.setTag(deviceEntity);
		Log.i("showDeviceInfo", ""+((DeviceEntity)bt_OnOff.getTag()).getDevName());
		bt_OnOff.setBackgroundResource(R.drawable.bt_background_select);
		
		tv_Online_State.setText(online?"在线":"离线");
		iv_Online_Icon.setImageResource(online?R.drawable.ic_dev_online_light:R.drawable.ic_dev_online_gray);
		
		tv_OnOff_State.setText(online?"开机":"关机");
		iv_OnOff_Icon.setImageResource(online?R.drawable.ic__dev_on_light:R.drawable.ic__dev_on_gray);
	}
	
	@Override
	public void onClick(View view) {
		DeviceEntity entity = null;
		switch (view.getId()) {
		case R.id.bt_dev_add_confirm:
			if (bt_Confirm.getTag(R.id.tag_first) == Integer.valueOf(MyApplication.REQUEST_DEV_ADD_STATE)) {
				AddDeviceInfo();
			}
			entity = (DeviceEntity) bt_Confirm.getTag(R.id.tag_second);
			if (entity == null) {
				return;
			}
			Log.i("onClick", ""+entity.toString());
			entity.setDevLocation(et_devLocal.getText().toString());
			entity.setDevName(et_devName.getText().toString());
			Log.d("onClick", ""+entity.toString());
			setReslutDeviceListActivity(entity, MyApplication.PARAMETER_DEV_UPDATE);
			break;
		case R.id.bt_menu_confirm:
				saveConfig();
		case R.id.bt_menu_cancle:
		case R.id.bt_dev_add_cancle:
		case R.id.bt_dialog_on_off_cancle:
			finish();
			break;
		case R.id.bt_dialog_on_off:
			entity = (DeviceEntity) bt_OnOff.getTag();
			if (entity == null) {
				return;
			}
			if (bt_OnOff.getText().toString().equals("开机")) {//开机操作
				entity.setOnline(true);
				Log.e("onClick", "bt_dialog_on_off ds" +bt_OnOff.getText().toString());
			}else{//关机操作
				entity.setOnline(false);
			}
			Log.e("onClick", "bt_dialog_on_off " +entity.isOnline());
			setReslutDeviceListActivity(entity,MyApplication.PARAMETER_DEV_ONOFF);
			break;
		}
	}

	private void saveConfig() {
		
		String ip = mDevIP.getText().toString();
		String port = mDevPort.getText().toString();
		String id = mDevID.getText().toString();
		String auth = mDevAuth.getText().toString();
		
		Log.i("bt_menu_confirm", "ip:"+ip+",port:"+port+",id:"+id+",auth:"+auth);
		
		if (ip.equals("")) {
			ShowToast("IP地址不能为空!");
		}else if (id.equals("")) {
			ShowToast("ID不能为空!");
		}else if (port.equals("")) {
			ShowToast("端口不能为空!");
		}else if (auth.equals("")) {
			ShowToast("授权码不能为空!");
		}
//		if ((!AuthenCode.equals("") && auth.equals(auth))
//				&& (!ID.equals("") && id.equals(id))
//				&& (!ServerPort.equals("") && port.equals(port))
//				&& (!ServerSite.equals("") && ip.equals(ip))) {
//			Log.i("saveConfig", "配置没有修改");
//		}else
		{
			Editor edit = getSharedPreferences("config", MODE_APPEND).edit();
			edit.putString("ServerSite", ip);
			edit.putString("ServerPort", port);
			edit.putString("ID", id);
			edit.putString("AuthenCode", auth);
			edit.commit();
		}
	}

	private void setReslutDeviceListActivity(DeviceEntity entity,String onoff ) {
		Intent intent = new Intent();
		intent.putExtra(onoff, entity);
		setResult(RESULT_OK,intent);
		finish();
	}
}
