package com.lang.acrapp.util;

import android.content.Context;
import android.widget.Toast;

 public class ToastUtil {

	public static void print(Context context,String str,int duration){
		Toast.makeText(context, str, duration).show();
	};
	public static void show(Context context,int str,int duration){
		Toast.makeText(context, str, duration).show();
	};
}
