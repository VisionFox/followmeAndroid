package com.followme.util;

import android.util.Log;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.followme.common.MyApplication;

public class CoordinateTransform {
    /**
     * 因为我们服务器上的数据爬取的是去哪网的业务数据，爬到景点的经纬度（使用的是百度地图坐标系）和高德地图的不一致
     * 所以要转换一下
     *
     * @param sourceLatLng 原始经纬度坐标
     * @return 转换后经纬度坐标
     */
    public static LatLng transform(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter(MyApplication.getContext());
        converter.from(CoordinateConverter.CoordType.BAIDU);
        converter.coord(sourceLatLng);
        Log.d("原始：", "纬度：" + sourceLatLng.latitude + "  经度：" + sourceLatLng.longitude);
        LatLng desLatLng = converter.convert();
        Log.d("转换后：", "纬度：" + desLatLng.latitude + "  经度：" + desLatLng.longitude);
        return desLatLng;
    }
}
