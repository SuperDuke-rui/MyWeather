package com.android.myweather;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.mvplibrary.mvp.MvpActivity;
import com.android.myweather.bean.TodayResponse;
import com.android.myweather.contract.WeatherContract;
import com.android.myweather.utils.ToastUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tbruyelle.rxpermissions2.RxPermissions;
import okhttp3.*;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author 29340
 */
public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * 天气情况
     */
    @BindView(R.id.tv_info)
    TextView tvInfo;

    /**
     * 温度
     */
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;

    /**
     * 最高温和最低温
     */
    @BindView(R.id.tv_low_height)
    TextView tvLowHeight;

    /**
     * 城市
     */
    @BindView(R.id.tv_city)
    TextView tvCity;

    /**
     * 最近更新时间
     */
    @BindView(R.id.tv_old_time)
    TextView tvOldTime;
    /**
     * 权限请求框架
     */
    private RxPermissions rxPermissions;

    /**
     * 定位器
     */
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    /**
     * 数据初始化 主线程，onCreate方法可以删除
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        //实例化权限请求框架
        rxPermissions = new RxPermissions(this);
        //权限判断
        permissionVersion();
    }

    /**
     * 绑定布局文件
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 绑定Presenter
     * @return
     */
    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
    }

    /**
     * 权限判断
     */
    private void permissionVersion() {
        //6.0及以上
        if (Build.VERSION.SDK_INT >= 23) {
            //动态权限申请
            permissionRequest();
        } else { //6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted 提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android 6.0以下，不需要动态申请权限");
        }
    }

    /**
     * 动态权限申请
     */
    private void permissionRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //申请成功，得到权限后开始定位
                        startLocation();
                    } else {
                        //申请失败
                        ToastUtils.showShortToast(this, "权限未开启");
                    }
                });
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        //如果开发者需要获取当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        option.setNeedNewVersionRgc(true);
        //mLocationClient为第二部初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象。通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        //启动定位
        mLocationClient.start();
    }

    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String district = bdLocation.getDistrict(); //获取区县
            //获取今天的天气数据
            mPresent.todayWeather(context, district);
        }
    }

    /**
     * 查询当前天气，请求成功后的数据返回
     */
    @Override
    public void getTodayWeatherResult(Response<TodayResponse> response) {
        //数据返回后关闭定位
        mLocationClient.stop();
        if (response.body().getHeWeather6().get(0).getBasic() != null) {
            //显示数据
            tvTemperature.setText(response.body().getHeWeather6().get(0).getNow().getTmp());
            tvCity.setText(response.body().getHeWeather6().get(0).getBasic().getLocation());
            tvInfo.setText(response.body().getHeWeather6().get(0).getNow().getCond_txt());
            tvOldTime.setText(response.body().getHeWeather6().get(0).getUpdate().getLoc());
            tvLowHeight.setText(response.body().getHeWeather6().get(0).getNow().getFl() + "℃/" +
                    response.body().getHeWeather6().get(0).getNow().getHum() + "℃");
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    /**
     * 数据请求失败返回
     */
    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "网络异常");
    }
}