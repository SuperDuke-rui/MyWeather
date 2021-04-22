package com.android.myweather;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.mvplibrary.mvp.MvpActivity;
import com.android.mvplibrary.utils.ObjectUtils;
import com.android.mvplibrary.view.WhiteWindmills;
import com.android.myweather.adapter.WeatherForecastAdapter;
import com.android.myweather.bean.LifeStyleResponse;
import com.android.myweather.bean.TodayResponse;
import com.android.myweather.bean.WeatherForecastResponse;
import com.android.myweather.contract.WeatherContract;
import com.android.myweather.utils.ToastUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tbruyelle.rxpermissions2.RxPermissions;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 29340
 */
public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 天气情况
     **/
    @BindView(R.id.tv_info)
    TextView tvInfo;

    /**
     * 温度
     **/
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;

    /**
     * 最高温和最低温
     **/
    @BindView(R.id.tv_low_height)
    TextView tvLowHeight;

    /**
     * 城市
     **/
    @BindView(R.id.tv_city)
    TextView tvCity;

    /**
     * 最近更新时间
     **/
    @BindView(R.id.tv_old_time)
    TextView tvOldTime;

    /**
     * 天气预报显示列表
     **/
    @BindView(R.id.weatherForecast_view)
    RecyclerView weatherForecastView;

    /**
     * 舒适度
     **/
    @BindView(R.id.tv_comfort)
    TextView tvComfort;

    /**
     * 旅游指数
     **/
    @BindView(R.id.tv_travel)
    TextView tvTravel;

    /**
     * 运动指数
     **/
    @BindView(R.id.tv_sport)
    TextView tvSport;

    /**
     * 洗车指数
     **/
    @BindView(R.id.tv_carWash)
    TextView tvCarWash;

    /**
     * 空气指数
     **/
    @BindView(R.id.tv_air)
    TextView tvAir;

    /**
     * 穿衣指数
     **/
    @BindView(R.id.tv_wear)
    TextView tvWear;

    /**
     * 感冒指数
     **/
    @BindView(R.id.tv_flu)
    TextView tvFlu;

    /**
     * 大风车
     */
    @BindView(R.id.ww_big)
    WhiteWindmills wwBig;

    /**
     * 小风车
     */
    @BindView(R.id.ww_small)
    WhiteWindmills wwSmall;

    /**
     * 风向
     */
    @BindView(R.id.tv_wind_direction)
    TextView tvWindDirection;

    /**
     * 风力
     */
    @BindView(R.id.tv_wind_power)
    TextView tvWindPower;

    /**
     * 切换城市按钮
     */
    @BindView(R.id.btn_city_select)
    Button btnCitySelect;

    @OnClick(R.id.btn_city_select)
    public void onViewClicked() {

    }

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
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        //天气预报列表初始化
        initList();
        //实例化权限请求框架
        rxPermissions = new RxPermissions(this);
        //权限判断
        permissionVersion();
    }

    /**
     * 绑定布局文件
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 绑定Presenter u
     *
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
            //获取天气预报数据
            mPresent.weatherForecast(context, district);
            //获取生活指数数据
            mPresent.lifeStyle(context, district);
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

            tvWindDirection.setText("风向    " + response.body().getHeWeather6().get(0).getNow().getWind_dir());
            tvWindPower.setText("风力    " + response.body().getHeWeather6().get(0).getNow().getWind_sc() + "级");
            wwBig.startRotate();
            wwSmall.startRotate();
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    /**
     * 初始化数据源
     */
    List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> mList;
    WeatherForecastAdapter mAdapter;

    /**
     * 初始化天气预报数据列表
     */
    private void initList() {
        mList = new ArrayList<>();
        //为适配器设置布局和数据源
        mAdapter = new WeatherForecastAdapter(R.layout.item_weather_forecast_list, mList);
        //布局管理，默认是纵向
        LinearLayoutManager manager = new LinearLayoutManager(context);
        //为列表配置管理器
        weatherForecastView.setLayoutManager(manager);
        weatherForecastView.setAdapter(mAdapter);
    }

    /**
     * 查询天气预报，请求成功后的数据返回
     *
     * @param response
     */
    @Override
    public void getWeatherForecastResult(Response<WeatherForecastResponse> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            //最低气温和最高气温
            tvLowHeight.setText(response.body().getHeWeather6().get(0).getDailyForecast().get(0).getTmpMin() + "℃/" +
                    response.body().getHeWeather6().get(0).getDailyForecast().get(0).getTmpMax() + "℃");

            if (response.body().getHeWeather6().get(0).getDailyForecast() != null) {
                List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> data =
                        response.body().getHeWeather6().get(0).getDailyForecast();
                //添加数据前先清除列表
                mList.clear();
                //添加数据
                mList.addAll(data);
                //刷新列表
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    /**
     * 查询当前地区生活指数，请求成功后的数据返回
     *
     * @param response
     */
    @Override
    public void getLifeStyleResult(Response<LifeStyleResponse> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            List<LifeStyleResponse.HeWeather6Bean.LifestyleBean> data = response.body().getHeWeather6().get(0).getLifestyle();
            if (!ObjectUtils.isEmpty(data)) {
                for (int i = 0; i < data.size(); i++) {
                    if (("comf").equals(data.get(i).getType())) {
                        tvComfort.setText("舒适度：" + data.get(i).getTxt());
                    } else if (("drsg").equals(data.get(i).getType())) {
                        tvWear.setText("穿衣指数：" + data.get(i).getTxt());
                    } else if (("flu").equals(data.get(i).getType())) {
                        tvFlu.setText("感冒指数：" + data.get(i).getTxt());
                    } else if (("sport").equals(data.get(i).getType())) {
                        tvSport.setText("运动指数：" + data.get(i).getTxt());
                    } else if (("trav").equals(data.get(i).getType())) {
                        tvTravel.setText("旅游指数：" + data.get(i).getTxt());
                    } else if (("cw").equals(data.get(i).getType())) {
                        tvCarWash.setText("洗车指数：" + data.get(i).getTxt());
                    } else if (("air").equals(data.get(i).getType())) {
                        tvAir.setText("空气指数：" + data.get(i).getTxt());
                    }
                }
            } else {
                ToastUtils.showShortToast(context, "生活指数为空");
            }
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