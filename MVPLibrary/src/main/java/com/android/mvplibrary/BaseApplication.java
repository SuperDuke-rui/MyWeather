package com.android.mvplibrary;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import com.android.mvplibrary.utils.ActivityManager;

/**
 * @author wangrui
 * @date 2021/4/9 15:29
 * 工程管理
 */
public class BaseApplication extends Application {
    private static ActivityManager activityManager;
    private static BaseApplication application;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //声明Activity管理
        activityManager = new ActivityManager();
        context = getApplicationContext();
        application = this;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    public static BaseApplication getApplication() {
        return application;
    }

    /**
     * 内容提供器
     * @return
     */
    public static Context getContext() {
        return context;
    }
}
