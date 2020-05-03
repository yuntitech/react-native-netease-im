package com.netease.im.rtskit.common.log;

import android.util.Log;

/**
 * Created by winnie on 2017/12/19.
 */

public class LogUtil {

    public static void ui(String msg) {
        Log.i("ui", msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void log(String format, Object... args) {
        Log.d("##", String.format(format, args));
    }
}
