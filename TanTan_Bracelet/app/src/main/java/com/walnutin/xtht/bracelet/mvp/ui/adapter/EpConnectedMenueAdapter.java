package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.api.cache.CommonCache;
import com.walnutin.xtht.bracelet.mvp.model.entity.EpMenue;

import java.util.List;

/**
 * Created by Leiht on 2017/6/15.
 */

public class EpConnectedMenueAdapter extends RecyclerView.Adapter<EpConnectedMenueAdapter.MenueViewHolder> {
    List<EpMenue> epMenues = null;

    private Context mContext;

    public EpConnectedMenueAdapter(List<EpMenue> epMenues, Context context) {
        this.epMenues = epMenues;
        this.mContext = context;
    }

    @Override
    public EpConnectedMenueAdapter.MenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpConnectedMenueAdapter.MenueViewHolder holder = new EpConnectedMenueAdapter.MenueViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.ep_menue_item, null,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(EpConnectedMenueAdapter.MenueViewHolder holder, int position) {
//        holder.iv.setImageResource(R.id.);
//        holder.tv.setText("来电识别")
    }

    @Override
    public int getItemCount() {
        return epMenues.size();
    }

    class MenueViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;


        public MenueViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.ep_menue_icon);
            tv = (TextView) view.findViewById(R.id.ep_list_item_name);
        }
    }
}
