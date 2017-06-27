package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

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

                if(!isNetworkConnected(QuestionHandlerActivity.this)) {
                    ToastUtils.showToast("网络连接断开，请检查后再试", QuestionHandlerActivity.this);
                    return;
                }

                new Thread(){
                    @Override
                    public void run() {
                        try {
                           String subject = "用户建议";

                            String emailContent = "用户反馈意见：" + content + "\n";
                            if(contact != null && !contact.equals("")) {
                                emailContent += "联系方式：1.0" + contact + "\n";
                            }
                            emailContent += "软件版本号：1.0" + "\n";
                            emailContent += "android系统版本号" + android.os.Build.VERSION.RELEASE + "\n";
                            emailContent += "机型：" + android.os.Build.MODEL;

                            sendEmail(subject, emailContent);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(getResources().getString(R.string.question_ok), QuestionHandlerActivity.this);
                                    contentEt.setText("");
                                    contactEt.setText("");
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showToast(getResources().getString(R.string.question_failed), QuestionHandlerActivity.this);
                                }
                            });
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

    public void sendEmail(String subject, String content) throws Exception {
        Multipart multiPart;
        String finalString = "";

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.ym.163.com");
        props.put("mail.smtp.user", "suggest_gxd@walnutin.com");
        props.put("mail.smtp.password", "walnutinzhou");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "true");
        Log.i("Check", "done pops");
        Session session = Session.getDefaultInstance(props, null);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("suggest_gxd@walnutin.com"));
        message.setDataHandler(handler);
        Log.i("Check", "done sessions");

        multiPart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress("suggest_gxd@walnutin.com");
        message.addRecipient(Message.RecipientType.TO, toAddress);
        Log.i("Check", "added recipient");
        message.setSubject(subject);
        message.setContent(multiPart);
        message.setText(content);

        Log.i("check", "transport");
        Transport transport = session.getTransport("smtp");
        Log.i("check", "connecting");
        transport.connect("smtp.ym.163.com", "suggest_gxd@walnutin.com", "walnutinzhou");
        Log.i("check", "wana send");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        Log.i("check", "sent");
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