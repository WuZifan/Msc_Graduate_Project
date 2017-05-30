package com.wzf.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class InfoUtils {
	/**
	 * 由于logcat可能不好用，通过对话框的方式来进行debug
	 * @param string
	 */
	public static void showInfo(String string, Context context){
		// 1. 需要的是一个上下文对象
		AlertDialog.Builder builder=new Builder(context);
		// 2. 设置标题
		builder.setTitle("Debug信息");
		// 3. 设置内容
		builder.setMessage(string);
		// 4. 不需要点击事件，直接show
		builder.show();
	}
}
