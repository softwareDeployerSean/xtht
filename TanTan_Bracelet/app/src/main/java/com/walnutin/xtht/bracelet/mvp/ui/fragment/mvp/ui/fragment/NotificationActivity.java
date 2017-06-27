package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;

/**
 * Created by timaimee on 2016/9/6.
 */
public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTaskRoot()) {
            final Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(getIntent().getExtras()); // copy all extras
            startActivity(intent);
        }
        finish();
    }

}
