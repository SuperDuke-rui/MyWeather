package com.android.myweather.adapter;

import androidx.annotation.Nullable;
import com.android.myweather.R;
import com.android.myweather.bean.WeatherForecastResponse;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author wangrui
 * @date 2021/4/14 17:56
 *
 * 天气预报列表展示适配器
 */
public class WeatherForecastAdapter extends BaseQuickAdapter<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {
    public WeatherForecastAdapter(int layoutResId, @Nullable List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherForecastResponse.HeWeather6Bean.DailyForecastBean item) {
        //日期
        helper.setText(R.id.tv_date, item.getDate())
                //天气
                .setText(R.id.tv_info, item.getCondTxtD())
                //最高最低温度
                .setText(R.id.tv_low_and_height, item.getTmpMin() + "℃/" + item.getTmpMax() + "℃");
    }
}
