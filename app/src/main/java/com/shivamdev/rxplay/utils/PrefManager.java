package com.shivamdev.rxplay.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.shivamdev.rxplay.common.RxApplication;

/**
 * Created by shivam on 3/11/16.
 */

public class PrefManager {
    private static PrefManager manager;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private PrefManager() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RxApplication.getInstance());
        editor = sharedPreferences.edit();
    }

    public static PrefManager getInstance() {
        if (manager == null) {
            manager = new PrefManager();
        }
        return manager;
    }

    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }
}