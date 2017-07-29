package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.UiUtils;

import com.walnutin.xtht.bracelet.ProductList.TimeUtil;
import com.walnutin.xtht.bracelet.ProductList.db.SqlHelper;
import com.walnutin.xtht.bracelet.ProductList.entity.SleepModel;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerDaySelectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.DaySelectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.DaySelectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.DaySelectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.text.DecimalFormat;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DaySelectedFragment extends BaseFragment<DaySelectedPresenter> implements DaySelectedContract.View {

    @BindView(R.id.day_sleep_sw)
    public SwitchView clockSwitchView;

    @BindView(R.id.sleep_start_time)
    public TextView sleepStartTime;

    @BindView(R.id.sleep_end_time)
    public TextView sleepEndTime;

    @BindView(R.id.plan_sleep_time)
    public TextView planSleepTime;

    @BindView(R.id.actual_sleep_time)
    public TextView actualSleepTime;

    @BindView(R.id.line_start_time)
    public TextView lineStartTime;

    @BindView(R.id.line_end_time)
    public TextView lineEndTime;

    @BindView(R.id.sleep_deep_per_tv)
    public TextView deepSleepPerTv;

    @BindView(R.id.sleep_simple_per_tv)
    public TextView simpleSleepPerTv;

    @BindView(R.id.sleep_aweak_per_tv)
    public TextView awakeSleepPerTv;

    @BindView(R.id.sleep_deep_value_tv)
    public TextView deepSleepValueTv;

    @BindView(R.id.sleep_simple_value_tv)
    public TextView simpleSleepValueTv;

    @BindView(R.id.sleep_aweak_value_tv)
    public TextView aweakSleepValueTv;

    @BindView(R.id.sleep_all_time_tv)
    public TextView sleepAllTimeTv;

    @BindView(R.id.sleep_level_tv)
    public TextView sleepLevelTv;

    private String date;

    private SleepModel sleepModel = null;

    public static DaySelectedFragment newInstance() {
        DaySelectedFragment fragment = new DaySelectedFragment();
        return fragment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerDaySelectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .daySelectedModule(new DaySelectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_selected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        clockSwitchView.setState(true);

        loadDatas();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            sleepModel = new SleepModel();
//            sleepModel.setDate("2017-07-29");
//            sleepModel.setDuraionTimeArray(new int[] {30,15,45,30,30,30,45,30,30,15,15,15,45,15,15});
//            sleepModel.setTimePointArray(new int[] {1438,13,58,88,118,148,193,223,253,268,283,298,343,373,388});
//            sleepModel.setSleepStatusArray(new int[] {1,0,1,0,2,0,1,2,1,0,1,0,1,1,2});
            if(sleepModel != null) {
                int[] duraionTimeArray = sleepModel.getDuraionTimeArray();
                int[] timePointArray = sleepModel.getTimePointArray();
                int[] sleepStatusArray = sleepModel.getSleepStatusArray();
                sleepStartTime.setText((timePointArray[0] - duraionTimeArray[0]) / 60 + ":" + (timePointArray[0] - duraionTimeArray[0]) % 60);
                sleepEndTime.setText((timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) / 60 + ":" + (timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) % 60);
                lineEndTime.setText((timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) / 60 + ":" + (timePointArray[timePointArray.length - 1] - duraionTimeArray[duraionTimeArray.length - 1]) % 60);
                lineStartTime.setText((timePointArray[0] - duraionTimeArray[0]) / 60 + ":" + (timePointArray[0] - duraionTimeArray[0]) % 60);

                int totalSleepTime = 0;
                if(duraionTimeArray != null && duraionTimeArray.length > 0) {
                    for (int i = 0; i < duraionTimeArray.length; i++) {
                        totalSleepTime += duraionTimeArray[i];
                    }
                }
                actualSleepTime.setText(totalSleepTime > 60 ? (totalSleepTime / 60 + "小时" + (totalSleepTime % 60 > 0 ? totalSleepTime % 60 + "分" : "")) : totalSleepTime + "分");

                int deepSleepTotal = 0;
                int simpleSleepTotal = 0;
                int awakeSleepTotal = 0;
                if(sleepStatusArray != null && sleepStatusArray.length > 0) {
                    for(int i = 0; i < sleepStatusArray.length; i++) {
                        if(sleepStatusArray[i] == 2) {
                            awakeSleepTotal += duraionTimeArray[i];
                        }else if(sleepStatusArray[i] == 1) {
                            deepSleepTotal += duraionTimeArray[i];
                        }else if(sleepStatusArray[i] == 0) {
                            simpleSleepTotal += duraionTimeArray[i];
                        }
                    }
                }
                if((deepSleepTotal + simpleSleepTotal + awakeSleepTotal) > 0) {
                    int a1 = (int)((((float)deepSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    int b1 = (int)((((float)simpleSleepTotal / (deepSleepTotal + simpleSleepTotal + awakeSleepTotal))) * 100);
                    String a = a1 + "%";
                    String b = b1 + "%";
                    String c = (100 -  a1 - b1) + "%";
                    deepSleepPerTv.setText(a);
                    simpleSleepPerTv.setText(b);
                    awakeSleepPerTv.setText(c);
                }else {
                    deepSleepPerTv.setText("");
                    simpleSleepPerTv.setText("");
                    awakeSleepPerTv.setText("");
                }

                deepSleepValueTv.setText(deepSleepTotal > 60 ?  (deepSleepTotal / 60 + "h" + (deepSleepTotal % 60 > 0 ? deepSleepTotal % 60 + "min" : "")) : deepSleepTotal + "min");
                simpleSleepValueTv.setText(simpleSleepTotal > 60 ?  (simpleSleepTotal / 60 + "h" + (simpleSleepTotal % 60 > 0 ? simpleSleepTotal % 60 + "min" : "")) : simpleSleepTotal + "min");
                aweakSleepValueTv.setText(awakeSleepTotal > 60 ?  (awakeSleepTotal / 60 + "h" + (awakeSleepTotal % 60 > 0 ? awakeSleepTotal % 60 + "min" : "")) : awakeSleepTotal + "min");

                DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p=decimalFormat.format(totalSleepTime / (float) 60);
                sleepAllTimeTv.setText(p);

                float t = Float.parseFloat(p);
                String level = "";
                if(t <= 10 && t >- 7) {
                    level = "优秀";
                }else if(t < 7 && t >= 5){
                    level = "良好";
                }else {
                    level = "较差";
                }
                sleepLevelTv.setText(level);
            }else {
                sleepStartTime.setText("");
                sleepEndTime.setText("");
                lineEndTime.setText("");
                lineStartTime.setText("");
                actualSleepTime.setText("");
                deepSleepPerTv.setText("");
                simpleSleepPerTv.setText("");
                awakeSleepPerTv.setText("");
                deepSleepValueTv.setText("");
                simpleSleepValueTv.setText("");
                aweakSleepValueTv.setText("");
                sleepAllTimeTv.setText("");
                sleepLevelTv.setText("");
            }
        }
    };

    private void loadDatas() {
        new Thread(){
            @Override
            public void run() {
                sleepModel = SqlHelper.instance().getOneDaySleepListTime(MyApplication.account, TimeUtil.getCurrentDate());
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

}