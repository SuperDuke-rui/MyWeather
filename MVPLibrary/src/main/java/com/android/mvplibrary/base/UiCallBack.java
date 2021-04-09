package com.android.mvplibrary.base;

import android.os.Bundle;

/**
 * @author wangrui
 * @date 2021/4/9 15:42
 *
 * UI回调接口
 */
public interface UiCallBack {

    /**
     * 初始化savedInstanceState
     * @param savedInstanceState
     */
    void initBeforeView(Bundle savedInstanceState);

    /**
     * 初始化
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 布局
     */
    int getLayoutId();

}
