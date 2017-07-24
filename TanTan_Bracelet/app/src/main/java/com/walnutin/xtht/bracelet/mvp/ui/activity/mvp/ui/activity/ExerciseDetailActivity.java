package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerExerciseDetailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ExerciseDetailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Util;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ExerciseDetailPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.RateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ExerciseDetailActivity extends BaseActivity<ExerciseDetailPresenter> implements ExerciseDetailContract.View, AMap.OnMapLoadedListener {


    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.tv_length)
    TextView tvLength;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_cal)
    TextView tvCal;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.relative_exercise)
    RelativeLayout relative_exercise;

    @BindView(R.id.tv_sudu)
    TextView tvSudu;

    @BindView(R.id.speed_rate_view)
    RateView speedRateView;

    @BindView(R.id.rate_rate_view)
    RateView rateRateView;

    @BindView(R.id.buping_rate_view)
    RateView buPingRateView;

    private AMap mAMap;
    private DbAdapter mDataBaseHelper;
    private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();

    private Polyline mOriginPolyline, mGraspPolyline;
    private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;

    private List<LatLng> mOriginLatLngList;
    private List<LatLng> mGraspLatLngList;

    List<AMapLocation> recordList;

    private final static int AMAP_LOADED = 2;
    int type = -1;
    int id = -1;

    private String[] xLabels = null;
    private int[] heartRateDatas = null;
    private int[] stepDatas = null;
    private int[] speedsDatas = null;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerExerciseDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .exerciseDetailModule(new ExerciseDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_exercise_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AMAP_LOADED:
                    //setupRecord();
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    public void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        type = getIntent().getIntExtra("type", -1);
        id = getIntent().getIntExtra("id", -1);
        if (type == 2) {
            relative_exercise.setVisibility(View.GONE);
        }

        setupRecord();

        setSpeedRateView();

        setRateRateView();

        setBupingRateView();
    }

    private void setBupingRateView() {
        buPingRateView.setLineColor(Color.parseColor("#6AD489"));
        buPingRateView.setBrokenLineColor(Color.parseColor("#47BBC3"));

        LogUtils.debugInfo("TAG", "========stepDatas.length=" + stepDatas.length);
        LogUtils.debugInfo("TAG", "========xLabels.length=" + xLabels.length);

        buPingRateView.setDatas(stepDatas);

        LogUtils.debugInfo("TAG", "-----------setRateRateView:xLables=" + xLabels.length);

        buPingRateView.setXlabel(xLabels);

        buPingRateView.setxDisplayInterval(10);

        int max = getMaxArray(stepDatas);
        int min = getMinArray(stepDatas);

        int linMax = ((int) ((max + 20) / 5)) * 5 + 5;

        String[] yLabels = new String[linMax + 20];
        for (int i = 0; i < yLabels.length; i++) {
            yLabels[i] = String.valueOf(i);
        }
        buPingRateView.setYlabel(yLabels);

        int a = linMax / 4;

        int[] brokenLineDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        buPingRateView.setBrolenLineDisplay(brokenLineDisplay);

        int[] yDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        buPingRateView.setyDisPlay(yDisplay);
    }

    private void setRateRateView() {
        rateRateView.setLineColor(Color.parseColor("#6AD489"));
        rateRateView.setBrokenLineColor(Color.parseColor("#D53D49"));

        LogUtils.debugInfo("TAG", "heartRateDatas.length=" + heartRateDatas.length);
        LogUtils.debugInfo("TAG", "xLabels.length=" + xLabels.length);

        rateRateView.setDatas(heartRateDatas);
        rateRateView.setXlabel(xLabels);

        String[] yLables = new String[230];
        for (int i = 0; i < yLables.length; i++) {
            yLables[i] = String.valueOf(i);
        }

        rateRateView.setYlabel(yLables);

        rateRateView.setxDisplayInterval(10);

        int[] brokenLineDisplay = new int[]{50, 100, 150, 200};
        rateRateView.setBrolenLineDisplay(brokenLineDisplay);

        int[] yDisplay = new int[]{50, 100, 150, 200};
        rateRateView.setyDisPlay(yDisplay);
    }

    private void setSpeedRateView() {
        speedRateView.setLineColor(Color.parseColor("#6AD489"));
        speedRateView.setBrokenLineColor(Color.parseColor("#8CC44A"));

        String[] yLables = new String[150];
        for (int i = 0; i < yLables.length; i++) {
            yLables[i] = String.valueOf(i);
        }

        speedRateView.setYlabel(yLables);

        speedRateView.setDatas(speedsDatas);
        speedRateView.setXlabel(xLabels);

        speedRateView.setxDisplayInterval(10);

        int max = getMaxArray(speedsDatas);
        int min = getMinArray(speedsDatas);

        int linMax = ((int) ((max + 20) / 5)) * 5 + 5;

        String[] yLabels = new String[linMax + linMax / 5];
        for (int i = 0; i < yLabels.length; i++) {
            yLabels[i] = String.valueOf(i);
        }
        speedRateView.setYlabel(yLabels);
        speedRateView.setyDisPlayType(1);
        speedRateView.setMarginPer(8);

        int a = linMax / 4;

        int[] brokenLineDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        speedRateView.setBrolenLineDisplay(brokenLineDisplay);

        int[] yDisplay = new int[]{linMax - 3 * a, linMax - 2 * a, linMax - a, linMax};
        speedRateView.setyDisPlay(yDisplay);
    }

    private int getMaxArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return max;
    }

    private int getMinArray(int[] array) {
        int i, min, max;

        min = max = array[0];
        System.out.print("数组A的元素包括：");
        for (i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
            if (array[i] > max)   // 判断最大值
                max = array[i];
            if (array[i] < min)   // 判断最小值
                min = array[i];
        }
        return min;
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
        }
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


    //轨迹数据初始化
    private void setupRecord() {
        // 轨迹纠偏初始化
        LBSTraceClient mTraceClient = new LBSTraceClient(
                getApplicationContext());
        DbAdapter dbhelper = new DbAdapter(this.getApplicationContext());
        dbhelper.open();
        PathRecord mRecord = new PathRecord();
        mRecord = dbhelper.queryRecordById(id);
        dbhelper.close();
        if (mRecord != null) {
            recordList = mRecord.getPathline();
            AMapLocation startLoc = mRecord.getStartpoint();
            AMapLocation endLoc = mRecord.getEndpoint();
            if (recordList != null && startLoc != null && endLoc != null) {
                LatLng startLatLng = new LatLng(startLoc.getLatitude(),
                        startLoc.getLongitude());
                LatLng endLatLng = new LatLng(endLoc.getLatitude(),
                        endLoc.getLongitude());
                mOriginLatLngList = Util.parseLatLngList(recordList);
                addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);
                tvLength.setText(mRecord.getDistance());
                tvDuration.setText(mRecord.getDuration());
                tvTime.setText(mRecord.getDate());
                tvHeight.setText(mRecord.getAltitude() + "m");
                tvSudu.setText(mRecord.getAveragespeed() + "km");
            }

            LogUtils.debugInfo("TAG", "recordList.size()=" + recordList.size());

            String duration = mRecord.getDuration();
            LogUtils.debugInfo("TAG", "----------------duration=" + duration);

            String[] times = duration.split(":");
            int minutes = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]) + (Integer.parseInt(times[2]) > 0 ? 1 : 0);

            LogUtils.debugInfo("TAG", "===========Integer.parseInt(times[0])=" + Integer.parseInt(times[0]));
            LogUtils.debugInfo("TAG", "===========Integer.parseInt(times[1])=" + Integer.parseInt(times[1]));
            LogUtils.debugInfo("TAG", "===========Integer.parseInt(times[2])=" + Integer.parseInt(times[2]));
            LogUtils.debugInfo("TAG", "===========minutes=" + minutes);

            xLabels = new String[minutes + 1];
            xLabels[0] = "0";
            for (int i = 0; i < xLabels.length - 1; i++) {
                xLabels[i + 1] = String.valueOf(i + 1);
            }

            String heartTracte = mRecord.getHeartrate();

            String[] heartRates = null;
            if (heartTracte != null && heartTracte.length() > 0) {
                heartRates = heartTracte.split(";");
                LogUtils.debugInfo("TAG", "===============heartRates.length=" + heartRates.length);
            }

            heartRateDatas = new int[xLabels.length];
            heartRateDatas[0] = 0;
            if (heartRates != null && heartRates.length > 0) {
                for (int i = 1; i < heartRateDatas.length; i++) {
                    if (i <= heartRates.length) {
                        heartRateDatas[i] = Integer.parseInt(heartRates[i - 1]);
                    } else {
                        heartRateDatas[i] = 0;
                    }
                }
            }

            String stepRate = mRecord.getSteprate();
            stepDatas = new int[xLabels.length];
            String[] steps = stepRate.split(";");
            stepDatas[0] = 0;
            if (steps != null && steps.length > 0) {
                for (int i = 1; i < stepDatas.length; i++) {
                    if (i <= steps.length) {
                        stepDatas[i] = Integer.parseInt(steps[i - 1]);
                    } else {
                        stepDatas[i] = 0;
                    }
                }
            }

            String speeds = mRecord.getSpeeds();

            String[] speedsTemp = speeds.split(";");
            speedsDatas = new int[xLabels.length];
            speedsDatas[0] = 0;
            for (int i = 1; i < speedsDatas.length; i++) {
                if (i <= speedsTemp.length && speedsTemp[i - 1].length() > 0) {
                    speedsDatas[i] = (int) (Float.parseFloat(speedsTemp[i - 1]) * 1000 / 60);
                } else {
                    speedsDatas[i] = 0;
                }
            }
            LogUtils.debugInfo("TAG", "stepRate=" + stepRate + ", heartTracte=" + heartTracte);
            LogUtils.debugInfo("速度====" + speeds);
        } else {
        }
    }


    /**
     * 地图上添加原始轨迹线路及起终点、轨迹动画小人
     *
     * @param startPoint
     * @param endPoint
     * @param originList
     */
    private void addOriginTrace(LatLng startPoint, LatLng endPoint,
                                List<LatLng> originList) {
        mOriginPolyline = mAMap.addPolyline(new PolylineOptions().color(
                Color.GREEN).addAll(originList));
        mOriginStartMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.dian)));
        mOriginEndMarker = mAMap.addMarker(new MarkerOptions().position(
                endPoint).icon(
                BitmapDescriptorFactory.fromResource(R.mipmap.jieshudian)));

        try {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(),
                    16));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        mOriginRoleMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.mipmap.jieshudian))));*/
    }

    private LatLngBounds getBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (mOriginLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mOriginLatLngList.size(); i++) {
            b.include(mOriginLatLngList.get(i));
        }
        return b.build();

    }


    @Override
    public void onMapLoaded() {
        LogUtils.debugInfo("第一个方法");
        if (type != 4) {
            Message msg = handler.obtainMessage();
            msg.what = AMAP_LOADED;
            handler.sendMessage(msg);
        }

    }

    @OnClick(R.id.iv_back)
    public void onClick(View view) {
        finish();
    }


}