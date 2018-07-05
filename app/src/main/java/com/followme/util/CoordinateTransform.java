package com.followme.util;

import android.util.Log;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.followme.common.MyApplication;

public class CoordinateTransform {
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
