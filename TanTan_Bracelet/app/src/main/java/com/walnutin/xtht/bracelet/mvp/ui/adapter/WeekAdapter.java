package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;

import java.net.ConnectException;

/**
 * Created by Leiht on 2017/6/18.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.MyViewHolder> {

    private Context mContext;
    private String[] weeks;

    public WeekAdapter(Context context, String[] weeks) {
        this.mContext = context;
        this.weeks = weeks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WeekAdapter.MyViewHolder holder = new WeekAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.clock_week_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(weeks[position]);
    }

    @Override
    public int getItemCount() {
        return weeks.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.clock_add_week_textview);
            iv = (ImageView) view.findViewById(R.id.clock_add_week_imageview);
        }
    }
}
