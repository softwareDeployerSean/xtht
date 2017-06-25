package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBindEndbyphoneComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindEndbyphoneModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindEndbyphoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BindEndbyphonePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BindEndbyphoneActivity extends BaseActivity<BindEndbyphonePresenter> implements BindEndbyphoneContract.View {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cheques_check)
    CheckBox chequesCheck;
    String phone;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBindEndbyphoneComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .bindEndbyphoneModule(new BindEndbyphoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_bind_endbyphone; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra("phone");
        tvName.setText(phone);
        chequesCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.getText().length());
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
        ToastUtils.showToast(message,this);
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


    @OnClick(R.id.bt_load)
    public void onViewClicked() {
        if (ConmonUtils.hasNetwork(this)) {
            String pwd = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
                ToastUtils.showToast(getString(R.string.pwd), this);
            } else {
                mPresenter.bind(phone, ConmonUtils.EncoderByMd5(ConmonUtils.EncoderByMd5(pwd)));
            }
        }
    }


    @Override
    public void bind_success() {
        DataHelper.setStringSF(MyApplication.getAppContext(), "isbind", "true");
        finish();
    }
}