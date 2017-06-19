package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerRegistbyEailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.RegistbyEailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.RegistbyEailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.RegistbyEailPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;


import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RegistbyEailActivity extends BaseActivity<RegistbyEailPresenter> implements RegistbyEailContract.View {

    @BindView(R.id.et_mail)
    EditText et_mail;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cheques_check)
    CheckBox cheques_check;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRegistbyEailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .registbyEailModule(new RegistbyEailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_registby_eail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        cheques_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                et_password.setSelection(et_password.getText().length());
            }
        });
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
                String email = et_mail.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !ConmonUtils.checkEmail(email)) {
                    ToastUtils.showToast(getString(R.string.pattern_email), this);
                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
                    ToastUtils.showToast(getString(R.string.pwd), this);
                } else {
                    mPresenter.regist(email, ConmonUtils.EncoderByMd5(ConmonUtils.EncoderByMd5(pwd)));
                }


                break;
        }
    }

    @Override
    public void regist_success() {
        ToastUtils.showToast(getString(R.string.regist_success), this);
        launchActivity(new Intent(this, MainActivity.class));

        finish();
    }
}