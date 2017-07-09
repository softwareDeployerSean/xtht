package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerExerciseDetailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ExerciseDetailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ExerciseDetailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.DbAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.Util;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ExerciseDetailPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.tv_sudu)
    TextView tvSudu;

    private AMap mAMap;
    private DbAdapter mDataBaseHelper;
    private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();

    private Polyline mOriginPolyline, mGraspPolyline;
    private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;

    private List<LatLng> mOriginLatLngList;
    private List<LatLng> mGraspLatLngList;

    private final static int AMAP_LOADED = 2;
    int type = -1;
    int id = -1;

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
                    setupRecord();
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
        if (type == 4) {
            mMapView.setVisibility(View.GONE);
        }
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
            List<AMapLocation> recordList = mRecord.getPathline();
            AMapLocation startLoc = mRecord.getStartpoint();
            AMapLocation endLoc = mRecord.getEndpoint();
            if (recordList == null || startLoc == null || endLoc == null) {
                return;
            }
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

}