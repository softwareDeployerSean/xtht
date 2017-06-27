package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.app.Activity;

import no.nordicsemi.android.dfu.DfuBaseService;

/**
 * Created by timaimee on 2016/9/6.
 */
public class DfuService extends DfuBaseService {
    @Override
    protected Class<? extends Activity> getNotificationTarget() {
        return NotificationActivity.class;
    }
}
