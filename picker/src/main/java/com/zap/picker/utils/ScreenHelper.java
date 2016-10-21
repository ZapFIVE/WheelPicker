package com.zap.picker.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 屏幕相关工具类
 * <p>
 * Created by Will on 2016/10/11.
 */
public class ScreenHelper {

    public static class Screen {
        public int widthPixels;
        public int heightPixels;
        public int densityDpi;
        public float density;
    }

    public static Screen getScreenPixels(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Screen screen = new Screen();
        screen.widthPixels = dm.widthPixels;
        screen.heightPixels = dm.heightPixels;
        screen.densityDpi = dm.densityDpi;
        screen.density = dm.density;
        return screen;
    }

    public static int dp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }
}
