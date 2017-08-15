package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.walnutin.xtht.bracelet.ProductList.DensityUtils;
import com.walnutin.xtht.bracelet.ProductList.DeviceSharedPf;
import com.walnutin.xtht.bracelet.ProductList.GlobalValue;
import com.walnutin.xtht.bracelet.ProductList.HardSdk;
import com.walnutin.xtht.bracelet.ProductList.Jinterface.IHardSdkCallback;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.model.entity.EpMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BasicSettingsActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ClockListActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.HardUpdateActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.KnownCallActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.MessagePushActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.QuestionHandlerActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomGridLayoutManager;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2016/5/6.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener, IHardSdkCallback {

    View view;

    final String TAG = SettingFragment.class.getSimpleName();
    @BindView(R.id.topTitle)
    TopTitleLableView topTitleLableView;
    @BindView(R.id.ivCircle)
    RelativeLayout ivCircle;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.rlStartSearch)
    RelativeLayout rlStartSearch;
    Unbinder unbinder;
    @BindView(R.id.llLinkedView)
    LinearLayout llLinkedView;
    @BindView(R.id.ep_connected_name)
    TextView epConnectedName;
    @BindView(R.id.ep_connected_status)
    TextView epConnectedStatus;
    Unbinder unbinder1;
    @BindView(R.id.ep_connected_rl)
    AutoRelativeLayout epConnectedRl;
    private DeviceSharedPf mySharedPf;
    private String deviceAddr;
    private String deviceName;
    private String factoryName;
    @BindView(R.id.ep_connected_menue)
    public RecyclerView epMenue;


    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_configpage, container, false);
        unbinder = ButterKnife.bind(this, view);
        HardSdk.getInstance().setHardSdkCallback(this);
        topTitleLableView.getBackView().setVisibility(View.GONE);
        topTitleLableView.getTitleView().setTextColor(Color.WHITE);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = topTitleLableView.getTitleRl().getLayoutParams();
            layoutParams.height = DensityUtils.dip2px(getContext(), 72);
            topTitleLableView.setLayoutParams(layoutParams);
            topTitleLableView.getTitleRl().setLayoutParams(layoutParams);
            topTitleLableView.getBackView().setPadding(DensityUtils.dip2px(getContext(), 14), DensityUtils.dip2px(getContext(), 22), 0, 0);
            topTitleLableView.getTitleView().setPadding(0, DensityUtils.dip2px(getContext(), 22), 0, 0);
        }*/

        mySharedPf = DeviceSharedPf.getInstance(getContext());

        deviceName = mySharedPf.getString("device_name");
        deviceAddr = mySharedPf.getString("device_address");
        factoryName = mySharedPf.getString("device_factory");

        if (TextUtils.isEmpty(deviceName)) {
            //  topTitleLableView.getTitleView().setText("未连接设备");
            showUnBindView();
        } else {
            //   topTitleLableView.getTitleView().setText("已绑定设备");
            showBindView();
        }

        setadatper();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceName = mySharedPf.getString("device_name");
        if (TextUtils.isEmpty(deviceName)) {
            //  topTitleLableView.getTitleView().setText("未连接设备");
            showUnBindView();
            return;
        }
    }

    private void setadatper() {
        epMenue.setLayoutManager(new CustomGridLayoutManager(getActivity(), 3, false));
//        EpConnectedMenueAdapter adapter = new EpConnectedMenueAdapter(getMenues(), getContext());
//        epMenue.setAdapter(adapter);
//        adapter.setmOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (position) {
//                    case 0:
//                        Intent callIntent = new Intent(getActivity(), KnownCallActivity_New.class);
//                        startActivity(callIntent);
//                        break;
//                    case 1:
//                        Intent clockIntent = new Intent(getActivity(), ClockListActivity.class);
//                        startActivity(clockIntent);
//                        break;
//                    case 2:
//                        Intent messageIntent = new Intent(getActivity(), MessagePushActivity.class);
//                        startActivity(messageIntent);
//                        break;
//                    case 3:
//                        Intent questionIntent = new Intent(getActivity(), QuestionHandlerActivity.class);
//                        startActivity(questionIntent);
//                        break;
//                    case 4:
//                        Intent updateIntent = new Intent(getActivity(), HardUpdateActivity.class);
//                        startActivity(updateIntent);
//                        break;
//                }
//            }
//        });
        EpConnectedMenueAdapter adapter = new EpConnectedMenueAdapter(getMenues(), getContext());
        epMenue.setAdapter(adapter);
//        adapter.setmOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (position) {
//                    case 0:
//                        Intent callIntent = new Intent(getActivity(), KnownCallActivity.class);
//                        startActivity(callIntent);
//                        break;
//                    case 1:
//                        Intent clockIntent = new Intent(getActivity(), ClockListActivity.class);
//                        startActivity(clockIntent);
//                        break;
//                    case 2:
//                        Intent messageIntent = new Intent(getActivity(), MessagePushActivity.class);
//                        startActivity(messageIntent);
//                        break;
//                    case 3:
//                        Intent questionIntent = new Intent(getActivity(), QuestionHandlerActivity.class);
//                        startActivity(questionIntent);
//                        break;
//                    case 4:
//                        Intent updateIntent = new Intent(getActivity(), HardUpdateActivity.class);
//                        startActivity(updateIntent);
//                        break;
//                }
//            }
//        });
        epMenue.setAdapter(new EpConnectedMenueAdapter(getMenues(), getContext()));

    }

    public List<EpMenue> getMenues() {
        List<EpMenue> epMenues = null;
        epMenues = new ArrayList<EpMenue>();
        EpMenue epMenue1 = new EpMenue(1, "来电识别", "laidianshibie.png");
        epMenues.add(epMenue1);

        EpMenue epMenue2 = new EpMenue(2, "日常闹钟", "richangnaozhong.png");
        epMenues.add(epMenue2);

        EpMenue epMenue3 = new EpMenue(3, "消息推送", "xiaoxi.png");
        epMenues.add(epMenue3);

        EpMenue epMenue4 = new EpMenue(4, "问题诊断", "wenti.png");
        epMenues.add(epMenue4);

        EpMenue epMenue5 = new EpMenue(5, "固件更新", "gujiangengxin.png");
        epMenues.add(epMenue5);

        return epMenues;
    }


    @OnClick(R.id.llSearch)
    public void clickSearch() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.ep_connected_rl)
    public void clickLinkoper() {
//        if (MyApplication.isDevConnected == false) {
//            ToastUtils.showToast(getActivity().getResources().getString(R.string.pre_connecte), getActivity());
//            return;
//        }
        Intent basicSettingsIntent = new Intent(getActivity(), BasicSettingActivity.class);
        startActivity(basicSettingsIntent);

    }


    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCallbackResult(int flag, boolean state, Object obj) {
        switch (flag) {
            case GlobalValue.CONNECTED_MSG:
                topTitleLableView.getTitleView().setText("已绑定设备");
                epConnectedName.setText(MyApplication.tmpDeviceName);
                epConnectedStatus.setText("已连接");
                showBindView();
                break;
            case GlobalValue.DISCONNECT_MSG:
                deviceName = mySharedPf.getString("device_name");
                if (TextUtils.isEmpty(deviceName)) {
                    //  topTitleLableView.getTitleView().setText("未连接设备");
                    showUnBindView();
                    return;
                }
                epConnectedStatus.setText("连接中...");
//                connCount++;
//                if (connCount > 3 && isUnBindView == false) {
//                    Toast.makeText(getContext(), "请尝试解绑再连接...", Toast.LENGTH_LONG).show();
//                }
                break;
            case GlobalValue.SYNC_FINISH:
//                txtEnergy.setText("获取电量中...");
                break;

        }
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


    private void showBindView() {
        rlStartSearch.setVisibility(View.GONE);
        llLinkedView.setVisibility(View.VISIBLE);
        isUnBindView = false;
    }

    boolean isUnBindView;

    private void showUnBindView() {
        rlStartSearch.setVisibility(View.VISIBLE);
        llLinkedView.setVisibility(View.GONE);
        //  topTitleLableView.getTitleView().setText("未绑定设备");
        isUnBindView = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        HardSdk.getInstance().removeHardSdkCallback(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}

