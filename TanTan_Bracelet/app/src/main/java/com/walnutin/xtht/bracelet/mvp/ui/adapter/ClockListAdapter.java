package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.clock_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlarmSetting alarmSetting = clockList.get(position);
        holder.timeTv.setText(alarmSetting.getHour() + ":" + (alarmSetting.getMinute() < 10 ? "0" + alarmSetting.getMinute() : alarmSetting.getMinute()));
        if (alarmSetting.isOpen()) {
            if(holder.sv.getState() == SwitchView.STATE_SWITCH_OFF) {
                holder.sv.setState(true);
            }
        } else {
            if(holder.sv.getState() == SwitchView.STATE_SWITCH_ON) {
                holder.sv.setState(false);
            }
        }

        holder.sv.setmOnStateTriggerListener(new SwitchView.OnStateTriggerListener() {
            @Override
            public void triggerOn() {
                if (mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.onSwitchOn(position);
                }
            }

            @Override
            public void triggerOff() {
                if (mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.onSwitchOff(position);
                }
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.ondelClick(position);
                }
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnSwitchChangedListenerer != null) {
                    mOnSwitchChangedListenerer.onItemClick(position);
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
        Button delBtn;
        SwitchView sv;
        LinearLayout parent;
        public MyViewHolder(View view) {
            super(view);
            timeTv = (TextView) view.findViewById(R.id.clock_list_item_time);
            sv = (SwitchView) view.findViewById(R.id.clock_list_item_sw);
            delBtn = (Button) view.findViewById(R.id.clock_list_item_del_btn);
            parent = (LinearLayout) view.findViewById(R.id.clock_list_item_parent);
        }
    }

    public interface OnSwitchChangedListenerer {
        void onSwitchOn(int position);

        void onSwitchOff(int position);

        void ondelClick(int position);

        void onItemClick(int position);
    }
}
