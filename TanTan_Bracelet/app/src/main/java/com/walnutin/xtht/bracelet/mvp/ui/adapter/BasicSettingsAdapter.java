package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.BasicSettingsMenue;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.util.List;

/**
 * Created by Leiht on 2017/6/17.
 */

public class BasicSettingsAdapter extends RecyclerView.Adapter<BasicSettingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<BasicSettingsMenue> settingsMenues;

    public void setmOnSwitchButtonChanged(OnSwitchButtonChanged mOnSwitchButtonChanged) {
        this.mOnSwitchButtonChanged = mOnSwitchButtonChanged;
    }

    OnSwitchButtonChanged mOnSwitchButtonChanged;

    public BasicSettingsAdapter(Context context, List<BasicSettingsMenue> settingsMenues) {
        this.mContext = context;
        this.settingsMenues = settingsMenues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BasicSettingsAdapter.MyViewHolder holder = new BasicSettingsAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.basic_settings_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BasicSettingsMenue menue = settingsMenues.get(position);
        holder.tv.setText(menue.getName());
        if(menue.isChangeTextColor()) {
            //这里没有判断颜色，直接设置为红色
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.redFF1212));
        }
        if(menue.isSetMargin()) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.parent.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin + menue.getMargin(), params.rightMargin, params.bottomMargin);
            holder.parent.setLayoutParams(params);
        }
        if(!menue.isNeedCheckBox()) {
            holder.sv.setVisibility(View.INVISIBLE);
        }
        if(menue.isChecked()) {
            holder.sv.setState(true);
        }else {
            holder.sv.setState(false);
        }

        holder.sv.setmOnStateTriggerListener(new SwitchView.OnStateTriggerListener() {
            @Override
            public void triggerOn() {
                if(mOnSwitchButtonChanged != null) {
                    mOnSwitchButtonChanged.onSwitchOn(position);
                }
            }
            @Override
            public void triggerOff() {
                if(mOnSwitchButtonChanged != null) {
                    mOnSwitchButtonChanged.onSwitchOff(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsMenues.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        SwitchView sv;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.basic_settings_top_textview);
            sv = (SwitchView) view.findViewById(R.id.basic_settings_item_switchview);
            parent = (RelativeLayout) view.findViewById(R.id.basic_settings_item_parent);
        }
    }

    public interface OnSwitchButtonChanged {
        void onSwitchOn(int position);
        void onSwitchOff(int position);
    }
}
