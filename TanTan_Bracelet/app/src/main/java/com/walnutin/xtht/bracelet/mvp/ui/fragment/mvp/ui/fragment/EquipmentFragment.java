package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.UiUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IABleConnectStatusListener;
import com.veepoo.protocol.listener.base.IABluetoothStateListener;
import com.veepoo.protocol.listener.base.IConnectResponse;
import com.veepoo.protocol.listener.base.INotifyResponse;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BasicSettingsActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.EpConnectedActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.Personal_dataActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpSearchListAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerEquipmentComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EquipmentModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EquipmentContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.EquipmentPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.DrawableCenterButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EquipmentFragment extends BaseFragment<EquipmentPresenter> implements EquipmentContract.View {

    private static String TAG1 = "RAYHT";

    private String TANTAN = "TANTAN";

    @BindView(R.id.ep_list_recyclerview)
    public RecyclerView ePListRecyclerView;

    private final int REQUEST_CODE = 1;

    private static final int BAIDU_READ_PHONE_STATE = 100;

    List<SearchResult> mListData = new ArrayList<>();
    List<String> mListAddress = new ArrayList<>();

    VPOperateManager mVpoperateManager;

    private Context mContext;

    private boolean mIsOadModel;

    @BindView(R.id.ep_search_btn)
    public DrawableCenterButton epSearchBtn;

    private EpSearchListAdapter epSearchListAdapter;

    public static EquipmentFragment newInstance() {
        EquipmentFragment fragment = new EquipmentFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerEquipmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .equipmentModule(new EquipmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equipment, container, false);
    }


    @Override
    public void setAdapter(List<SearchResult> epList) {
        for (SearchResult result : epList) {
            mListData.add(result);
        }
        epSearchListAdapter = new EpSearchListAdapter(mListData, mContext);

        epSearchListAdapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogUtils.debugInfo(TAG1 + "onItemClick click");
                connectDevice(mListData.get(position));
            }
        });

        ePListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        ePListRecyclerView.setAdapter(epSearchListAdapter);
    }

    @Override
    public void notifyAdapter() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

        requestLocationPermission();

        mContext = MyApplication.getAppContext();

        epSearchBtn.setText(" " + mContext.getResources().getString(R.string.ep_search_btn));

        mPresenter.searchEpList();

        mVpoperateManager = VPOperateManager.getMangerInstance(getActivity());

        LogUtils.debugInfo("[TAN]" + mVpoperateManager);

        registerBluetoothStateListener();
    }

    /**
     * 检测蓝牙设备是否开启
     *
     * @return
     */
    private boolean checkBLE() {
        if (!BluetoothUtils.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    private boolean scanDevice() {
        if (!mListAddress.isEmpty()) {
            mListAddress.clear();
        }
        if (!mListData.isEmpty()) {
            mListData.clear();
            //删除显示数据
            //bleConnectAdatpter.notifyDataSetChanged();
            setStyle(mListData.size());
            epSearchListAdapter.notifyDataSetChanged();
        }

        if (!BluetoothUtils.isBluetoothEnabled()) {
            Toast.makeText(mContext, "蓝牙没有开启", Toast.LENGTH_SHORT).show();
            return true;
        }
        LogUtils.debugInfo(TAG1 + " startDevice Start scan device");
        mVpoperateManager.startScanDevice(mSearchResponse);
        return false;
    }

    /**
     * 扫描的回调
     */
    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
//            Logger.t(TAG).i("onSearchStarted");
            LogUtils.debugInfo(TAG1 + "onSearchStarted");
        }

        @Override
        public void onDeviceFounded(final SearchResult device) {
            //Logger.t(TAG).i(String.format("device for %s-%s-%d", device.getName(), device.getAddress(), device.rssi));

            LogUtils.debugInfo(TAG1 + String.format("device for %s-%s-%d", device.getName(), device.getAddress(), device.rssi));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mListAddress.contains(device.getAddress())) {
                        mListData.add(device);
                        mListAddress.add(device.getAddress());
                    }
                }
            });
        }

        @Override
        public void onSearchStopped() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    epSearchBtn.setText(" " + mContext.getResources().getString(R.string.ep_search_btn));
                    epSearchBtn.setEnabled(true);

                    Collections.sort(mListData, new DeviceCompare());
                    setStyle(mListData.size());
                    epSearchListAdapter.notifyDataSetChanged();
                }
            });
            refreshStop();
            LogUtils.debugInfo(TAG1 + "onSearchStopped");
        }

        @Override
        public void onSearchCanceled() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    epSearchBtn.setText(" " + mContext.getResources().getString(R.string.ep_search_btn));
                    epSearchBtn.setEnabled(true);
                }
            });
            refreshStop();
            LogUtils.debugInfo(TAG1 + "onSearchCanceled");
        }
    };

    /**
     * 结束刷新
     */
    void refreshStop() {
//        Logger.t(TAG).i("refreshComlete");
        LogUtils.debugInfo(TANTAN + "refreshComlete");
//        if (mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }


    private void connectDevice(SearchResult searchResult) {
        String mac = searchResult.getAddress();

        mVpoperateManager.registerConnectStatusListener(mac, mBleConnectStatusListener);

        mVpoperateManager.connectDevice(mac, new IConnectResponse() {

            @Override
            public void connectState(int code, BleGattProfile profile, boolean isoadModel) {
                if (code == Code.REQUEST_SUCCESS) {
                    //蓝牙与设备的连接状态
//                    Logger.t(TAG).i("连接成功");
                    LogUtils.debugInfo(TAG1 + "连接成功");
//                    Logger.t(TAG).i("是否是固件升级模式=" + isoadModel);
                    LogUtils.debugInfo(TAG1 + "是否是固件升级模式=" + isoadModel);
                    mIsOadModel = isoadModel;
                } else {
//                    Logger.t(TAG).i("连接失败");
                    LogUtils.debugInfo(TAG1 + "连接失败");
                }
            }
        }, new INotifyResponse() {
            @Override
            public void notifyState(int state) {
                if (state == Code.REQUEST_SUCCESS) {

                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("bracelet", Context.MODE_PRIVATE); //私有数据
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("connected_address", mac);
                    editor.commit();

                    //蓝牙与设备的连接状态
//                    Logger.t(TAG).i("监听成功-可进行其他操作");
                    LogUtils.debugInfo(TAG1 + "监听成功-可进行其他操作");
                    Intent intent = new Intent(mContext, EpConnectedActivity.class);
                    intent.putExtra("isoadmodel", mIsOadModel);
                    intent.putExtra("deviceaddress", mac);
                    intent.putExtra("searchResult", searchResult);
                    startActivity(intent);
                } else {
//                    Logger.t(TAG).i("监听失败，重新连接");
                    LogUtils.debugInfo(TAG1 + "监听失败，重新连接");
                }

            }
        });
    }

    /**
     * 监听系统蓝牙的打开和关闭的回调状态
     */
    private final IABleConnectStatusListener mBleConnectStatusListener = new IABleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {
//                Logger.t(TAG).i("STATUS_CONNECTED");
                LogUtils.debugInfo(TANTAN + "STATUS_CONNECTED");
            } else if (status == Constants.STATUS_DISCONNECTED) {
//                Logger.t(TAG).i("STATUS_DISCONNECTED");
                LogUtils.debugInfo(TANTAN + "STATUS_DISCONNECTED");
            }
        }
    };

    /**
     * 蓝牙打开or关闭状态
     */
    private void registerBluetoothStateListener() {
        mVpoperateManager.registerBluetoothStateListener(mBluetoothStateListener);
    }

    /**
     * 监听蓝牙与设备间的回调状态
     */
    private final IABluetoothStateListener mBluetoothStateListener = new IABluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
//            Log.d(TAG).i("open=" + openOrClosed);
            LogUtils.debugInfo("open=" + openOrClosed);
        }
    };

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }


    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void setStyle(int epSize) {
        int titleBarHeight = 44;
        int epAllSize = 100 * epSize;
        int windowHeight = getWindowHeight();
        LogUtils.debugInfo("TAG windowHeight=" + windowHeight);
        ;
        int layoutHeiht = (windowHeight - titleBarHeight - epAllSize);

        LogUtils.debugInfo("TAG windowHeight * 0.1=" + windowHeight * 0.1);
        if (layoutHeiht <= 80) {
            layoutHeiht = 80;
        }
        LogUtils.debugInfo("TAG layoutHeiht=" + layoutHeiht);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) epSearchBtn.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.rightMargin, px2dip(getActivity(), layoutHeiht));
        epSearchBtn.setLayoutParams(layoutParams);

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getWindowWidth() {
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public int getWindowHeight() {
        WindowManager wm = getActivity().getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    private void requestLocationPermission() {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.launchLocation(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //selectPicFromCamera();
            }
            @Override
            public void onRequestPermissionFailure() {
                new AlertDialog.Builder(mContext).setTitle(getString(R.string.hint)).setMessage(getString(R.string.locationbypermisstion)).setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent localIntent = new Intent();
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                        launchActivity(localIntent);
                    }
                }).setNegativeButton(getString(R.string.canecl), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        }, new RxPermissions(getActivity()), mPresenter.getmErrorHandler());
    }

    @OnClick({R.id.ep_search_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ep_search_btn:
                epSearchBtn.setText(" " + mContext.getResources().getString(R.string.ep_searching_btn));
                epSearchBtn.setEnabled(false);

                LogUtils.debugInfo(TAG1 + "SearchButton click start search");

                if (checkBLE()) {
                    scanDevice();
                }
                break;
        }
    }

}