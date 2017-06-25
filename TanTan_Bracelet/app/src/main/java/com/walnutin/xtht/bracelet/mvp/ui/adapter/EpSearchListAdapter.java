package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inuker.bluetooth.library.search.SearchResult;
import com.walnutin.xtht.bracelet.R;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Leiht on 2017/6/14.
 */

public class EpSearchListAdapter extends RecyclerView.Adapter<EpSearchListAdapter.EpListViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<SearchResult> epList;

    private OnItemClickListener mOnItemClickListener = null;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public EpSearchListAdapter(List<SearchResult> epList, Context context) {
        this.epList = epList;
        this.mContext = context;
    }

    @Override
    public EpListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.ep_list_item, parent,
                false);
        EpListViewHolder holder = new EpListViewHolder(view);
//        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(EpListViewHolder holder, int position) {
        SearchResult searchResult = epList.get(position);
        holder.epNameTv.setText(searchResult.getName());
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bracelet", MODE_PRIVATE);
        String address = sharedPreferences.getString("connected_address", "null");
        if(address.equals(searchResult.getAddress())) {
            holder.epStatusTv.setText(mContext.getResources().getString(R.string.ep_connected));

        }else {
            holder.epStatusTv.setText(mContext.getResources().getString(R.string.ep_not_connected));
        }

        holder.parent.setOnClickListener(this);

//        if(position % 2 == 0) {
//            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.epListOdd));
//        }else {
//            holder.parent.setBackgroundColor(mContext.getResources().getColor(R.color.epListEven));
//        }

        holder.epDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onDelBtnClick(position);
                }
            }
        });

//        holder.itemView.setTag(position);
        holder.parent.setTag(position);
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

        Button epDelBtn;
        public EpListViewHolder(View view) {
            super(view);
            epNameTv = (TextView) view.findViewById(R.id.ep_list_item_name);
            epStatusTv = (TextView) view.findViewById(R.id.ep_list_item_status);
            parent = (AutoRelativeLayout) view.findViewById(R.id.ep_list_item_parent_ll);
            epDelBtn = (Button) view.findViewById(R.id.ep_search_del_btn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onDelBtnClick(int position);

    }

}
