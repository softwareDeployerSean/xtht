package com.walnutin.xtht.bracelet.ProductList.fragment;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.walnutin.xtht.bracelet.ProductList.DensityUtils;
import com.walnutin.xtht.bracelet.R;


/**
 * 作者：MrJiang on 2016/6/28 18:38
 */
public class BaseActivity extends AppCompatActivity {
    public TopTitleLableView topTitleLableView;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        topTitleLableView = (TopTitleLableView) findViewById(R.id.topTitle);
        topTitleLableView.setOnBackListener(new TopTitleLableView.OnBackListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = topTitleLableView.getTitleRl().getLayoutParams();
            layoutParams.height = DensityUtils.dip2px(getApplicationContext(), 72);
            topTitleLableView.setLayoutParams(layoutParams);
            topTitleLableView.getTitleRl().setLayoutParams(layoutParams);
            topTitleLableView.getBackView().setPadding(DensityUtils.dip2px(getApplicationContext(), 14), DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
            topTitleLableView.getTitleView().setPadding(0, DensityUtils.dip2px(getApplicationContext(), 22), 0, 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
