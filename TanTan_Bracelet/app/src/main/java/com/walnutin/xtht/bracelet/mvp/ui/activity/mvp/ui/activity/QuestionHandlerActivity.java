package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.app.utils.ToastUtils;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.component.DaggerQuestionHandlerComponent;
import com.walnutin.xtht.bracelet.mvp.ui.activity.di.module.QuestionHandlerModule;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.contract.QuestionHandlerContract;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.presenter.QuestionHandlerPresenter;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

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
//                if(contact == null || contact.equals("") || contact.length() == 0) {
//                    ToastUtils.showToast("请输入联系方式", QuestionHandlerActivity.this);
//                    return;
//                }

                if(!isNetworkConnected(QuestionHandlerActivity.this)) {
                    ToastUtils.showToast("网络连接断开，请检查后再试", QuestionHandlerActivity.this);
                    return;
                }

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            //创建HtmlEmail类
                            HtmlEmail email = new HtmlEmail();
                            //填写邮件的主机明
                            email.setHostName("walnutin.com");
                            email.setTLS(true);
                            email.setSSL(true);
                            //设置字符编码格式，防止中文乱码
                            email.setCharset("gbk");
                            //设置收件人的邮箱
                            email.addTo("suggest_gxd@walnutin.com");
                            //设置发件人的邮箱
                            email.setFrom("suggest_gxd@walnutin.com");
                            //填写发件人的用户名和密码
                            email.setAuthentication("suggest_gxd@walnutin.com", "walnutinzhou");
                            //填写邮件主题
                            email.setSubject("用户建议");

                            String emailContent = "用户反馈意见：" + content + "\n";
                            if(contact != null && !contact.equals("")) {
                                emailContent += "联系方式：1.0" + contact + "\n";
                            }
                            emailContent += "软件版本号：1.0" + "\n";
                            emailContent += "android系统版本号" + android.os.Build.VERSION.RELEASE + "\n";
                            emailContent += "机型：" + android.os.Build.MODEL;


                            //填写邮件内容
                            email.setMsg(emailContent);
                            //发送邮件
                            email.send();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast("感谢您的反馈", QuestionHandlerActivity.this);
                                }
                            });
                        } catch (EmailException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
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