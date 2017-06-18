package com.walnutin.xtht.bracelet.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.jess.arms.base.App;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.base.delegate.AppDelegate;
import com.jess.arms.di.component.AppComponent;
import com.mob.MobApplication;

/**
 * Created by suns on 2017-06-18.
 */

public class MyApplication  extends MobApplication implements App {

    private AppDelegate mAppDelegate;

    private static MyApplication bizApp;//单列模式

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.onCreate();
        bizApp = this;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return bizApp.getApplicationContext();
    }


    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate();
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }



}
