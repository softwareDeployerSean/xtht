package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBindEndComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindEndModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindEndContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BindEndPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BindEndActivity extends BaseActivity<BindEndPresenter> implements BindEndContract.View {


    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_yanzhenma)
    EditText etYanzhenma;
    @BindView(R.id.bt_yanzhenma)
    Button btYanzhenma;
    @BindView(R.id.bt_regist)
    Button btRegist;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBindEndComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .bindEndModule(new BindEndModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_bind_end; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btYanzhenma.setText(millisUntilFinished / 1000 + "秒");
            btYanzhenma.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btYanzhenma.setText("发送验证码");
            btYanzhenma.setEnabled(true);
        }
    };


    @Override
    public void initData(Bundle savedInstanceState) {

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

    String email = "",code="";
    @OnClick({R.id.bt_yanzhenma, R.id.bt_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_yanzhenma:
                email = etNumber.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !ConmonUtils.checkEmail(email)) {
                    ToastUtils.showToast(getString(R.string.pattern_email), this);
                } else {
                    mPresenter.getcode(email);
                    timer.start();
                }
                break;
            case R.id.bt_regist:




                break;
        }
    }
}