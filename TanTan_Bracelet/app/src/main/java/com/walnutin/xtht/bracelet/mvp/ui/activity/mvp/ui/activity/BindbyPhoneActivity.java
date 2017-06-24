package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBindbyPhoneComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindbyPhoneModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BindbyPhonePresenter;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BindbyPhoneActivity extends BaseActivity<BindbyPhonePresenter> implements BindbyPhoneContract.View {


    @BindView(R.id.iv_bind)
    ImageView ivBind;
    @BindView(R.id.tv_bind)
    TextView tvBind;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerBindbyPhoneComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .bindbyPhoneModule(new BindbyPhoneModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_bindby_phone; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    String tag = "";

    @Override
    public void initData(Bundle savedInstanceState) {
        tag = getIntent().getStringExtra("tag");
        if (tag.equals("phone")) {
            tvBind.setText(getString(R.string.phone_number));
            ivBind.setImageResource(R.mipmap.shouji);
        } else if (tag.equals("email")) {
            tvBind.setText(getString(R.string.email_number));
            ivBind.setImageResource(R.mipmap.youjian);
        }
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


    @OnClick({R.id.linear_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_bind:
                if (tag.equals("phone")) {
                    // 打开注册页面
                    RegisterPage registerPage = new RegisterPage("bind");
                    registerPage.setRegisterCallback(new EventHandler() {
                        public void afterEvent(int event, int result, Object data) {
                            // 解析注册结果
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                @SuppressWarnings("unchecked")
                                HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                                String country = (String) phoneMap.get("country");
                                String phone = (String) phoneMap.get("phone");
                                Intent intent = new Intent(BindbyPhoneActivity.this, BindEndbyphoneActivity.class);
                                intent.putExtra("phone", phone);
                                launchActivity(intent);

                            } else {

                            }
                        }
                    });
                    registerPage.show(this);

                } else if (tag.equals("email")) {
                    Intent intent=new Intent(this,ResetbyEmailActivity.class);
                    intent.putExtra("tag","bind");
                    launchActivity(intent);
                }


                break;
        }
    }

}