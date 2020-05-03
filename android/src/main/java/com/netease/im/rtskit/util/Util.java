package com.netease.im.rtskit.util;

import android.content.Context;
import android.util.TypedValue;

import com.facebook.react.bridge.ReadableMap;

public class Util {

    public static int dp2px(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static String strFromReadableMap(ReadableMap map, String key) {
        return map.hasKey(key) ? map.getString(key) : null;
    }

    public static String strFormat(String format, Object... args) {
        return String.format(format, args);
    }


}
