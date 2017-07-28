package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns on 2017-07-13.
 */

public class SleepTagLinearlayout extends AutoLinearLayout {
    private TextView tv_item;
    int total = 10;//总共多少份
    List<TextView> tvs = new ArrayList<>();
    int time = 0;//小时
    SleepModel sleepModel = new SleepModel();

    int[] sleep_during;

    public SleepTagLinearlayout(Context context) {
        this(context, null);
    }

    public SleepTagLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepTagLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sleepModel = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, TimeUtil.getCurrentDate());
        if (sleepModel != null) {
            sleep_during = sleepModel.getDuraionTimeArray();
            int during_all = 0;
            for (int i = 0; i < sleep_during.length; i++) {
                during_all += sleep_during[i];
            }
            time = (int) during_all / 60;
            if (time < 9) {
                for (int j = 1; j <= time; j++) {
                    TextView tv = new TextView(context);
                    tv.setText(j + "");
                    tv.setTextColor(context.getResources().getColor(R.color.fontColor));
                    tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv.setGravity(Gravity.BOTTOM);
                    addView(tv, new LinearLayout.LayoutParams(
                            0, 50, 1));
                }
            } else {
                for (int j = 1; j <= time; j++) {
                    if (j % 2 != 0) {
                        TextView tv = new TextView(context);
                        tv.setText(j + "");
                        tv.setTextColor(context.getResources().getColor(R.color.fontColor));
                        tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                        tv.setGravity(Gravity.CENTER_HORIZONTAL);
                        tv.setGravity(Gravity.BOTTOM);
                        addView(tv, new LinearLayout.LayoutParams(
                                0, 50, 1));
                    }

                }
            }
        }

    }


}

