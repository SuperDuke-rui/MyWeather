package com.android.myweather.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 未来三天的天气情况
 * 天气接口：https://devapi.qweather.com/v7/weather/3d?key=8d902bea490f4ef8b32e589adc780a3d&location=101010100
 * @author 29340
 */

public class WeatherForecastResponse implements Serializable {

    @SerializedName("HeWeather6")
    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101020600","location":"浦东新区","parent_city":"上海","admin_area":"上海市","cnty":"中国","lat":"31.24594307","lon":"121.56770325","tz":"+8.00"}
         * update : {"loc":"2021-04-14 16:35","utc":"2021-04-14 08:35"}
         * status : ok
         * daily_forecast : [{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2021-04-14","hum":"64","mr":"06:40","ms":"20:18","pcpn":"0.0","pop":"4","pres":"1018","sr":"05:25","ss":"18:22","tmp_max":"16","tmp_min":"10","uv_index":"5","vis":"25","wind_deg":"45","wind_dir":"东北风","wind_sc":"3-4","wind_spd":"16"},{"cond_code_d":"104","cond_code_n":"100","cond_txt_d":"阴","cond_txt_n":"晴","date":"2021-04-15","hum":"89","mr":"07:13","ms":"21:14","pcpn":"0.0","pop":"25","pres":"1016","sr":"05:24","ss":"18:23","tmp_max":"16","tmp_min":"12","uv_index":"3","vis":"25","wind_deg":"135","wind_dir":"东南风","wind_sc":"1-2","wind_spd":"4"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2021-04-16","hum":"62","mr":"07:49","ms":"22:09","pcpn":"0.0","pop":"10","pres":"1020","sr":"05:23","ss":"18:23","tmp_max":"21","tmp_min":"12","uv_index":"9","vis":"25","wind_deg":"45","wind_dir":"东北风","wind_sc":"3-4","wind_spd":"16"},{"cond_code_d":"101","cond_code_n":"100","cond_txt_d":"多云","cond_txt_n":"晴","date":"2021-04-17","hum":"52","mr":"08:29","ms":"23:04","pcpn":"0.0","pop":"0","pres":"1023","sr":"05:22","ss":"18:24","tmp_max":"19","tmp_min":"11","uv_index":"10","vis":"25","wind_deg":"0","wind_dir":"北风","wind_sc":"1-2","wind_spd":"7"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2021-04-18","hum":"59","mr":"09:15","ms":"23:58","pcpn":"0.0","pop":"0","pres":"1017","sr":"05:20","ss":"18:25","tmp_max":"18","tmp_min":"12","uv_index":"10","vis":"25","wind_deg":"90","wind_dir":"东风","wind_sc":"1-2","wind_spd":"3"},{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2021-04-19","hum":"72","mr":"10:06","ms":"00:00","pcpn":"0.0","pop":"0","pres":"1017","sr":"05:19","ss":"18:25","tmp_max":"20","tmp_min":"12","uv_index":"10","vis":"25","wind_deg":"135","wind_dir":"东南风","wind_sc":"3-4","wind_spd":"16"},{"cond_code_d":"101","cond_code_n":"104","cond_txt_d":"多云","cond_txt_n":"阴","date":"2021-04-20","hum":"66","mr":"11:02","ms":"00:48","pcpn":"0.0","pop":"0","pres":"1021","sr":"05:18","ss":"18:26","tmp_max":"21","tmp_min":"14","uv_index":"10","vis":"25","wind_deg":"135","wind_dir":"东南风","wind_sc":"3-4","wind_spd":"22"}]
         */

        @SerializedName("basic")
        private BasicBean basic;
        @SerializedName("update")
        private UpdateBean update;
        @SerializedName("status")
        private String status;
        @SerializedName("daily_forecast")
        private List<DailyForecastBean> dailyForecast;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<DailyForecastBean> getDailyForecast() {
            return dailyForecast;
        }

        public void setDailyForecast(List<DailyForecastBean> dailyForecast) {
            this.dailyForecast = dailyForecast;
        }

        public static class BasicBean {
            /**
             * cid : CN101020600
             * location : 浦东新区
             * parent_city : 上海
             * admin_area : 上海市
             * cnty : 中国
             * lat : 31.24594307
             * lon : 121.56770325
             * tz : +8.00
             */

            @SerializedName("cid")
            private String cid;
            @SerializedName("location")
            private String location;
            @SerializedName("parent_city")
            private String parentCity;
            @SerializedName("admin_area")
            private String adminArea;
            @SerializedName("cnty")
            private String cnty;
            @SerializedName("lat")
            private String lat;
            @SerializedName("lon")
            private String lon;
            @SerializedName("tz")
            private String tz;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParentCity() {
                return parentCity;
            }

            public void setParentCity(String parentCity) {
                this.parentCity = parentCity;
            }

            public String getAdminArea() {
                return adminArea;
            }

            public void setAdminArea(String adminArea) {
                this.adminArea = adminArea;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2021-04-14 16:35
             * utc : 2021-04-14 08:35
             */

            @SerializedName("loc")
            private String loc;
            @SerializedName("utc")
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }

        public static class DailyForecastBean {
            /**
             * cond_code_d : 101
             * cond_code_n : 101
             * cond_txt_d : 多云
             * cond_txt_n : 多云
             * date : 2021-04-14
             * hum : 64
             * mr : 06:40
             * ms : 20:18
             * pcpn : 0.0
             * pop : 4
             * pres : 1018
             * sr : 05:25
             * ss : 18:22
             * tmp_max : 16
             * tmp_min : 10
             * uv_index : 5
             * vis : 25
             * wind_deg : 45
             * wind_dir : 东北风
             * wind_sc : 3-4
             * wind_spd : 16
             */

            @SerializedName("cond_code_d")
            private String condCodeD;
            @SerializedName("cond_code_n")
            private String condCodeN;
            @SerializedName("cond_txt_d")
            private String condTxtD;
            @SerializedName("cond_txt_n")
            private String condTxtN;
            @SerializedName("date")
            private String date;
            @SerializedName("hum")
            private String hum;
            @SerializedName("mr")
            private String mr;
            @SerializedName("ms")
            private String ms;
            @SerializedName("pcpn")
            private String pcpn;
            @SerializedName("pop")
            private String pop;
            @SerializedName("pres")
            private String pres;
            @SerializedName("sr")
            private String sr;
            @SerializedName("ss")
            private String ss;
            @SerializedName("tmp_max")
            private String tmpMax;
            @SerializedName("tmp_min")
            private String tmpMin;
            @SerializedName("uv_index")
            private String uvIndex;
            @SerializedName("vis")
            private String vis;
            @SerializedName("wind_deg")
            private String windDeg;
            @SerializedName("wind_dir")
            private String windDir;
            @SerializedName("wind_sc")
            private String windSc;
            @SerializedName("wind_spd")
            private String windSpd;

            public String getCondCodeD() {
                return condCodeD;
            }

            public void setCondCodeD(String condCodeD) {
                this.condCodeD = condCodeD;
            }

            public String getCondCodeN() {
                return condCodeN;
            }

            public void setCondCodeN(String condCodeN) {
                this.condCodeN = condCodeN;
            }

            public String getCondTxtD() {
                return condTxtD;
            }

            public void setCondTxtD(String condTxtD) {
                this.condTxtD = condTxtD;
            }

            public String getCondTxtN() {
                return condTxtN;
            }

            public void setCondTxtN(String condTxtN) {
                this.condTxtN = condTxtN;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getMr() {
                return mr;
            }

            public void setMr(String mr) {
                this.mr = mr;
            }

            public String getMs() {
                return ms;
            }

            public void setMs(String ms) {
                this.ms = ms;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }

            public String getTmpMax() {
                return tmpMax;
            }

            public void setTmpMax(String tmpMax) {
                this.tmpMax = tmpMax;
            }

            public String getTmpMin() {
                return tmpMin;
            }

            public void setTmpMin(String tmpMin) {
                this.tmpMin = tmpMin;
            }

            public String getUvIndex() {
                return uvIndex;
            }

            public void setUvIndex(String uvIndex) {
                this.uvIndex = uvIndex;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWindDeg() {
                return windDeg;
            }

            public void setWindDeg(String windDeg) {
                this.windDeg = windDeg;
            }

            public String getWindDir() {
                return windDir;
            }

            public void setWindDir(String windDir) {
                this.windDir = windDir;
            }

            public String getWindSc() {
                return windSc;
            }

            public void setWindSc(String windSc) {
                this.windSc = windSc;
            }

            public String getWindSpd() {
                return windSpd;
            }

            public void setWindSpd(String windSpd) {
                this.windSpd = windSpd;
            }
        }
    }
}
