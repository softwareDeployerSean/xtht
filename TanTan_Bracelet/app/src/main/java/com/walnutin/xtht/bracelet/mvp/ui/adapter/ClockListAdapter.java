package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veepoo.protocol.model.settings.AlarmSetting;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.Clock;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.util.List;

/**
 * Created by Leiht on 2017/6/17.
 */

public class ClockListAdapter extends RecyclerView.Adapter<ClockListAdapter.MyViewHolder> {

    private Context mContext;

    private List<AlarmSetting> clockList;

    public void setmOnSwitchChangedListenerer(OnSwitchChangedListenerer mOnSwitchChangedListenerer) {
        this.mOnSwitchChangedListenerer = mOnSwitchChangedListenerer;
    }

    private OnSwitchChangedListenerer mOnSwitchChangedListenerer;

    public ClockListAdapter(Context context, List<AlarmSetting> clockList) {
        this.mContext = context;
        this.clockList = clockList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ClockListAdapter.MyViewHolder holder = new ClockListAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.clock_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlarmSetting alarmSetting = clockList.get(position);
        holder.timeTv.setText(alarmSetting.getHour() + ":" + alarmSetting.getMinute());
        if(alarmSetting.isOpen()) {
            holder.sv.setState(true);
        }else {
            holder.sv.setState(false);
        }

        holder.sv.setmOnStateTriggerListener(new SwitchView.OnStateTriggerListener() {
            @Override
            public void triggerOn() {
                if(mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.onSwitchOn(position);
                }
            }

            @Override
            public void triggerOff() {
                if(mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.onSwitchOff(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clockList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView timeTv;
        TextView weekTv;

        SwitchView sv;
        public MyViewHolder(View view) {
            super(view);
            timeTv = (TextView) view.findViewById(R.id.clock_list_item_time);
            weekTv = (TextView) view.findViewById(R.id.clock_list_item_week);
            sv = (SwitchView) view.findViewById(R.id.clock_list_item_sw);
        }
    }
    public interface OnSwitchChangedListenerer {
        void onSwitchOn(int position);
        void onSwitchOff(int position);
    }
}
