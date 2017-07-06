package com.walnutin.xtht.bracelet.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.LogUtils;
import com.walnutin.xtht.bracelet.R;
import com.walnutin.xtht.bracelet.mvp.model.entity.ExerciserData;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Leiht on 2017/7/5.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private onItemClickListener mOnItemClickListener;

    private Context mContext;

    private List<ExerciserData> exerciseList;

    public ExerciseListAdapter(Context context, List<ExerciserData> exerciseList) {
        this.mContext = context;
        this.exerciseList = exerciseList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(
                    mContext).inflate(R.layout.exercise_list_item, parent,
                    false);
            ExerciseListAdapter.MyViewHolder holder = new ExerciseListAdapter.MyViewHolder(view);
//        view.setOnClickListener(this);
            return holder;
        } else if (viewType == TYPE_FOOTER) {
            // type == TYPE_FOOTER 返回footerView
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.exerceser_list_footer, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder)viewHolder;
            holder.monthTitleTv.setVisibility(View.GONE);

            ExerciserData data = exerciseList.get(position);
            holder.timeTv.setText(data.getDate());

            if (data.isDisplayMonthTitle()) {
                holder.monthTitleTv.setVisibility(View.VISIBLE);
                try {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                    Date date = df.parse(data.getDate());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    int month = calendar.get(Calendar.MONTH) + 1;

                    LogUtils.debugInfo("这是第" + month + "月");

                    String[] months = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

                    holder.monthTitleTv.setText(months[month - 1] + "月");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemDel(position);
                    }
                }
            });

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onIemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return exerciseList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeTv;
        TextView monthTitleTv;
        Button delBtn;

        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            timeTv = (TextView) view.findViewById(R.id.exercise_list_date);
            monthTitleTv = (TextView) view.findViewById(R.id.month_title_tv);
            delBtn = (Button) view.findViewById(R.id.exercise_list_item_del_btn);

            parent = (RelativeLayout) view.findViewById(R.id.exercise_list_parent);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public interface onItemClickListener {
        void onIemClick(int position);

        void onItemDel(int position);
    }

}
