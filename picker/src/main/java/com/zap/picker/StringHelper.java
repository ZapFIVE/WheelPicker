package com.zap.picker;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * String相关工具类
 * <p>
 * Created by Will on 2016/10/14.
 */
public class StringHelper {

    /**
     * 获取当前小时（两位）
     */
    public static String getCurrHour() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return String.format(Locale.getDefault(), "%02d", hour);
    }

    /**
     * 获取当前分钟（两位）
     */
    public static String getCurrMinute() {
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%02d", minute);
    }

    /**
     * 获取当前分钟（两位）
     */
    public static String getCurrSecond() {
        int second = Calendar.getInstance().get(Calendar.SECOND);
        return String.format(Locale.getDefault(), "%02d", second);
    }

    /**
     * @param a   对比对象a
     * @param b   对比对象b
     * @param <T> 有序化对象
     * @return true:两个对象有序化后内容一致
     */
    public static <T extends Comparable<T>> boolean orderCompare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * @param a   对比对象a
     * @param b   对比对象b
     * @param <T> List类型
     * @return true:两个对象内容一致
     */
    public static <T> boolean disorderCompare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }


}
