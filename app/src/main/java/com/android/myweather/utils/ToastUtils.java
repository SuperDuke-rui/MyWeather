package com.android.myweather.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author wangrui
 * @date 2021/4/9 10:15
 *
 * 消息提示工具类
 */
public class ToastUtils {
    public static void showLongToast(Context context, CharSequence message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, CharSequence message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
