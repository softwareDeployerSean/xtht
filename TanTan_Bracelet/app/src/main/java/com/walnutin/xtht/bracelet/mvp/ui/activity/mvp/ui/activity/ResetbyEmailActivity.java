package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerResetbyEmailComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.ResetbyEmailModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.ResetbyEmailContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.ResetbyEmailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ResetbyEmailActivity extends BaseActivity<ResetbyEmailPresenter> implements ResetbyEmailContract.View {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_yanzhenma)
    EditText etYanzhenma;
    @BindView(R.id.bt_yanzhenma)
    Button btYanzhenma;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerResetbyEmailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .resetbyEmailModule(new ResetbyEmailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_resetby_email; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

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


    @OnClick({R.id.bt_next, R.id.bt_yanzhenma})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:


                launchActivity(new Intent(ResetbyEmailActivity.this, ResetpwdActivity.class));
                break;
            case R.id.bt_yanzhenma:
                String email = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !ConmonUtils.checkEmail(email)) {
                    ToastUtils.showToast(getString(R.string.pattern_email), this);
                } else {



                }

                break;


        }
    }


}