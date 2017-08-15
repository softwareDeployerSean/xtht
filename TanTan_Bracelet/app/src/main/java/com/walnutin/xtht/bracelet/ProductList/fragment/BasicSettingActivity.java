package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.walnutin.xtht.bracelet.ProductList.DensityUtils;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.HardSdk;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.ProductList.LineItemView;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/5/6.
 */
public class BasicSettingActivity extends Activity implements View.OnClickListener, IHardSdkCallback {

    @BindView(R.id.topTitle)
    TopTitleLableView topTitleLableView;
    @BindView(R.id.findBraceletItemView)
    LineItemView findBraceletItemView;
    private DeviceSharedPf mySharedPf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configpage);
        ButterKnife.bind(this);
        mySharedPf = DeviceSharedPf.getInstance(getApplicationContext());
        topTitleLableView.getTitleView().setTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = topTitleLableView.getTitleRl().getLayoutParams();
            layoutParams.height = DensityUtils.dip2px(getApplicationContext(), 72);
            topTitleLableView.setLayoutParams(layoutParams);
            topTitleLableView.getTitleRl().setLayoutParams(layoutParams);
            topTitleLableView.getBackView().setPadding(DensityUtils.dip2px(getApplicationContext(), 14), DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
            topTitleLableView.getTitleView().setPadding(0, DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
        }

        HardSdk.getInstance().setHardSdkCallback(this);
    }

    @OnClick(R.id.findBraceletItemView)
    public void findBracelet() {
        if (MyApplication.isDevConnected == false) {
            return;
        }
        HardSdk.getInstance().findBand(3);
    }


    @OnClick(R.id.unBind)
    public void unBind() {
//        if (MyApplication.isDevConnected == false) {
//            return;
//        }

        MyApplication.isManualOff = true;
        HardSdk.getInstance().disconnect();
        mySharedPf.setString("device_name", null);
        mySharedPf.setString("device_address", null);
        mySharedPf.setString("device_factory", null);
        finish();

    }


    @Override
    public void onCallbackResult(int flag, boolean state, Object obj) {

    }

    @Override
    public void onStepChanged(int step, float distance, int calories, boolean finish_status) {

    }

    @Override
    public void onSleepChanged(int lightTime, int deepTime, int sleepAllTime, int[] sleepStatusArray, int[] timePointArray, int[] duraionTimeArray) {

    }

    @Override
    public void onHeartRateChanged(int rate, int status) {

    }

    @Override
    public void bloodPressureChange(int hightPressure, int lowPressure, int status) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HardSdk.getInstance().removeHardSdkCallback(this);
    }
}

