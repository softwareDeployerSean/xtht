package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.AppManager;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ConmonUtils;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerBindbyPhoneComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.BindbyPhoneModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.BindbyPhoneContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.BindbyPhonePresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.AlertView;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnDismissListener;
import com.walnutin.xtht.bracelet.mvp.ui.widget.defineddialog.OnItemClickListener;

import org.simple.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.jess.arms.integration.AppManager.APPMANAGER_MESSAGE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class BindbyPhoneActivity extends BaseActivity<BindbyPhonePresenter> implements BindbyPhoneContract.View, OnItemClickListener, OnDismissListener {


    @BindView(R.id.iv_bind)
    ImageView ivBind;
    @BindView(R.id.tv_bind)
    TextView tvBind;
    @BindView(R.id.tv_remove)
    TextView tv_remove;
    String is_bind = DataHelper.getStringSF(MyApplication.getAppContext(), "isbind");

    AlertView alertView_remove;

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


        if (is_bind.equals("default")) {
            tv_remove.setText(getString(R.string.bind_account));
            if (tag.equals("phone")) {
                tvBind.setText(getString(R.string.phone_number));
                ivBind.setImageResource(R.mipmap.shouji);
            } else if (tag.equals("email")) {
                tvBind.setText(getString(R.string.email_number));
                ivBind.setImageResource(R.mipmap.youjian);
            }
        } else {
            tv_remove.setText(getString(R.string.relieve_bind));
            if (tag.equals("phone")) {
                tvBind.setText(getString(R.string.bindbyphone_number));
                ivBind.setImageResource(R.mipmap.shouji);
            } else if (tag.equals("email")) {
                tvBind.setText(getString(R.string.bindbyemail_number));
                ivBind.setImageResource(R.mipmap.youjian);
            }


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

    public void remove() {
        alertView_remove = new AlertView(null, getString(R.string.remove_ok), getString(R.string.canecl), new String[]{getString(R.string.confirm)}, null, this, AlertView.Style.Alert, this)
                .setCancelable(true).setOnDismissListener(this);
        alertView_remove.show();
    }


    @OnClick({R.id.linear_bind})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_bind:
                if (ConmonUtils.hasNetwork(this)) {
                    if (is_bind.equals("default")) {
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
                            Intent intent = new Intent(this, ResetbyEmailActivity.class);
                            intent.putExtra("tag", "bind");
                            launchActivity(intent);
                        }
                    } else {
                        remove();
                    }
                }
                break;
        }
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0:
                if (ConmonUtils.hasNetwork(this)) {
                    mPresenter.unbind(tag);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (alertView_remove != null) {
                if (alertView_remove.isShowing()) {
                    alertView_remove.dismiss();
                    return false;
                }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}