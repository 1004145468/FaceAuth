package com.xtu.faceauth.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.xtu.faceauth.App.TYApplication;
import com.xtu.faceauth.R;

public class ToastUtils {
	
	public static void show(String msg){
		Context context = TYApplication.getContext();
		Toast toast = new Toast(context);
		
		TextView view = new TextView(context);
		view.setBackgroundResource(R.drawable.toast_bg);
		view.setText(msg);
		view.setTextColor(Color.WHITE);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
}
