package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.mob.MobSDK;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerLoadComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.LoadModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.LoadPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoadActivity extends BaseActivity<LoadPresenter> implements LoadContract.View, OnItemClickListener, Handler.Callback {


    @BindView(R.id.bt_regist)
    Button btRegist;
    @BindView(R.id.bt_load)
    Button btLoad;
    AlertView alertView;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoadComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loadModule(new LoadModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_load; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    @OnClick({R.id.bt_regist, R.id.bt_load})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_regist:
                regist();
                break;
            case R.id.bt_load://登陆
                startActivity(new Intent(LoadActivity.this, LoadingActivity.class));
                break;
        }
    }

    @Override
    public void regist() {
        alertView = new AlertView(null, null, null, null, new String[]{getString(R.string.registbyphone), getString(R.string.registbyemail)}, this, AlertView.Style.ActionSheet, this).setCancelable(true);
        alertView.show();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                this.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
        registerSDK();
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


    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                // 打开注册页面
                RegisterPage registerPage = new RegisterPage("regist");
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            Intent intent = new Intent(LoadActivity.this, RegistbyPhoneActivity.class);
                            intent.putExtra("phone", phone);
                            launchActivity(intent);

                        } else {

                        }
                    }
                });
                registerPage.show(this);
                break;
            case 1:
                launchActivity(new Intent(LoadActivity.this, RegistbyEailActivity.class));
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (alertView != null) {
                if (alertView.isShowing()) {
                    alertView.dismiss();
                    return false;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerSDK() {
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        SMSSDK.setAskPermisionOnReadContact(true);
        final Handler handler = new Handler(this);
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();

    }

    public boolean handleMessage(Message msg) {

        int event = msg.arg1;
        int result = msg.arg2;
        Object data = msg.obj;
        if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
            // 短信注册成功后，返回MainActivity,然后提示新好友
            if (result == SMSSDK.RESULT_COMPLETE) {
                Toast.makeText(this, R.string.smssdk_user_info_submited, Toast.LENGTH_SHORT).show();
            } else {
                ((Throwable) data).printStackTrace();
            }
        } else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
            if (result == SMSSDK.RESULT_COMPLETE) {


            } else {
                ((Throwable) data).printStackTrace();
            }
        }
        return false;
    }




}