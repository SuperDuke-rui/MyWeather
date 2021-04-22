package com.android.myweather.contract;

import android.content.Context;
import com.android.mvplibrary.base.BasePresenter;
import com.android.mvplibrary.base.BaseView;
import com.android.mvplibrary.net.NetCallBack;
import com.android.mvplibrary.net.ServiceGenerator;
import com.android.myweather.api.ApiService;
import com.android.myweather.bean.LifeStyleResponse;
import com.android.myweather.bean.TodayResponse;
import com.android.myweather.bean.WeatherForecastResponse;
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

        /**
         * 天气预报
         * @param context
         * @param location
         */
        public void weatherForecast(final Context context, String location) {
            //得到构建之后的网络请求服务，这里的地址已经拼接完成，只差一个location了
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getWeatherForecast(location).enqueue(new NetCallBack<WeatherForecastResponse>() {
                @Override
                public void onSuccess(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                    if (getView() != null) {
                        getView().getWeatherForecastResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        public void lifeStyle(final Context context, String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getLifeStyle(location).enqueue(new NetCallBack<LifeStyleResponse>() {
                @Override
                public void onSuccess(Call<LifeStyleResponse> call, Response<LifeStyleResponse> response) {
                    if (getView()!=null) {
                        getView().getLifeStyleResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }
    }


    public interface IWeatherView extends BaseView {
        /**
         * 将数据放入实体
         * @param response
         */
        void getTodayWeatherResult(Response<TodayResponse> response);

        /**
         * 查询天气预报的数据返回
         * @param response
         */
        void getWeatherForecastResult(Response<WeatherForecastResponse> response);

        /**
         * 查询生活指数的数据返回
         */
        void getLifeStyleResult(Response<LifeStyleResponse> response);

        /**
         * 错误返回
         */
        void getDataFailed();
    }
}
