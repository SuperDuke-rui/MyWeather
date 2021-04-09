package com.android.mvplibrary.mvp;

import android.os.Bundle;
import com.android.mvplibrary.base.BaseActivity;
import com.android.mvplibrary.base.BasePresenter;
import com.android.mvplibrary.base.BaseView;

/**
 * @author wangrui
 * @date 2021/4/9 16:07
 *
 * 适用于需要访问网络接口的Activity
 */
public abstract class MvpActivity <P extends BasePresenter> extends BaseActivity {
    protected P mPresent;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresent.detach((BaseView) this);
    }
}
