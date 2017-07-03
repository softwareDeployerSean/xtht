package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.HealthPageData;
import com.walnutin.xtht.bracelet.mvp.ui.fragment.mvp.ui.fragment.HomePageItem;
import com.walnutin.xtht.bracelet.mvp.ui.widget.SwitchView;

import java.util.List;

/**
 * Created by Leiht on 2017/7/2.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    private List<HealthPageData> healthDatas;

    public HomePageAdapter(Context context, List<HealthPageData> healthDatas) {
        this.mContext = context;
        this.healthDatas = healthDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomePageAdapter.MyViewHolder holder = new HomePageAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.home_page_recyclerview_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HealthPageData data = healthDatas.get(position);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onImteClick(data.getType());
                }
            }
        });

        holder.timeIconTv.setText(data.getTime());
        if (data.getType() == 1) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xinlv);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);

            holder.rightTopTv.setTextColor(mContext.getResources().getColor(R.color.red_FF6466));

            holder.rightIconTv.setTextColor(mContext.getResources().getColor(R.color.red_FF6466));
        } else if (data.getType() == 2) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xueya);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 3) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.xueyang);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 4) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.jiuzuo);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 5) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.walk);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 6) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.run);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 7) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.sleep);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        } else if (data.getType() == 8) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.zhaixia);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.timeIconTv.setCompoundDrawables(null, drawable, null, null);
        }


        holder.leftTopTv.setText(data.getLeftTopName());

        holder.leftButtomTv.setText(data.getLeftButtom());

        if (data.getType() == 1) {
            String str = data.getRightTop() + "-" + data.getRightButtom() + "BPM";
            holder.rightTopTv.setText(str);
            holder.rightButtomTv.setText("");
            holder.rightIconTv.setText("查看");
            holder.rightIconTv.setTextSize(px2dip(mContext, 12));
        } else if (data.getType() == 2) {
            holder.rightTopTv.setText("高压  " + data.getRightTop() + " mmhg");
            holder.rightButtomTv.setText("低压  " + data.getRightButtom() + " mmhg");
            holder.rightIconTv.setText(data.getRightText());
            holder.rightIconTv.setTextSize(px2dip(mContext, 16));
        } else if (data.getType() == 4 || data.getType() == 5 || data.getType() == 6) {
            holder.rightTopTv.setText(data.getRightTop() + " min");
            holder.rightButtomTv.setText(data.getRightTop() + "大卡");

            holder.rightIconTv.setText(data.getRightText() + "公里");
            holder.rightIconTv.setTextSize(px2dip(mContext, 16));
        } else {
            holder.rightTopTv.setText(data.getRightTop());
            holder.rightButtomTv.setText(data.getRightButtom());
            holder.rightIconTv.setText(data.getRightText());
            holder.rightIconTv.setTextSize(px2dip(mContext, 16));
        }

        if (data.isRightIcon()) {
            Drawable drawable = null;
            if (data.getType() == 1) {
                drawable = mContext.getResources().getDrawable(R.mipmap.fanhuijianhs);
            } else {
                drawable = mContext.getResources().getDrawable(R.mipmap.fanhuijian);
            }
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.rightIconTv.setCompoundDrawables(null, null, drawable, null);
        } else {
            holder.rightIconTv.setCompoundDrawables(null, null, null, null);
        }

    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return healthDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeIconTv;

        TextView leftTopTv;

        TextView leftButtomTv;

        TextView rightTopTv;

        TextView rightButtomTv;

        TextView rightIconTv;

        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            timeIconTv = (TextView) view.findViewById(R.id.tv_time_icon);
            leftTopTv = (TextView) view.findViewById(R.id.tv_left_top);
            leftButtomTv = (TextView) view.findViewById(R.id.tv_left_buttom);
            rightTopTv = (TextView) view.findViewById(R.id.tv_right_top);
            rightButtomTv = (TextView) view.findViewById(R.id.tv_right_buttom);
            rightIconTv = (TextView) view.findViewById(R.id.tv_right_icon_text);

            parent = (RelativeLayout) view.findViewById(R.id.rl_parent);
        }
    }

    public interface OnItemClickListener {
        public void onImteClick(int type);
    }

}
