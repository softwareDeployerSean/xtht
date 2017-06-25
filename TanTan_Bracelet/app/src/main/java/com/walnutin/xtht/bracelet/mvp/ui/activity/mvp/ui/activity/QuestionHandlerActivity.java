package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerQuestionHandlerComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.QuestionHandlerModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.QuestionHandlerContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.QuestionHandlerPresenter;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class QuestionHandlerActivity extends BaseActivity<QuestionHandlerPresenter> implements QuestionHandlerContract.View {

    @BindView(R.id.question_handler_btn)
    public Button handleBtn;

    @BindView(R.id.qusuggestion_handler_suggestion_edittext)
    public EditText contentEt;
    @BindView(R.id.qusuggestion_handler_contact)
    public EditText contactEt;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerQuestionHandlerComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .questionHandlerModule(new QuestionHandlerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_question_handler; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        handleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentEt.getText().toString();

                if(content == null || content.equals("") || content.length() == 0) {
                    ToastUtils.showToast("请输入反馈内容", QuestionHandlerActivity.this);
                    return;
                }

                String contact = contactEt.getText().toString();
                if(contact == null || contact.equals("") || contact.length() == 0) {
                    ToastUtils.showToast("请输入联系方式", QuestionHandlerActivity.this);
                    return;
                }

                ToastUtils.showToast("输入正常，等待服务器接口", QuestionHandlerActivity.this);

            }
        });
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


}