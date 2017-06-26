package com.walnutin.xtht.bracelet.mvp.ui.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.rance.library.ButtonData;
import com.rance.library.ButtonEventListener;
import com.rance.library.SectorMenuButton;
import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IABleConnectStatusListener;
import com.veepoo.protocol.listener.base.IABluetoothStateListener;
import com.veepoo.protocol.listener.base.IConnectResponse;
import com.veepoo.protocol.listener.base.INotifyResponse;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.MyFragmentViewPagerAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.RunningOutsideActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.FragmentViewPagerAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.EpConnectedFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.EquipmentFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.ExerciseFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MainFragment;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.MineFragment;
import com.walnutin.xtht.bracelet.mvp.ui.widget.ContainerViewPager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.MyViewPager;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.integration.AppManager.APPMANAGER_MESSAGE;
import static com.jess.arms.integration.AppManager.SHOW_SNACKBAR;

/**
 * Created by suns on 2017-06-13.
 */

public class MainActivity extends FragmentActivity {

    private final int REQUEST_CODE = 1;
    @BindView(R.id.viewpager)
    ContainerViewPager viewpager;
    @BindView(R.id.radio_main)
    RadioButton radioMain;
    @BindView(R.id.radio_exerse)
    RadioButton radioExerse;
    @BindView(R.id.radio_equipment)
    RadioButton radioEquipment;
    @BindView(R.id.radio_mine)
    RadioButton radioMine;
    @BindView(R.id.bottom_sector_menu)
    SectorMenuButton bottom_sector_menu;
    //菜单
    private static final int[] ITEM_DRAWABLES = {R.mipmap.jiahao, R.mipmap.paobu, R.mipmap.jianshen,
            R.mipmap.qixin, R.mipmap.tubu};

    public static final int TAB_HOME = 0;
    public static final int TAB_EXERCISE = 1;
    public static final int TAB_EQUIPMENT = 2;
    public static final int TAB_MINE = 3;

    List<Fragment> fragments = new ArrayList<Fragment>();

    MainFragment mainfragment;
    ExerciseFragment exerciseFragment;
    MineFragment mineFragment;
    EquipmentFragment equipmentFragment;

    EpConnectedFragment epConnectedFragment;

    List<ButtonData> buttonDatas = new ArrayList<>();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    MyViewPagerAdapter adapter;

    VPOperateManager mVpoperateManager;

    private static final String TAG = "[TAN][" + MainActivity.class.getSimpleName() + "]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVpoperateManager = VPOperateManager.getMangerInstance(MyApplication.getAppContext());
        registerBluetoothStateListener();

        if(checkBLE()) {
            connectDevice();
        }

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        boolean isLocationPermisstion = checkPermission();
        if (!isLocationPermisstion) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }

        initView();
        addPageChangeListener();
        for (int i = 0; i < 5; i++) {
            ButtonData buttonData = ButtonData.buildIconButton(this, ITEM_DRAWABLES[i], 0);
            //buttonData.setBackgroundColorId(this, R.color.add);
            buttonDatas.add(buttonData);
        }

        ivBack.setVisibility(View.GONE);
        bottom_sector_menu.setButtonDatas(buttonDatas);
        setListener(bottom_sector_menu);
        DataHelper.setStringSF(MyApplication.getAppContext(), "isload", "true");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.debugInfo("onResume啊");
    }

    private void setListener(final SectorMenuButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index){
                    case 1:
                        startActivity(new Intent(MainActivity.this, RunningOutsideActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:

                        break;
                }

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onCollapse() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1000) {
            for (int i = 0; i < permissions.length; i++) {

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {

        String connectedState = DataHelper.getStringSF(this, "connect_state");
        LogUtils.debugInfo(TAG + "---------connectedState=" + connectedState);

        mainfragment = new MainFragment();
        exerciseFragment = ExerciseFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        epConnectedFragment = EpConnectedFragment.newInstance();
        equipmentFragment = EquipmentFragment.newInstance();
        fragments.add(mainfragment);
        fragments.add(exerciseFragment);
        fragments.add(equipmentFragment);
        fragments.add(mineFragment);
        this.viewpager.setOffscreenPageLimit(0);
        MyFragmentViewPagerAdapter adapter = new MyFragmentViewPagerAdapter(this.getSupportFragmentManager(), viewpager, fragments);
//        adapter = new MyViewPagerAdapter(this.getSupportFragmentManager(), viewpager, fragments);
    }


    private void connectDevice() {

        if (!BluetoothUtils.isBluetoothEnabled()) {
            Toast.makeText(this, "蓝牙没有开启", Toast.LENGTH_SHORT).show();
            return;
        }

        String mac = DataHelper.getStringSF(this, "mac");
        LogUtils.debugInfo(TAG + ",mac=" + mac);
        if(mac != null && !mac.equals("") && !mac.equals("default")) {
            mVpoperateManager.registerConnectStatusListener(mac, mBleConnectStatusListener);
            mVpoperateManager.connectDevice(mac, new IConnectResponse() {
                @Override
                public void connectState(int code, BleGattProfile profile, boolean isoadModel) {
                    if (code == Code.REQUEST_SUCCESS) {
                        //蓝牙与设备的连接状态
//                    Logger.t(TAG).i("连接成功");
                        LogUtils.debugInfo(TAG + "连接成功");
//                    Logger.t(TAG).i("是否是固件升级模式=" + isoadModel);
                        LogUtils.debugInfo(TAG + "是否是固件升级模式=" + isoadModel);
                        DataHelper.setStringSF(MyApplication.getAppContext(), "isoadModel", isoadModel + ""); //连接成功
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "2"); //连接成功
                    } else {
//                    Logger.t(TAG).i("连接失败");
                        LogUtils.debugInfo(TAG + "连接失败");
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "0"); //连接失败
                    }
                }
            }, new INotifyResponse() {
                @Override
                public void notifyState(int state) {
                    if (state == Code.REQUEST_SUCCESS) {
                        DataHelper.setStringSF(MyApplication.getAppContext(), "mac", mac);

                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "3"); //连接监听
                    } else {
                        LogUtils.debugInfo(TAG + "监听失败，重新连接");
                        DataHelper.setStringSF(MyApplication.getAppContext(), "connect_state", "1"); //连接监听
                    }

                }
            });
        }
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

    /**
     * 监听系统蓝牙的打开和关闭的回调状态
     */
    private final IABleConnectStatusListener mBleConnectStatusListener = new IABleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == Constants.STATUS_CONNECTED) {
//                Logger.t(TAG).i("STATUS_CONNECTED");
                LogUtils.debugInfo(TAG + "STATUS_CONNECTED");
            } else if (status == Constants.STATUS_DISCONNECTED) {
//                Logger.t(TAG).i("STATUS_DISCONNECTED");
                LogUtils.debugInfo(TAG + "STATUS_DISCONNECTED");
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

    private void addPageChangeListener() {
        viewpager.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case TAB_HOME:
                        radioMain.setChecked(true);
                        break;
                    case TAB_EXERCISE:
                        radioExerse.setChecked(true);
                        break;
                    case TAB_EQUIPMENT:
                        String state = DataHelper.getStringSF(MainActivity.this, "connect_state");
                        LogUtils.debugInfo(TAG+"-------------------state=" + state);
                        radioEquipment.setChecked(true);
                        break;
                    case TAB_MINE:
                        radioMine.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @OnClick({R.id.radio_main, R.id.radio_exerse, R.id.radio_equipment, R.id.radio_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_main:
                viewpager.setCurrentItem(TAB_HOME, false);
                toolbarTitle.setText("");
                break;
            case R.id.radio_exerse:
                viewpager.setCurrentItem(TAB_EXERCISE, false);
                toolbarTitle.setText("");
                break;
            case R.id.radio_equipment:

                String connect_state = DataHelper.getStringSF(this, "connect_state");
                LogUtils.debugInfo(TAG + ", table change connect_state=" + connect_state);
                LogUtils.debugInfo(TAG + ", (fragments.get(2) instanceof EpConnectedFragment)=" + (fragments.get(2) instanceof EpConnectedFragment));
                if(connect_state.equals("3") && !(fragments.get(2) instanceof EpConnectedFragment)) {
                    fragments.set(2, epConnectedFragment);
//                    adapter.updateList(fragments);
                }

                viewpager.setCurrentItem(TAB_EQUIPMENT, false);
                toolbarTitle.setText(getString(R.string.ep_setup));
                break;
            case R.id.radio_mine:
                viewpager.setCurrentItem(TAB_MINE, false);
                toolbarTitle.setText(getString(R.string.name));
                break;
        }
    }

    //点击返回键返回桌面而不是退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    class MyViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments; // 每个Fragment对应一个Page
        private FragmentManager fragmentManager;
        private ContainerViewPager viewPager; // viewPager对象

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyViewPagerAdapter(FragmentManager fragmentManager, ContainerViewPager viewPager, List<Fragment> fragments) {
            super(fragmentManager);
            this.fragments = fragments;
            this.fragmentManager = fragmentManager;
            this.viewPager = viewPager;
            this.viewPager.setAdapter(this);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void updateList(List<Fragment> arrayList) {
            this.fragments.clear();
            this.fragments.addAll(arrayList);

            notifyDataSetChanged();
        }
    }

}
