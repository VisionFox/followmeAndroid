package com.followme.activity;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.followme.bean.Attraction;
import com.followme.bean.User;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.common.ServerResponse;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.CoordinateTransform;
import com.followme.util.JsonTransform;

import com.followme.util.ToastUtil;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;


public class AttractionDetailActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener, View.OnClickListener {
    private Activity thisActivity = this;
    private String attractionJson;
    private ImageView imageView;
    private Button button;
    private TextView textView_name;
    private TextView textView_price;
    private TextView textView_type;
    private TextView textView_addr;
    private TextView textView_description;

    private Attraction attraction;

    private MapView mMapView = null;
    private AMap aMap = null;
    private MyLocationStyle myLocationStyle;
    private RouteSearch routeSearch;
    private LatLng attractionLatLng;


    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private static final int flag_fail = Const.handlerFlag.FAIL;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case flag_error:
                    String m1 = (String) msg.obj;
                    ToastUtil.show(thisActivity, m1);
                    break;
                case flag_fail:
                    String m2 = (String) msg.obj;
                    ToastUtil.show(thisActivity, m2);
                    break;
                case flag_success:
                    String m3 = (String) msg.obj;
                    ToastUtil.show(thisActivity, m3);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);
        mMapView = findViewById(R.id.attraction_detail_map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        imageView = findViewById(R.id.img_attraction_detail);
        button = findViewById(R.id.attraction_detail_plus_button);
        textView_name = findViewById(R.id.attraction_detail_name);
        textView_price = findViewById(R.id.attraction_detail_price);
        textView_type = findViewById(R.id.attraction_detail_type);
        textView_addr = findViewById(R.id.attraction_detail_addr);
        textView_description = findViewById(R.id.attraction_detail_description);

        button.setOnClickListener(this);
        attractionJson = this.getIntent().getExtras().getString("attractionJson");
        attraction = JsonTransform.attractionJsonToattraction(attractionJson);

        mapInit();

        //景点信息如果不为空就绘制相应的界面
        if (attraction != null) {
            String imgURL = attraction.getImageurl();
            Picasso.with(this).load(imgURL).placeholder(R.drawable.ic_localtion).into(imageView);

            textView_name.setText(attraction.getName());
            textView_price.setText("价格: " + String.valueOf(attraction.getPrice() == null ? 0.00 : attraction.getPrice()) + "元");
            textView_type.setText(attraction.getType());
            textView_addr.setText("地址: " + attraction.getAddr());
            textView_description.setText(attraction.getDescription());

            attractionLatLng = new LatLng(attraction.getLatitude(), attraction.getLongitude());
            //因为我们的景点信息是通过爬虫爬取去哪儿网的业务数据，位置坐标系和高德的不一样所以要转换一下
            attractionLatLng = CoordinateTransform.transform(attractionLatLng);
            //在地图上设置定位小蓝点
            aMap.addMarker(new MarkerOptions().position(attractionLatLng).title(attraction.getName()).snippet("纬度：" + attractionLatLng.latitude + " 经度：" + attractionLatLng.longitude));
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(attractionLatLng, 12, 30, 0));
            aMap.moveCamera(mCameraUpdate);
        }
    }

    //初始化高德地图的各种配置
    private void mapInit() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showIndoorMap(true);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.setOnMyLocationChangeListener(this);

        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }


    @Override
    public void onMyLocationChange(Location location) {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attraction_detail_plus_button:
                addPland(attraction);
        }
    }

    //将此景点的id信息写入手机的sqlite里
    private void addPland(Attraction attraction) {
        Connector.getDatabase();
        User currentUser = MyApplication.getCurrentUser();
        //查询sqlite里本人的计划列表
        List<UserPlan> userPlans = DataSupport.select("*").where("uid = ?", String.valueOf(currentUser.getId())).find(UserPlan.class);

        //判断userPlans有没有加入当前景点
        if (attractionHaveBeChoice(userPlans)) {
            Message msg = Message.obtain();
            msg.what = Const.handlerFlag.FAIL;
            msg.obj = "景点已经添加成功，无需重复添加";
            mHandler.sendMessage(msg);
            return;
        }

        UserPlan userPlan = new UserPlan();
        userPlan.setUid(currentUser.getId());
        userPlan.setPlanNo(1);
        userPlan.setAttractionId(attraction.getAttractionid());
        if (userPlan.save()) {
            Message msg = Message.obtain();
            msg.what = Const.handlerFlag.SUCCESS;
            msg.obj = "景点添加成功！！！";
            mHandler.sendMessage(msg);
        } else {
            Message msg = Message.obtain();
            msg.what = Const.handlerFlag.ERROR;
            msg.obj = "景点添加失败！！！";
            mHandler.sendMessage(msg);
        }
    }

    //判断userPlans里有没有当前景点（全局：attraction）
    private boolean attractionHaveBeChoice(List<UserPlan> userPlans) {
        for (UserPlan t : userPlans) {
            if (t.getAttractionId().longValue() == attraction.getAttractionid().longValue()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

}
