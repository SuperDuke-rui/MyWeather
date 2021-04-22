package com.android.mvplibrary.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.Unbinder;
import com.android.mvplibrary.BaseApplication;
import com.android.mvplibrary.kit.KnifeKit;

/**
 * @author wangrui
 * @date 2021/4/9 15:54
 *
 * 用于不需要请求网络接口的Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements UiCallBack {

    protected Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
        this.context = this;
        //添加继承这个BaseActivity的Activity
        BaseApplication.getActivityManager().addActivity(this);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            Unbinder unbinder = KnifeKit.bind(this);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initData(savedInstanceState);
    }

    @Override
    public void initBeforeView(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
