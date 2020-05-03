package com.netease.im.rtskit;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.im.IMApplication;

/**
 * Created by hzxuwen on 2015/4/13.
 */
public class Preferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_USER_ROLE = "role";

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static int getUserRole() {
        return getSharedPreferences().getInt(KEY_USER_ROLE, 1);
    }

    public static void saveUserRole(int roleType) {
        getSharedPreferences().edit().putInt(KEY_USER_ROLE, roleType).apply();
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return IMApplication.getContext().getSharedPreferences("NimRTS", Context.MODE_PRIVATE);
    }
}
