package com.followme.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.followme.lusir.followmeandroid.R;
import com.followme.overlay.DrivingRouteOverlay;
import com.followme.util.AMapUtil;
import com.followme.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class TestRouteActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, OnRouteSearchListener, AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mStartPoint;//起点，
    private LatLonPoint mEndPoint;//终点
    private boolean isGetMyLocation = false;
    private MyLocationStyle myLocationStyle;
    private LatLonPoint myLocationLatLonPoint = new LatLonPoint(39.942295, 116.335891);
    private List<LatLonPoint> myThroughPointList;

    private final int ROUTE_TYPE_DRIVE = 2;

    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private ProgressDialog progDialog = null;// 搜索时进度条

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //        setContentView(R.layout.activity_test_route);
        setContentView(R.layout.route_activity);
        //        mapView = findViewById(R.id.test_route_map);
        mapView = (MapView) findViewById(R.id.route_map);
        mContext = this.getApplicationContext();
        mapView.onCreate(bundle);
        init();
        setStartThroughEndPoit();


        setfromandtoMarker();
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);

    }

    private void setStartThroughEndPoit() {
        myThroughPointList=new ArrayList<>();
        mStartPoint = new LatLonPoint(34.125727, 108.833901);//起点，39.942295,116.335891
        mEndPoint = new LatLonPoint(34.31136709, 108.88083104);//终点，39.995576,116.481288
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mHeadLayout.setVisibility(View.GONE);


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
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }
        showProgressDialog();


        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划

            //设置途经点
            ArrayList<LatLonPoint> lonPointArrayList=new ArrayList<>();
            lonPointArrayList.add(new LatLonPoint(34.27332744,109.05107505));

            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, lonPointArrayList,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    if (drivePath == null) {
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    mRouteDetailDes.setText("打车约" + taxiCost + "元");
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    TestRouteActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);
                        }
                    });

                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, "no_result");
                }

            } else {
                ToastUtil.show(mContext, "no_result");
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

    }


    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null) {
            progDialog = new ProgressDialog(this);
        }
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onMyLocationChange(Location location) {
        if (!isGetMyLocation && aMap.getMyLocation() != null) {
            LatLng mLatLng = new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 12, 30, 0));
            aMap.moveCamera(mCameraUpdate);
            isGetMyLocation = true;
        }

        if (isGetMyLocation) {
            myLocationLatLonPoint.setLatitude(aMap.getMyLocation().getLatitude());
            myLocationLatLonPoint.setLongitude(aMap.getMyLocation().getLongitude());
        }
    }


//    private MapView mMapView = null;
//    private AMap aMap;
//    private MyLocationStyle myLocationStyle;
//    private boolean isFirstUser = true;
//
//    private List<Attraction> attractionList=new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test_route);
//
//        mMapView = findViewById(R.id.test_route_map);
//        mMapView.onCreate(savedInstanceState);
//
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//        }
//
//        mapInit();
//        makeTestList();
//
////        makeTestList();
//        beginRoute();
//
//    }
//
//    private void testPoi(){}
//
//    private void makeTestList() {
//        Attraction a1 = new Attraction();
//        Attraction a2 = new Attraction();
//        Attraction a3 = new Attraction();
//
//        a1.setLatitude(34.12247268);
//        a1.setLongitude(109.55371);
//        a2.setLatitude(34.19884099);
//        a2.setLongitude(108.99167);
//        a3.setLatitude(34.21268653);
//        a3.setLongitude(108.9743612);
//
//        attractionList.add(a1);
//        attractionList.add(a2);
//        attractionList.add(a3);
//    }
//
//
//    private void mapInit() {
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
////        myLocationStyle.showMyLocation(true);
//        //连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        aMap.showIndoorMap(true);
//        aMap.getUiSettings().setCompassEnabled(true);
//        aMap.getUiSettings().setScaleControlsEnabled(true);
//        aMap.setOnMyLocationChangeListener(this);
//    }
//
//    @Override
//    public void onMyLocationChange(Location location) {
//        if (isFirstUser && aMap.getMyLocation() != null) {
//            LatLng mLatLng = new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude());
//            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 12, 30, 0));
//            aMap.moveCamera(mCameraUpdate);
//            isFirstUser = false;
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        mMapView.onDestroy();
//    }
//
//
//    private void beginRoute() {
//        RouteSearch routeSearch = new RouteSearch(this);
//        routeSearch.setRouteSearchListener(this);
//
//        RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(new LatLonPoint(attractionList.get(0).getLatitude(),
//                attractionList.get(0).getLongitude()),
//                new LatLonPoint(attractionList.get(2).getLatitude()
//                        ,attractionList.get(2).getLongitude()));
//
//        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,
//                RouteSearch.DRIVING_MULTI_CHOICE_SAVE_MONEY,
//                null,
//                null,
//                "");
//
//        routeSearch.calculateDriveRouteAsyn(query);
//    }
//
//    @Override
//    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
//        Log.d("test","test");
//        if (rCode == 0) {
//            if (result != null && result.getPaths() != null
//                    && result.getPaths().size() > 0) {
//
//                DrivePath drivePath = result.getPaths().get(0);
//                aMap.clear();// 清理地图上的所有覆盖物
//
//
//                List<LatLonPoint> tujingdian=new ArrayList<>();
//                tujingdian.add(new LatLonPoint(attractionList.get(1).getLatitude(),attractionList.get(1).getLongitude()));
//
//                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(this, aMap, drivePath, result.getStartPos(),
//                        result.getTargetPos(),tujingdian);
//                drivingRouteOverlay.removeFromMap();
//                drivingRouteOverlay.addToMap();
//                drivingRouteOverlay.zoomToSpan();
//            }
//        }else {
//
//        }
//    }
//
//    @Override
//    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//
//    }
//
//    @Override
//    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
//
//    }
//
//    @Override
//    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
//
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
//        mMapView.onSaveInstanceState(outState);
//    }

}
