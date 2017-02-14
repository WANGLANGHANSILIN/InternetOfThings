package com.wanglang.internetofthings.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wanglang.internetofthings.R;

public class DialogUtil {

	public static AlertDialog showDialog(Context context,View view,String title){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setView(view);
		return builder.create();
		}
	
	public static AlertDialog showListDialog(Context context,ListView listView,String title){
		listView.setAdapter(new SimpleAdapter(context, getData(), R.layout.item_simple_dev, new String[]{"img","msg"}, new int[] {R.id.iv_dev_img,R.id.tv_dev_msg}));
		return showDialog(context, listView,title);
	}
	
	private static List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.ic_dev_add_normal);
        map.put("msg", "添加选中的设备");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.ic_dev_del_normal);
        map.put("msg", "移除选中的设备");
        list.add(map);
        return list;
	}
}
