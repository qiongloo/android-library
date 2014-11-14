package com.qiongloo.util;

import com.qiongloo.app.DefaultConfig;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
	public static SharedPreferences sharedPreferences = null;
	public static SharedPreferences.Editor editor = null;

	/**Environment
	 * 获取sharedPreferences对象
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences getPreferences(Context context, String str) {
		if (sharedPreferences == null) {
			str = str==null?DefaultConfig.PREFERENCES_DEFAULT:str;
			sharedPreferences = context.getSharedPreferences(str,
					Context.MODE_APPEND);
		}
		return sharedPreferences;
	}
	/**
	 * 获取editor对象
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences.Editor getEditor(Context context, String str) {
		if (editor == null) {
			if (sharedPreferences == null) {
				str = str==null?DefaultConfig.PREFERENCES_DEFAULT:str;
				sharedPreferences = getPreferences(context, str);
			}
			editor = sharedPreferences.edit();
		}
		return editor;
	}

	/**
	 * 设置int类型的数据
	 * 
	 * @param context
	 * @param name
	 * @param value
	 */
	public static void putIng(Context context, String fileName, String name,
			int value) {
		if (editor == null) {
			editor = getEditor(context, fileName);
		}
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * 设置String 类型的数据
	 * 
	 * @param context
	 * @param name
	 * @param value
	 */
	public static void putString(Context context, String fileName, String name,
			String value) {
		if (editor == null) {
			editor = getEditor(context, fileName);
		}
		editor.putString(name, value);
		editor.commit();
	}

	/**
	 * 设置boolean类型的数据
	 * 
	 * @param context
	 * @param name
	 * @param value
	 */
	public static void putBoolean(Context context, String FileName,
			String name, Boolean value) {
		if (editor == null) {
			editor = getEditor(context, FileName);
		}
		editor.putBoolean(name, value);
		editor.commit();
	}

	/**
	 * 设置Long 类型的数据
	 * 
	 * @param context
	 * @param name
	 * @param value
	 */
	public static void putLong(Context context, String fileName, String name,
			Long value) {
		if (editor == null) {
			editor = getEditor(context, fileName);
		}
		editor.putLong(name, value);
		editor.commit();
	}

	/**
	 * 设置float类型，默认返回值0
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static void putFloat(Context context, String fileName, String name,
			Float value) {
		if (editor == null) {
			editor = getEditor(context, fileName);
		}
		editor.putFloat(name, value);
		editor.commit();
	}

	/**
	 * 获取int类型，默认返回值-1
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static int getInt(Context context, String fileName, String name) {
		if (sharedPreferences == null) {
			sharedPreferences = getPreferences(context, fileName);
		}
		return sharedPreferences.getInt(name, -1);
	}

	/**
	 * 获取String 类型，默认返回值""
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getString(Context context, String fileName, String name) {
		if (sharedPreferences == null) {
			sharedPreferences = getPreferences(context, fileName);
		}
		return sharedPreferences.getString(name, "");
	}

	/**
	 * 获取boolean类型，默认返回值false
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean getBoolean(Context context, String FileName,
			String name) {
		if (sharedPreferences == null) {
			sharedPreferences = getPreferences(context, FileName);
		}
		return sharedPreferences.getBoolean(name, false);
	}

	/**
	 * 获取Long类型，默认返回值-1
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static Long getLong(Context context, String FileName, String name) {
		if (sharedPreferences == null) {
			sharedPreferences = getPreferences(context, FileName);
		}
		return sharedPreferences.getLong(name, -1);
	}

	/**
	 * 获取float类型，默认返回值0
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static Float getFloat(Context context, String FileName, String name) {
		if (sharedPreferences == null) {
			sharedPreferences = getPreferences(context, FileName);
		}
		return sharedPreferences.getFloat(name, 0);
	}
}
