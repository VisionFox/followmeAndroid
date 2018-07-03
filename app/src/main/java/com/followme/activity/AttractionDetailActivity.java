package com.followme.activity;


import android.location.Location;
import android.os.Bundle;

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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.followme.bean.Attraction;
import com.followme.bean.User;
import com.followme.common.MyApplication;
import com.followme.litePalJavaBean.UserPlan;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;

import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;


public class AttractionDetailActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, RouteSearch.OnRouteSearchListener, View.OnClickListener {
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
    private boolean isFirstUser = true;
    private RouteSearch routeSearch;
    private LatLng myLocationLatLng;
    private LatLng attractionLatLng;

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


        if (attraction != null) {
            String imgURL = attraction.getImageurl();
            Picasso.with(this).load(imgURL).placeholder(R.drawable.ic_localtion).into(imageView);

            textView_name.setText(attraction.getName());
            textView_price.setText("价格: " + String.valueOf(attraction.getPrice() == null ? 0.00 : attraction.getPrice()) + "元");
            textView_type.setText(attraction.getType());
            textView_addr.setText("地址: " + attraction.getAddr());
            textView_description.setText(attraction.getDescription());
            attractionLatLng = new LatLng(attraction.getLatitude(), attraction.getLongitude());

            LatLng attractionLatlng = new LatLng(attraction.getLatitude(), attraction.getLongitude());
            aMap.addMarker(new MarkerOptions().position(attractionLatlng).title(attraction.getName()).snippet("default"));


//            while (true){
//                if (aMap.getMyLocation()!=null){
//                    LatLonPoint fromeLatLonPoint=new LatLonPoint(myLocationLatLng.latitude,myLocationLatLng.longitude);
//                    LatLonPoint toLatLonPoint=new LatLonPoint(attractionLatlng.latitude,attractionLatlng.longitude);
//                    RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(fromeLatLonPoint,toLatLonPoint), 0, null, null, "");
//                    routeSearch.calculateDriveRouteAsyn(query);
//                    break;
//                }
//            }
        }


    }

    private void mapInit() {

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.showMyLocation(true);
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
        if (isFirstUser && aMap.getMyLocation() != null) {
            LatLng mLatLng = new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 12, 30, 0));
            aMap.moveCamera(mCameraUpdate);
            myLocationLatLng = new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
            isFirstUser = false;
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attraction_detail_plus_button:
                addPland(attraction);
        }
    }

    private void addPland(Attraction attraction) {
        Connector.getDatabase();
        User currentUser = MyApplication.getCurrentUser();
        Log.d("当前用户为", currentUser.toString());
        List<UserPlan> userPlans = DataSupport.select("*").where("uid = ?", String.valueOf(currentUser.getId())).find(UserPlan.class);
        if (attractionHaveBeChoice(userPlans)) {
            Log.d("当前景点已被选择", "当前景点已被选择");
            return;
        }
        writePland();
    }

    private void writePland() {
        UserPlan userPlan = new UserPlan();
        User currentUser = MyApplication.getCurrentUser();
        userPlan.setUid(currentUser.getId());
        userPlan.setPlanNo(1);
        userPlan.setAttractionId(attraction.getAttractionid());
        if (userPlan.save()) {
            Log.d("景点加入计划成功", "景点加入计划成功");
        } else {
            Log.d("景点加入计划失败", "景点加入计划失败");
        }
    }

    private boolean attractionHaveBeChoice(List<UserPlan> userPlans) {
        for (UserPlan t : userPlans) {
            if (t.getAttractionId().longValue() == attraction.getAttractionid().longValue()) {
                return true;
            }
        }
        return false;
    }
}
