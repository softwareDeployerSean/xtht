package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRegistbyPhoneComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RegistbyPhoneModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RegistbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RegistbyPhonePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;


import org.simple.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegistbyPhoneActivity extends BaseActivity<RegistbyPhonePresenter> implements RegistbyPhoneContract.View {


    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.et_password)
    TextView et_password;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegistbyPhoneComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registbyPhoneModule(new RegistbyPhoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_registby_phone; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    String phone = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        tv_phone.setText(phone);
    }


    @Override
    public void showLoading() {
        CustomProgressDialog.show(this);
    }

    @Override
    public void hideLoading() {
        CustomProgressDialog.dissmiss();
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


    @OnClick({R.id.bt_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_regist:
                String pwd = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
                    ToastUtils.showToast(getString(R.string.pwd), this);
                } else {
                    mPresenter.regist(phone, ConmonUtils.EncoderByMd5(ConmonUtils.EncoderByMd5(pwd)));
                }


                break;
        }
    }

    @Override
    public void regist_success() {
        ToastUtils.showToast(getString(R.string.regist_success), this);
        Intent intent = new Intent(this, Personal_dataActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}