package com.wanglang.internetofthings.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MyTextView extends TextView{

	private Context context;
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public MyTextView(Context context) {
		this(context,null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			return super.onTouchEvent(event);
		} catch (Exception e) {
			Toast.makeText(context, "链接出错", 1000).show();
		}
		return false;
	}

}
