package com.android.mvplibrary.kit;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wangrui
 * @date 2021/4/9 15:34
 *
 * 绑定控件视图ID
 */
public class KnifeKit {

    /**
     * 绑定
     * @param target
     * @return
     */
    public static Unbinder bind(Object target) {
        if (target instanceof Activity) {
            return ButterKnife.bind((Activity) target);
        } else if (target instanceof Dialog) {
            return ButterKnife.bind((Dialog) target);
        } else if (target instanceof View) {
            return ButterKnife.bind((View) target);
        }
        return Unbinder.EMPTY;
    }

    /**
     * 绑定输入目标资源
     * @param target
     * @param source
     * @return
     */
    public static Unbinder bind(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        }
        return Unbinder.EMPTY;
    }

    /**
     * 解绑
     * @param unbinder
     * @return
     */
    public static void unbind(Unbinder unbinder) {
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
    }
}
