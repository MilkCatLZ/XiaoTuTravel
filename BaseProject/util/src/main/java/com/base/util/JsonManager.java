package com.base.util;


import android.support.annotation.NonNull;

import org.json.JSONObject;


/**
 * Created by Syokora on 2016/8/20.
 */
public class JsonManager {
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static String getJsonString(@NonNull String resource, @NonNull String key) {
        try {
            JSONObject jsonObject = new JSONObject(resource);
            return jsonObject.get(key).toString();
        } catch (Exception e) {
            Log.e("getJsonString", resource + "不是一个JsonString!!!!!!!!!!!!!!!!!!!!!!");
            return "";
        }
        
    }
    
    /**
     * @param resource
     * @param key
     * @param index
     *
     * @return
     */
    public static String getJsonArrayString(@NonNull String resource, @NonNull String key, int index) {
        
        try {
            JSONObject jsonObject = new JSONObject(resource);
            return jsonObject.getJSONArray(key).get(index).toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static int getJsonInt(@NonNull String resource, @NonNull String key) {
        
        try {
            JSONObject jsonObject = new JSONObject(resource);
            return jsonObject.getInt(key);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * @param key
     *
     * @return
     */
    @NonNull
    public static double getJsonDouble(@NonNull String resource, @NonNull String key) {
        
        try {
            JSONObject jsonObject = new JSONObject(resource);
            return jsonObject.getDouble(key);
        } catch (Exception e) {
            return 0;
        }
    }
}
