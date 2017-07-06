package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.veepoo.protocol.model.enums.EFunctionStatus;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.MarginMenue;
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.ui.activity.MessagePushActivity;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by Leiht on 2017/6/17.
 */

public class MessagePushAdapter extends RecyclerView.Adapter<MessagePushAdapter.MyViewHolder> implements View.OnClickListener {

    private static final String TAG = "[TAN][" + MessagePushAdapter.class.getSimpleName() + "]";
    private Context mContext;
    List<MarginMenue> msgMenues;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener = null;

    public MessagePushAdapter(Context context, List<MarginMenue> msgMenues) {
        this.mContext = context;
        this.msgMenues = msgMenues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.message_push_item, parent,
                false);
        MessagePushAdapter.MyViewHolder holder = new MessagePushAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MarginMenue msgMenux = msgMenues.get(position);
        holder.tv.setText(msgMenux.getName());
        if(msgMenux.getName().contains("Facebook")) {
            holder.iv.setImageResource(R.mipmap.afacebook);
        }else if(msgMenux.getName().contains("Twitter")) {
            holder.iv.setImageResource(R.mipmap.twitters);
        }
        else if(msgMenux.getName().contains("QQ")) {
            holder.iv.setImageResource(R.mipmap.weixin);
        }
        else if(msgMenux.getName().contains("微信")) {
            holder.iv.setImageResource(R.mipmap.weixin);
        }
        else if(msgMenux.getName().contains("短信")) {
            holder.iv.setImageResource(R.mipmap.duanxin);
        }
        else if(msgMenux.getName().contains("其它")) {
            holder.iv.setImageResource(R.mipmap.other);
        }

        if(msgMenux.isSetMargin()) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.parent.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin + msgMenux.getMargin(), params.rightMargin, params.bottomMargin);
            holder.parent.setLayoutParams(params);
        }

        if(msgMenux.isChecked()) {
            holder.sv.setState(true);
        }

        holder.sv.setmOnStateTriggerListener(new SwitchView.OnStateTriggerListener() {
            @Override
            public void triggerOn() {
                LogUtils.debugInfo(TAG + " triggerOn ");
                ((MessagePushActivity)mContext).updateSocailMsgData(position, EFunctionStatus.SUPPORT_OPEN);
            }

            @Override
            public void triggerOff() {
                LogUtils.debugInfo(TAG + " triggerOff ");
                ((MessagePushActivity)mContext).updateSocailMsgData(position, EFunctionStatus.SUPPORT_CLOSE);
            }
        });

        holder.parent.setTag(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return msgMenues.size();
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv;
        SwitchView sv;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.message_push_item_imageview);
            tv = (TextView) view.findViewById(R.id.message_push_item_textview);
            sv = (SwitchView) view.findViewById(R.id.message_push_item_switchview);
            parent = (RelativeLayout) view.findViewById(R.id.message_push_item_parent);
        }
    }
}
