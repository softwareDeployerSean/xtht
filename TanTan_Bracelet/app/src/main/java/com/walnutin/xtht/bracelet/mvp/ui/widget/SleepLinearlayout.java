package com.walnutin.xtht.bracelet.mvp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suns on 2017-07-13.
 */

public class SleepLinearlayout extends LinearLayout {
    private TextView tv_item;
    int total = 4;//总共多少份
    List<TextView> tvs = new ArrayList<>();

    public SleepLinearlayout(Context context) {
        this(context, null);
    }

    public SleepLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SleepLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        for (int i = 1; i < total; i++) {
            TextView tv = new TextView(context);
            if (i == 1) {
                tv.setBackgroundColor(context.getResources().getColor(R.color.sleep_deep));
                tv.setGravity(Gravity.BOTTOM);
                addView(tv, new LinearLayout.LayoutParams(
                        0, 400, 2));
            } else if (i == 2) {
                tv.setBackgroundColor(context.getResources().getColor(R.color.sleep_normal));
                tv.setGravity(Gravity.BOTTOM);
                addView(tv, new LinearLayout.LayoutParams(
                        0, 200, 1));
            } else if (i == 3) {
                tv.setBackgroundColor(context.getResources().getColor(R.color.white));
                tv.setGravity(Gravity.BOTTOM);
                addView(tv, new LinearLayout.LayoutParams(
                        0, 200, 1));
            }

        }


    }

}
