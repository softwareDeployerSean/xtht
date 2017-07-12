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
import com.walnutin.xtht.bracelet.mvp.ui.activity.mvp.maputils.PathRecord;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Leiht on 2017/7/5.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private onItemClickListener mOnItemClickListener;

    private Context mContext;

    private List<PathRecord> exerciseList;

    private Map<String, String> monthTotleMap;

    public ExerciseListAdapter(Context context, List<PathRecord> exerciseList, Map<String, String> monthTotleMap) {
        this.mContext = context;
        this.exerciseList = exerciseList;
        this.monthTotleMap = monthTotleMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.exercise_list_item, parent,
                false);
        ExerciseListAdapter.MyViewHolder holder = new ExerciseListAdapter.MyViewHolder(view);
//        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.monthTitleTv.setVisibility(View.GONE);
            holder.month_distance.setVisibility(View.GONE);
            PathRecord data = exerciseList.get(position);
            holder.timeTv.setText(data.getDate());
            holder.time_value_tv.setText(data.getDuration());
            LogUtils.debugInfo("速度==" + data.getAveragespeed());
            holder.speeds_value_tv.setText(data.getAveragespeed());
            holder.iexercise_list_distance_tv.setText(data.getDistance());
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

                    if (monthTotleMap != null && monthTotleMap.containsKey(data.getDate()) && Integer.parseInt(monthTotleMap.get(data.getDate())) > 0) {
                        holder.month_distance.setVisibility(View.VISIBLE);
                        holder.month_distance.setText(monthTotleMap.get(data.getDate()));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

           /* holder.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemDel(position);
                    }
                }
            });*/

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
        return exerciseList.size();
    }


    public void setmOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView timeTv;
        TextView monthTitleTv;
        //Button delBtn;
        TextView time_value_tv, speeds_value_tv, iexercise_list_distance_tv, month_distance;
        RelativeLayout parent;

        public MyViewHolder(View view) {
            super(view);
            timeTv = (TextView) view.findViewById(R.id.exercise_list_date);
            monthTitleTv = (TextView) view.findViewById(R.id.month_title_tv);
            month_distance = (TextView) view.findViewById(R.id.month_distance);
            //delBtn = (Button) view.findViewById(R.id.exercise_list_item_del_btn);
            time_value_tv = (TextView) view.findViewById(R.id.time_value_tv);
            speeds_value_tv = (TextView) view.findViewById(R.id.speeds_value_tv);
            iexercise_list_distance_tv = (TextView) view.findViewById(R.id.iexercise_list_distance_tv);
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

    public void setDatas(List<PathRecord> objects) {
        this.exerciseList = objects;
        notifyDataSetChanged();
    }

}
