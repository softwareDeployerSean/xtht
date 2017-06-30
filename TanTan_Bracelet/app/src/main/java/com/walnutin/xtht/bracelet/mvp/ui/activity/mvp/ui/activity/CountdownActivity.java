package com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.utils.MyCountTimer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suns on 2017-06-28.
 */

public class CountdownActivity extends BaseActivity {

    @BindView(R.id.btnCountTimer)
    TextView btnCountTimer;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public int initView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_countdown;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String tag =getIntent().getStringExtra("tag");
        MyCountTimer myCountTimer = new MyCountTimer(4000, 1000, btnCountTimer,tag, this);
        myCountTimer.start();
    }

}
