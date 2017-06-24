package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerLoadingComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.LoadingModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.LoadingContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.LoadingPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class LoadingActivity extends BaseActivity<LoadingPresenter> implements LoadingContract.View, OnItemClickListener {

    AlertView alertView;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cheques_check)
    CheckBox cheques_check;
    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoadingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loadingModule(new LoadingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_loading; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String username = DataHelper.getStringSF(MyApplication.getAppContext(), "username");
        if (!username.equals("default")) {
            etName.setText(username);
        }
        cheques_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    @OnClick({R.id.bt_load, R.id.tv_froget_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_load:
                String username = etName.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                if (!ConmonUtils.checkEmail(username) && !ConmonUtils.isMobileNO(username)) {
                    ToastUtils.showToast(getString(R.string.patten_username), this);
                } else if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
                    ToastUtils.showToast(getString(R.string.pwd), this);
                } else {
                    mPresenter.load(username, ConmonUtils.EncoderByMd5(ConmonUtils.EncoderByMd5(pwd)));
                }
                break;
            case R.id.tv_froget_pwd://点击保存按钮
                forgetpwd();
                break;
        }
    }


    @Override
    public void forgetpwd() {
        alertView = new AlertView(null, null, null, null, new String[]{getString(R.string.reset_pwdbyphone), getString(R.string.reset_pwdbyemail)}, this, AlertView.Style.ActionSheet, this).setCancelable(true);
        alertView.show();
    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:

                // 打开注册页面
                RegisterPage registerPage = new RegisterPage("reset");
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            Intent intent = new Intent(LoadingActivity.this, ResetpwdActivity.class);
                            intent.putExtra("username", phone);
                            launchActivity(intent);
                        } else {
                        }
                    }
                });
                registerPage.show(this);
                break;
            case 1:
                launchActivity(new Intent(LoadingActivity.this, ResetbyEmailActivity.class));
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


    @Override
    public void load_success() {
        ToastUtils.showToast(getString(R.string.load_success), this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}