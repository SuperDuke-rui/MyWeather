package com.android.myweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author 29340
 */
public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息
     */
    public void updateWeather() {

    }
}