package com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.UiUtils;

import com.veepoo.protocol.VPOperateManager;
import com.veepoo.protocol.listener.base.IBleWriteResponse;
import com.veepoo.protocol.listener.data.IBatteryDataListener;
import com.veepoo.protocol.model.datas.BatteryData;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.ui.activity.MainActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.BasicSettingsActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.ClockListActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.EpConnectedActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.HardUpdateActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.KnownCallActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.MessagePushActivity;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.QuestionHandlerActivity;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.EpConnectedMenueAdapter;
import com.walnutin.xtht.bracelet.mvp.ui.adapter.OnItemClickListener;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.component.DaggerEpConnectedComponent;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.di.module.EpConnectedModule;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.contract.EpConnectedContract;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.presenter.EpConnectedPresenter;
import com.walnutin.xtht.bracelet.mvp.ui.widget.CustomGridLayoutManager;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class EpConnectedFragment extends BaseFragment<EpConnectedPresenter> implements EpConnectedContract.View {


    @BindView(R.id.ep_connected_menue)
    public RecyclerView epMenue;
    @BindView(R.id.ep_connected_name)
    public TextView epNameTextView;
    @BindView(R.id.ep_connected_status)
    public TextView epStateTextView;
    @BindView(R.id.ep_connected_dianliang)
    public TextView epPower;

    VPOperateManager mVpoperateManager;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            epPower.setText(message);
        }
    };

    public static EpConnectedFragment newInstance() {
        EpConnectedFragment fragment = new EpConnectedFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerEpConnectedComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .epConnectedModule(new EpConnectedModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ep_connected, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mVpoperateManager = VPOperateManager.getMangerInstance(MyApplication.getAppContext());
        mPresenter.loadMenue();

        String name = DataHelper.getStringSF(MyApplication.getAppContext(), "ep_name");

        if (name == null || name.equals("") || name.length() == 0) {
            name = getResources().getString(R.string.default_ep_name);
        }

        String mac = DataHelper.getStringSF(MyApplication.getAppContext(), "mac");
        LogUtils.debugInfo(TAG + "-------------mac=" + mac);

        epNameTextView.setText(name);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("bracelet", MODE_PRIVATE);
        String address = sharedPreferences.getString("connected_address", "null");
        if (address.equals(mac)) {
            epStateTextView.setText(getResources().getString(R.string.ep_connected));
        } else {
            epStateTextView.setText(getResources().getString(R.string.ep_not_connected));
        }

        mVpoperateManager.readBattery(new IBleWriteResponse() {
            @Override
            public void onResponse(int i) {
                LogUtils.debugInfo(TAG + "读取电晕 onResponse i=" + i);
            }
        }, new IBatteryDataListener() {
            @Override
            public void onDataChange(BatteryData batteryData) {
                String message = batteryData.getBatteryLevel() * 25 + "%";
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            }
        });

    }


    @Override
    public void setAdapter(EpConnectedMenueAdapter adapter) {
        epMenue.setLayoutManager(new CustomGridLayoutManager(getActivity(), 3, false));
        epMenue.setAdapter(adapter);

        adapter.setmOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent callIntent = new Intent(getActivity(), KnownCallActivity.class);
                        startActivity(callIntent);
                        break;
                    case 1:
                        Intent clockIntent = new Intent(getActivity(), ClockListActivity.class);
                        startActivity(clockIntent);
                        break;
                    case 2:
                        Intent messageIntent = new Intent(getActivity(), MessagePushActivity.class);
                        startActivity(messageIntent);
                        break;
                    case 3:
                        Intent questionIntent = new Intent(getActivity(), QuestionHandlerActivity.class);
                        startActivity(questionIntent);
                        break;
                    case 4:
                        Intent updateIntent = new Intent(getActivity(), HardUpdateActivity.class);
                        startActivity(updateIntent);
                        break;
                }
            }
        });
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @OnClick({R.id.ep_connected_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ep_connected_rl:
                Intent basicSettingsIntent = new Intent(getActivity(), BasicSettingsActivity.class);
//                startActivity(basicSettingsIntent);
                startActivityForResult(basicSettingsIntent, 100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == 101) {
            LogUtils.debugInfo("---------------------------------------------------");

            ((MainActivity)getActivity()).disConnecte();
        }
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