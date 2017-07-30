package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.HeartRateModel;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns on 2017-07-13.
 */

public class SleepLinearlayout extends AutoLinearLayout {
    private TextView tv_item;
    int total = 10;//总共多少份
    List<TextView> tvs = new ArrayList<>();
    int[] sleep_during;
    int sleep_all = 0;
    int[] sleep_status;

    SleepModel sleepModel = new SleepModel();

    public SleepLinearlayout(Context context) {
        this(context, null);
    }

    public SleepLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sleepModel = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, TimeUtil.getCurrentDate());
        if (sleepModel != null) {
            sleep_during = sleepModel.getDuraionTimeArray();
            sleep_status = sleepModel.getSleepStatusArray();
            for (int i = 0; i < sleep_during.length; i++) {
                sleep_all += sleep_during[i];
            }
            for (int j = 0; j < sleep_status.length; j++) {
                TextView tv = new TextView(context);
                int status = sleep_status[j];
                float proportion = sleep_during[j];
                LogUtils.debugInfo("1数据" + sleep_during[j]);
                LogUtils.debugInfo("2数据" + sleep_during[j] / sleep_all);
                switch (status) {
                    case 2:
                        tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                        tv.setGravity(Gravity.BOTTOM);
                        addView(tv, new LinearLayout.LayoutParams(
                                0, 400, proportion));
                        break;
                    case 1:
                        tv.setBackgroundColor(context.getResources().getColor(R.color.sleep_normal));
                        tv.setGravity(Gravity.BOTTOM);
                        addView(tv, new LinearLayout.LayoutParams(
                                0, 400, proportion));
                        break;
                    case 0:
                        tv.setBackgroundColor(context.getResources().getColor(R.color.sleep_deep));
                        tv.setGravity(Gravity.BOTTOM);
                        addView(tv, new LinearLayout.LayoutParams(
                                0, 800, proportion));
                        break;
                }
            }


        }


    }

}
