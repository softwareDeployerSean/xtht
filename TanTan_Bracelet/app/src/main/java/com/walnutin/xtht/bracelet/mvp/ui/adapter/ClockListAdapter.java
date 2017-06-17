package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.Clock;

import java.util.List;

/**
 * Created by Leiht on 2017/6/17.
 */

public class ClockListAdapter extends RecyclerView.Adapter<ClockListAdapter.MyViewHolder> {

    private Context mContext;

    private List<Clock> clockList;

    public ClockListAdapter(Context context, List<Clock> clockList) {
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

    }

    @Override
    public int getItemCount() {
        return clockList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

        }
    }
}
