package com.android.mvplibrary.mvp;

import android.os.Bundle;
import com.android.mvplibrary.base.BaseFragment;
import com.android.mvplibrary.base.BasePresenter;
import com.android.mvplibrary.base.BaseView;

/**
 * @author wangrui
 * @date 2021/4/9 16:07
 */
public abstract class MvpFragment <P extends BasePresenter> extends BaseFragment {
    protected P mPresent;
    protected abstract P createPresent();

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresent != null) {
            mPresent.detach((BaseView) this);
        }
    }
}
