package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.app.MyApplication;
import com.walnutin.xtht.bracelet.mvp.model.entity.Device;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Leiht on 2017/6/14.
 */

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.EpListViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<String> epList;
    private int weight = 0;

    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SleepAdapter(List<String> epList, Context context, int weight) {
        this.epList = epList;
        this.mContext = context;
        this.weight = weight;
    }

    @Override
    public EpListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.sleep_item, parent,
                false);
        EpListViewHolder holder = new EpListViewHolder(view);
//        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(EpListViewHolder holder, int position) {
        LogUtils.debugInfo("高度多少+"+weight);
        int weight_item = weight / epList.size();
        ViewGroup.LayoutParams lp = holder.linear_sleep.getLayoutParams();
        lp.width = weight_item;
        lp.height = 420;
        holder.linear_sleep.setLayoutParams(lp);
        String device = epList.get(position);
        holder.tv_hour.setText(device);
    }

    @Override
    public int getItemCount() {
        return epList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    class EpListViewHolder extends RecyclerView.ViewHolder {

        TextView tv_hour;
        LinearLayout linear_sleep;

        public EpListViewHolder(View view) {
            super(view);
            tv_hour = (TextView) view.findViewById(R.id.tv_hour);
            linear_sleep = (LinearLayout) view.findViewById(R.id.linear_sleep);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onDelBtnClick(int position);

    }

}
