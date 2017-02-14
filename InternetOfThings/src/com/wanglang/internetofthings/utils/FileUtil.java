package com.wanglang.internetofthings.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.wanglang.internetofthings.R;

public class FileUtil {


	// 读取配置文件
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
	public static Properties readRawConfig(Context context) throws IOException {
		Properties prop = new Properties();
		InputStream in = context.getResources().openRawResource(R.raw.config);
		prop.load(in);
		return prop;
	}
	
	public static void updataRawConfig(Properties properties){
		try {
			File file = new File("res/raw/config.properties");
			Log.e("writeRawConfig", "Path : "+file.getAbsolutePath());
			FileOutputStream outputStream = new FileOutputStream(file, true);
			properties.store(outputStream, "");
			Log.e("writeRawConfig", "Success----"+properties.get("ID"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
