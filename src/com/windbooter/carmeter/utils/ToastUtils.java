package com.windbooter.carmeter.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 */
public class ToastUtils {

	private ToastUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;// 是否需要显示，可以在application的onCreate函数里面初始化

	private static List<Toast> list = new ArrayList<Toast>();

	private static void clear() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).cancel();
			list.remove(i);
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {

		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			list.add(toast);
			toast.show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			list.add(toast);
			toast.show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			list.add(toast);
			toast.show();
		}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			list.add(toast);
			toast.show();
		}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, duration);
			list.add(toast);
			toast.show();
		}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow) {
			clear();
			Toast toast = Toast.makeText(context, message, duration);
			list.add(toast);
			toast.show();
		}
	}

}