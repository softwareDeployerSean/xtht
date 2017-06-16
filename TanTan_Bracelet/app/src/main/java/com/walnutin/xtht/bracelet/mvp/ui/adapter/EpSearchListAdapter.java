package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.Epuipment;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Leiht on 2017/6/14.
 */

public class EpSearchListAdapter extends RecyclerView.Adapter<EpSearchListAdapter.EpListViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Epuipment> epList;

    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public EpSearchListAdapter(List<Epuipment> epList, Context context) {
        this.epList = epList;
        this.mContext = context;
    }

    @Override
    public EpSearchListAdapter.EpListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.ep_list_item, parent,
                false);
        EpListViewHolder holder = new EpListViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(EpListViewHolder holder, int position) {
        if(position % 2 == 0) {
            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.epListOdd));
        }else {
            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.epListEven));
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return epList.size();
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    class EpListViewHolder extends RecyclerView.ViewHolder {

        TextView epNameTv;

        TextView epStatusTv;

        AutoRelativeLayout parent;

        public EpListViewHolder(View view) {
            super(view);
            parent = (AutoRelativeLayout) view.findViewById(R.id.ep_list_item_parent_ll);
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
}
