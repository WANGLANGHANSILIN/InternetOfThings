package com.wanglang.internetofthings.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanglang.internetofthings.R;
import com.wanglang.internetofthings.bean.DeviceEntity;
import com.wanglang.internetofthings.database.DBManger;
import com.wanglang.internetofthings.ui.BaseActivity;
import com.wanglang.internetofthings.ui.MainActivity;
import com.wanglang.internetofthings.utils.CompareUtil;

public class DeviceShowAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<DeviceEntity> mDeviceEntities;//设备数据源


//	private static HashMap<Integer, Boolean> isSelected;
	
	private SparseArray<DeviceEntity> mArray;//记录当前设备是被选中的
	private SparseBooleanArray isSelected;//记录当前item是被选中的
	private DBManger dbManger;

	public DeviceShowAdapter(Context context) {
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		
		dbManger = new DBManger(mContext);
		
		mArray = new SparseArray<DeviceEntity>();
		isSelected = new SparseBooleanArray();
	}

	//设置数据源
	public void setDeviceData(ArrayList<DeviceEntity> deviceEntities) {
		this.mDeviceEntities = deviceEntities;
		initDate();
		notifyDataSetChanged();
	}

	// 初始化isSelected的数据，默认为false
	private void initDate() {
		for (int i = 0; i < mDeviceEntities.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return mDeviceEntities.size();
	}

	@Override
	public DeviceEntity getItem(int position) {
		return mDeviceEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lv_item_adapter_dev, null);
		}
		boolean online = getItem(position).isOnline();
		convertView.setBackgroundResource(R.drawable.adapter_item_shape);

		DevHodler devHodler = DevHodler.getInstance(convertView);
		devHodler.iv_head
				.setImageResource(online ? R.drawable.ic_dev_logo_light
						: R.drawable.ic_dev_logo_gray);

		devHodler.tv_devID.setText(getItem(position).getDevId()+"");
		devHodler.tv_devID.setTextColor(mContext.getResources().getColor(
				online ? R.color.chocolate : R.color.lightgray));
		
		
		devHodler.tv_devName.setText(getItem(position).getDevName());
		devHodler.tv_devName.setTextSize(20);
		devHodler.tv_devName.setTextColor(mContext.getResources().getColor(
				online ? R.color.colorPrimary : R.color.lightgray));

		devHodler.tv_devLoaction.setText(getItem(position).getDevLocation());
		devHodler.tv_devLoaction.setTextColor(mContext.getResources().getColor(
				online ? R.color.thistle : R.color.lightgray));


		if (MainActivity.OnClickCount % 2 != 0) {
			devHodler.cb_check.setVisibility(View.VISIBLE);
			initDate();
		} else {
			devHodler.cb_check.setVisibility(View.INVISIBLE);
		}
		if (getItem(position).isCheck()) {
			isSelected.put(position, true);
			mArray.put(position, getItem(position));
		} else {
			isSelected.put(position, false);
			mArray.remove(position);
		}
		devHodler.cb_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isSelected.get(position) || getItem(position).isCheck()) {
					isSelected.put(position, false);
					mArray.remove(position);
					getItem(position).setCheck(false);
				} else {
					isSelected.put(position, true);
					mArray.put(position, getItem(position));
					getItem(position).setCheck(true);
				}
				Log.i("setOnClickListener", "position:"+position+"  "+getItem(position).isCheck());
			}
		});
		devHodler.cb_check.setChecked(isSelected.get(position));
		return convertView;
	}

	// 获取选中的设备数据列表
	public ArrayList<DeviceEntity> getCheckDevices() {
		ArrayList<DeviceEntity> entities = new ArrayList<DeviceEntity>();
		
		for (int i = 0; i < mArray.size(); i++) {
			if (mArray.keyAt(i) >= 0) {
				entities.add(mArray.get(mArray.keyAt(i)));
			}
		}
	
		Log.e("getCheckDevices", "getCheckDevices : " + entities.size());
		return entities;
	}

	// 删除设备
	public void removeDevice(DeviceEntity deviceEntity) {
		mDeviceEntities.remove(deviceEntity);
		dbManger.delDevice(deviceEntity);
		notifyDataSetChanged();
	}

	//添加一个设备，判断添加的设备ID是否存在，如果存在就更新设备，否则就添加设备
	public void addDevice(DeviceEntity deviceEntity) {
		if (!CompareUtil.IsDeviceExits(mDeviceEntities, deviceEntity)) {
			// 当前设备列表中设备不存在，需要添加新的设备
			ArrayList<DeviceEntity> arrayList = new ArrayList<DeviceEntity>();
			arrayList.addAll(mDeviceEntities);
			Log.d("addDevice", "" + deviceEntity.getDevName() + ", "
					+ deviceEntity.isOnline());
			mDeviceEntities.clear();
			arrayList.add(0, deviceEntity);
			dbManger.addDevice(deviceEntity);
			setDeviceData(arrayList);
			BaseActivity.ShowToast(mContext, "添加了一个新的设备...");

		} else {
			// 更新当前操作的设备
			upDeviceDate(deviceEntity);
			BaseActivity.ShowToast(mContext, "该设备已在设备列表中存在了，更新当前设备列表...");
		}
	}

	public void removeSelectDevice() {
		for (DeviceEntity deviceEntity2 : getCheckDevices()) {
			removeDevice(deviceEntity2);
		}
	}

	private static class DevHodler {

		private TextView tv_devName, tv_devLoaction, tv_devID;
		private CheckBox cb_check;
		private ImageView iv_head;

		private DevHodler(View convertView) {
			tv_devID = (TextView) convertView.findViewById(R.id.tv_dev_id);
			tv_devName = (TextView) convertView.findViewById(R.id.tv_dev_name);
			tv_devLoaction = (TextView) convertView
					.findViewById(R.id.tv_dev_locat);
			cb_check = (CheckBox) convertView.findViewById(R.id.cb_check);
			iv_head = (ImageView) convertView.findViewById(R.id.iv_lv_head);
		}

		public static DevHodler getInstance(View convertView) {
			DevHodler hodler = (DevHodler) convertView.getTag();
			if (hodler == null) {
				hodler = new DevHodler(convertView);
				convertView.setTag(hodler);
			}
			return hodler;
		}
	}

	//更新当前设备信息
	public void upDeviceDate(DeviceEntity entity) {
		if (CompareUtil.IsDeviceExits(mDeviceEntities, entity)) {
			int index = CompareUtil
					.getDeviceExitsIndex(mDeviceEntities, entity);
			mDeviceEntities.set(index, entity);
			dbManger.updateDevice(entity);
			Log.i("upDeviceDate", mDeviceEntities.get(index).toString());
		}
		notifyDataSetChanged();
	}

}
