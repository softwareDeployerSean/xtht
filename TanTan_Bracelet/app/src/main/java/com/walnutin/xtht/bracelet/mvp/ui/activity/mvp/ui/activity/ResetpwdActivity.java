package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerResetpwdComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ResetpwdModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetpwdContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ResetpwdPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.SMSSDK;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ResetpwdActivity extends BaseActivity<ResetpwdPresenter> implements ResetpwdContract.View {


    @BindView(R.id.input_new_pwd)
    EditText inputNewPwd;
    @BindView(R.id.firm_pwd)
    EditText firmPwd;
    String username = "";

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerResetpwdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .resetpwdModule(new ResetpwdModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_resetpwd; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");
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


    @OnClick(R.id.bt_ok)
    public void onViewClicked() {
        if (ConmonUtils.hasNetwork(this)) {
            String new_pwd = inputNewPwd.getText().toString().trim();
            String firm_pwd = firmPwd.getText().toString().trim();
            if (TextUtils.isEmpty(new_pwd) || new_pwd.length() < 6 || new_pwd.length() > 16) {
                ToastUtils.showToast(getString(R.string.pwd), this);
            } else if (!new_pwd.equals(firm_pwd)) {
                ToastUtils.showToast(getString(R.string.wrong_password), this);
            } else {
                mPresenter.check_password(username, ConmonUtils.EncoderByMd5(ConmonUtils.EncoderByMd5(new_pwd)).trim());
            }
        }
    }

    @Override
    public void check_success() {
        ToastUtils.showToast(getString(R.string.password_success), this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();

    }

}