package com.android.myweather.contract;

import android.content.Context;
import com.android.mvplibrary.base.BasePresenter;
import com.android.mvplibrary.base.BaseView;
import com.android.mvplibrary.net.NetCallBack;
import com.android.mvplibrary.net.ServiceGenerator;
import com.android.myweather.api.ApiService;
import com.android.myweather.bean.TodayResponse;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author wangrui
 * @date 2021/4/9 16:45
 *
 * 天气订阅器
 */
public class WeatherContract {
    public static class WeatherPresenter extends BasePresenter<IWeatherView> {
        /**
         * 当日天气
         * @param context
         * @param location  区/县
         */
        public void todayWeather(final Context context, String location) {
            //得到构建之后的网络请求服务，这里的地址已经拼接完成，只差一个location了
            ApiService service = ServiceGenerator.createService(ApiService.class);
            //设置请求回调  NetCallBack是重写请求回调
            service.getTodayWeather(location).enqueue(new NetCallBack<TodayResponse>() {
                //成功回调
                @Override
                public void onSuccess(Call<TodayResponse> call, Response<TodayResponse> response) {
                    if (getView() != null) {//当视图不会空时返回请求数据
                        getView().getTodayWeatherResult(response);
                    }
                }

                //失败回调
                @Override
                public void onFailed() {
                    if (getView() != null) {//当视图不会空时获取错误信息
                        getView().getDataFailed();
                    }
                }
            });
        }
    }


    public interface IWeatherView extends BaseView {
        //将数据放入实体
        void getTodayWeatherResult(Response<TodayResponse> response);

        //错误返回
        void getDataFailed();
    }
}