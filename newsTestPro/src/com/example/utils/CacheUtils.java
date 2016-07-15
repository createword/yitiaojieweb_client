package com.example.utils;

import android.content.Context;

public class CacheUtils {

	/**
	 * 设置缓存
	 */
	public static void setCache(String key, String value, Context cx) {
		PrefUtils.setString(cx, key, value);
	}

	/**
	 * 获取缓存
	 */
	public static String getCache(String key, Context cx) {
		return PrefUtils.getString(cx, key,null);
	}
}
