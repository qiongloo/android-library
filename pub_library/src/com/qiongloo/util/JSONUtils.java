package com.qiongloo.util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
	private static JSONUtils jsonUtils = null;

	private JSONUtils() {
	}

	/**
	 * 返回当前的实例
	 */
	public static JSONUtils getInstance() {
		if (jsonUtils == null) {
			jsonUtils = new JSONUtils();
		}
		return jsonUtils;
	}

	/**
	 * 返回JSONObject
	 */
	public JSONObject getJsonObject(String jsonResult) {
		JSONObject jsonObject = null;
		if (jsonObject == null) {
			try {
				jsonObject = new JSONObject(jsonResult);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}

	/**
	 * 返回jsonArraya
	 */
	public JSONArray getJsonArray(String jsonResult) {
		JSONArray jsonArray = null;
		if (jsonArray == null) {
			try {
				jsonArray = new JSONArray(jsonResult);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	/**
	 * 返回jsonArray
	 * 
	 * @return
	 */
	public JSONArray getJsonArray(JSONObject jsonObject, String jsonkey) {
		JSONArray jsonArray = null;
		if (jsonObject != null) {
			try {
				jsonArray = jsonObject.getJSONArray(jsonkey);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
/**
 * String 类型的返回
 * @param jsonObject
 * @param jsonkey
 * @return
 */
	public String getJsonString(JSONObject jsonObject, String jsonkey) {
		String jsonstr = null;
		try {
			jsonstr = jsonObject.getString(jsonkey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonstr;
	}
	/**
	 * double类型的返回
	 * @param jsonObject
	 * @param jsonkey
	 * @return
	 */
	public Double getJsonDouble(JSONObject jsonObject, String jsonkey) {
		double jsonDb = 0.0;
		try {
			jsonDb = jsonObject.getDouble(jsonkey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonDb;
	}
	/**
	 * int类型的返回
	 * @param jsonObject
	 * @param jsonkey
	 * @return
	 */
	public Integer getJsonInt(JSONObject jsonObject, String jsonkey){
		int jsonInt=-1;
		try {
			jsonInt=jsonObject.getInt(jsonkey);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonInt;
	}
	/**
	 * boolean 数值的返回
	 * @param jsonObject
	 * @param jsonkey
	 * @return
	 */
	public Boolean getJsonBoolean(JSONObject jsonObject, String jsonkey){
		boolean jsonBoolean=false;
		try {
			jsonBoolean=jsonObject.getBoolean(jsonkey);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonBoolean;
	}
	/**
	 * hashmap集合的返回
	 */
	
	public HashMap<String, Object> getHashMap(String jsonKey){
		HashMap<String, Object>JsonMap=new HashMap<String, Object>();
		 JSONObject json=null;
		try {
			json = new JSONObject(jsonKey);
			  Iterator iterator = json.keys();  
			   String key;  
			     while (iterator.hasNext()) {  
			         key = iterator.next().toString();  
			       JsonMap.put(key, json.get(key));  
			      }  
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		return JsonMap;
	}
	
	
	
	/**
	 * hashmap集合的返回
	 */
	public HashMap<String, String> convertToHashMap(String jsonKey){
		HashMap<String, String>JsonMap=new HashMap<String, String>();
		 JSONObject json=null;
		try {
			json = new JSONObject(jsonKey);
			  Iterator iterator = json.keys();  
			   String key;  
			     while (iterator.hasNext()) {  
			         key = iterator.next().toString();  
			       JsonMap.put(key, json.get(key).toString());  
			      }  
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		return JsonMap;
	}
	
	
}
