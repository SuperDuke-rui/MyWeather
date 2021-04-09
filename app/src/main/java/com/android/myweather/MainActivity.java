package com.android.myweather;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.android.myweather.utils.ToastUtils;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;

/**
 * @author 29340
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 权限请求框架
     */
    private RxPermissions rxPermissions;

    /**
     * 定位器
     */
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @BindView(R.id.tv_address_detail)
    TextView tvAddressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //实例化权限请求框架
        rxPermissions = new RxPermissions(this);
        //权限判断
        permissionVersion();
    }

    /**
     * 权限判断
     */
    private void permissionVersion(){
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
    private void permissionRequest(){
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
            double latitude = bdLocation.getLatitude(); //获取维度信息
            double longitude = bdLocation.getLongitude(); //获取经度信息
            float radius = bdLocation.getRadius(); //获取定位精度，默认值为0.0f
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = bdLocation.getCoorType();
            //获取定位类型、定位错误返回码
            int errorCode = bdLocation.getLocType();
            String address = bdLocation.getAddrStr(); //获取详细地址信息
            String country = bdLocation.getCountry(); //获取国家
            String province = bdLocation.getProvince(); //获取省份
            String city = bdLocation.getCity(); //获取城市
            String district = bdLocation.getDistrict(); //获取区县
            String street = bdLocation.getStreet(); //获取街道信息
            String locationDescribe = bdLocation.getLocationDescribe(); //获取位置描述信息

            //设置文本显示
            tvAddressDetail.setText(address);

            //获取今天的天气数据
            getTodayWeather(district);
        }
    }

    /**
     * 获取今天的天气数据
     */
    private void getTodayWeather(String district) {
        //使用Get异步请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //拼接地址访问
                .url("https://free-api.heweather.net/s6/weather/now?key=8d902bea490f4ef8b32e589adc780a3d&location=" + district)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("wangrui", "获取数据成功");
                    Log.d("wangrui", "response.code()==" + response.code());
                    Log.d("wangrui", "response.body().string()==" + response.body().string());
                }
            }
        });
    }
}