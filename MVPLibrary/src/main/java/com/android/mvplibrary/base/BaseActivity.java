package com.android.mvplibrary.base;

import android.app.Activity;
import android.os.Bundle;
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
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
        this.context = this;
        //添加继承这个BaseActivity的Activity
        BaseApplication.getActivityManager().addActivity(this);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            unbinder = KnifeKit.bind(this);
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
