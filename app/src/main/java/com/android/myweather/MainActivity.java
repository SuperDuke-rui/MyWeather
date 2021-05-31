package com.android.myweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.mvplibrary.mvp.MvpActivity;
import com.android.mvplibrary.utils.ObjectUtils;
import com.android.mvplibrary.view.WhiteWindmills;
import com.android.myweather.adapter.AreaAdapter;
import com.android.myweather.adapter.CityAdapter;
import com.android.myweather.adapter.ProvinceAdapter;
import com.android.myweather.adapter.WeatherForecastAdapter;
import com.android.myweather.bean.*;
import com.android.myweather.contract.WeatherContract;
import com.android.myweather.utils.HttpPostRequest;
import com.android.myweather.utils.LiWindow;
import com.android.myweather.utils.ToastUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import okhttp3.Call;
import okhttp3.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    /**
     * 登录按钮
     */
    @BindView(R.id.btn_login)
    Button btnLogin;


    public void initView() {
        //设置登录按钮的点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
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
     * 城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        citylist = new ArrayList<>();
        arealist = new ArrayList<>();
        list = new ArrayList<>();
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView areaBack = (ImageView) view.findViewById(R.id.iv_back_area);
        ImageView cityBack = (ImageView) view.findViewById(R.id.iv_back_city);
        TextView windowTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        liWindow.showRightPopupWindow(view);
        initCityData(recyclerView,areaBack,cityBack,windowTitle);//加载城市列表数据
    }

    /**
     * 省市县数据渲染
     * @param recyclerView  列表
     * @param areaBack 区县返回
     * @param cityBack 市返回
     * @param windowTitle  窗口标题
     */
    private void initCityData(RecyclerView recyclerView,ImageView areaBack,ImageView cityBack,TextView windowTitle) {
        //初始化省数据 读取省数据并显示到列表中
        try {
            //读取数据
            InputStream inputStream = getResources().getAssets().open("City.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }

            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //循环这个文件数组、获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityResponse response = new CityResponse();
                response.setName(provinceName);
                provinceList.add(response);
            }

            //定义省份显示适配器
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        //返回上一级数据
                        cityBack.setVisibility(View.VISIBLE);
                        cityBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                                cityBack.setVisibility(View.GONE);
                                windowTitle.setText("中国");
                            }
                        });

                        //根据当前位置的省份所在的数组位置、获取城市的数组
                        JSONObject provinceObject = Data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //更新列表数据
                        if (citylist != null) {
                            citylist.clear();
                        }

                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject cityObj = cityArray.getJSONObject(i);
                            String cityName = cityObj.getString("name");
                            CityResponse.CityBean response = new CityResponse.CityBean();
                            response.setName(cityName);
                            citylist.add(response);
                        }

                        cityAdapter = new CityAdapter(R.layout.item_city_list, citylist);
                        LinearLayoutManager manager1 = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();

                        cityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                try {
                                    //返回上一级数据
                                    areaBack.setVisibility(View.VISIBLE);
                                    areaBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerView.setAdapter(cityAdapter);
                                            cityAdapter.notifyDataSetChanged();
                                            areaBack.setVisibility(View.GONE);
                                            windowTitle.setText(provinceTitle);
                                            arealist.clear();
                                        }
                                    });
                                    //根据当前城市数组位置 获取地区数据
                                    windowTitle.setText(citylist.get(position).getName());
                                    JSONObject cityJsonObj = cityArray.getJSONObject(position);
                                    JSONArray areaJsonArray = cityJsonObj.getJSONArray("area");
                                    if (arealist != null) {
                                        arealist.clear();
                                    }
                                    if(list != null){
                                        list.clear();
                                    }
                                    for (int i = 0; i < areaJsonArray.length(); i++) {
                                        list.add(areaJsonArray.getString(i));
                                    }
                                    Log.i("list", list.toString());
                                    for (int j = 0; j < list.size(); j++) {
                                        CityResponse.CityBean.AreaBean response = new CityResponse.CityBean.AreaBean();
                                        response.setName(list.get(j).toString());
                                        arealist.add(response);
                                    }
                                    areaAdapter = new AreaAdapter(R.layout.item_city_list, arealist);
                                    LinearLayoutManager manager2 = new LinearLayoutManager(context);

                                    recyclerView.setLayoutManager(manager2);
                                    recyclerView.setAdapter(areaAdapter);
                                    areaAdapter.notifyDataSetChanged();

                                    areaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                        @Override
                                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                                            mPresent.todayWeather(context,arealist.get(position).getName());//今日天气
                                            mPresent.weatherForecast(context, arealist.get(position).getName());//天气预报
                                            mPresent.lifeStyle(context, arealist.get(position).getName());//生活指数
                                            liWindow.closePopupWindow();

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //点击事件
    @OnClick(R.id.btn_city_select)
    public void onViewClicked() {//显示城市弹窗
        showCityWindow();
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

    private List<String> list;//字符串列表
    private List<CityResponse> provinceList;//省列表数据
    private List<CityResponse.CityBean> citylist;//市列表数据
    private List<CityResponse.CityBean.AreaBean> arealist;//区/县列表数据
    ProvinceAdapter provinceAdapter;//省数据适配器
    CityAdapter cityAdapter;//市数据适配器
    AreaAdapter areaAdapter;//县/区数据适配器
    String provinceTitle;//标题
    LiWindow liWindow;//自定义弹窗



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
    @SuppressLint("CheckResult")
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
        initView();
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
    @SuppressLint("SetTextI18n")
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