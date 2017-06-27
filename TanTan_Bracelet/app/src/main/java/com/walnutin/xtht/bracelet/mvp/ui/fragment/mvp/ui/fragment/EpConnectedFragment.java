package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.err.OadErrorState;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBatteryDataListener;
import com.veepoo.protocol.listener.data.IDeviceFuctionDataListener;
import com.veepoo.protocol.listener.data.IPwdDataListener;
import com.veepoo.protocol.listener.data.ISocialMsgDataListener;
import com.veepoo.protocol.listener.oad.OnFindOadDeviceListener;
import com.veepoo.protocol.listener.oad.OnUpdateCheckListener;
import com.veepoo.protocol.model.datas.BatteryData;
import com.veepoo.protocol.model.datas.FunctionDeviceSupportData;
import com.veepoo.protocol.model.datas.FunctionSocailMsgData;
import com.veepoo.protocol.model.datas.PwdData;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.veepoo.protocol.model.settings.OadSetting;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BasicSettingsActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ClockListActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.EpConnectedActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.HardUpdateActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.KnownCallActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.MessagePushActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.QuestionHandlerActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerEpConnectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EpConnectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.EpConnectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomGridLayoutManager;

import butterknife.BindView;
import butterknife.OnClick;
import no.nordicsemi.android.dfu.DfuLogListener;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

import static android.content.Context.MODE_PRIVATE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EpConnectedFragment extends BaseFragment<EpConnectedPresenter> implements EpConnectedContract.View {


    @BindView(R.id.ep_connected_menue)
    public RecyclerView epMenue;
    @BindView(R.id.ep_connected_name)
    public TextView epNameTextView;
    @BindView(R.id.ep_connected_status)
    public TextView epStateTextView;
    @BindView(R.id.ep_connected_dianliang)
    public TextView epPower;

    VPOperateManager mVpoperateManager;

    OadSetting oadSetting;

    private String mOadAddress;
    private String mOadFileName;
    private int failCount = 0;
    private boolean isCanEnterOadModel = false;
    private boolean isFindOadDevice = false;
    private final static int MAX_ALLOW_FAIL_COUNT = 5;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String message = (String) msg.obj;
                    epPower.setText(message);
                    break;
                case 1:
                    startUpgrade();

                    break;
            }
        }
    };

    public void startUpgrade() {
        if (isCanEnterOadModel) {
            if (isFindOadDevice) {
                /**
                 * 当然执行这步的可能很少
                 * 如果设备版本以及文件校验成功，设备也进入升级模式，同时固件升级下的设备也被找到，就执行官方固件升级程序
                 */
                startOad();
            } else {
                /**
                 * 当然执行这步的可能很少
                 * 如果设备版本以及文件校验成功，就先让设备进入升级模式，同时查找固件升级下的设备
                 */
                findOadModelDevice();
            }
        } else {

            checkVersionAndFile();
        }
    }

    /**
     * 升级前的检验，设备版本以及文件校验，同时查找固件升级下的设备
     */
    private void checkVersionAndFile() {
        oadSetting.setDeviceVersion("00.17.07");
        oadSetting.setDeviceTestVersion("00.17.07.00");
        LogUtils.debugInfo(TAG + "升级前：版本验证->文件验证->查找目标设备");
        mVpoperateManager.checkVersionAndFile(oadSetting, new OnUpdateCheckListener() {

            /**
             * 获取服务器上的最新版本信息
             * @param deviceNumber 当前的设备号
             * @param deviceVersion 当前设备的最新版本
             * @param des 升级的文本提示
             */
            public void onNetVersionInfo(int deviceNumber, String deviceVersion, String des) {
                LogUtils.debugInfo(TAG + "服务器版本信息,设备号=" + deviceNumber + ",最新版本=" + deviceVersion + ",升级描述=" + des);
            }


            /**
             * 如果有从服务上下载文件，返回下载的进度。文件很小，目前不会超过100k,下载很快，可以在这个回调后不做任何操作
             * @param progress
             */
            @Override
            public void onDownLoadOadFile(float progress) {
                LogUtils.debugInfo(TAG + "从服务器下载文件,进度=" + progress);
            }

            /**
             * 表示版本检验或者是文件检验没有通过
             * @param endState
             */
            @Override
            public void onCheckFail(int endState) {
                switch (endState) {
                    case OadErrorState.UNCONNECT_NETWORK:
                        LogUtils.debugInfo(TAG + "网络出错");
                        ToastUtils.showToast("网络连接错误，请检查网络", getActivity());
                        break;
                    case OadErrorState.UNCONNECT_SERVER:
                        LogUtils.debugInfo(TAG + "服务器连接不上");
                        ToastUtils.showToast("网络连接错误，请检查网络", getActivity());
                        break;
                    case OadErrorState.SERVER_NOT_HAVE_NEW:
                        LogUtils.debugInfo(TAG + "服务器无此版本");
                        ToastUtils.showToast("设备是最新版本", getActivity());
                        break;
                    case OadErrorState.DEVICE_IS_NEW:
                        LogUtils.debugInfo(TAG + "设备是最新版本");
                        ToastUtils.showToast("设备是最新版本", getActivity());
                        break;
                    case OadErrorState.OAD_FILE_UNEXITS:
                        LogUtils.debugInfo(TAG + "文件不存在");
                        break;
                    case OadErrorState.OAD_FILE_MD5_UNSAME:
                        LogUtils.debugInfo(TAG + "文件md5不一致");
                        break;
                }
            }

            /**
             * 表示版本以及文件校验通过，通过此回调返回升级文件的文件路径，调用startOad方法会用到此参数
             * @param oadFileName
             */
            @Override
            public void onCheckSuccess(String oadFileName) {
                LogUtils.debugInfo(TAG + "版本确认无误，文件确认无误");
                mOadFileName = oadFileName;
                if (!TextUtils.isEmpty(mOadFileName)) {
                    isCanEnterOadModel = true;
                }
            }

            /**
             * 表示设备成功进入固件模式并被找到，通过此回调返回升级设备在固件升级模式下的地址，调用startOad方法时会用到此参数
             * 为什么前面已经知道了设备地址， 还要用回调的地址？因为通过仔细观察，你就会发现设备通过正常模式下的升级，进入固件升级模式后，设备地址会自动加1
             * @param oadAddress
             */
            @Override
            public void findOadDevice(String oadAddress) {
                LogUtils.debugInfo(TAG + "找到OAD模式下的设备了");
                mOadAddress = oadAddress;
                if (!TextUtils.isEmpty(mOadAddress)) {
                    isFindOadDevice = true;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startOad();
                        }
                    });
                }
            }
        });
    }

    /**
     * 如果设备版本以及升级文件没有问题，但是没有查找固件升级下的设备，就执行这个步骤
     * 这个不般不会被调用
     */
    private void findOadModelDevice() {
        mVpoperateManager.findOadModelDevice(oadSetting, new OnFindOadDeviceListener() {
            @Override
            public void findOadDevice(String oadAddress) {
                startOad();
            }
        });
    }


    /**
     * 固件升级成功
     */
    private void oadSuccess() {
        isFindOadDevice = false;
        Toast.makeText(getActivity(), "升级成功", Toast.LENGTH_SHORT).show();
        LogUtils.debugInfo(TAG + "升级成功");
    }

    /**
     * 固件升级失败，默认会尝试5次
     */
    private void oadFail() {
        failCount++;
        LogUtils.debugInfo(TAG + "showErrorMessage");
        try {
            Thread.currentThread();
            Thread.sleep(300);
        } catch (Exception e) {
        }

        if (failCount < MAX_ALLOW_FAIL_COUNT) {
            LogUtils.debugInfo(TAG + "再试一次=" + failCount);
//            showProgressBar();
            startOad();
        } else {
            showOadFailDialog();
        }
    }

    /**
     * 执行官方升级程序
     * mOadFileName 通过onCheckSuccess回调取得
     * mOadAddress 通过findOadDevice回调取得
     */
    private void startOad() {
        LogUtils.debugInfo(TAG + "执行升级程序，最多尝试5次");
        Boolean isBinder = false;
        final DfuServiceInitiator dfuServiceInitiator = new DfuServiceInitiator(mOadAddress)
                .setDeviceName("veepoo")
                .setKeepBond(isBinder);
        dfuServiceInitiator.setZip(null, mOadFileName);
        dfuServiceInitiator.start(getActivity(), DfuService.class);

    }

    /**
     * 5次升级失败后的提示框
     */
    private void showOadFailDialog() {
//        isFindOadDevice = false;
//        String mStringContent = "升级失败，设备名字会变成DfuLang";
//        String mStringTitle = "提示";
//        String mStringOk = "知道了";
//        AlertDialog oadFailDialog = new AlertDialog.Builder(mContext).setTitle(mStringTitle)
//                .setIconAttribute(android.R.attr.alertDialogIcon).setCancelable(false)
//                .setMessage(mStringContent)
//                .setPositiveButton(mStringOk, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        OadActivity.this.finish();
//                    }
//                }).create();
//        oadFailDialog.setCanceledOnTouchOutside(false);
//        oadFailDialog.show();
        ToastUtils.showToast("升级失败", getActivity());
    }

    /**
     * 注册固件升级的操作的监听
     */
    private void registerListener() {
        /**
         * 注册升级过程的监听，可在回调中做相应的处理
         */
        DfuServiceListenerHelper.registerProgressListener(getActivity(), mDfuProgressListener);
        /**
         * 注册升级过程的监听，主要是用于打印升级的日志，方便调试
         */
        DfuServiceListenerHelper.registerLogListener(getActivity(), mDfuLogListener);
    }

    /**
     * 升级过程的监听，回调会返回相应的状态
     */
    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            LogUtils.debugInfo(TAG + "onDeviceConnecting");
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            LogUtils.debugInfo(TAG + "onDfuProcessStarting");
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            LogUtils.debugInfo(TAG + "onDfuProcessStarted");
            super.onDfuProcessStarted(deviceAddress);
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            LogUtils.debugInfo(TAG + "onProgressChanged-" + percent);
            super.onProgressChanged(deviceAddress, percent, speed, avgSpeed, currentPart, partsTotal);
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            super.onDeviceConnected(deviceAddress);

        }


        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            super.onEnablingDfuMode(deviceAddress);

        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            super.onFirmwareValidating(deviceAddress);
            LogUtils.debugInfo(TAG + "onFirmwareValidating");
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            super.onDeviceDisconnecting(deviceAddress);
            LogUtils.debugInfo(TAG + "onDeviceDisconnecting");
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            super.onDeviceDisconnected(deviceAddress);
            LogUtils.debugInfo(TAG + "onDeviceDisconnected");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            super.onDfuCompleted(deviceAddress);
            LogUtils.debugInfo(TAG + "onDfuCompleted");

            // let's wait a bit until we cancel the notification. When canceled
            // immediately it will be recreated by service again.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    oadSuccess();

                    // if this activity is still open and upload process was
                    // completed, cancel the notification
//                    final NotificationManager manager = (NotificationManager) getSystemService(
//                            Context.NOTIFICATION_SERVICE);
//                    manager.cancel(DfuService.NOTIFICATION_ID);
                }
            }, 200);

        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            LogUtils.debugInfo(TAG + "onDfuAborted");
            super.onDfuAborted(deviceAddress);
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            LogUtils.debugInfo(TAG + "onError=" + error + ",errorType=" + errorType + ",message=" + message);
            super.onError(deviceAddress, error, errorType, message);
            oadFail();

        }
    };

    /**
     * 升级过程的监听，主要用于查看日志，方便调试
     */
    private final DfuLogListener mDfuLogListener = new DfuLogListener() {
        @Override
        public void onLogEvent(String deviceAddress, int level, String message) {
            LogUtils.debugInfo(TAG + "deviceAddress=" + deviceAddress + ",message=" + message);
        }
    };

    /**
     * 固件升级必要的几个参数
     *
     * @return
     */
    private OadSetting getIntData() {
        boolean isOadModel = Boolean.getBoolean(DataHelper.getStringSF(MyApplication.getAppContext(), "isoadModel"));
        String deviceAddress = DataHelper.getStringSF(MyApplication.getAppContext(), "mac");
        OadSetting oadSetting = new OadSetting(deviceAddress, deviceVersion, deviceTestVersion, deviceNumber, isOadModel);
        return oadSetting;
    }

    public static EpConnectedFragment newInstance() {
        EpConnectedFragment fragment = new EpConnectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerEpConnectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .epConnectedModule(new EpConnectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ep_connected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mVpoperateManager = VPOperateManager.getMangerInstance(MyApplication.getAppContext());
        mPresenter.loadMenue();

        String name = DataHelper.getStringSF(MyApplication.getAppContext(), "ep_name");

        if (name == null || name.equals("") || name.length() == 0) {
            name = getResources().getString(R.string.default_ep_name);
        }

        String mac = DataHelper.getStringSF(MyApplication.getAppContext(), "mac");
        LogUtils.debugInfo(TAG + "-------------mac=" + mac);

        epNameTextView.setText(name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bracelet", MODE_PRIVATE);
        String address = sharedPreferences.getString("connected_address", "null");
        if (address.equals(mac)) {
            epStateTextView.setText(getResources().getString(R.string.ep_connected));
        } else {
            epStateTextView.setText(getResources().getString(R.string.ep_not_connected));
        }

        mVpoperateManager.readBattery(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "读取电晕 onResponse i=" + i);
            }
        }, new IBatteryDataListener() {
            @Override
            public void onDataChange(BatteryData batteryData) {
                String message = batteryData.getBatteryLevel() * 25 + "%";
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            }
        });

    }

    private int deviceNumber = 1;
    private String deviceVersion;
    private String deviceTestVersion;
    boolean isOadModel = false;
    boolean isNewSportCalc = false;

    @Override
    public void setAdapter(EpConnectedMenueAdapter adapter) {
        epMenue.setLayoutManager(new CustomGridLayoutManager(getActivity(), 3, false));
        epMenue.setAdapter(adapter);

        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent callIntent = new Intent(getActivity(), KnownCallActivity.class);
                        startActivity(callIntent);
                        break;
                    case 1:
                        Intent clockIntent = new Intent(getActivity(), ClockListActivity.class);
                        startActivity(clockIntent);
                        break;
                    case 2:
                        Intent messageIntent = new Intent(getActivity(), MessagePushActivity.class);
                        startActivity(messageIntent);
                        break;
                    case 3:
                        Intent questionIntent = new Intent(getActivity(), QuestionHandlerActivity.class);
                        startActivity(questionIntent);
                        break;
                    case 4:
                        LogUtils.debugInfo(TAG + ", deviceNumber=" + deviceNumber);

                        oadSetting = getIntData();
                        registerListener();

                        if(deviceNumber < 0) {
                            boolean is24Hourmodel = false;
                            mVpoperateManager.confirmDevicePwd(new IBleWriteResponse() {
                                @Override
                                public void onResponse(int i) {
                                    LogUtils.debugInfo(TAG + "固件升级查询版本号 confirmDevicePwd onResponse i=" + i);
                                }
                            }, new IPwdDataListener() {
                                @Override
                                public void onPwdDataChange(PwdData pwdData) {
                                    String message = "PwdData:\n" + pwdData.toString();
                                    LogUtils.debugInfo(TAG + message);
//                                sendMsg(message, 1);
                                    deviceNumber = pwdData.getDeviceNumber();
                                    deviceVersion = pwdData.getDeviceVersion();
                                    deviceTestVersion = pwdData.getDeviceTestVersion();

                                    mHandler.sendEmptyMessage(1);
                                }
                            }, new IDeviceFuctionDataListener() {
                                @Override
                                public void onFunctionSupportDataChange(FunctionDeviceSupportData functionSupport) {
                                    String message = "FunctionDeviceSupportData:\n" + functionSupport.toString();
                                    LogUtils.debugInfo(TAG + message);
                                    //sendMsg(message, 2);
                                    if (functionSupport.getNewCalcSport().equals(EFunctionStatus.SUPPORT)) {
                                        isNewSportCalc = true;
                                    } else {
                                        isNewSportCalc = false;
                                    }
                                }
                            }, new ISocialMsgDataListener() {
                                @Override
                                public void onSocialMsgSupportDataChange(FunctionSocailMsgData socailMsgData) {
                                    String message = "FunctionSocailMsgData:\n" + socailMsgData.toString();
                                    LogUtils.debugInfo(TAG + message);
                                    //sendMsg(message, 3);
                                }
                            }, "0000", is24Hourmodel);
                        }else {
                            startUpgrade();
                        }


                        break;
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @OnClick({R.id.ep_connected_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ep_connected_rl:
                Intent basicSettingsIntent = new Intent(getActivity(), BasicSettingsActivity.class);
//                startActivity(basicSettingsIntent);
                startActivityForResult(basicSettingsIntent, 100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 101) {
            LogUtils.debugInfo("---------------------------------------------------");

            ((MainActivity) getActivity()).disConnecte();
        }
    }

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

}