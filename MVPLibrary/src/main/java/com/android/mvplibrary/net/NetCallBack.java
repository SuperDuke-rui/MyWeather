package com.android.mvplibrary.net;

import android.util.Log;
import android.view.Window;
import com.android.mvplibrary.base.BaseResponse;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wangrui
 * @date 2021/4/9 16:29
 *
 * 网络请求回调
 */
public abstract class NetCallBack<T> implements Callback<T> {

    /**
     * 访问成功回调
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null && response.body() != null && response.isSuccessful()) {
            BaseResponse baseResponse = new Gson().fromJson(new Gson().toJson(response.body()), BaseResponse.class);
            if (baseResponse.getCode() == 404) {
                Log.e("Warning", baseResponse.getData().toString());
            } else if (baseResponse.getCode() == 500) {
                Log.e("Warning", baseResponse.getData().toString());
            } else{
                onSuccess(call, response);
                Log.e("Warning", "其他情况");
            }
        } else {
            onFailed();
        }
    }

    /**
     * 访问失败回调
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailed();
    }

    /**
     * 数据返回
     * @param call
     * @param response
     */
    public abstract void onSuccess(Call<T> call, Response<T> response);

    /**
     * 失败异常
     */
    public abstract void onFailed();
}
