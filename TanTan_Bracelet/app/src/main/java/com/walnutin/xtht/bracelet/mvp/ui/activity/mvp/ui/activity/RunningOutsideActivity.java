package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.adam.gpsstatus.GpsStatusProxy;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRunningOutsideComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RunningOutsideModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RunningOutsideContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Util;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RunningOutsidePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomerRelativeLayout;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RunningOutsideActivity extends BaseActivity<RunningOutsidePresenter> implements RunningOutsideContract.View, LocationSource,
        AMapLocationListener, OnItemClickListener, OnDismissListener, SensorEventListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.frame_map)
    FrameLayout frameMap;
    @BindView(R.id.ib_location)
    ImageButton ibLocation;
    @BindView(R.id.ib_close)
    ImageButton ibClose;
    @BindView(R.id.tv_gps)
    TextView tvGps;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_calories)
    TextView tvCalories;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_length)
    TextView mResultShow;
    @BindView(R.id.layout)
    CustomerRelativeLayout mCustomerRelativeLayout;
    @BindView(R.id.iv_jiesu)
    ImageView ivJiesu;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_goin)
    ImageView ivGoin;
    @BindView(R.id.tv_jieshu)
    TextView tvJieshu;
    @BindView(R.id.tv_stop)
    TextView tvStop;
    @BindView(R.id.tv_goin)
    TextView tvGoin;
    Boolean isFirstLatLng = true;
    //以前的定位点
    private LatLng oldLatLng;
    UiSettings uiSettings;


    //地图模块
    private final static int CALLTRACE = 0;
    private AMap mAMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions, tracePolytion;
    private Polyline mpolyline;
    private PathRecord record;
    private long mStartTime;
    private long mEndTime;
    private ToggleButton btn;
    private DbAdapter DbHepler;
    private List<TraceLocation> mTracelocationlist = new ArrayList<TraceLocation>();
    private List<TraceOverlay> mOverlayList = new ArrayList<TraceOverlay>();
    private List<AMapLocation> recordList = new ArrayList<AMapLocation>();
    private int tracesize = 30;
    private int mDistance = 0;
    private TraceOverlay mTraceoverlay;
    //private TextView mResultShow;
    private Marker mlocMarker;
    //计时器
    private Timer timer1;
    private TimerTask timerTask;
    GpsStatusProxy proxy;
    AlertView alertView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRunningOutsideComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .runningOutsideModule(new RunningOutsideModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_running_outside; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroyLocation();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        proxy.unRegister();
        mAMap = null;
    }


    int cnt = 0;
    String tag_title = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        tag_title = getIntent().getStringExtra("tag");
        if (tag_title.equals("riding")) {
            tv_title.setText(getString(R.string.rideing));
        } else if (tag_title.equals("mountaineering")) {
            tv_title.setText(getString(R.string.mountaineering));
        } else if (tag_title.equals("running_out")) {
            tv_title.setText(getString(R.string.run_out));
        }

        timer1 = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText(getStringTime(cnt++));
                    }
                });
            }
        };
        timer1.schedule(timerTask, 0, 1000);

        //地图
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initpolyline();
        mAMap.clear(true);
        if (record != null) {
            record = null;
        }
        //给对象设置
        record = new PathRecord();
        mStartTime = System.currentTimeMillis();
        record.setDate(getcueDate(mStartTime));
        mCustomerRelativeLayout.setOnFinishListener(new CustomerRelativeLayout.OnFinishListener() {
            @Override
            public void onFinish(boolean isUpOrDown) {

                if (isUpOrDown) {
                    //可点击
                    set_click(false);
                } else {
                    //不可点击
                    set_click(true);
                }
            }
        });
        proxy = GpsStatusProxy.getInstance(getApplicationContext());
        proxy.register();
        set_button_nomal();
        setmap_gesture(false);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        setUpMap();
        uiSettings = mAMap.getUiSettings();
        mTraceoverlay = new TraceOverlay(mAMap);
    }


    protected void saveRecord(List<AMapLocation> list, String time) {
        if (list != null && list.size() > 0) {
            DbHepler = new DbAdapter(this);
            DbHepler.open();
            String duration = getDuration();
            float distance = getDistance(list);
            String average = getAverage(distance);
            String pathlineSring = getPathLineString(list);
            AMapLocation firstLocaiton = list.get(0);
            AMapLocation lastLocaiton = list.get(list.size() - 1);
            String stratpoint = amapLocationToString(firstLocaiton);
            String endpoint = amapLocationToString(lastLocaiton);
            String height = getheight(list);
            DbHepler.createrecord(String.valueOf(distance), duration, average,
                    pathlineSring, stratpoint, endpoint, time, getcalorie(), height);
            DbHepler.close();
        } else {
            ToastUtils.showToast(getString(R.string.no_path), this);
        }
    }


    public String getheight(List<AMapLocation> list) {
        double start_hetght = 0;
        double end_height = 0;
        for (int i = 0; i < list.size(); i++) {
            double tag = list.get(i).getAltitude();
            if (tag != 0) {
                start_hetght = tag;
                break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            double tag = list.get(i).getAltitude();
            if (tag != 0) {
                end_height = tag;
                break;
            }
        }
        double height = end_height - start_hetght;

        return String.valueOf(height);

    }


    public String getcalorie() {
        return tvCalories.getText().toString().trim();
    }


    private String getDuration() {
        return timer.getText().toString().trim();
    }


    private String getAverage(float distance) {
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }

    private float getDistance(List<AMapLocation> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            AMapLocation firstpoint = list.get(i);
            AMapLocation secondpoint = list.get(i + 1);
            LatLng firstLatLng = new LatLng(firstpoint.getLatitude(),
                    firstpoint.getLongitude());
            LatLng secondLatLng = new LatLng(secondpoint.getLatitude(),
                    secondpoint.getLongitude());
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    private String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;
    }

    private String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.GREEN);
        tracePolytion = new PolylineOptions();
        tracePolytion.width(80);
        tracePolytion.setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.read_circle));
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        startlocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mAMap = null;
    }

    LatLng mylocation;
    double distance = 0;

    /**
     * 定位结果回调
     *
     * @param amapLocation 位置信息类
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mylocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                if (amapLocation.getSpeed() == 0) {
                    tvSpeed.setText("--");
                } else {
                    int a = (int) (1000 / amapLocation.getSpeed());
                    LogUtils.debugInfo("sudua =" + a);
                    tvSpeed.setText(ConmonUtils.secToTime(a));
                }
                LogUtils.debugInfo("海拔速度===" + amapLocation.getAltitude());
                record.addpoint(amapLocation);
                mPolyoptions.add(mylocation);
                mTracelocationlist.add(Util.parseTraceLocation(amapLocation));
                redrawline();
                LatLng newLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (isFirstLatLng) {
                    //记录第一次的定位信息
                    oldLatLng = newLatLng;
                    isFirstLatLng = false;
                    mAMap.addMarker(new MarkerOptions().position(oldLatLng)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.dian)));
                }
                //位置有变化
                if (oldLatLng != newLatLng) {
                    distance += AMapUtils.calculateLineDistance(oldLatLng, newLatLng);
                    oldLatLng = newLatLng;
                    mResultShow.setText(ConmonUtils.formatDouble(distance / 1000) + "");
                }
                proxy.notifyLocation(amapLocation);

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 开始定位。
     */
    private void startlocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(2000);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        } else {
            mLocationClient.startLocation();
        }
    }


    /**
     * 实时轨迹画线
     */
    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 1) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = mAMap.addPolyline(mPolyoptions);
            }
        }

        mOverlayList.add(mTraceoverlay);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

//		if (mpolyline != null) {
//			mpolyline.remove();
//		}
//		mPolyoptions.visible(true);
//		mpolyline = mAMap.addPolyline(mPolyoptions);
//			PolylineOptions newpoly = new PolylineOptions();
//			mpolyline = mAMap.addPolyline(newpoly.addAll(mPolyoptions.getPoints()));
//		}
    }

    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    Marker marker_end;

    @OnClick({R.id.ib_location, R.id.ib_close, R.id.iv_map, R.id.iv_jiesu, R.id.iv_stop, R.id.iv_goin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_location:
                if (mylocation != null) {
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                }
                break;
            case R.id.ib_close:
                frameMap.setVisibility(View.VISIBLE);
                setmap_gesture(false);
                break;
            case R.id.iv_map:
                frameMap.setVisibility(View.GONE);
                setmap_gesture(true);
                break;
            case R.id.iv_jiesu:
                if (distance < 50) {
                    short_distance();
                } else {
                    exit();
                }


                break;
            case R.id.iv_stop:
                if (timerTask != null) {
                    timerTask.cancel();  //将原任务从队列中移除
                }
                set_button_click();
                stopLocation();
                Location location = record.getPathline().get(record.getPathline().size() - 1);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                marker_end = mAMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.jieshudian)));


                break;
            case R.id.iv_goin:
                if (timerTask != null) {
                    timerTask.cancel();  //将原任务从队列中移除
                }
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(getStringTime(cnt++));
                            }
                        });
                    }
                };
                timer1.schedule(timerTask, 0, 1000);
                set_button_nomal();
                startlocation();
                marker_end.remove();
                break;
        }
    }

    public void setmap_gesture(Boolean booleab) {
        uiSettings.setScrollGesturesEnabled(booleab);
        uiSettings.setAllGesturesEnabled(booleab);
    }

    public void set_click(Boolean b) {
        ivMap.setClickable(b);
        ivStop.setClickable(b);
        tvStop.setClickable(b);
        ivJiesu.setClickable(b);
        tvJieshu.setClickable(b);
        ivGoin.setClickable(b);
        tvGoin.setClickable(b);
    }


    private String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ToastUtils.showToast(getString(R.string.click_end), this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void set_button_nomal() {
        ivStop.setVisibility(View.VISIBLE);
        tvStop.setVisibility(View.VISIBLE);
        ivJiesu.setVisibility(View.GONE);
        tvJieshu.setVisibility(View.GONE);
        ivGoin.setVisibility(View.GONE);
        tvGoin.setVisibility(View.GONE);
    }

    public void set_button_click() {
        ivStop.setVisibility(View.GONE);
        tvStop.setVisibility(View.GONE);
        ivJiesu.setVisibility(View.VISIBLE);
        tvJieshu.setVisibility(View.VISIBLE);
        ivGoin.setVisibility(View.VISIBLE);
        tvGoin.setVisibility(View.VISIBLE);
    }


    private void stopLocation() {
        // 停止定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    int tag = 0;

    public void short_distance() {
        tag = 2;
        alertView = new AlertView(null, getString(R.string.short_distance), getString(R.string.tuichu), new String[]{getString(R.string.goin)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView.show();
    }

    public void exit() {
        tag = 1;
        alertView = new AlertView(null, getString(R.string.save_map), getString(R.string.canecl), new String[]{getString(R.string.confirm)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView.show();
    }

    @Override
    public void onDismiss(Object o) {
        LogUtils.debugInfo("继续11");
        if (tag == 2) {
            finish();
        }

    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                if (tag == 2) {
                    alertView.dismiss();
                    tag = 3;
                    LogUtils.debugInfo("继续22");
                } else {
                    mEndTime = System.currentTimeMillis();
                    saveRecord(record.getPathline(), record.getDate());
                    finish();
                }
                break;

        }
    }

    long lastTime = 0;
    float mAngle = 0;
    long TIME_SENSOR = 500;  //66667;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                float x = event.values[0];
                x += 0;
                x %= 360.0F;
                if (x > 180.0F)
                    x -= 360.0F;
                else if (x < -180.0F)
                    x += 360.0F;

                if (Math.abs(mAngle - x) < 3.0f) {
                    break;
                }
                mAngle = Float.isNaN(x) ? 0 : x;
                mAMap.setMyLocationRotateAngle(360 - mAngle);
                lastTime = System.currentTimeMillis();
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}